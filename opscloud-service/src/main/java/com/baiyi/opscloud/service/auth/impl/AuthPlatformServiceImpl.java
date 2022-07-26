package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatform;
import com.baiyi.opscloud.mapper.opscloud.AuthPlatformMapper;
import com.baiyi.opscloud.service.auth.AuthPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2022/7/26 11:22
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthPlatformServiceImpl implements AuthPlatformService {

    private final AuthPlatformMapper authPlatformMapper;

    @Override
    public AuthPlatform getByName(String name) {
        Example example = new Example(AuthPlatform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("name", name)
                .andEqualTo("isActive", true);
        return authPlatformMapper.selectOneByExample(example);
    }

}