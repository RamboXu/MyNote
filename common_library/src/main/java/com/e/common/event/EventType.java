package com.e.common.event;


/**
 * @{# EventType.java Create on 2015年1月20日 下午5:30:22
 *
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public class EventType {

    private int tag;

    private Object object;

    /**
     * @return the tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * @param tag
     *            the tag to set
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
