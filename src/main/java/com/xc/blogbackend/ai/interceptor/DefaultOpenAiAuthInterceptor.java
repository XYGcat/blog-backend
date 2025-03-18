package com.xc.blogbackend.ai.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 默认的OpenAi鉴权拦截器
 *
 * @author xc
 */
@Slf4j
public class DefaultOpenAiAuthInterceptor extends OpenAiAuthInterceptor {
    /**
     * 请求头处理
     */
    public DefaultOpenAiAuthInterceptor() {
        super.setWarringConfig(null);
    }

    /**
     * 构造方法
     *
     * @param warringConfig 所有的key都失效后的告警参数配置
     */
    public DefaultOpenAiAuthInterceptor(Map warringConfig) {
        super.setWarringConfig(warringConfig);
    }

    /**
     * 拦截器鉴权
     *
     * @param chain Chain
     * @return Response对象
     * @throws IOException io异常
     */
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        return chain.proceed(auth(super.getKey(), original));
    }

    @Override
    protected List<String> onErrorDealApiKeys(String errorKey) {
        return Collections.emptyList();
    }

    @Override
    protected void noHaveActiveKeyWarring() {
        log.error("--------> [告警] 没有可用的key！！！");
        return;
    }
}
