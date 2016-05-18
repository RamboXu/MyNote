package com.e.common.widget.wheel.bean;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class WheelItemEntity.java Create on 2015-03-15 下午5:49
 * @description
 */
public class WheelItemEntity {

    public final int ITEM_TYPE_NUMBER = 1, ITEM_TYPE_STRING = 2, ITEM_TYPE_WHEEL_ENTITY = 3;

    private int itemType;
    private int maxValue;
    private int minValue;
    private boolean isAutoPlus0;
    private int step = 1;
    private WheelEntity[] wheelEntities;
    private String[] wheelItems;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isAutoPlus0() {
        return isAutoPlus0;
    }

    public void setAutoPlus0(boolean isAutoPlus0) {
        this.isAutoPlus0 = isAutoPlus0;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public WheelEntity[] getWheelEntities() {
        return wheelEntities;
    }

    public void setWheelEntities(WheelEntity[] wheelEntities) {
        this.wheelEntities = wheelEntities;
    }

    public String[] getWheelItems() {
        return wheelItems;
    }

    public void setWheelItems(String[] wheelItems) {
        this.wheelItems = wheelItems;
    }
}
