package com.baiyi.opscloud.leo.build;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.log.BuildingLogHelper;
import com.baiyi.opscloud.service.leo.LeoBuildService;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/11/14 17:06
 * @Version 1.0
 */
public abstract class BaseBuildHandler {

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected BuildingLogHelper logHelper;

    @Resource
    protected LeoBuildService leoBuildService;

    private BaseBuildHandler next;

    protected JenkinsConfig getJenkinsConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(uuid);
        return dsConfigHelper.build(dsConfig, JenkinsConfig.class);
    }

    public void setNextHandler(BaseBuildHandler next) {
        this.next = next;
    }

    public BaseBuildHandler getNext() {
        return next;
    }

    public void handleRequest(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        this.handle(leoBuild, buildConfig);
        if (getNext() != null) {
            try {
                getNext().handleRequest(leoBuild, buildConfig);
            } catch (LeoBuildException e) {
                // 记录日志
                logHelper.error(leoBuild, e.getMessage());
                throw e;
            }
        }
    }

    /**
     * 抽象方法，具体实现
     * @param leoBuild
     * @param buildConfig
     */
    protected abstract void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig);

}