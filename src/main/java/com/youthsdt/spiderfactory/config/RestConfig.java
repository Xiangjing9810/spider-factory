package com.youthsdt.spiderfactory.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/27 20:16
 */
@Configuration
public class RestConfig {
    @Value("${elasticsearch.Authorization}")
    private String authorization;
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private Integer port;

    @Bean("EsRestClient")
    public RestHighLevelClient getClient() {
        // 如果有多个从节点可以持续在内部new多个HttpHost，参数1是ip,参数2是HTTP端口，参数3是通信协
        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost(host, port, "http"));
        Header[] defaultHeaders = {new BasicHeader("Authorization", authorization)};
        clientBuilder.setDefaultHeaders(defaultHeaders);
        // 设置监听器，每次节点失败都可以监听到，可以作额外处理
        clientBuilder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
                System.out.println(node.getName() + "==节点失败了");
            }
        });
        clientBuilder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
        clientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultIOReactorConfig(
                IOReactorConfig.custom().setIoThreadCount(1).build()
        ));
        clientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            // 连接5秒超时，套接字连接60s超时
            return requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000);
        });
        return new RestHighLevelClient(clientBuilder);
    }
}
