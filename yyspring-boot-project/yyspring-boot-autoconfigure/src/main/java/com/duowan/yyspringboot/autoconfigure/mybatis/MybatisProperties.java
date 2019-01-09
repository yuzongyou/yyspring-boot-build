package com.duowan.yyspringboot.autoconfigure.mybatis;

import com.duowan.common.utils.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 15:43
 */
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties {

    public static final String MYBATIS_PREFIX = "yyspring.mybatis";

    /**
     * 自定义配置，key 为 数据源ID，即JDBC中的数据源ID
     **/
    private Map<String, MybatisConfiguration> sources;


    public Map<String, MybatisConfiguration> getSources() {
        return sources;
    }

    private List<MybatisConfiguration> fixDataSourceId(Map<String, MybatisConfiguration> sources) {
        if (sources == null || sources.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<MybatisConfiguration> configurationList = new ArrayList<>(sources.size());

        for (Map.Entry<String, MybatisConfiguration> entry : sources.entrySet()) {
            String dsId = entry.getKey();
            MybatisConfiguration configuration = entry.getValue();
            configuration.setDataSourceId(StringUtil.isNotBlank(configuration.getDataSourceId()) ? configuration.getDataSourceId() : dsId);

            configurationList.add(configuration);

        }

        return configurationList;
    }

    public void setSources(Map<String, MybatisConfiguration> sources) {
        this.sources = sources;
    }

    public List<MybatisConfiguration> getConfigurations() {
        return fixDataSourceId(sources);
    }
}
