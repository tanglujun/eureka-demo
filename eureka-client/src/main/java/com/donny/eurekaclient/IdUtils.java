
package com.donny.eurekaclient;

import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

/**
 * 功能说明
 * <p>
 *
 * @author 唐陆军
 * @version 1.0.0
 * @date 2020/11/19
 */
public final class IdUtils {
    private static final String SEPARATOR = ":";
    public static final String DEFAULT_SERVICE_ID_STRING = "${vcap.application.name:${spring.application.name:application}}:${vcap.application.instance_index:${spring.application.index:${local.server.port:${server.port:0}}}}:${vcap.application.instance_id:${cachedrandom.${vcap.application.name:${spring.application.name:application}}.value}}";

    private IdUtils() {
        throw new IllegalStateException("Can't instantiate a utility class");
    }

    public static String getDefaultInstanceId(PropertyResolver resolver) {
        return getDefaultInstanceId(resolver, true);
    }

    public static String getDefaultInstanceId(PropertyResolver resolver, boolean includeHostname) {
        String vcapInstanceId = resolver.getProperty("vcap.application.instance_id");
        if (StringUtils.hasText(vcapInstanceId)) {
            return vcapInstanceId;
        } else {
            String hostname = null;
            if (includeHostname) {
                hostname = resolver.getProperty("spring.cloud.client.hostname");
            }

            String appName = resolver.getProperty("spring.application.name");
            String namePart = combineParts(hostname, ":", appName);
            String indexPart = resolver.getProperty("spring.application.instance_id", resolver.getProperty("server.port"));
            return combineParts(namePart, ":", indexPart);
        }
    }

    public static String getResolvedServiceId(PropertyResolver resolver) {
        return resolver.resolvePlaceholders(getUnresolvedServiceId());
    }

    public static String getUnresolvedServiceId() {
        return "${vcap.application.name:${spring.application.name:application}}:${vcap.application.instance_index:${spring.application.index:${local.server.port:${server.port:0}}}}:${vcap.application.instance_id:${cachedrandom.${vcap.application.name:${spring.application.name:application}}.value}}";
    }

    public static String combineParts(String firstPart, String separator, String secondPart) {
        String combined = null;
        if (firstPart != null && secondPart != null) {
            combined = firstPart + separator + secondPart;
        } else if (firstPart != null) {
            combined = firstPart;
        } else if (secondPart != null) {
            combined = secondPart;
        }

        return combined;
    }
}
