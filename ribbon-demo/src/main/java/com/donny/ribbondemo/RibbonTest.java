
package com.donny.ribbondemo;

import com.alibaba.fastjson.JSON;
import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.client.http.RestClient;

import java.net.URI;


public class RibbonTest {


    public static void main(String[] args) throws Exception {

        //读取sample-client客户端的配置
        ConfigurationManager.loadPropertiesFromResources("sample-client.properties");  // 1

        //1、创建DefaultClientConfigImpl 每个client对应的配置 存放在namedConfig这个map中

        /*   DefaultClientConfigImpl的几个默认配置如下
        public static final String DEFAULT_NFLOADBALANCER_PING_CLASSNAME = "com.netflix.loadbalancer.DummyPing";

        public static final String DEFAULT_NFLOADBALANCER_RULE_CLASSNAME = "com.netflix.loadbalancer.AvailabilityFilteringRule";

        public static final String DEFAULT_NFLOADBALANCER_CLASSNAME = "com.netflix.loadbalancer.ZoneAwareLoadBalancer";
        * */

        //2、根据DefaultClientConfigImpl这个配置创建RestClient，内部主要是对apache httpclient的包装 存放在simpleClientMap这个map

        //3、根据DefaultClientConfigImpl这个配置创建默认的负载均衡器ZoneAwareLoadBalancer 存放在namedLBMap

        //4、整个ribbon客户端创建完毕 主要是得到RestClient对象，继承的是AbstractLoadBalancerAwareClient是具备负债均衡能力的http客户端
        RestClient client = (RestClient) ClientFactory.getNamedClient("sample-client");  // 2

        HttpRequest httpRequest=new HttpRequest.Builder().uri(new URI("/")).build();
        for (int i = 0; i < 20; i++)  {

            //1、执行待负载均衡器的方法

            //2、创建负债均衡器的命令LoadBalancerCommand

            //3、执行LoadBalancerCommand的submit方法

            //4、执行LoadBalancerCommand的selectServer方法

            //5、通过 loadBalancerContext.getServerFromLoadBalancer从负载均衡器上下文中调用获取服务方法

            //6、通过lb.chooseServer调用负债均衡器的选择服务方法，实际调用的是ZoneAwareLoadBalancer的chooseServer方法

            //7、调用AvailabilityFilteringRule路由规则类

            //8、AvailabilityFilteringRule会先使用roundRobinRule，随机获取一台服务，然后调用AvailabilityFilteringRule的
            // AvailabilityPredicate.apply判断服务的可用性 是否熔断或者链接数是否达到限制

            //9、获取到一台服务后 会回到executeWithLoadBalancer中executeWithLoadBalancer方法中 回调LoadBalancerCommand.submit中的方法

            //10、reconstructURIWithServer方法用来重新构造请求地址

            //11、最后会回到RestClient的execute方法真正发起的调用，最后真正是通过ApacheHttpClient4的ApacheHttpClient4Handler的handle发起调用

            //12、最终通过apache的HttpClient发起调用

            HttpResponse response = client.executeWithLoadBalancer(httpRequest); // 4
            System.out.println("Status code for " + JSON.toJSONString(response.getHttpHeaders().getAllHeaders()) + "  :" + response.getStatus());
        }
        ZoneAwareLoadBalancer lb = (ZoneAwareLoadBalancer) client.getLoadBalancer();
        System.out.println(lb.getLoadBalancerStats());

    }
}
