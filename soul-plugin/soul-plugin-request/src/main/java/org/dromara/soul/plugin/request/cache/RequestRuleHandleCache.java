package org.dromara.soul.plugin.request.cache;

import org.dromara.soul.common.dto.convert.RequestHandle;
import org.dromara.soul.plugin.base.cache.BaseHandleCache;

/**
 * The rule handle cache.
 *
 * @author Asxing
 */
public class RequestRuleHandleCache extends BaseHandleCache<String, RequestHandle> {

    private RequestRuleHandleCache() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RequestRuleHandleCache getInstance() {
        return RequestRuleHandleCacheInstance.INSTANCE;
    }

    /**
     * The type rule handle cache instance.
     */
    static class RequestRuleHandleCacheInstance {
        /**
         * The instance.
         */
        static final RequestRuleHandleCache INSTANCE = new RequestRuleHandleCache();
    }
}
