package com.baiyi.caesar.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.builder.AssetContainerBuilder;
import com.baiyi.caesar.datasource.util.TimeUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/18 3:31 下午
 * @Version 1.0
 */
public class ComputeAssetConvert {

    private static final String VPC = "vpc";

    private static final String PRE_PAID = "PrePaid";

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeUtil.Format.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getInstanceId()) // 资产id = 实例id
                .name(entry.getInstanceName())
                // priveteIp
                .assetKey(entry.getInstanceNetworkType().equals(VPC) ? entry.getVpcAttributes().getPrivateIpAddress().get(0) :
                        entry.getInnerIpAddress().get(0))
                // publicIp
                .assetKey2(entry.getPublicIpAddress().size() != 0 ? entry.getPublicIpAddress().get(0) : "")
                .assetType(DsAssetTypeEnum.ECS.name())
                .kind(entry.getInstanceType())
                .regionId(entry.getRegionId())
                .zone(entry.getZoneId())
                .createdTime(toGmtDate(entry.getCreationTime()))
                .expiredTime(
                        entry.getInstanceChargeType().equalsIgnoreCase(PRE_PAID) && !StringUtils.isEmpty(entry.getExpiredTime())
                                ? toGmtDate(entry.getExpiredTime()) : null)
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("cpu", entry.getCpu())
                .paramProperty("vpcId", entry.getVpcAttributes() != null ? entry.getVpcAttributes().getVpcId() : "")
                .paramProperty("memory", entry.getMemory())
                .paramProperty("chargeType", entry.getInstanceChargeType())
                .paramProperty("imageId", entry.getImageId())
                .paramProperty("osName", entry.getOSName())
                .paramProperty("osType", entry.getOSType())
                .build();
    }
}