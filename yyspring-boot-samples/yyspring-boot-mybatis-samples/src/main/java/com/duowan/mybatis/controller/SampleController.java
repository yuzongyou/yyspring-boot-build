package com.duowan.mybatis.controller;

import com.duowan.mybatis.service.SampleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/9 14:50
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

    private SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping("/databases")
    public List<String> databases() {
        return this.sampleService.getDatabases();
    }

    @GetMapping("/tables")
    public List<String> tables() {
        return this.sampleService.getTables();
    }

    @GetMapping("/get/{self}")
    public String get(@PathVariable("self") String self) {
        return this.sampleService.selectSelf(self);
    }

}
