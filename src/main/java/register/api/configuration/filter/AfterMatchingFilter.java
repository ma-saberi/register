package register.api.configuration.filter;

import org.apache.catalina.connector.RequestFacade;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.servlet.*;
import java.io.IOException;

@Component
@Order(Integer.MIN_VALUE + 10)
@Priority(1)
public class AfterMatchingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        RequestFacade facade = (RequestFacade) servletRequest;
        String method = facade.getMethod();
        String requestURI = facade.getRequestURI();
        servletRequest.setAttribute("userUri", method + " " + requestURI);

        filterChain.doFilter(servletRequest, servletResponse);

    }

}
