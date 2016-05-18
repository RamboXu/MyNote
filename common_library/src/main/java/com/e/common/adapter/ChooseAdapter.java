package com.e.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.common.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhujiancheng@gepingtech.com">mark</a>
 * @version 1.0
 * @{Create on 2015-01-22 13:04
 * @description 医生端和患者端ChooseActivity同用一个ChooseAdapter
 */
public class ChooseAdapter extends TAdapter<Map.Entry<Object, String>> {

    private Map<Object, Boolean> mSelectMap = new HashMap<>();

    private boolean mIsSingleChoose;
    public ChooseAdapter(Context con) {
        super(con, ChooseViewHolder.class);
    }

    public ChooseAdapter(Context con,boolean isSingleChoose){
        super(con, ChooseViewHolder.class);
        this.mIsSingleChoose = isSingleChoose;
    }

    public void updateData(List<Map.Entry<Object, String>> list, Object[] selectIds) {
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }
        boolean isSelected;
        for (Map.Entry<Object, String> entry : list) {
            isSelected = false;
            String key = entry.getKey().toString().trim();
            if (selectIds != null && selectIds.length > 0) {
                for (Object obj : selectIds) {
                    if (key.equals(obj.toString())) {
                        isSelected = true;
                        break;
                    }
                }
            }
            mSelectMap.put(key, isSelected);
        }
        super.updateData(list);
    }

    public void updateData(Object selectId, boolean isSingle) {
        mIsSingleChoose = isSingle;
        updateData(selectId);
    }

    public void updateData(Object selectId){
        if (mIsSingleChoose) {
            mSelectMap.clear();
        }
        String key = selectId.toString().trim();
        if (mSelectMap.containsKey(key) && mSelectMap.get(key)) {
            mSelectMap.put(key, false);
        } else {
            mSelectMap.put(key, true);
        }
        notifyDataSetChanged();
    }


    public class ChooseViewHolder extends ViewHolderObj {
        public ChooseViewHolder() {

        }
        private CheckBox mCheckBox;
        private TextView mTextView;
        private View mViewLine;

        @Override
        protected void populateItemView(View convertView, Map.Entry<Object, String> entry, int position) {
            mTextView.setText(entry.getValue());
            convertView.setTag(R.id.tag_obj, mCheckBox);
            if (mSelectMap.containsKey(entry.getKey().toString().trim()) && mSelectMap.get(entry.getKey().toString().trim())) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }

            if(position == getCount() - 1){//最后一个
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mViewLine.getLayoutParams();
                params.leftMargin = 0;
                mViewLine.setLayoutParams(params);
            }else{
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mViewLine.getLayoutParams();
                params.leftMargin = _context.getResources().getDimensionPixelOffset(R.dimen.size_dp_15);
                mViewLine.setLayoutParams(params);
            }
        }

        protected View createViewAndMapHolder() {
            View convertView = mInflater.inflate(
                    R.layout.item_choose, null);
            this.mCheckBox = (CheckBox) convertView.findViewById(R.id.check_choose);
            this.mTextView = (TextView) convertView.findViewById(R.id.text_choose);
            this.mViewLine = convertView.findViewById(R.id.view_line);
            return convertView;
        }
    }

    /**
     * 获取选中的key集合
     * 一般用于数据提交
     * @return
     */
    public String getResult() {
        StringBuffer sb = new StringBuffer("");
        for (Map.Entry<Object, Boolean> entry : mSelectMap.entrySet()) {
            if (entry.getValue()) {
                sb.append(entry.getKey() + "|");
            }
        }
        int s_length = sb.length();
        if (s_length <= 0) {
            return "";
        }
        return sb.delete(s_length - 1, s_length).toString();
    }

    /**
     * 选取选中的key的数据结果集
     * 一般用于文本的显示
     * @return
     */
    public String getValue(){
        StringBuffer sb = new StringBuffer("");
        for (Map.Entry<Object, Boolean> selectEntry : mSelectMap.entrySet()) {
            if (selectEntry.getValue()) {
                for(Map.Entry<Object, String> data : _dataEntries){
                    if(data.getKey() != null){
                        if(data.getKey().toString().equals(selectEntry.getKey().toString())){
                            sb.append(data.getValue() + ",");
                        }
                    }
                }
            }
        }
        int s_length = sb.length();
        if (s_length <= 0) {
            return "";
        }
        return sb.delete(s_length - 1, s_length).toString();
    }

}
