package com.e.common.event;

/**
 * @author <a href="mailto:AndroidHubery@gmail.com"> e</a>
 * @version 1.0
 * @class  Create on 2015-06-04 下午6:49
 * @description 通知notificaton刷新，标记小红点，医生患者通用
 */
public class EventIsMarketNotification {
     public boolean isMarket;//标记是否标红
     public EventIsMarketNotification(){

     }

     public EventIsMarketNotification(boolean isMarket){
        this.isMarket = isMarket;
     }

}