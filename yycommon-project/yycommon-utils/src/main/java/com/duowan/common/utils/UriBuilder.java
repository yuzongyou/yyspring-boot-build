package com.duowan.common.utils;

import com.duowan.common.utils.exception.InvalidURISyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/24 11:09
 */
public class UriBuilder {

    private static final int DEFAULT_HTTP_PORT = 80;

    private static final String HTTP_PATTERN = "(?i)(http|https):";

    private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";

    private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";

    private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]";

    private static final String HOST_PATTERN = "(" + HOST_IPV6_PATTERN + "|" + HOST_IPV4_PATTERN + ")";

    private static final String PORT_PATTERN = "(\\d*(?:\\{[^/]+?\\})?)";

    private static final String PATH_PATTERN = "([^?#]*)";

    private static final String LAST_PATTERN = "(.*)";

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile(
            "^" + HTTP_PATTERN + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?" + ")?" +
                    PATH_PATTERN + "(\\?" + LAST_PATTERN + ")?");

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

    private String userinfo;

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

        builder.paramsMap = extractParamsAsMap(uri.getRawQuery(), builder.paramsKey);
        builder.port = uri.getPort() > 0 ? uri.getPort() : 80;
        builder.fragment = uri.getRawFragment();
        builder.path = uri.getRawPath();
        builder.userinfo = uri.getRawUserInfo();

        return builder;
    }

    public static UriBuilder fromHttpUrl(String httpUrl) {

        try {
            return fromUri(new URI(httpUrl));
        } catch (URISyntaxException e) {
            return createBuilderFromHttpUrl(httpUrl);
        }
    }

    private static UriBuilder createBuilderFromHttpUrl(String httpUrl) {
        Matcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
        if (matcher.matches()) {
            String scheme = matcher.group(1);
            String host = matcher.group(5);
            if (StringUtils.isAnyBlank(scheme, host)) {
                throw new InvalidURISyntaxException("[" + httpUrl + "] is not a valid HTTP URL");
            }
            UriBuilder builder = new UriBuilder(scheme, host);
            builder.userinfo(matcher.group(4));

            String port = matcher.group(7);
            if (StringUtils.isNotBlank(port)) {
                builder.port(Integer.parseInt(port));
            }
            builder.path(matcher.group(8));

            String queryString = matcher.group(10);
            if (StringUtils.isBlank(queryString)) {
                return builder;
            }
            String[] array = queryString.split("#");
            if (array.length > 0) {
                builder.params(extractParamsAsMap(array[0], builder.paramsKey));
            }
            if (array.length > 1) {
                builder.fragment = array[1];
            }
            return builder;
        }
        throw new InvalidURISyntaxException("[" + httpUrl + "] is not a valid HTTP URL");
    }

    private static Map<String, String> extractParamsAsMap(String rawQuery, List<String> paramsKey) {
        Map<String, String> map = new TreeMap<>();
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

    public UriBuilder userinfo(String userinfo) {
        this.userinfo = userinfo;
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
        StringBuilder builder = new StringBuilder();

        appendSchema(builder);
        appendUserinfo(builder);

        builder.append(this.host);

        appendPort(builder);

        appendPath(builder);

        appendParams(builder);

        appendFragment(builder);

        return builder.toString();
    }

    private void appendFragment(StringBuilder builder) {
        if (null != fragment && !"".equals(fragment)) {
            builder.append("#").append(fragment);
        }
    }

    private void appendParams(StringBuilder builder) {
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
    }

    private void appendPath(StringBuilder builder) {
        if (null != path && !"".equals(path.trim())) {
            builder.append(path);
        }
    }

    private void appendPort(StringBuilder builder) {
        if (port != DEFAULT_HTTP_PORT && port != -1) {
            builder.append(":").append(port);
        }
    }

    private void appendUserinfo(StringBuilder builder) {
        if (StringUtils.isNotBlank(this.userinfo)) {
            builder.append(this.userinfo).append("@");
        }
    }

    private void appendSchema(StringBuilder builder) {
        if (StringUtils.isBlank(this.schema)) {
            builder.append("//");
        } else {
            builder.append(this.schema).append("://");
        }
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

    public String getUserinfo() {
        return userinfo;
    }

    public String getParam(String name) {
        return paramsMap.get(name);
    }

    public boolean containParam(String name) {
        return paramsMap.containsKey(name);
    }
}
