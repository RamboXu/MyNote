/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.e.common.widget.wheel.adapter;

import android.content.Context;

import com.e.common.utility.CommonUtility;
import com.e.common.widget.wheel.bean.WheelEntity;


/**
 * The simple Array wheel adapter
 */
public class ArrayWheelAdapter extends AbstractWheelTextAdapter {

    // items
    private Object items[];

    /**
     * Constructor
     *
     * @param context the current context
     */
    public ArrayWheelAdapter(Context context) {
        super(context);
    }

    public void updateItems(Object[] items) {
        this.items = items;
        notifyDataInvalidatedEvent();
    }

    @Override
    public CharSequence getItemText(int index) {
        if (!CommonUtility.Utility.isNull(items)) {
            if (index >= 0 && index < items.length) {
                Object item = items[index];
                if (item instanceof CharSequence) {
                    return (CharSequence) item;
                } else if (item instanceof WheelEntity) {
                    return ((WheelEntity) item).getItem();
                }
                return item.toString();
            }
        }
        return null;
    }

    @Override
    public Object getItemEntity(int index) {
        if (!CommonUtility.Utility.isNull(items)) {
            if (index >= 0 && index < items.length) {
                Object item = items[index];
                return item;
            }
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        if (CommonUtility.Utility.isNull(items)) {
            return 0;
        }
        return items.length;
    }
}
