package com.duowan.yyspringcloud.msauth.app;

import com.duowan.common.utils.JsonUtil;
import org.junit.Test;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 9:57
 */
public class ApolloAppReaderTest {

    @Test
    public void read() {

        AppReader reader = new ApolloAppReader("bizSupportGroup.ms-rpc-auth");

        System.out.println(JsonUtil.toJson(reader.read("yysc-admin")));
        System.out.println(JsonUtil.toJson(reader.read("yysc-follow")));
        System.out.println(JsonUtil.toJson(reader.read("yysc-gateway")));
        System.out.println(JsonUtil.toJson(reader.read("yysc-xxx")));

    }
}