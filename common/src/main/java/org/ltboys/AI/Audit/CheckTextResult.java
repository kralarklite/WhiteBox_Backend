package org.ltboys.AI.Audit;

/**
 * CheckTextResult.java
 * com.prereadweb.baidu.api.entity
 * Copyright (c) 2019, 北京聚智未来科技有限公司版权所有.
 */
import lombok.Data;

/**
 * @Description: 接口返回
 * @author: ZZX
 * @date: 2023/11/28
 */
@Data
public class CheckTextResult {

    /* 正确调用生成的唯一标识码，用于问题定位 */
    private long log_id;

    /* 包含审核结果详情 */
    private Result result;
}