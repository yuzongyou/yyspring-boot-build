package com.duowan.common.httpclient;

import com.duowan.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/21 10:29
 */
public class HcUtil {

    private HcUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HcUtil.class);

    private static class Holder {

        private Holder() {
            throw new IllegalStateException("Holder class");
        }

        private static final HcConfig HC_CONFIG = loadHcConfig();

        private static final String PROPERTIES_NAME_SUFFIX = ".properties";
        private static final String DEFAULT_HC_CONFIG_PROPERTIES = "default-hcconfig";
        private static final String HC_CONFIG_PROPERTIES = "hcconfig";

        private static HcConfig loadHcConfig() {

            HcConfig hcConfig = new HcConfig();

            URL url = HcUtil.class.getClassLoader().getResource(HC_CONFIG_PROPERTIES + PROPERTIES_NAME_SUFFIX);
            if (url == null) {
                url = HcUtil.class.getClassLoader().getResource(DEFAULT_HC_CONFIG_PROPERTIES + PROPERTIES_NAME_SUFFIX);
            }
            if (null == url) {
                return hcConfig;
            }

            Properties properties = new Properties();
            try {
                properties.load(url.openStream());
                hcConfig.setEnvVarNames(getStringArray(properties.getProperty(HcConfig.KEY_ENV_VAR_NAME), hcConfig.getEnvVarNames()));
                hcConfig.setCurrentEnv(deduceCurrentEnv(hcConfig.getEnvVarNames()));

                hcConfig.setEnvDev(lookupProperty(properties, hcConfig.getEnvDev(), HcConfig.KEY_ENV_DEV));
                hcConfig.setEnvTest(lookupProperty(properties, hcConfig.getEnvTest(), HcConfig.KEY_ENV_TEST));
                hcConfig.setEnvProd(lookupProperty(properties, hcConfig.getEnvProd(), HcConfig.KEY_ENV_PROD));

                fillHcConfig(hcConfig, properties);

            } catch (IOException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e.getMessage(), e);
                }
            }

            // 应用特定环境的配置
            applySpecialEnvHcConfig(hcConfig);

            return hcConfig;
        }

        private static void applySpecialEnvHcConfig(HcConfig hcConfig) {

            URL url = HcUtil.class.getClassLoader().getResource(DEFAULT_HC_CONFIG_PROPERTIES + hcConfig.getCurrentEnv() + PROPERTIES_NAME_SUFFIX);

            if (null != url) {
                try {
                    Properties properties = new Properties();
                    properties.load(url.openStream());
                    fillHcConfig(hcConfig, properties);
                } catch (IOException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                }
            }

        }

        private static void fillHcConfig(HcConfig hcConfig, Properties properties) {

            hcConfig.setMaxTotal(lookupIntProperty(properties, hcConfig.getMaxTotal(), HcConfig.KEY_MANAGER_MAX_TOTAL));
            hcConfig.setDefaultMaxPerRoute(lookupIntProperty(properties, hcConfig.getMaxTotal(), HcConfig.KEY_MANAGER_DEFAULT_MAX_PER_ROUTE));
            hcConfig.setValidateAfterInactivity(lookupIntProperty(properties, hcConfig.getMaxTotal(), HcConfig.KEY_MANAGER_DEFAULT_VALIDATE_AFTER_INACTIVITY));
            hcConfig.setConnectionRequestTimeout(lookupIntProperty(properties, hcConfig.getConnectionRequestTimeout(), HcConfig.KEY_CONNECTION_REQUEST_TIMEOUT));
            hcConfig.setConnectTimeout(lookupIntProperty(properties, hcConfig.getConnectTimeout(), HcConfig.KEY_CONNECTION_TIMEOUT));
            hcConfig.setSocketTimeout(lookupIntProperty(properties, hcConfig.getSocketTimeout(), HcConfig.KEY_SOCKET_TIMEOUT));
            hcConfig.setStaleConnectionCheckEnabled(lookupBooleanProperty(properties, hcConfig.isStaleConnectionCheckEnabled(), HcConfig.KEY_STALE_CONNECTION_CHECK_ENABLED));
            hcConfig.setRedirectsEnabled(lookupBooleanProperty(properties, hcConfig.isRedirectsEnabled(), HcConfig.KEY_REDIRECTS_ENABLED));
            hcConfig.setMaxRedirects(lookupIntProperty(properties, hcConfig.getMaxRedirects(), HcConfig.KEY_MAX_REDIRECTS));
            hcConfig.setRelativeRedirectsAllowed(lookupBooleanProperty(properties, hcConfig.isRelativeRedirectsAllowed(), HcConfig.KEY_RELATIVE_REDIRECTS_ALLOWED));
            hcConfig.setAuthenticationEnabled(lookupBooleanProperty(properties, hcConfig.isAuthenticationEnabled(), HcConfig.KEY_AUTHENTICATION_ENABLED));
            hcConfig.setContentCompressionEnabled(lookupBooleanProperty(properties, hcConfig.isContentCompressionEnabled(), HcConfig.KEY_CONTENT_COMPRESSION_ENABLED));
            hcConfig.setConnectionManagerShared(lookupBooleanProperty(properties, hcConfig.isConnectionManagerShared(), HcConfig.KEY_CONNECTION_MANAGER_SHARED));
            hcConfig.setRetryTimes(lookupIntProperty(properties, hcConfig.getRetryTimes(), HcConfig.KEY_RETRY_TIMES));
            hcConfig.setLogEnabled(lookupBooleanProperty(properties, hcConfig.isLogEnabled(), HcConfig.KEY_LOG_ENABLED));

        }

        private static String deduceCurrentEnv(String[] envVarNames) {
            if (envVarNames == null || envVarNames.length < 1) {
                envVarNames = new String[]{"ENV", "DWENV"};
            }
            return lookupProperty(null, "dev", envVarNames);
        }

        private static String[] getStringArray(String value, String[] defaultArray) {
            if (Util.isBlank(value)) {
                return defaultArray;
            }
            return value.split(",");
        }

        private static int lookupIntProperty(Properties properties, int defaultValue, String... keys) {
            String value = lookupProperty(properties, String.valueOf(defaultValue), keys);

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        private static boolean lookupBooleanProperty(Properties properties, boolean defaultValue, String... keys) {
            String value = lookupProperty(properties, String.valueOf(defaultValue), keys);

            try {
                return value.matches("(?i)true|yes|ok|1|yeah|on|open|enabled|enable");
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        private static String lookupProperty(Properties properties, String defaultValue, String... keys) {
            for (String key : keys) {
                String value = System.getProperty(key);
                if (Util.isBlank(value)) {
                    value = System.getenv(key);
                }
                if (Util.isBlank(value) && null != properties) {
                    value = properties.getProperty(key);
                }
                if (Util.isNotBlank(value)) {
                    return value;
                }
            }

            return defaultValue;
        }

    }

    public static HcConfig getConfig() {
        return Holder.HC_CONFIG;
    }

}
