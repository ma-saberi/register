package register.api.configuration;

import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${server.version.code}")
    private String SERVER_VERSION_CODE;
    @Value("${server.version.name}")
    private String SERVER_VERSION_NAME;
    @Value("${swagger.title}")
    private String SWAGGER_TITLE;
    @Value("${swagger.host.url}")
    private String SWAGGER_HOST_URL;
    @Value("${swagger.host.protocols}")
    private Set<String> SWAGGER_HOST_PROTOCOLS;
    @Value("${swagger.description}")
    private String SWAGGER_DESCRIPTION;


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .protocols(SWAGGER_HOST_PROTOCOLS)
                .host(SWAGGER_HOST_URL)
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(securityContexts())
                .globalOperationParameters(
                        Arrays.asList(new ParameterBuilder().name("Accept-Language")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .build()))
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title(SWAGGER_TITLE)
                .description(SWAGGER_DESCRIPTION)
                .version("v" + SERVER_VERSION_CODE + "-" + SERVER_VERSION_NAME)
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{};
        return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .docExpansion(DocExpansion.LIST)
                .filter(true)
                .build();
    }


    @ApiIgnore
    @Controller()
    public static class Home {

        @GetMapping("/api/docs/**")
        public ModelAndView docs(ModelMap model, HttpServletRequest request) {

            String sharp = request.getServletPath().substring("/api/docs".length());
            String redirect;
            if (StringUtils.isNotEmpty(sharp) && !sharp.equals("/")) {
                redirect = "redirect:/swagger-ui.html" + "#" + sharp;
            } else {
                redirect = "redirect:/swagger-ui.html";
            }
            return new ModelAndView(redirect, model);

        }

        @GetMapping("/api/swagger-ui.html/**")
        public ModelAndView help(ModelMap model, HttpServletRequest request) {

            String sharp = request.getServletPath().substring("/api/swagger-ui.html".length());
            String redirect;
            if (StringUtils.isNotEmpty(sharp) && !sharp.equals("/")) {
                redirect = "redirect:/swagger-ui.html" + "#" + sharp;
            } else {
                redirect = "redirect:/swagger-ui.html";
            }
            return new ModelAndView(redirect, model);

        }

    }
}