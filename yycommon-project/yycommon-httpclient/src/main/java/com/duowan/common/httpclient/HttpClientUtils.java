package com.duowan.common.httpclient;

import com.duowan.common.exception.HttpInvokeException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Http Client util
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 20:15
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static class Holder {
        /**
         * 连接管理器
         **/
        private static PoolingHttpClientConnectionManager manager = null;

        /**
         * http client 客户端
         **/
        private static CloseableHttpClient HTTP_CLIENT_INSTANCE = null;

        /**
         * 默认的请求配置信息
         **/
        private static RequestConfig DEFAULT_REQUEST_CONFIG = null;

        static {
            initialize();
        }

        /**
         * 获取 http 客户端
         *
         * @return 返回http 客户端
         */
        static void initialize() {
            // 注册协议相关的 socket 工厂
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
                    .build();

            // HttpConnection 工厂： 配置写请求、响应处理
            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                    DefaultHttpRequestWriterFactory.INSTANCE,
                    DefaultHttpResponseParserFactory.INSTANCE
            );

            HcConfig hcConfig = HcUtil.getConfig();

            // DNS 解析器
            DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

            // 创建连接池管理器
            manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, dnsResolver);

            // 设置默认 socket 配置
            SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            manager.setDefaultSocketConfig(defaultSocketConfig);

            // 设置最大连接数目，这个应该由什么决定？
            manager.setMaxTotal(hcConfig.getMaxTotal());

            // 设置每个路由的最大连接数
            manager.setDefaultMaxPerRoute(hcConfig.getDefaultMaxPerRoute());

            // 在从连接池获取连接时，连接不活跃多长时间需要进行一次验证，默认是 2 秒
            manager.setValidateAfterInactivity(hcConfig.getValidateAfterInactivity());

            // 默认的请求配置信息
            DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
                    // 连接超时时间， 2秒
                    .setConnectTimeout(hcConfig.getConnectTimeout())
                    // 设置等待数据超时时间， 5秒
                    .setSocketTimeout(hcConfig.getSocketTimeout())
                    // 设置从连接池获取连接的等待超时时间
                    .setConnectionRequestTimeout(hcConfig.getConnectionRequestTimeout())
                    .build();

            // 创建 httpclient
            HTTP_CLIENT_INSTANCE = HttpClients.custom()
                    .setConnectionManager(manager)
                    // 连接池是否是共享模式
                    .setConnectionManagerShared(hcConfig.isConnectionManagerShared())
                    // 定期回收空闲连接
                    .evictIdleConnections(60, TimeUnit.SECONDS)
                    .evictExpiredConnections()
                    // 连接存活时间，如果不设置则根据长连接信息确定
                    .setConnectionTimeToLive(60, TimeUnit.SECONDS)
                    // 默认请求配置
                    .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
                    // 连接重用策略，即是否能 keepAlive
                    .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                    // 长连接配置， 获取长连接需要多少时间
                    .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                    // 设置重试次数，默认是 3 次，可以进行禁用
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(hcConfig.getRetryTimes(), false))
                    .build();

            // JVM 停止或重启时，关闭连接池释放连接
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (null != HTTP_CLIENT_INSTANCE) {
                            HTTP_CLIENT_INSTANCE.close();
                        }
                    } catch (Exception e) {
                        logger.warn("释放 HttpClient 连接失败： " + e.getMessage(), e);
                    }
                }
            }));
        }
    }

    public static CloseableHttpClient getHttpClient() {
        return Holder.HTTP_CLIENT_INSTANCE;
    }

    public static RequestConfig getDefaultRequestConfig() {
        return Holder.DEFAULT_REQUEST_CONFIG;
    }

    public static HcGetContext get(String url) throws HttpInvokeException {
        return new HcGetContext(url, getHttpClient(), getDefaultRequestConfig());
    }

    public static HcPostContext post(String url) throws HttpInvokeException {
        return new HcPostContext(url, getHttpClient(), getDefaultRequestConfig());
    }

    public static HcDeleteContext delete(String url) throws HttpInvokeException {
        return new HcDeleteContext(url, getHttpClient(), getDefaultRequestConfig());
    }

    public static HcPutContext put(String url) throws HttpInvokeException {
        return new HcPutContext(url, getHttpClient(), getDefaultRequestConfig());
    }
}
