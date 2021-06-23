package com.baiyi.caesar.event.listener;

import com.baiyi.caesar.domain.annotation.TagClear;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.event.param.ServerGroupEventParam;
import com.baiyi.caesar.facade.server.ServerGroupFacade;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/16 10:22 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class ServerGroupDeleteEventListener {

    @Resource
    private ServerGroupFacade serverGroupFacade;


    @Subscribe
    @TagClear(type = BusinessTypeEnum.SERVERGROUP)
    public void clearTag(ServerGroupEventParam.delete delete) {
    }

    @Subscribe
    public void ServerGroupCacheEvict(ServerGroupEventParam.delete delete) {
        serverGroupFacade.ServerGroupCacheEvict(delete.getId());
    }
}