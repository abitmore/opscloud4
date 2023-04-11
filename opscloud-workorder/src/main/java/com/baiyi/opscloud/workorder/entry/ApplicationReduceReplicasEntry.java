package com.baiyi.opscloud.workorder.entry;

import com.baiyi.opscloud.common.util.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/4/10 18:21
 * @Version 1.0
 */
public class ApplicationReduceReplicasEntry {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KubernetesDeployment implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(name = "数据实例UUID")
        private String instanceUuid;

        @Schema(name = "namespace:deployment")
        private String name;

        private String namespace;

        private String deploymentName;

        @Schema(name = "原副本数量")
        private Integer replicas;

        @Schema(name = "缩容后的副本数量")
        private Integer reduceReplicas;

        private Integer id;

        private String comment;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}
