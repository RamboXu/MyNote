package com.e.common.exception;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class UserNullPointerException.java Create on 2015-03-03 上午11:22
 * @description
 */
@EBean(scope = Scope.Singleton)
public class UserNullPointerException extends NullPointerException {

    public UserNullPointerException() {
        super("您还没有登录呢！");
    }
}
