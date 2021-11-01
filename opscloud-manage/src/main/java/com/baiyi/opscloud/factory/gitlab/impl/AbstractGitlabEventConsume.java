package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitlabNotifyParam;
import com.baiyi.opscloud.facade.datasource.DsInstanceFacade;
import com.baiyi.opscloud.facade.event.EventFacade;
import com.baiyi.opscloud.factory.gitlab.GitlabEventConsumeFactory;
import com.baiyi.opscloud.factory.gitlab.GitlabEventNameEnum;
import com.baiyi.opscloud.factory.gitlab.IGitlabEventConsume;
import com.baiyi.opscloud.factory.gitlab.context.GitlabEventContext;
import com.baiyi.opscloud.factory.gitlab.convert.SystemHookConvert;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/10/29 10:54 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractGitlabEventConsume implements IGitlabEventConsume, InitializingBean {

    @Resource
    private EventFacade eventFacade;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    protected DsInstanceFacade dsInstanceFacade;

    protected abstract GitlabEventNameEnum[] getEventNameEnums();

    protected final ThreadLocal<GitlabEventContext> eventContext = ThreadLocal.withInitial(GitlabEventContext::new);

    @Override
    public List<String> getEventNames() {
        return Arrays.stream(getEventNameEnums()).map(e -> e.name().toLowerCase()).collect(Collectors.toList());
    }

    @Override
    @Async(value = Global.TaskPools.DEFAULT)
    public void consumeEventV4(DatasourceInstance instance, GitlabNotifyParam.SystemHook systemHook) {
        eventContext.get().setInstance(instance);
        eventContext.get().setSystemHook(systemHook);
        preHandle();
        proceed();
        postHandle();
    }

    /**
     * TODO 这只是偷懒写法(全量同步)，最好重写
     */
    protected void proceed() {
        List<SimpleAssetProvider> providers = AssetProviderFactory.getProviders(eventContext.get().getInstance().getInstanceType(), getAssetType());
        assert providers != null;
        providers.forEach(x -> x.pullAsset(eventContext.get().getInstance().getId()));
    }

    protected abstract String getAssetType();

    /**
     * 预处理
     */
    private void preHandle() {
        Event event = SystemHookConvert.toEvent(eventContext.get().getInstance(), eventContext.get().getSystemHook());
        eventFacade.recordEvent(event);
        eventContext.get().setEvent(event);
    }

    /**
     * 后处理，这只是通用写法
     */
    protected void postHandle() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GitlabEventConsumeFactory.register(this);
    }

}