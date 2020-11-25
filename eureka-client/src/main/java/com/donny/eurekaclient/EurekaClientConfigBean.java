package com.donny.eurekaclient;

import com.netflix.appinfo.EurekaAccept;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.DefaultEurekaTransportConfig;
import com.netflix.discovery.shared.transport.EurekaTransportConfig;
import org.springframework.core.Ordered;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@ConfigurationProperties(EurekaClientConfigBean.PREFIX) PREFIX = "eureka.client";
public class EurekaClientConfigBean implements EurekaClientConfig, Ordered {

    public static final String DEFAULT_PREFIX = "/eureka";

    /**
     * eureka默认配置前缀
     */
    public static final String PREFIX = "eureka.client";

    /**
     * Eureka默认地址
     */
    public static final String DEFAULT_URL = "http://localhost:8761" + DEFAULT_PREFIX
            + "/";

    /**
     * 默认区域
     */
    public static final String DEFAULT_ZONE = "defaultZone";

    private static final int MINUTES = 60;


    PropertyResolver propertyResolver;

    /**
     * Flag to indicate that the Eureka client is enabled.
     */
    private boolean enabled = true;

    //todo
    private EurekaTransportConfig transport = new DefaultEurekaTransportConfig(null, com.netflix.config.DynamicPropertyFactory.getInstance());

    /**
     指示从eureka *服务器获取注册表信息的频率（以秒为单位）。
     */
    private int registryFetchIntervalSeconds = 30;

    /**
     *指示将实例更改复制到*尤里卡服务器的频率（以秒为单位）。
     */
    private int instanceInfoReplicationIntervalSeconds = 30;

    /**
     *表示最初将实例信息复制到eureka *服务器的时间（以秒为单位）
     */
    private int initialInstanceInfoReplicationIntervalSeconds = 40;

    /**
     *指示轮询尤里卡服务器信息更改的频率（以秒为单位）。 *可以添加或删除Eureka服务器，并且此设置控制* eureka客户端应该多久知道一次。
     */
    private int eurekaServiceUrlPollIntervalSeconds = 5 * MINUTES;

    /**
     * Gets the proxy port to eureka server if any.
     */
    private String proxyPort;

    /**
     * Gets the proxy host to eureka server if any.
     */
    private String proxyHost;

    /**
     * Gets the proxy user name if any.
     */
    private String proxyUserName;

    /**
     * Gets the proxy password if any.
     */
    private String proxyPassword;

    /**
     *指示从eureka服务器读取需要等待多长时间（以秒为单位）。
     */
    private int eurekaServerReadTimeoutSeconds = 8;

    /**
     *指示与eureka服务器的连接需要*等待超时的时间（以秒为单位）。
     * 请注意，客户端中的连接由org.apache.http.client.HttpClient池化，
     * 此设置会影响实际的连接创建以及从池中获取连接的等待时间。
     */
    private int eurekaServerConnectTimeoutSeconds = 5;

    /**
     * Gets the name of the implementation which implements BackupRegistry to fetch the
     * registry information as a fall back option for only the first time when the eureka
     * client starts.
     *
     * This may be needed for applications which needs additional resiliency for registry
     * information without which it cannot operate.
     */
    private String backupRegistryImpl;

    /**
     *获取从eureka客户端到所有* eureka服务器的允许的连接总数。
     */
    private int eurekaServerTotalConnections = 200;

    /**
     * Gets the total number of connections that is allowed from eureka client to a eureka
     * server host.
     */
    private int eurekaServerTotalConnectionsPerHost = 50;

    /**
     * Gets the URL context to be used to construct the service url to contact eureka
     * server when the list of eureka servers come from the DNS. This information is not
     * required if the contract returns the service urls from eurekaServerServiceUrls.
     *
     * The DNS mechanism is used when useDnsForFetchingServiceUrls is set to true and the
     * eureka client expects the DNS to configured a certain way so that it can fetch
     * changing eureka servers dynamically. The changes are effective at runtime.
     */
    private String eurekaServerURLContext;

    /**
     * Gets the port to be used to construct the service url to contact eureka server when
     * the list of eureka servers come from the DNS.This information is not required if
     * the contract returns the service urls eurekaServerServiceUrls(String).
     *
     * The DNS mechanism is used when useDnsForFetchingServiceUrls is set to true and the
     * eureka client expects the DNS to configured a certain way so that it can fetch
     * changing eureka servers dynamically.
     *
     * The changes are effective at runtime.
     */
    private String eurekaServerPort;

