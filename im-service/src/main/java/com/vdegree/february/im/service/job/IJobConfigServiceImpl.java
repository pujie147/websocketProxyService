package com.vdegree.february.im.service.job;

import com.google.common.collect.Lists;
import com.vdpub.crontab.bean.ScheduleJobEntity;
import com.vdpub.crontab.bean.vo.ScheduleJobVo;
import com.vdpub.crontab.service.IJobConfigService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO 没有用只是为了不报错
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/2 17:24
 */
@Component
public class IJobConfigServiceImpl implements IJobConfigService {
    @Override
    public List<ScheduleJobVo> getAll(ScheduleJobEntity jobEntity) {
        return Lists.newArrayList();
    }
}
