package com.xc.blogbackend.annotation.context;

import com.xc.blogbackend.model.domain.vo.UserVo;

/**
 * 用户上下文
 *
 * @author xc
 */
public class UserContext {

    private static final ThreadLocal<UserVo> userThreadLocal = new ThreadLocal<>();

    public static void setUser(UserVo user) {
        userThreadLocal.set(user);
    }

    public static UserVo getUser() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}
