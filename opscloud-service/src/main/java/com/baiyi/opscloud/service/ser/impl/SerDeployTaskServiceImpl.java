package com.baiyi.opscloud.service.ser.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTask;
import com.baiyi.opscloud.domain.param.ser.SerDeployParam;
import com.baiyi.opscloud.mapper.SerDeployTaskMapper;
import com.baiyi.opscloud.service.ser.SerDeployTaskService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/7 10:58 AM
 * @Since 1.0
 */
@Service
@RequiredArgsConstructor
public class SerDeployTaskServiceImpl implements SerDeployTaskService {

    private final SerDeployTaskMapper serDeployTaskMapper;

    @Override
    public void add(SerDeployTask serDeployTask) {
        serDeployTaskMapper.insert(serDeployTask);
    }

    @Override
    public void update(SerDeployTask serDeployTask) {
        serDeployTaskMapper.updateByPrimaryKey(serDeployTask);
    }

    @Override
    public SerDeployTask getById(Integer id) {
        return serDeployTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<SerDeployTask> queryPageByParam(SerDeployParam.TaskPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(SerDeployTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", pageQuery.getApplicationId());
        if (pageQuery.getIsFinish() != null) {
            criteria.andEqualTo("isFinish", pageQuery.getIsFinish());
        }
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        List<SerDeployTask> data = serDeployTaskMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }
}
