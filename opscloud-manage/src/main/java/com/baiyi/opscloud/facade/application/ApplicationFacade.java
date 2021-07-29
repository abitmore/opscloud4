package com.baiyi.opscloud.facade.application;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;

/**
 * @Author baiyi
 * @Date 2021/7/12 12:58 下午
 * @Version 1.0
 */
public interface ApplicationFacade {

    DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery);

    DataTable<ApplicationVO.Application> queryApplicationPageByWebTerminal(ApplicationParam.ApplicationPageQuery pageQuery);

    ApplicationVO.Application queryApplicationById(ApplicationParam.Query query);

    void addApplication(ApplicationVO.Application application);

    void updateApplication(ApplicationVO.Application application);

    void deleteApplication(Integer id);

    void bindApplicationResource(ApplicationResourceVO.Resource resource);

    void unbindApplicationResource(Integer id);
}