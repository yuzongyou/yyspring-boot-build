package com.duowan.mybatis.service.impl;

import com.duowan.mybatis.dao.mybatis.SampleMapper;
import com.duowan.mybatis.service.SampleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/9 14:47
 */
@Service
public class SampleServiceImpl implements SampleService {

    private SampleMapper sampleMapper;

    public SampleServiceImpl(SampleMapper sampleMapper) {
        this.sampleMapper = sampleMapper;
    }

    @Override
    public List<String> getDatabases() {
        return sampleMapper.getDatabases();
    }

    @Override
    public List<String> getTables() {
        return sampleMapper.getTables();
    }

    @Override
    public String selectSelf(String self) {
        return sampleMapper.selectSelf(self);
    }
}