    /**
     * Gets the DNS name to be queried to get the list of eureka servers.This information
     * is not required if the contract returns the service urls by implementing
     * serviceUrls.
     *
     * The DNS mechanism is used when useDnsForFetchingServiceUrls is set to true and the
     * eureka client expects the DNS to configured a certain way so that it can fetch
     * changing eureka servers dynamically.
     *
     * The changes are effective at runtime.
     */
    private String eurekaServerDNSName;

    /**
     * Gets the region (used in AWS datacenters) where this instance resides.
     */
    private String region = "us-east-1";

    /**
     *指示与eureka服务器的HTTP连接可以空闲多少时间（以秒为单位），然后才能关闭它。
     *  * *在AWS环境中，建议将该值设置为30秒或更短，因为防火墙会在几分钟后清除连接信息，
     *  使连接处于混乱状态，因此建议该值不超过30秒。
     */
    private int eurekaConnectionIdleTimeoutSeconds = 30;

    /**
     * Indicates whether the client is only interested in the registry information for a
     * single VIP.
     */
    private String registryRefreshSingleVipAddress;

    /**
     * The thread pool size for the heartbeatExecutor to initialise with.
     */
    private int heartbeatExecutorThreadPoolSize = 2;

    /**
     * Heartbeat executor exponential back off related property. It is a maximum
     * multiplier value for retry delay, in case where a sequence of timeouts occurred.
     */
    private int heartbeatExecutorExponentialBackOffBound = 10;

    /**
     * The thread pool size for the cacheRefreshExecutor to initialise with.
     */
    private int cacheRefreshExecutorThreadPoolSize = 2;

    /**
     * Cache refresh executor exponential back off related property. It is a maximum
     * multiplier value for retry delay, in case where a sequence of timeouts occurred.
     */
    private int cacheRefreshExecutorExponentialBackOffBound = 10;

    /**
     * Map of availability zone to list of fully qualified URLs to communicate with eureka
     * server. Each value can be a single URL or a comma separated list of alternative
     * locations.
     *
     * Typically the eureka server URLs carry protocol,host,port,context and version
     * information if any. Example:
     * https://ec2-256-156-243-129.compute-1.amazonaws.com:7001/eureka/
     *
     * The changes are effective at runtime at the next service url refresh cycle as
     * specified by eurekaServiceUrlPollIntervalSeconds.
     */
    private Map<String, String> serviceUrl = new HashMap<>();

    {
        this.serviceUrl.put(DEFAULT_ZONE, DEFAULT_URL);
    }

    /**
     * Indicates whether the content fetched from eureka server has to be compressed
     * whenever it is supported by the server. The registry information from the eureka
     * server is compressed for optimum network traffic.
     */
    private boolean gZipContent = true;

    /**
     * Indicates whether the eureka client should use the DNS mechanism to fetch a list of
     * eureka servers to talk to. When the DNS name is updated to have additional servers,
     * that information is used immediately after the eureka client polls for that
     * information as specified in eurekaServiceUrlPollIntervalSeconds.
     *
     * Alternatively, the service urls can be returned serviceUrls, but the users should
     * implement their own mechanism to return the updated list in case of changes.
     *
     * The changes are effective at runtime.
     */
    private boolean useDnsForFetchingServiceUrls = false;

    /**
     *指示该实例是否应在eureka服务器上注册其信息以供他人发现。 * *在某些情况下，您不希望发现实例，而您*只是希望发现其他实例。
     */
    private boolean registerWithEureka = true;

    /**
     *指示此实例是否应出于延迟和/或其他原因尝试在同一区域中使用eureka服务器。 * *理想情况下，
     * 将eureka客户端配置为与同一区域中的服务器进行通信* *这些更改在运行时在由registryFetchIntervalSeconds
     * 指定的下一个注册表获取周期有效
     */
    private boolean preferSameZoneEureka = true;

