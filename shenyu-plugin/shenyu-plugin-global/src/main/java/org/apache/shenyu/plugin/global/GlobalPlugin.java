/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.plugin.global;

import org.apache.commons.lang3.StringUtils;
import org.apache.shenyu.common.constant.Constants;
import org.apache.shenyu.common.enums.PluginEnum;
import org.apache.shenyu.plugin.api.ShenyuPlugin;
import org.apache.shenyu.plugin.api.ShenyuPluginChain;
import org.apache.shenyu.plugin.api.context.ShenyuContext;
import org.apache.shenyu.plugin.api.context.ShenyuContextBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * The type Global plugin.
 */
public class GlobalPlugin implements ShenyuPlugin {
    
    private final ShenyuContextBuilder builder;
    
    /**
     * Instantiates a new Global plugin.
     *
     * @param builder the builder
     */
    public GlobalPlugin(final ShenyuContextBuilder builder) {
        this.builder = builder;
    }
    
    @Override
    public Mono<Void> execute(final ServerWebExchange exchange, final ShenyuPluginChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final HttpHeaders headers = request.getHeaders();
        final String upgrade = headers.getFirst("Upgrade");
        ShenyuContext shenyuContext;
        if (StringUtils.isBlank(upgrade) || !"websocket".equals(upgrade)) {
            shenyuContext = builder.build(exchange);
        } else {
            final MultiValueMap<String, String> queryParams = request.getQueryParams();
            shenyuContext = transformMap(queryParams);
        }
        exchange.getAttributes().put(Constants.CONTEXT, shenyuContext);
        return chain.execute(exchange);
    }
    
    @Override
    public int getOrder() {
        return PluginEnum.GLOBAL.getCode();
    }
    
    private ShenyuContext transformMap(final MultiValueMap<String, String> queryParams) {
        ShenyuContext shenyuContext = new ShenyuContext();
        shenyuContext.setModule(queryParams.getFirst(Constants.MODULE));
        shenyuContext.setMethod(queryParams.getFirst(Constants.METHOD));
        shenyuContext.setRpcType(queryParams.getFirst(Constants.RPC_TYPE));
        return shenyuContext;
    }
    
    @Override
    public String named() {
        return PluginEnum.GLOBAL.getName();
    }
}
