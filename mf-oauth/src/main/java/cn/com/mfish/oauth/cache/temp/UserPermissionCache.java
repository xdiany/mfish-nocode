package cn.com.mfish.oauth.cache.temp;

import cn.com.mfish.common.redis.common.RedisPrefix;
import cn.com.mfish.common.redis.temp.BaseTempCache;
import cn.com.mfish.oauth.mapper.SsoUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ：qiufeng
 * @description：用户权限缓存
 * @date ：2022/12/5 22:06
 */
@Component
public class UserPermissionCache extends BaseTempCache<Set<String>> {
    @Resource
    SsoUserMapper ssoUserMapper;

    /**
     * key [0] userId [1] clientId
     *
     * @param key
     * @return
     */
    @Override
    protected String buildKey(String... key) {
        return RedisPrefix.buildUser2PermissionsKey(key[0], key[1]);
    }

    @Override
    protected Set<String> getFromDB(String... key) {
        String permissions = ssoUserMapper.getUserPermissions(key[0], key[1]);
        Set<String> perSet = new HashSet<>();
        if (StringUtils.isEmpty(permissions)) {
            return perSet;
        }
        for (String per : permissions.split(",")) {
            if (StringUtils.isEmpty(per)) {
                continue;
            }
            perSet.add(per.trim());
        }
        return perSet;
    }
}