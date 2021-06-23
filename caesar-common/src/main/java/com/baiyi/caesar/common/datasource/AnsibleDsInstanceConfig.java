package com.baiyi.caesar.common.datasource;

import com.baiyi.caesar.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsAnsibleConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/6/22 5:13 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AnsibleDsInstanceConfig extends BaseDsInstanceConfig {
    private DsAnsibleConfig.Ansible ansible;
}