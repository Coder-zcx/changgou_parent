package com.changgou.user.dao;

import com.changgou.user.pojo.PointLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/5 22:02
 */
public interface PointLogMapper extends Mapper<PointLog> {

    @Select("select * from tb_point_log where order_id=#{orderId}")
    PointLog findLogInfoByOrderId(@Param("orderId") String orderId);
}
