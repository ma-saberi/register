package register.api.configuration.filter;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import register.data.entity.User;
import register.exception.UserException;
import register.utils.JsonUtil;
import register.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Aspect
@Order(1)
@Component
@ConditionalOnExpression("${endpoint.aspect.enabled:false}")
public class EndpointAspect {

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private UserUtil userUtil;

    @Before("within(register.api.*)")
    public void endpointBefore(JoinPoint p) {

        if (request != null) {

            try {
                User user = userUtil.getCredential();
                String address = request.getHeader("X-Forwarded-For");
                address = StringUtils.isBlank(address) ? request.getRemoteAddr() : address;
                String requestURI = request.getRequestURI();
                String method = request.getMethod();
                request.setAttribute("userUri", method + " " + requestURI);
                log.info("*REQUEST--> " + address + " | " + (user != null ? user.getUsername() : "_ANONYMOUS_") + " | " + method + " " + requestURI + " | " + "*PARAMETER--> " + JsonUtil.getStringJson(request.getParameterMap()));
            } catch (Exception e) {
                log.warn(e.getMessage());
            }


        }

    }

    @AfterReturning(value = ("within(register.api.*)"),
            returning = "returnValue")
    public void endpointAfterReturning(JoinPoint p, Object returnValue) {

        try {
            if (returnValue instanceof ResponseEntity) {

                User user = userUtil.getCredential();
                String address = request.getHeader("X-Forwarded-For");
                address = StringUtils.isBlank(address) ? request.getRemoteAddr() : address;
                log.info("*RESPONSE--> " + address + " | " + (user != null ? user.getUsername() : "_ANONYMOUS_") + " | " + JsonUtil.getStringJson(returnValue));
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }


    @AfterThrowing(pointcut = ("within(register.api.*)"), throwing = "e")
    public void endpointAfterThrowing(JoinPoint p, Exception e) {

        try {

            User user = userUtil.getCredential();
            String address = request.getHeader("X-Forwarded-For");
            address = StringUtils.isBlank(address) ? request.getRemoteAddr() : address;

            if (e instanceof UserException) {
                log.info("*THROW--> " + address + " | " + (user != null ? user.getUsername() : "_ANONYMOUS_") + " | " + ((UserException) e).getStatus().getReasonPhrase());
            } else {
                log.info("*THROW--> " + address + " | " + (user != null ? user.getUsername() : "_ANONYMOUS_") + " | " + e.getMessage());
            }

        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
    }
}
