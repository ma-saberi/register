package register.api.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import register.api.model.RestResponse;
import register.exception.UserException;
import register.exception.UserExceptionStatus;
import register.utils.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class ResponseWriterUtil {

    @Value("${server.version.name}")
    private String SEVER_NAME;
    @Value("${server.version.code}")
    private String SEVER_VERSION;


    public void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, UserException e) throws IOException {

        UserExceptionStatus status = e.getStatus();
        RestResponse<Object> restResponse = new RestResponse<>(
                e.getStatus().getCode(),
                e.getStatus().getReasonPhrase(),
                (String) request.getAttribute("userUri"),
                e.getStatus().getMessage()
        );

        sendResponse(response, status, restResponse);
    }

    private void sendResponse(HttpServletResponse response, UserExceptionStatus status, RestResponse<Object> restResponse) throws IOException {
        response.setStatus(status.getCode());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String server = "Register Server " + "v" + SEVER_VERSION + " #" + SEVER_NAME;
        response.setHeader("Server", server);

        response.getWriter().write(JsonUtil.getStringJson(restResponse));
    }
}