    /**
     * Indicates whether to log differences between the eureka server and the eureka
     * client in terms of registry information.
     *
     * Eureka client tries to retrieve only delta changes from eureka server to minimize
     * network traffic. After receiving the deltas, eureka client reconciles the
     * information from the server to verify it has not missed out some information.
     * Reconciliation failures could happen when the client has had network issues
     * communicating to server.If the reconciliation fails, eureka client gets the full
     * registry information.
     *
     * While getting the full registry information, the eureka client can log the
     * differences between the client and the server and this setting controls that.
     *
     * The changes are effective at runtime at the next registry fetch cycle as specified
     * by registryFetchIntervalSecondsr
     */
    private boolean logDeltaDiff;

    /**
     * Indicates whether the eureka client should disable fetching of delta and should
     * rather resort to getting the full registry information.
     *
     * Note that the delta fetches can reduce the traffic tremendously, because the rate
     * of change with the eureka server is normally much lower than the rate of fetches.
     *
     * The changes are effective at runtime at the next registry fetch cycle as specified
     * by registryFetchIntervalSeconds
     */
    private boolean disableDelta;

    /**
     *逗号分隔的区域列表，将获取*尤里卡注册表信息。必须定义每个区域的可用区*由availabilityZones返回。否则，将导致*发现客户端启动失败。
     *
     */
    private String fetchRemoteRegionsRegistry;

    /**
     * Gets the list of availability zones (used in AWS data centers) for the region in
     * which this instance resides.
     *
     * The changes are effective at runtime at the next registry fetch cycle as specified
     * by registryFetchIntervalSeconds.
     */
    private Map<String, String> availabilityZones = new HashMap<>();

    /**
     * Indicates whether to get the applications after filtering the applications for
     * instances with only InstanceStatus UP states.
     */
    private boolean filterOnlyUpInstances = true;

    /**
     *指示此客户端是否应从eureka *服务器获取eureka注册表信息。
     */
    private boolean fetchRegistry = true;

    /**
     * Get a replacement string for Dollar sign <code>$</code> during
     * serializing/deserializing information in eureka server.
     */
    private String dollarReplacement = "_-";

    /**
     * Get a replacement string for underscore sign <code>_</code> during serializing/
     * deserializing information in eureka server.
     */
    private String escapeCharReplacement = "__";

    /**
     * Indicates whether server can redirect a client request to a backup server/cluster.
     * If set to false, the server will handle the request directly, If set to true, it
     * may send HTTP redirect to the client, with a new server location.
     */
    private boolean allowRedirects = false;

    /**
     * If set to true, local status updates via ApplicationInfoManager will trigger
     * on-demand (but rate limited) register/updates to remote eureka servers.
     */
    private boolean onDemandUpdateStatusChange = true;

    /**
     * This is a transient config and once the latest codecs are stable, can be removed
     * (as there will only be one).
     */
    private String encoderName;

    /**
     * This is a transient config and once the latest codecs are stable, can be removed
     * (as there will only be one).
     */
    private String decoderName;

    /**
     * EurekaAccept name for client data accept.
     */
    private String clientDataAccept = EurekaAccept.full.name();

    /**
     *指示客户端是否应该在客户端关闭时从远程服务器显式注销自己。
     */
    private boolean shouldUnregisterOnShutdown = true;

    /**
     *指示客户端是否应在初始化期间强制注册。 *默认为false。
     */
    private boolean shouldEnforceRegistrationAtInit = false;

    /**
     * Order of the discovery client used by `CompositeDiscoveryClient` for sorting
     * available clients.
     */
    private int order = 0;

    @Override
    public boolean shouldGZipContent() {
        return this.gZipContent;
    }

    @Override
    public boolean shouldUseDnsForFetchingServiceUrls() {
        return this.useDnsForFetchingServiceUrls;
    }

    @Override
    public boolean shouldRegisterWithEureka() {
        return this.registerWithEureka;
    }

    @Override
    public boolean shouldPreferSameZoneEureka() {
        return this.preferSameZoneEureka;
    }

    @Override
    public boolean shouldLogDeltaDiff() {
        return this.logDeltaDiff;
    }

    @Override
    public boolean shouldDisableDelta() {
        return this.disableDelta;
    }

    public boolean shouldUnregisterOnShutdown() {
        return this.shouldUnregisterOnShutdown;
    }


    public boolean shouldEnforceRegistrationAtInit() {
        return this.shouldEnforceRegistrationAtInit;
    }

