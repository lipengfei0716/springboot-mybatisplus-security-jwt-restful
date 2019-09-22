package com.github.base.db.mapper.primary.manage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.github.common.config.mybatis.cache.MybatisRedisCacheConfig;
import com.github.common.db.entity.primary.User;

import java.util.Collection;

@Component
@CacheNamespace(implementation = MybatisRedisCacheConfig.class)
public interface UserMapper extends BaseMapper<User> {
    int insertBatch(@Param(value = "items") Collection idList);
}