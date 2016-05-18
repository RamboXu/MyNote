package com.e.common.widget.wheel.bean;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class WheelEntity.java Create on 2015-03-15 上午11:10
 * @description
 */
public class WheelEntity {

    private String alias;
    private String item;
    private Object object;

    public WheelEntity(String alias, String item) {
        this.alias = alias;
        this.item = item;
    }

    public WheelEntity(String alias, String item, Object object) {
        this.alias = alias;
        this.item = item;
        this.object = object;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
