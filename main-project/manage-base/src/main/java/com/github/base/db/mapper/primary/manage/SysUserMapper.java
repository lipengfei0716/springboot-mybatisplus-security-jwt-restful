package com.github.base.db.mapper.primary.manage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.config.mybatis.cache.RedisCache;
import com.github.common.db.entity.primary.SysUser;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;

@Component
@CacheNamespace(implementation = RedisCache.class)
public interface SysUserMapper extends BaseMapper<SysUser> {

}