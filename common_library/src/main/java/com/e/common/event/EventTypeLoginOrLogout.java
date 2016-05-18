package com.e.common.event;


/**
 * @{# EventType.java Create on 2015年1月20日 下午5:30:22
 *
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description 处理登录和退出的实体
 */
public final class EventTypeLoginOrLogout extends EventType {

    public static final int LOGIN = 1, LOGOUT = 2;

    private int loginStatus;
    private Class<?> target; // 特定的接收类的class

    public EventTypeLoginOrLogout(){

    }

    public EventTypeLoginOrLogout(int loginStatus){
        this.loginStatus = loginStatus;
    }

    /**
     * @return the loginStatus
     */
    public int getLoginStatus() {
        return loginStatus;
    }

    /**
     * @param loginStatus
     *            the loginStatus to set
     */
    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }
}
