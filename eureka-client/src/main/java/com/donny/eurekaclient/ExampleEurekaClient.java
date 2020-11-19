package com.donny.eurekaclient;


import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class ExampleEurekaClient {

    static Properties properties = new Properties();

    static StandardEnvironment env = new StandardEnvironment();

    static {

        properties.setProperty("eureka.instance.ip-address", "true");
        properties.setProperty("eureka.client.registerWithEureka", "true");
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public EurekaInstanceConfigBean eurekaInstanceConfigBean() {
        String hostname = getProperty("eureka.instance.hostname");
        boolean preferIpAddress = Boolean.parseBoolean(getProperty("eureka.instance.prefer-ip-address"));
        String ipAddress = getProperty("eureka.instance.ip-address");
        boolean isSecurePortEnabled = Boolean.parseBoolean(getProperty("eureka.instance.secure-port-enabled"));

        //String serverContextPath = getProperty("server.servlet.context-path", "/");
        int serverPort = Integer.parseInt(getProperty("server.port", getProperty("port", "8080")));

//        Integer managementPort =Integer.valueOf( getProperty("management.server.port"));
//        String managementContextPath = getProperty("management.server.servlet.context-path");
//        Integer jmxPort =Integer.valueOf( getProperty("com.sun.management.jmxremote.port"));
        InetUtilsProperties inetUtilsProperties = new InetUtilsProperties();
        InetUtils inetUtils = new InetUtils(inetUtilsProperties);
        EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);
        instance.setNonSecurePort(serverPort);
        instance.setInstanceId(IdUtils.getDefaultInstanceId(env));
        instance.setPreferIpAddress(preferIpAddress);
        instance.setSecurePortEnabled(isSecurePortEnabled);
        if (StringUtils.hasText(ipAddress)) {
            instance.setIpAddress(ipAddress);
        }
        if (isSecurePortEnabled) {
            instance.setSecurePort(serverPort);
        }
        if (StringUtils.hasText(hostname)) {
            instance.setHostname(hostname);
        }
        String statusPageUrlPath = getProperty("eureka.instance.status-page-url-path");
        String healthCheckUrlPath = getProperty("eureka.instance.health-check-url-path");

        if (StringUtils.hasText(statusPageUrlPath)) {
            instance.setStatusPageUrlPath(statusPageUrlPath);
        }
        if (StringUtils.hasText(healthCheckUrlPath)) {
            instance.setHealthCheckUrlPath(healthCheckUrlPath);
        }

        //setupJmxPort(instance, jmxPort);
        return instance;

    }


    public EurekaClientConfigBean eurekaClientConfigBean() {
        EurekaClientConfigBean clientConfig = new EurekaClientConfigBean();
        clientConfig.setRegisterWithEureka(Boolean.valueOf(getProperty("eureka.client.registerWithEureka", "true")));
        Map<String, String> serviceUrl = new HashMap<>();
        serviceUrl.put("defaultZone", "http://localhost:8761/eureka/");
        clientConfig.setServiceUrl(serviceUrl);
        return clientConfig;
    }


    public static void main(String[] args) {

        ExampleEurekaClient exampleEurekaClient = new ExampleEurekaClient();
        //eureka本身的配置
        EurekaInstanceConfig config = exampleEurekaClient.eurekaInstanceConfigBean();

        LeaseInfo.Builder leaseInfoBuilder = LeaseInfo.Builder.newBuilder()
                .setRenewalIntervalInSecs(config.getLeaseRenewalIntervalInSeconds())
                .setDurationInSecs(config.getLeaseExpirationDurationInSeconds());

        //eureka实列构造器
        InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder();
        builder.setNamespace(config.getNamespace()).setAppName(config.getAppname())
                .setInstanceId(config.getInstanceId())
                .setAppGroupName(config.getAppGroupName())
                .setDataCenterInfo(config.getDataCenterInfo())
                .setIPAddr(config.getIpAddress()).setHostName(config.getHostName(false))
                .setPort(config.getNonSecurePort())
                .enablePort(InstanceInfo.PortType.UNSECURE,
                        config.isNonSecurePortEnabled())
                .setSecurePort(config.getSecurePort())
                .enablePort(InstanceInfo.PortType.SECURE, config.getSecurePortEnabled())
                .setVIPAddress(config.getVirtualHostName())
                .setSecureVIPAddress(config.getSecureVirtualHostName())
                .setHomePageUrl(config.getHomePageUrlPath(), config.getHomePageUrl())
                .setStatusPageUrl(config.getStatusPageUrlPath(),
                        config.getStatusPageUrl())
                .setHealthCheckUrls(config.getHealthCheckUrlPath(),
                        config.getHealthCheckUrl(), config.getSecureHealthCheckUrl())
                .setASGName(config.getASGName());
        //实列信息
        InstanceInfo instanceInfo = builder.build();
        instanceInfo.setLeaseInfo(leaseInfoBuilder.build());

        ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(config, instanceInfo);

        //eureka客户端配置
        EurekaClientConfigBean clientConfig = exampleEurekaClient.eurekaClientConfigBean();

        //创建eureka客户端
        EurekaClient eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);

        Set<String> regions= eurekaClient.getAllKnownRegions();
        System.out.println(regions);
        //从远程获取应用
        Applications applications = eurekaClient.getApplications();
        System.out.println(applications);
        eurekaClient.shutdown();
    }

}
