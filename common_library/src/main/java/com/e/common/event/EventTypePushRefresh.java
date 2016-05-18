package com.e.common.event;

/**
 * @author <a href="mailto:zjc198805@qq.com">mark</a>
 * @version 1.0
 * @class EventTypePushRefresh.java Create on 2015-04-09 上午11:00
 * @description 主要用于jpush通知过来以后界面的刷新
 */
public class EventTypePushRefresh extends EventType{
    public String classType;//通知类刷新的类型
    public EventTypePushRefresh(String classType){
        this.classType = classType;
    }

    public EventTypePushRefresh(){

    }

    public EventTypePushRefresh(Class objectClass){
        this.classType = objectClass.getSimpleName();
    }

    public void setClassType(String classType){
        this.classType = classType;
    }

}