    @Override
    public String fetchRegistryForRemoteRegions() {
        return this.fetchRemoteRegionsRegistry;
    }

    @Override
    public String[] getAvailabilityZones(String region) {
        String value = this.availabilityZones.get(region);
        if (value == null) {
            value = DEFAULT_ZONE;
        }
        return value.split(",");
    }

    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        String serviceUrls = this.serviceUrl.get(myZone);
        if (serviceUrls == null || serviceUrls.isEmpty()) {
            serviceUrls = this.serviceUrl.get(DEFAULT_ZONE);
        }
        if (!StringUtils.isEmpty(serviceUrls)) {
            final String[] serviceUrlsSplit = StringUtils
                    .commaDelimitedListToStringArray(serviceUrls);
            List<String> eurekaServiceUrls = new ArrayList<>(serviceUrlsSplit.length);
            for (String eurekaServiceUrl : serviceUrlsSplit) {
                if (!endsWithSlash(eurekaServiceUrl)) {
                    eurekaServiceUrl += "/";
                }
                eurekaServiceUrls.add(eurekaServiceUrl.trim());
            }
            return eurekaServiceUrls;
        }

        return new ArrayList<>();
    }

    private boolean endsWithSlash(String url) {
        return url.endsWith("/");
    }


    @Override
    public boolean shouldFilterOnlyUpInstances() {
        return this.filterOnlyUpInstances;
    }


    @Override
    public boolean shouldFetchRegistry() {
        return this.fetchRegistry;
    }

    @Override
    public boolean allowRedirects() {
        return this.allowRedirects;
    }

    @Override
    public boolean shouldOnDemandUpdateStatusChange() {
        return this.onDemandUpdateStatusChange;
    }

    @Override
    public String getExperimental(String name) {
        if (this.propertyResolver != null) {
            return this.propertyResolver.getProperty(PREFIX + ".experimental." + name,
                    String.class, null);
        }
        return null;
    }

    @Override
    public EurekaTransportConfig getTransportConfig() {
        return getTransport();
    }

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public EurekaTransportConfig getTransport() {
        return transport;
    }

    public void setTransport(EurekaTransportConfig transport) {
        this.transport = transport;
    }

    @Override
    public int getRegistryFetchIntervalSeconds() {
        return registryFetchIntervalSeconds;
    }

    public void setRegistryFetchIntervalSeconds(int registryFetchIntervalSeconds) {
        this.registryFetchIntervalSeconds = registryFetchIntervalSeconds;
    }

    @Override
    public int getInstanceInfoReplicationIntervalSeconds() {
        return instanceInfoReplicationIntervalSeconds;
    }

    public void setInstanceInfoReplicationIntervalSeconds(
            int instanceInfoReplicationIntervalSeconds) {
        this.instanceInfoReplicationIntervalSeconds = instanceInfoReplicationIntervalSeconds;
    }

    @Override
    public int getInitialInstanceInfoReplicationIntervalSeconds() {
        return initialInstanceInfoReplicationIntervalSeconds;
    }

    public void setInitialInstanceInfoReplicationIntervalSeconds(
            int initialInstanceInfoReplicationIntervalSeconds) {
        this.initialInstanceInfoReplicationIntervalSeconds = initialInstanceInfoReplicationIntervalSeconds;
    }

    @Override
    public int getEurekaServiceUrlPollIntervalSeconds() {
        return eurekaServiceUrlPollIntervalSeconds;
    }

    public void setEurekaServiceUrlPollIntervalSeconds(
            int eurekaServiceUrlPollIntervalSeconds) {
        this.eurekaServiceUrlPollIntervalSeconds = eurekaServiceUrlPollIntervalSeconds;
    }

    @Override
    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Override
    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    @Override
    public String getProxyUserName() {
        return proxyUserName;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }

    @Override
    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    @Override
    public int getEurekaServerReadTimeoutSeconds() {
        return eurekaServerReadTimeoutSeconds;
    }

    public void setEurekaServerReadTimeoutSeconds(int eurekaServerReadTimeoutSeconds) {
        this.eurekaServerReadTimeoutSeconds = eurekaServerReadTimeoutSeconds;
    }

    @Override
    public int getEurekaServerConnectTimeoutSeconds() {
        return eurekaServerConnectTimeoutSeconds;
    }

    public void setEurekaServerConnectTimeoutSeconds(
            int eurekaServerConnectTimeoutSeconds) {
        this.eurekaServerConnectTimeoutSeconds = eurekaServerConnectTimeoutSeconds;
    }

    @Override
    public String getBackupRegistryImpl() {
        return backupRegistryImpl;
    }

    public void setBackupRegistryImpl(String backupRegistryImpl) {
        this.backupRegistryImpl = backupRegistryImpl;
    }

    @Override
    public int getEurekaServerTotalConnections() {
        return eurekaServerTotalConnections;
    }

    public void setEurekaServerTotalConnections(int eurekaServerTotalConnections) {
        this.eurekaServerTotalConnections = eurekaServerTotalConnections;
    }

    @Override
    public int getEurekaServerTotalConnectionsPerHost() {
        return eurekaServerTotalConnectionsPerHost;
    }

    public void setEurekaServerTotalConnectionsPerHost(
            int eurekaServerTotalConnectionsPerHost) {
        this.eurekaServerTotalConnectionsPerHost = eurekaServerTotalConnectionsPerHost;
    }

    @Override
    public String getEurekaServerURLContext() {
        return eurekaServerURLContext;
    }

    public void setEurekaServerURLContext(String eurekaServerURLContext) {
        this.eurekaServerURLContext = eurekaServerURLContext;
    }

    @Override
    public String getEurekaServerPort() {
        return eurekaServerPort;
    }

    public void setEurekaServerPort(String eurekaServerPort) {
        this.eurekaServerPort = eurekaServerPort;
    }

    @Override
    public String getEurekaServerDNSName() {
        return eurekaServerDNSName;
    }

    public void setEurekaServerDNSName(String eurekaServerDNSName) {
        this.eurekaServerDNSName = eurekaServerDNSName;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int getEurekaConnectionIdleTimeoutSeconds() {
        return eurekaConnectionIdleTimeoutSeconds;
    }

    public void setEurekaConnectionIdleTimeoutSeconds(
            int eurekaConnectionIdleTimeoutSeconds) {
        this.eurekaConnectionIdleTimeoutSeconds = eurekaConnectionIdleTimeoutSeconds;
    }

    @Override
    public String getRegistryRefreshSingleVipAddress() {
        return registryRefreshSingleVipAddress;
    }

    public void setRegistryRefreshSingleVipAddress(
            String registryRefreshSingleVipAddress) {
        this.registryRefreshSingleVipAddress = registryRefreshSingleVipAddress;
    }

    @Override
    public int getHeartbeatExecutorThreadPoolSize() {
        return heartbeatExecutorThreadPoolSize;
    }

    public void setHeartbeatExecutorThreadPoolSize(int heartbeatExecutorThreadPoolSize) {
        this.heartbeatExecutorThreadPoolSize = heartbeatExecutorThreadPoolSize;
    }

    @Override
    public int getHeartbeatExecutorExponentialBackOffBound() {
        return heartbeatExecutorExponentialBackOffBound;
    }

    public void setHeartbeatExecutorExponentialBackOffBound(
            int heartbeatExecutorExponentialBackOffBound) {
        this.heartbeatExecutorExponentialBackOffBound = heartbeatExecutorExponentialBackOffBound;
    }

    @Override
    public int getCacheRefreshExecutorThreadPoolSize() {
        return cacheRefreshExecutorThreadPoolSize;
    }

    public void setCacheRefreshExecutorThreadPoolSize(
            int cacheRefreshExecutorThreadPoolSize) {
        this.cacheRefreshExecutorThreadPoolSize = cacheRefreshExecutorThreadPoolSize;
    }

    @Override
    public int getCacheRefreshExecutorExponentialBackOffBound() {
        return cacheRefreshExecutorExponentialBackOffBound;
    }

    public void setCacheRefreshExecutorExponentialBackOffBound(
            int cacheRefreshExecutorExponentialBackOffBound) {
        this.cacheRefreshExecutorExponentialBackOffBound = cacheRefreshExecutorExponentialBackOffBound;
    }

    public Map<String, String> getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(Map<String, String> serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public boolean isgZipContent() {
        return gZipContent;
    }

    public void setgZipContent(boolean gZipContent) {
        this.gZipContent = gZipContent;
    }

    public boolean isUseDnsForFetchingServiceUrls() {
        return useDnsForFetchingServiceUrls;
    }

    public void setUseDnsForFetchingServiceUrls(boolean useDnsForFetchingServiceUrls) {
        this.useDnsForFetchingServiceUrls = useDnsForFetchingServiceUrls;
    }

    public boolean isRegisterWithEureka() {
        return registerWithEureka;
    }

    public void setRegisterWithEureka(boolean registerWithEureka) {
        this.registerWithEureka = registerWithEureka;
    }

    public boolean isPreferSameZoneEureka() {
        return preferSameZoneEureka;
    }

    public void setPreferSameZoneEureka(boolean preferSameZoneEureka) {
        this.preferSameZoneEureka = preferSameZoneEureka;
    }

    public boolean isLogDeltaDiff() {
        return logDeltaDiff;
    }

    public void setLogDeltaDiff(boolean logDeltaDiff) {
        this.logDeltaDiff = logDeltaDiff;
    }

    public boolean isDisableDelta() {
        return disableDelta;
    }

    public void setDisableDelta(boolean disableDelta) {
        this.disableDelta = disableDelta;
    }

    public String getFetchRemoteRegionsRegistry() {
        return fetchRemoteRegionsRegistry;
    }

    public void setFetchRemoteRegionsRegistry(String fetchRemoteRegionsRegistry) {
        this.fetchRemoteRegionsRegistry = fetchRemoteRegionsRegistry;
    }

    public Map<String, String> getAvailabilityZones() {
        return availabilityZones;
    }

    public void setAvailabilityZones(Map<String, String> availabilityZones) {
        this.availabilityZones = availabilityZones;
    }

    public boolean isFilterOnlyUpInstances() {
        return filterOnlyUpInstances;
    }

    public void setFilterOnlyUpInstances(boolean filterOnlyUpInstances) {
        this.filterOnlyUpInstances = filterOnlyUpInstances;
    }

    public boolean isFetchRegistry() {
        return fetchRegistry;
    }

    public void setFetchRegistry(boolean fetchRegistry) {
        this.fetchRegistry = fetchRegistry;
    }

    @Override
    public String getDollarReplacement() {
        return dollarReplacement;
    }

    public void setDollarReplacement(String dollarReplacement) {
        this.dollarReplacement = dollarReplacement;
    }

    @Override
    public String getEscapeCharReplacement() {
        return escapeCharReplacement;
    }

    public void setEscapeCharReplacement(String escapeCharReplacement) {
        this.escapeCharReplacement = escapeCharReplacement;
    }

    public boolean isAllowRedirects() {
        return allowRedirects;
    }

    public void setAllowRedirects(boolean allowRedirects) {
        this.allowRedirects = allowRedirects;
    }

    public boolean isOnDemandUpdateStatusChange() {
        return onDemandUpdateStatusChange;
    }

    public void setOnDemandUpdateStatusChange(boolean onDemandUpdateStatusChange) {
        this.onDemandUpdateStatusChange = onDemandUpdateStatusChange;
    }

    @Override
    public String getEncoderName() {
        return encoderName;
    }

    public void setEncoderName(String encoderName) {
        this.encoderName = encoderName;
    }

    @Override
    public String getDecoderName() {
        return decoderName;
    }

    public void setDecoderName(String decoderName) {
        this.decoderName = decoderName;
    }

    @Override
    public String getClientDataAccept() {
        return clientDataAccept;
    }

    public void setClientDataAccept(String clientDataAccept) {
        this.clientDataAccept = clientDataAccept;
    }

    public boolean isShouldUnregisterOnShutdown() {
        return shouldUnregisterOnShutdown;
    }

    public void setShouldUnregisterOnShutdown(boolean shouldUnregisterOnShutdown) {
        this.shouldUnregisterOnShutdown = shouldUnregisterOnShutdown;
    }

    public boolean isShouldEnforceRegistrationAtInit() {
        return shouldEnforceRegistrationAtInit;
    }

    public void setShouldEnforceRegistrationAtInit(
            boolean shouldEnforceRegistrationAtInit) {
        this.shouldEnforceRegistrationAtInit = shouldEnforceRegistrationAtInit;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }







}
