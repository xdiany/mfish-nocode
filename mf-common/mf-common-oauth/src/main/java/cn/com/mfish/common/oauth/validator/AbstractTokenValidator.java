package cn.com.mfish.common.oauth.validator;

import cn.com.mfish.common.core.utils.AuthInfoUtils;
import cn.com.mfish.common.core.web.Result;
import cn.com.mfish.common.oauth.service.TokenService;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 抽象token校验
 * @author: mfish
 * @date: 2023/1/7 15:25
 */
public abstract class AbstractTokenValidator<T> implements IBaseValidator<T> {
    TokenService tokenService;

    public AbstractTokenValidator(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 校验不同类型request中包含的token信息是否正确
     *
     * @param request
     * @param result
     * @param <R>
     * @return
     */
    public <R> Result<T> validateT(R request, Result<T> result) {
        T token;
        if (result == null || result.getData() == null) {
            result = new Result<>();
            String accessToken = AuthInfoUtils.getAccessToken(request);
            if (StringUtils.isEmpty(accessToken)) {
                return result.setSuccess(false).setMsg("错误:令牌token不允许为空");
            }
            token = (T) tokenService.getToken(accessToken);
            if (token == null) {
                return result.setSuccess(false).setMsg("错误:token不存在或已过期");
            }
            return result.setData(token);
        }
        return result;
    }
}