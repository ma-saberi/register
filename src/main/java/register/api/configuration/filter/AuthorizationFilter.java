package register.api.configuration.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import register.api.configuration.ResponseWriterUtil;
import register.data.entity.User;
import register.domain.annotation.ResourceRole;
import register.exception.UserException;
import register.exception.UserExceptionStatus;

import javax.annotation.Priority;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Integer.MAX_VALUE - 2000)
@Priority(2000)
public class AuthorizationFilter implements Filter {

    @Autowired
    private List<HandlerMapping> handlerMappings;
    @Autowired
    private ResponseWriterUtil responseWriterUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Object handler = getHandler((HttpServletRequest) servletRequest);

        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResourceRole role = handlerMethod.getMethod().getAnnotation(ResourceRole.class);

            if (role != null && role.value().length > 0) {

                Authentication authentication = securityContext.getAuthentication();
                if (authentication == null || authentication.getPrincipal().equals("_ANONYMOUS_") ||
                        authentication.getCredentials() == null || Arrays.stream(role.value()).noneMatch(x -> x.equals(((User) authentication.getCredentials()).getRole()))) {
                    // not authorized
                    responseWriterUtil.sendErrorResponse((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, new UserException(UserExceptionStatus.UNAUTHORIZED));

                    return;
                }

            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    private Object getHandler(HttpServletRequest servletRequest) {
        Object handler = null;
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                HandlerExecutionChain handlerExecutionChain =
                        handlerMapping.getHandler(servletRequest);
                if (handlerExecutionChain != null) {
                    if (handlerExecutionChain.getHandler() instanceof HandlerMethod) {
                        handler = handlerExecutionChain.getHandler();
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return handler;
    }
}
