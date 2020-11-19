package com.donny.eurekaserver;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;

import java.util.Properties;

public class ExampleEurekaService {


    public static void main(String[] args) {
        Properties configInstance = new Properties();
        //服务部署的区域
        configInstance.setProperty("eureka.region","default");
        //注册的服务名
        configInstance.setProperty("eureka.name","donny-test");

        configInstance.setProperty("eureka.vipAddress","com.donny.com");

        configInstance.setProperty("eureka.port","8001");
        //指向相同区域
        configInstance.setProperty("eureka.preferSameZone","true");
        //是否使用DNS
        configInstance.setProperty("eureka.shouldUseDns","false");
        //
        configInstance.setProperty("eureka.serviceUrl.default","http://localhost:8080/eureka/v2/");


        Injector injector = Guice.createInjector(new BasicModule());

        EurekaInstanceConfig instanceConfig = injector.getInstance(EurekaInstanceConfig.class);
        EurekaConfigBasedInstanceInfoProvider instanceInfoProvider = injector.getInstance(EurekaConfigBasedInstanceInfoProvider.class);

        InstanceInfo instanceInfo = instanceInfoProvider.get();

        //ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);

        //DefaultEurekaClientConfig clientConfig = new DefaultEurekaClientConfig();

        ApplicationInfoManager applicationInfoManager = injector.getInstance(ApplicationInfoManager.class);
        DefaultEurekaClientConfig clientConfig = injector.getInstance(DefaultEurekaClientConfig.class);
        EurekaClient eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);
        ExampleServiceBase exampleServiceBase = new ExampleServiceBase(applicationInfoManager, eurekaClient, configInstance);

        try {
            exampleServiceBase.start();
        } finally {
            // the stop calls shutdown on eurekaClient
            exampleServiceBase.stop();
        }
    }
}
