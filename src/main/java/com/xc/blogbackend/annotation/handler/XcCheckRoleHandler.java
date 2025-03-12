package com.xc.blogbackend.annotation.handler;

import cn.hutool.core.util.ObjectUtil;
import com.xc.blogbackend.annotation.XcCheckRole;
import com.xc.blogbackend.context.UserContext;
import com.xc.blogbackend.enums.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.vo.UserVo;

import java.lang.reflect.Method;

/**
 * 注解 SaCheckRole 的处理器
 */
public class XcCheckRoleHandler implements XcAnnotationHandlerInterface<XcCheckRole>{
    
    @Override
    public Class<XcCheckRole> getHandlerAnnotationClass() {
        return XcCheckRole.class;
    }

    @Override
    public void checkMethod(XcCheckRole at, Method method) {
        _checkMethod(at.value());
    }

    /**
     * 检查方法
     * @param value
     */
    public static void _checkMethod(String value) {
        UserVo user = UserContext.getUser();
        if (ObjectUtil.isNotEmpty(user)) {
            if (!user.getRoles().contains(value)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
        }
    }
}
