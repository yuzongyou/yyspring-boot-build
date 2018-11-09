package com.duowan.yyspringboot.autoconfigure.swagger2.web;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static springfox.documentation.swagger.common.HostNameProvider.componentsFrom;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 21:42
 */
@Controller
@ApiIgnore
public class YySwagger2Controller {

    public static final String DEFAULT_URL = "/vyy/api-docs";
    private static final Logger LOGGER = LoggerFactory.getLogger(Swagger2Controller.class);
    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    private String hostNameOverride;
    private DocumentationCache documentationCache;
    private ServiceModelToSwagger2Mapper mapper;
    private JsonSerializer jsonSerializer;

    private String serviceId;

    @Autowired
    public YySwagger2Controller(
            Environment environment,
            DocumentationCache documentationCache,
            ServiceModelToSwagger2Mapper mapper,
            JsonSerializer jsonSerializer) {

        this.hostNameOverride =
                environment.getProperty(
                        "springfox.documentation.swagger.v2.host",
                        "DEFAULT");
        this.documentationCache = documentationCache;
        this.mapper = mapper;
        this.jsonSerializer = jsonSerializer;

        this.serviceId = environment.resolvePlaceholders("${yyspring.swagger2.gateway-route-path:${spring.application.name}}");
    }

    @RequestMapping(
            value = DEFAULT_URL,
            method = RequestMethod.GET,
            produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
    @PropertySourcedMapping(
            value = "${springfox.documentation.swagger.vyy.path}",
            propertyKey = "springfox.documentation.swagger.vyy.path")
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(
            @RequestParam(value = "group", required = false) String swaggerGroup,
            HttpServletRequest servletRequest) {

        String groupName = Optional.fromNullable(swaggerGroup).or(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            LOGGER.warn("Unable to find specification for group {}", groupName);
            return new ResponseEntity<Json>(HttpStatus.NOT_FOUND);
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
        UriComponents uriComponents = componentsFrom(servletRequest, swagger.getBasePath());
        swagger.basePath(Strings.isNullOrEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath());
        if (isNullOrEmpty(swagger.getHost())) {
            swagger.host(hostName(uriComponents));
        }

        Map<String, Path> mypaths = new TreeMap<>();
        for(Map.Entry<String, Path> entry : swagger.getPaths().entrySet()) {
            mypaths.put("/" + this.serviceId + entry.getKey(), entry.getValue());
        }
        swagger.setPaths(mypaths);

        return new ResponseEntity<Json>(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

    private String hostName(UriComponents uriComponents) {
        if ("DEFAULT".equals(hostNameOverride)) {
            String host = uriComponents.getHost();
            int port = uriComponents.getPort();
            if (port > -1) {
                return String.format("%s:%d", host, port);
            }
            return host;
        }
        return hostNameOverride;
    }
}
