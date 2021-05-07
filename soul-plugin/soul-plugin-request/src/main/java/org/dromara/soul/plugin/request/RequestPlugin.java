package org.dromara.soul.plugin.request;

import lombok.extern.slf4j.Slf4j;
import org.dromara.soul.common.dto.RuleData;
import org.dromara.soul.common.dto.SelectorData;
import org.dromara.soul.common.dto.convert.RequestHandle;
import org.dromara.soul.common.enums.PluginEnum;
import org.dromara.soul.plugin.api.SoulPluginChain;
import org.dromara.soul.plugin.base.AbstractSoulPlugin;
import org.dromara.soul.plugin.request.cache.RequestRuleHandleCache;
import org.dromara.soul.plugin.request.handler.RequestPluginDataHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
public class RequestPlugin extends AbstractSoulPlugin {

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, SoulPluginChain chain, SelectorData selector, RuleData rule) {
        String handle = rule.getHandle();
        final RequestHandle requestHandle = RequestRuleHandleCache.getInstance()
                .obtainHandle(RequestPluginDataHandler.getCacheKeyName(rule));
        if (Objects.isNull(requestHandle)) {
            log.error("uri request rule can not configuration: {}", handle);
            return chain.execute(exchange);
        }
        return chain.execute(exchange.mutate().request(exchange.getRequest()).response(exchange.getResponse()).build());
    }

    @Override
    public int getOrder() {
        return PluginEnum.REQUEST.getCode();
    }

    @Override
    public String named() {
        return PluginEnum.REQUEST.getName();
    }
}
