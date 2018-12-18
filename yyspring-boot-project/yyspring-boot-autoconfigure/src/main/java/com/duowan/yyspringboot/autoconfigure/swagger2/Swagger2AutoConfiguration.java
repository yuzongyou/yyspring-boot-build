package com.duowan.yyspringboot.autoconfigure.swagger2;

import com.duowan.yyspring.boot.AppContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 18:03
 */
@Configuration
@ConditionalOnProperty(value = "yyspring.swagger2.enabled", matchIfMissing = true)
public class Swagger2AutoConfiguration {

    @ConditionalOnClass({ApiInfo.class, Docket.class})
    @ConditionalOnWebApplication
    @EnableConfigurationProperties(Swagger2Properties.class)
    @AutoConfigureAfter(Swagger2DocumentationConfiguration.class)
    @EnableSwagger2
    @ComponentScan({"com.duowan.yyspringboot.autoconfigure.swagger2.web"})
    public static class Swagger2ServiceProviderConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ApiInfo swagger2ApiInfo(Swagger2Properties properties) {
            ApiInfoBuilder builder = new ApiInfoBuilder();
            builder.title(resolveTitle(properties));
            builder.description(resolveDescription(properties));
            builder.version(resolveVersion(properties));

            Contact contact = resolveContact(properties);
            if (null != contact) {
                builder.contact(contact);
            }

            return builder.build();
        }

        private Contact resolveContact(Swagger2Properties properties) {

            if (StringUtils.isAllBlank(properties.getContactName(), properties.getContactUrl(), properties.getContactEmail())) {
                return null;
            }

            return new Contact(properties.getContactName(), properties.getContactUrl(), properties.getContactEmail());

        }

        private String resolveVersion(Swagger2Properties properties) {
            if (StringUtils.isNotBlank(properties.getVersion())) {
                return properties.getVersion();
            }
            return "1.0";
        }

        private String resolveTitle(Swagger2Properties properties) {

            if (StringUtils.isNotBlank(properties.getTitle())) {
                return properties.getTitle();
            }

            // 自动生成一个Title，使用项目名称
            return "[" + AppContext.getProjectNo() + "-" + AppContext.getModuleNo() + "] API文档";
        }

        private String resolveDescription(Swagger2Properties properties) {

            if (StringUtils.isNotBlank(properties.getDescription())) {
                return properties.getTitle();
            }

            // 自动生成一个Title，使用项目名称
            return "[" + AppContext.getProjectNo() + "-" + AppContext.getModuleNo() + "] 接入教程！";
        }

        @Bean
        @ConditionalOnMissingBean
        public Docket swagger2Docket(ApiInfo apiInfo, Swagger2Properties properties) {

            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo);


            ApiSelectorBuilder builder = docket.select();

            if (properties.isJustWithApiAnnotation()) {
                builder.apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
            }
            if (properties.isJustWithApiOperationAnnotation()) {
                builder.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
            }
            if (StringUtils.isNotBlank(properties.getBasePackage())) {
                // 自行修改为自己的包路径
                builder.apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()));
            }

            builder.paths(PathSelectors.any());

            return builder.build();
        }

    }

    @Configuration
    @ConditionalOnClass(GlobalFilter.class)
    public static class GatewaySwagger2ReplaceUrlConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SwaggerHeaderGlobalFilter swaggerHeaderGlobalFilter() {
            return new SwaggerHeaderGlobalFilter();
        }
    }
}
