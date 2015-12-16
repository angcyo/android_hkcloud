package com.zhoukl.androidRDP.RdpViews.RdpDBViews;

import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RdpDBImageView extends ImageView implements IRdpDBView {
    private RdpDBExtendView mDBExtendView;
	public String mDBFieldName;
	public int mDBDefaultImage;
	//private DisplayImageOptions mOptions;
	
	public RdpDBImageView(Context context) {
		super(context);
		mDBExtendView = new RdpDBExtendView(context);
	}
	public RdpDBImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZklDBView);
//		mDBExtendView.mDBFieldName = typedArray.getString(R.styleable.ZklDBView_dbFieldName);
//		mDBDefaultImage = typedArray.getInt(R.styleable.ZklDBView_dbDefaultImage, 0);
		
	}
	
	public RdpDBImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZklDBView);
//		mDBExtendView.mDBFieldName = typedArray.getString(R.styleable.ZklDBView_dbFieldName);
//		mDBDefaultImage = typedArray.getInt(R.styleable.ZklDBView_dbDefaultImage, 0);
		
		
		
		
		/*mOptions = new DisplayImageOptions.Builder()
		 .showImageForEmptyUri(R.drawable.common_lv_avator)
		 .showImageOnFail(R.drawable.common_lv_avator)
		// .showImageOnLoading(R.drawable.loading)
		.resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)//
		.imageScaleType(ImageScaleType.EXACTLY).build();  */
	}
	
	public void displayImage(String ImageUrl) {
//		if (!TextUtils.isEmpty(ImageUrl)) {
//			displayImage(ImageUrl); // mOptions);
//		} else {
//			this.setImageResource(mDBDefaultImage);
//		}
//		setImageResource(R.drawable.abc_ic_voice_search_api_holo_light);
	}
	
    @Override
    public RdpDBExtendView getDBExtendView() {
        return mDBExtendView;
    }
    
    @Override
    public void refreshView(String value, RdpDBViewListener listener) {
        StringBuffer displayValue = new StringBuffer(value);
        if (listener != null) {
            if (listener.onRefreshDBView(this, displayValue.toString(), displayValue)) {
                return;
            }
        }
        RdpImageLoader.displayImage(displayValue.toString(), this);
        //imageView.displayImage(displayValue.toString());
        displayImg(displayValue.toString());
    }
    
    public void displayImg(String imageUrl){  
//      RequestQueue mQueue = Volley.newRequestQueue(mContext);   
//            
//      ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());  
//      RdpImageLoader imageLoader = new RdpImageLoader();
//      ImageListener listener = ImageLoader.getImageListener(imageView
//              ,R.drawable.abc_ic_voice_search_api_holo_light, R.drawable.abc_ic_voice_search_api_holo_light);  
//      imageLoader.get(imageUrl, listener);  
      //指定图片允许的最大宽度和高度  
      //imageLoader.get("http://developer.android.com/images/home/aw_dac.png",listener, 200, 200);  
  }

}
