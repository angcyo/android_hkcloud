package com.zhoukl.androidRDP.RdpAdapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class RdpAdapter extends BaseAdapter {

    public final int NO_LIMIT_COUNT = -1;
    protected final int VIEW_NOT_DEFINE_ID = -1;
    protected int mItemLayoutID = -1;

    protected LayoutInflater mInflater;
    protected Context mContext = null;
    protected OnRefreshItemViewsListener mListener;

    protected int mMaxCount = NO_LIMIT_COUNT;

    public RdpAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public RdpAdapter(Context context, int itemLayoutID) {
        this(context);
        mItemLayoutID = itemLayoutID;
    }

    @Override
    public int getCount() {
        return 0;
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public void setMaxCount(int maxCount) {
        if (maxCount <= NO_LIMIT_COUNT)
            mMaxCount = NO_LIMIT_COUNT;
        else
            mMaxCount = maxCount;
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mItemLayoutID == -1) {
            return null;
        } 
        AdapterViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new AdapterViewHolder();
            viewHolder.mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(mItemLayoutID, parent, false);
            createItemViews(convertView, viewHolder);
            onAfterCreateItemViews(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AdapterViewHolder) convertView.getTag();
        }

        // 这里可以增加个接口回调，让用户自己刷新数据；refreshItemViews
        boolean dealComplete = false;
        if (mListener != null) {
            dealComplete = mListener.onRefreshItemViews(this, position, convertView, viewHolder);
        }
        if (!dealComplete) {
            refreshItemViews(position, viewHolder);
        }
        return convertView;
    }
    
    protected void onAfterCreateItemViews(View convertView, AdapterViewHolder viewHolder) {
        
    }

    protected void refreshItemViews(int position, AdapterViewHolder viewHolder) {

    }

    protected void createItemViews(View view, AdapterViewHolder viewHolder) {
        if (view.getId() != VIEW_NOT_DEFINE_ID)
            viewHolder.put(view.getId(), view);

        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                createItemViews(vp.getChildAt(i), viewHolder);
            }
        }
    }

    public void setItemLayoutID(int itemLayoutID) {
        mItemLayoutID = itemLayoutID;
    }

    public void setListener(OnRefreshItemViewsListener listener) {
        mListener = listener;
    }


    public interface OnRefreshItemViewsListener {
        boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder);
    }
    
    public static class AdapterViewHolder {
        SparseArray<Object> mViewArray;
        LayoutInflater mInflater;

        public AdapterViewHolder() {
            mViewArray = new SparseArray<Object>();
        }

        public void put(int key, Object value) {
            mViewArray.put(key, value);
        }

        public Object get(int key) {
            return mViewArray.get(key);
        }

        public View getView(int key) {
            return (View) mViewArray.get(key);
        }

        public TextView getTextView(int key) {
            return (TextView) mViewArray.get(key);
        }

        public CheckBox getCheckBox(int key) {
            return (CheckBox) mViewArray.get(key);
        }

        public Button getButton(int key) {
            return (Button) mViewArray.get(key);
        }

        public LinearLayout getLinearLayout(int key) {
            return (LinearLayout) mViewArray.get(key);
        }

        public RelativeLayout getRelativeLayout(int key) {
            return (RelativeLayout) mViewArray.get(key);
        }

        public ImageView getImageView(int key) {
            return (ImageView) mViewArray.get(key);
        }
        public EditText getEditText(int key) {
        	return (EditText) mViewArray.get(key);
        }
        public RadioButton getRadioButton(int key) {
        	return (RadioButton) mViewArray.get(key);
        }

        public RatingBar getRatingBar(int key) {
            return (RatingBar) mViewArray.get(key);
        }

        //AbsListView
        public AbsListView getAbsListView(int key) {
            return (AbsListView) mViewArray.get(key);
        }

    }

}
