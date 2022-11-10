package com.baiyi.opscloud.leo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/11/4 16:48
 * @Version 1.0
 */
public class LeoBaseModel {

    @Data
    @AllArgsConstructor
    public static class Parameter {

        private String name;
        private String value;
        private String description;

    }

    @Data
    @AllArgsConstructor
    public static class DsInstance {

        private String name;
        private String uuid;

    }

    @Data
    @AllArgsConstructor
    public static class GitLab {

        private LeoBaseModel.DsInstance instance;
        private GitLabProject project;

    }

    @Data
    @AllArgsConstructor
    public static class GitLabProject {

        private String sshUrl;
        private String branch;
        private GitLabCommit commit;

    }

    @Data
    @AllArgsConstructor
    public static class GitLabCommit {

        private String id;

    }

}