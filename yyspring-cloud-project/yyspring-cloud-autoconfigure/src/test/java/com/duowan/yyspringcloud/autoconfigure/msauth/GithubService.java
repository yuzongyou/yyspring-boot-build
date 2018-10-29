package com.duowan.yyspringcloud.autoconfigure.msauth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 14:41
 */
@FeignClient(name = "github-service", url = "${github.service.url:}")
public interface GithubService {

    @RequestMapping(value = "rate_limit", method = RequestMethod.GET)
    String rateLimit();
}
