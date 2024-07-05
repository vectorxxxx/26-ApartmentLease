package com.atguigu.lease.web.admin.schedule;

import com.atguigu.lease.model.entity.LeaseAgreement;
import com.atguigu.lease.model.enums.LeaseStatus;
import com.atguigu.lease.web.admin.service.LeaseAgreementService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-05 17:28:06
 */
@Component
public class ScheduledTasks
{
    @Autowired
    private LeaseAgreementService leaseAgreementService;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkLeaseStatus() {
        leaseAgreementService.update(new LambdaUpdateWrapper<LeaseAgreement>()
                .le(LeaseAgreement::getLeaseEndDate, new Date())
                .eq(LeaseAgreement::getStatus, LeaseStatus.SIGNED)
                .in(LeaseAgreement::getStatus, LeaseStatus.SIGNED, LeaseStatus.WITHDRAWING));
    }
}
