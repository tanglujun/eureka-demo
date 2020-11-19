package com.donny.eurekaclient;

import com.google.inject.AbstractModule;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;

/**
 * 功能说明
 * <p>
 *
 * @author 唐陆军
 * @version 1.0.0
 * @date 2020/11/19
 */
public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
        // 表明：当需要 Communicator 这个变量时，我们注入 DefaultCommunicatorImpl 的实例作为依赖
//        bind(Communicator.class).to(DefaultCommunicatorImpl.class);
//
//        bind(Communication.class)
//                .toInstance(new Communication(true));

        EurekaInstanceConfig instanceConfig = new MyDataCenterInstanceConfig();
        bind(EurekaInstanceConfig.class).toInstance(instanceConfig);

        DefaultEurekaClientConfig clientConfig = new DefaultEurekaClientConfig();



        bind(DefaultEurekaClientConfig.class).toInstance(clientConfig);

        //bind(AbstractDiscoveryClientOptionalArgs.class).toInstance(null);

        //bind(EurekaClient.class).to(DiscoveryClient.class);
    }
}
