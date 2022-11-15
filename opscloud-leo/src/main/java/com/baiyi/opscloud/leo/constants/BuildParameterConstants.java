package com.baiyi.opscloud.leo.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/11/15 11:23
 * @Version 1.0
 */
public enum BuildParameterConstants {

    BRANCH("branch"),
    COMMIT_ID("commitId"),
    SSH_URL("sshUrl"),
    ENV("env"),
    JOB_BUILD_NUMBER("jobBuildNumber"),
    APPLICATION_NAME("applicationName");


    @Getter
    private final String param;

    BuildParameterConstants(String param) {
        this.param = param;
    }

}