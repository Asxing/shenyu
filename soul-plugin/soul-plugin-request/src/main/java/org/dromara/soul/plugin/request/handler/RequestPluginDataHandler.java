package org.dromara.soul.plugin.request.handler;

import org.dromara.soul.common.dto.RuleData;
import org.dromara.soul.common.dto.convert.RequestHandle;
import org.dromara.soul.common.enums.PluginEnum;
import org.dromara.soul.common.utils.GsonUtils;
import org.dromara.soul.plugin.base.handler.PluginDataHandler;
import org.dromara.soul.plugin.request.cache.RequestRuleHandleCache;

import java.util.Optional;

/**
 * The type request plugin data subscriber.
 * 
 * @author Asxing
 */
public class RequestPluginDataHandler implements PluginDataHandler {
    
    @Override
    public void handlerRule(final RuleData ruleData) {
        Optional.ofNullable(ruleData.getHandle()).ifPresent(s -> {
            final RequestHandle requestHandle = GsonUtils.getInstance().fromJson(s, RequestHandle.class);
            RequestRuleHandleCache.getInstance().cachedHandle(getCacheKeyName(ruleData), requestHandle);
        });
        PluginDataHandler.super.handlerRule(ruleData);
    }


    /**
     * return rule handle cache key name.
     *
     * @param ruleData ruleData
     * @return string string
     */
    public static String getCacheKeyName(final RuleData ruleData) {
        return ruleData.getSelectorId() + "_" + ruleData.getName();
    }

    @Override
    public void removeRule(final RuleData ruleData) {
        Optional.ofNullable(ruleData.getHandle()).ifPresent(s ->
                RequestRuleHandleCache.getInstance().removeHandle(getCacheKeyName(ruleData)));
    }

    @Override
    public String pluginNamed() {
        return PluginEnum.REQUEST.getName();
    }
}
