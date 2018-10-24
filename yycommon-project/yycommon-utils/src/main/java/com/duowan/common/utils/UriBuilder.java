package com.duowan.common.utils;

import com.duowan.common.utils.exception.InvalidURISyntaxException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/24 11:09
 */
public class UriBuilder {

    private String schema;

    private String host;

    private int port = 80;

    /**
     * hash data
     **/
    private String fragment;

    /**
     * 请求路径
     **/
    private String path;

    private Map<String, String> paramsMap;

    private List<String> paramsKey;

    private UriBuilder(String schema, String host) {
        this.schema = schema;
        this.host = host;
        this.paramsMap = new HashMap<>();
        this.paramsKey = new LinkedList<>();

    }

    public static UriBuilder builder(String schema, String host) {
        return new UriBuilder(schema, host);
    }

    public static UriBuilder fromUri(URI uri) {
        UriBuilder builder = new UriBuilder(uri.getScheme(), uri.getHost());

        builder.paramsMap = extractParamsAsMap(uri, builder.paramsKey);
        builder.port = uri.getPort() > 0 ? uri.getPort() : 80;
        builder.fragment = uri.getRawFragment();
        builder.path = uri.getRawPath();

        return builder;
    }

    public static UriBuilder fromHttpUrl(String httpUrl) {
        try {
            return fromUri(new URI(httpUrl));
        } catch (URISyntaxException e) {
            throw new InvalidURISyntaxException(e);
        }
    }

    private static Map<String, String> extractParamsAsMap(URI uri, List<String> paramsKey) {
        Map<String, String> map = new TreeMap<>();
        String rawQuery = uri.getRawQuery();
        if (null == rawQuery || "".equals(rawQuery.trim())) {
            return map;
        }

        String[] keyValues = rawQuery.split("&");
        for (String keyValue : keyValues) {
            if (null == keyValue || "".equals(keyValue.trim())) {
                continue;
            }
            if (!keyValue.contains("=")) {
                paramsKey.add(keyValue);
                map.put(keyValue, null);
            } else {
                String[] array = keyValue.split("=");
                if (array.length == 1) {
                    map.put(array[0], "");
                } else {
                    map.put(array[0], array[1]);
                }
                paramsKey.add(array[0]);
            }
        }
        return map;
    }

    public UriBuilder schema(String schema) {
        if (null != schema && !"".equals(schema.trim())) {
            this.schema = schema.toLowerCase();
        }
        return this;
    }

    public UriBuilder host(String host) {
        if (null != host && !"".equals(host.trim())) {
            this.host = host;
        }
        return this;
    }

    public UriBuilder port(int port) {
        this.port = port;
        return this;
    }

    public UriBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public UriBuilder path(String path) {
        this.path = path;
        return this;
    }

    public UriBuilder param(String name, String value) {
        if (null != name && !"".equals(name.trim())) {
            paramsMap.put(name, value);
            if (!paramsKey.contains(name)) {
                paramsKey.add(name);
            }
        }
        return this;
    }

    public UriBuilder params(Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return this;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String name = entry.getKey();
            paramsMap.put(name, entry.getValue());
            if (!paramsKey.contains(name)) {
                paramsKey.add(name);
            }
        }
        return this;
    }

    public UriBuilder removeParam(String name) {
        this.paramsMap.remove(name);
        this.paramsKey.remove(name);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(this.schema).append("://").append(host);

        if (port != 80 && port != -1) {
            builder.append(":").append(port);
        }
        if (null != path && !"".equals(path.trim())) {
            builder.append(path);
        }
        boolean hadParams = this.paramsMap != null && !this.paramsMap.isEmpty();

        if (hadParams) {
            builder.append("?");
            for (String name : paramsKey) {
                if (null == name || "".equals(name.trim())) {
                    continue;
                }
                String value = paramsMap.get(name);
                builder.append(name);
                if (null != value && !"".equals(value.trim())) {
                    builder.append("=").append(value);
                }
                builder.append("&");
            }
            builder.setLength(builder.length() - 1);
        }

        if (null != fragment && !"".equals(fragment)) {
            builder.append("#").append(fragment);
        }

        return builder.toString();
    }

    public String getSchema() {
        return schema;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getFragment() {
        return fragment;
    }

    public String getPath() {
        return path;
    }

    public String getParam(String name) {
        return paramsMap.get(name);
    }

    public boolean containParam(String name) {
        return paramsMap.containsKey(name);
    }
}
