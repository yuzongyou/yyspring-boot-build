package com.duowan.okhttp3;

import com.duowan.common.utils.HttpUtil;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 23:25
 */
public class OkHttp3ApiTest {

    @Test
    public void testSyncGet() throws IOException {

        long begTime = System.currentTimeMillis();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .dns(Dns.SYSTEM)
                .build();

        String httpUrl = "http://proxyapi.yy.com/leopardtest-service/myip";

        System.out.println("初始化耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        System.out.println("构造Request耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        Response response = client.newCall(request).execute();

        System.out.println("发起请求耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        System.out.println(response.body().string());

        System.out.println("输出响应耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");

        System.out.println("===========================================================");

        begTime = System.currentTimeMillis();
        response = client.newCall(request).execute();

        System.out.println("发起请求耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        System.out.println(response.body().string());

        System.out.println("输出响应耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");

        System.out.println("===========================================================");

        begTime = System.currentTimeMillis();
        response = client.newCall(request).execute();

        System.out.println("发起请求耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        System.out.println(response.body().string());

        System.out.println("输出响应耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");

        System.out.println("===========================================================");

        begTime = System.currentTimeMillis();
        response = client.newCall(request).execute();

        System.out.println("发起请求耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        System.out.println(response.body().string());

        System.out.println("输出响应耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");



        System.out.println("===========================================================");

        begTime = System.currentTimeMillis();
        String resp = HttpUtil.doGet(httpUrl);

        System.out.println("发起请求耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        System.out.println(resp);

        System.out.println("输出响应耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");

        System.out.println("===========================================================");

        begTime = System.currentTimeMillis();
        resp = HttpUtil.doGet(httpUrl);

        System.out.println("发起请求耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        System.out.println(resp);

        System.out.println("输出响应耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        System.out.println("===========================================================");

        begTime = System.currentTimeMillis();
        resp = HttpUtil.doGet(httpUrl);

        System.out.println("发起请求耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
        begTime = System.currentTimeMillis();
        System.out.println(resp);

        System.out.println("输出响应耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒！");
    }
}
