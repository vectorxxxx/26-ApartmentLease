package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.BrowsingHistory;
import com.atguigu.lease.web.app.mapper.BrowsingHistoryMapper;
import com.atguigu.lease.web.app.service.BrowsingHistoryService;
import com.atguigu.lease.web.app.vo.history.HistoryItemVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liubo
 * @description 针对表【browsing_history(浏览历史)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory> implements BrowsingHistoryService
{
    @Override
    public IPage<HistoryItemVo> pageHistoryItemByUserId(Page<HistoryItemVo> page, Long userId) {
        return baseMapper.pageHistoryItemByUserId(page, userId);
    }

    @Override
    // 保存浏览历史不应影响正常查看房间详情信息，所以采用异步的方式处理
    @Async
    public void saveHistory(Long userId, Long roomId) {
        BrowsingHistory browsingHistory = baseMapper.selectOne(new LambdaQueryWrapper<BrowsingHistory>()
                .eq(BrowsingHistory::getUserId, userId)
                .eq(BrowsingHistory::getRoomId, roomId));

        if (browsingHistory != null) {
            browsingHistory.setBrowseTime(new Date());
            baseMapper.updateById(browsingHistory);
        }
        else {
            baseMapper.insert(BrowsingHistory
                    .builder()
                    .userId(userId)
                    .roomId(roomId)
                    .browseTime(new Date())
                    .build());
        }
    }
}
