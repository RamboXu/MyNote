package com.e.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.e.common.utility.CommonUtility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:zhujiancheng@gepingtech.com">mark</a>
 * @version 1.0
 * @{Create on 2015-01-22 13:04
 * @description
 */
public abstract class TAdapter<T> extends BaseAdapter {
    protected Context _context; // Activity
    protected List<T> _dataEntries;
    protected boolean mIsPatientClient;
    protected int position;

    protected LayoutInflater mInflater;

    protected View.OnClickListener mListener;

    protected Class<? extends ViewHolderObj> _viewHolderClass;

    protected void setViewHolderClass(Class<? extends ViewHolderObj> holderClass) {
    }

    public abstract class ViewHolderObj {
//        public ViewHolderObj(){}

        protected abstract void populateItemView(View convertView, T t, int position);

        protected abstract View createViewAndMapHolder();

    }

    public void updateData(List<T> its) {
        if (CommonUtility.Utility.isNull(its)) {
            its = new ArrayList<>();
        }
        this._dataEntries = its;
        this.notifyDataSetChanged();
    }

    public final List<T> getDataCurrent() {
        return this._dataEntries;
    }

    public TAdapter(Context context, Class<? extends ViewHolderObj> holderClass) {
        this._context = context;
        this._viewHolderClass = holderClass;
        mInflater = LayoutInflater.from(context);
        mIsPatientClient = CommonUtility.isPatientClient(_context);
        setViewHolderClass(holderClass);
    }

    public TAdapter(Context con, Class<? extends ViewHolderObj> holderClass, List<T> its) {
        this(con, holderClass);
        this._dataEntries = its;
    }

    @Override
    public int getCount() {
        if (this._dataEntries == null)
            return 0;
        return _dataEntries.size();
    }

    @Override
    public final T getItem(int position) {
        if (this._dataEntries == null || _dataEntries.size() == 0)
            return null;
        return _dataEntries.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return getItemId(getItem(position));

    }

    public final int getPosition() {
        return position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (!CommonUtility.Utility.isNull(convertView) &&
                !CommonUtility.UIUtility.isVisible(convertView)) {
            CommonUtility.DebugLog.log("---------visdfsdfasdf");
            convertView.setVisibility(View.VISIBLE);
        }
        this.position = position;
        T dataItem = getItem(position);
        if (convertView == null) {
            convertView = createItemView();
        }
        CommonUtility.UIUtility.setObj2View(convertView, dataItem);
        Object object = convertView.getTag();
        if (object != null) {
            ViewHolderObj holder = (ViewHolderObj) object;
            holder.populateItemView(convertView, dataItem, position);
        }
        return convertView;
    }

    /**
     * @return
     * @ATT QWY!!!:从getDeclaredConstructors里面才能够看到, inner class的construcor其实都隐藏了一个参数, 就外层class的实例,
     * 也就是藉以实现"闭包"的手段. 所有不存在"无参的default构造函数", 至少有一个外层对象!!!
     */
    public final View createItemView() {
        ViewHolderObj holder;
        try {
            Constructor<? extends ViewHolderObj> constructor = _viewHolderClass.getConstructor(this.getClass());
            holder = constructor.newInstance(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        View v = holder.createViewAndMapHolder();
        v.setTag(holder);
        return v;
    }

    /**
     * 手动调用数据源更新，主要未特殊需要设定
     */
    public void notifyDataChangedManual() {
        notifyDataSetChanged();
        isDataChanged = true;
    }

    protected long getItemId(T item) {
        return 0;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    private boolean isDataChanged;

    public boolean isAdapterDataChanged() {
        return isDataChanged;
    }

    public void removeEntity(T t) {
        if (_dataEntries != null && _dataEntries.size() > 0) {
            if (_dataEntries.contains(t)) {
                _dataEntries.remove(t);
                notifyDataSetChanged();
            }
        }
    }
}
