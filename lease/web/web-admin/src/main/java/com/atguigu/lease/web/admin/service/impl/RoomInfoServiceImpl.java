package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.ApartmentInfo;
import com.atguigu.lease.model.entity.FacilityInfo;
import com.atguigu.lease.model.entity.GraphInfo;
import com.atguigu.lease.model.entity.LabelInfo;
import com.atguigu.lease.model.entity.LeaseTerm;
import com.atguigu.lease.model.entity.PaymentType;
import com.atguigu.lease.model.entity.RoomAttrValue;
import com.atguigu.lease.model.entity.RoomFacility;
import com.atguigu.lease.model.entity.RoomInfo;
import com.atguigu.lease.model.entity.RoomLabel;
import com.atguigu.lease.model.entity.RoomLeaseTerm;
import com.atguigu.lease.model.entity.RoomPaymentType;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.ApartmentInfoMapper;
import com.atguigu.lease.web.admin.mapper.AttrValueMapper;
import com.atguigu.lease.web.admin.mapper.FacilityInfoMapper;
import com.atguigu.lease.web.admin.mapper.GraphInfoMapper;
import com.atguigu.lease.web.admin.mapper.LabelInfoMapper;
import com.atguigu.lease.web.admin.mapper.LeaseTermMapper;
import com.atguigu.lease.web.admin.mapper.PaymentTypeMapper;
import com.atguigu.lease.web.admin.mapper.RoomInfoMapper;
import com.atguigu.lease.web.admin.service.GraphInfoService;
import com.atguigu.lease.web.admin.service.RoomAttrValueService;
import com.atguigu.lease.web.admin.service.RoomFacilityService;
import com.atguigu.lease.web.admin.service.RoomInfoService;
import com.atguigu.lease.web.admin.service.RoomLabelService;
import com.atguigu.lease.web.admin.service.RoomLeaseTermService;
import com.atguigu.lease.web.admin.service.RoomPaymentTypeService;
import com.atguigu.lease.web.admin.vo.attr.AttrValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.atguigu.lease.web.admin.vo.room.RoomDetailVo;
import com.atguigu.lease.web.admin.vo.room.RoomItemVo;
import com.atguigu.lease.web.admin.vo.room.RoomQueryVo;
import com.atguigu.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo> implements RoomInfoService
{

    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private RoomAttrValueService roomAttrValueService;
    @Autowired
    private RoomFacilityService roomFacilityService;
    @Autowired
    private RoomLabelService roomLabelService;
    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;
    @Autowired
    private RoomLeaseTermService roomLeaseTermService;

    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
        super.saveOrUpdate(roomSubmitVo);

        //若为更新操作，则先删除与Room相关的各项信息列表
        final Long roomId = roomSubmitVo.getId();
        if (roomId != null) {
            //1.删除原有graphInfoList
            graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>()
                    .eq(GraphInfo::getItemType, ItemType.ROOM)
                    .eq(GraphInfo::getItemId, roomId));

            //2.删除原有roomAttrValueList
            roomAttrValueService.remove(new LambdaQueryWrapper<RoomAttrValue>().eq(RoomAttrValue::getRoomId, roomId));

            //3.删除原有roomFacilityList
            roomFacilityService.remove(new LambdaQueryWrapper<RoomFacility>().eq(RoomFacility::getRoomId, roomId));

            //4.删除原有roomLabelList
            roomLabelService.remove(new LambdaQueryWrapper<RoomLabel>().eq(RoomLabel::getRoomId, roomId));

            //5.删除原有paymentTypeList
            roomPaymentTypeService.remove(new LambdaQueryWrapper<RoomPaymentType>().eq(RoomPaymentType::getRoomId, roomId));

            //6.删除原有leaseTermList
            roomLeaseTermService.remove(new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, roomId));
        }

        //1.保存新的graphInfoList
        List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)) {
            List<GraphInfo> graphInfoList = graphVoList
                    .stream()
                    .map(graphVo -> GraphInfo
                            .builder()
                            .itemType(ItemType.ROOM)
                            .itemId(roomId)
                            .name(graphVo.getName())
                            .url(graphVo.getUrl())
                            .build())
                    .collect(Collectors.toList());
            graphInfoService.saveBatch(graphInfoList);
        }

        //2.保存新的roomAttrValueList
        List<Long> attrValueIds = roomSubmitVo.getAttrValueIds();
        if (!CollectionUtils.isEmpty(attrValueIds)) {
            List<RoomAttrValue> roomAttrValueList = attrValueIds
                    .stream()
                    .map(attrValueId -> RoomAttrValue
                            .builder()
                            .roomId(roomId)
                            .attrValueId(attrValueId)
                            .build())
                    .collect(Collectors.toList());
            roomAttrValueService.saveBatch(roomAttrValueList);
        }

        //3.保存新的facilityInfoList
        List<Long> facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)) {
            List<RoomFacility> roomFacilityList = facilityInfoIds
                    .stream()
                    .map(facilityInfoId -> RoomFacility
                            .builder()
                            .roomId(roomId)
                            .facilityId(facilityInfoId)
                            .build())
                    .collect(Collectors.toList());
            roomFacilityService.saveBatch(roomFacilityList);
        }

        //4.保存新的labelInfoList
        List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (!CollectionUtils.isEmpty(labelInfoIds)) {
            List<RoomLabel> roomLabelList = labelInfoIds
                    .stream()
                    .map(labelInfoId -> RoomLabel
                            .builder()
                            .roomId(roomId)
                            .labelId(labelInfoId)
                            .build())
                    .collect(Collectors.toList());
            roomLabelService.saveBatch(roomLabelList);
        }

        //5.保存新的paymentTypeList
        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if (!CollectionUtils.isEmpty(paymentTypeIds)) {
            List<RoomPaymentType> roomPaymentTypeList = paymentTypeIds
                    .stream()
                    .map(paymentTypeId -> RoomPaymentType
                            .builder()
                            .roomId(roomId)
                            .paymentTypeId(paymentTypeId)
                            .build())
                    .collect(Collectors.toList());
            roomPaymentTypeService.saveBatch(roomPaymentTypeList);
        }

        //6.保存新的leaseTermList
        List<Long> leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (!CollectionUtils.isEmpty(leaseTermIds)) {
            List<RoomLeaseTerm> roomLeaseTerms = leaseTermIds
                    .stream()
                    .map(leaseTermId -> RoomLeaseTerm
                            .builder()
                            .roomId(roomId)
                            .leaseTermId(leaseTermId)
                            .build())
                    .collect(Collectors.toList());
            roomLeaseTermService.saveBatch(roomLeaseTerms);
        }
    }

    @Override
    public IPage<RoomItemVo> pageRoomItemByQuery(IPage<RoomItemVo> page, RoomQueryVo queryVo) {
        return baseMapper.pageRoomItemByQuery(page, queryVo);
    }

    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
    @Autowired
    private LabelInfoMapper labelInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Override
    public RoomDetailVo getRoomDetailById(Long id) {
        //1.查询RoomInfo
        RoomInfo roomInfo = roomInfoMapper.selectById(id);

        //2.查询所属公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(roomInfo.getApartmentId());

        //3.查询graphInfoList
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);

        //4.查询attrValueList
        List<AttrValueVo> attrvalueVoList = attrValueMapper.selectListByRoomId(id);

        //5.查询facilityInfoList
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);

        //6.查询labelInfoList
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByRoomId(id);

        //7.查询paymentTypeList
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectListByRoomId(id);

        //8.查询leaseTermList
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectListByRoomId(id);

        RoomDetailVo adminRoomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, adminRoomDetailVo);
        adminRoomDetailVo.setApartmentInfo(apartmentInfo);
        adminRoomDetailVo.setGraphVoList(graphVoList);
        adminRoomDetailVo.setAttrValueVoList(attrvalueVoList);
        adminRoomDetailVo.setFacilityInfoList(facilityInfoList);
        adminRoomDetailVo.setLabelInfoList(labelInfoList);
        adminRoomDetailVo.setPaymentTypeList(paymentTypeList);
        adminRoomDetailVo.setLeaseTermList(leaseTermList);
        return adminRoomDetailVo;
    }

    @Override
    public void removeRoomById(Long id) {
        //1.删除RoomInfo
        super.removeById(id);

        //2.删除graphInfoList
        graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>()
                .eq(GraphInfo::getItemType, ItemType.ROOM)
                .eq(GraphInfo::getItemId, id));

        //3.删除attrValueList
        roomAttrValueService.remove(new LambdaQueryWrapper<RoomAttrValue>().eq(RoomAttrValue::getRoomId, id));

        //4.删除facilityInfoList
        roomFacilityService.remove(new LambdaQueryWrapper<RoomFacility>().eq(RoomFacility::getRoomId, id));

        //5.删除labelInfoList
        roomLabelService.remove(new LambdaQueryWrapper<RoomLabel>().eq(RoomLabel::getRoomId, id));

        //6.删除paymentTypeList
        roomPaymentTypeService.remove(new LambdaQueryWrapper<RoomPaymentType>().eq(RoomPaymentType::getRoomId, id));

        //7.删除leaseTermList
        roomLeaseTermService.remove(new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, id));
    }
}



