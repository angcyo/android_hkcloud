package com.huika.cloud.support.model;

import java.io.Serializable;

import com.huika.cloud.control.main.activity.ImageDetailAct.CommonBigImgBean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerData;

public class ProductImageArray implements Serializable, PagerData, Parcelable,CommonBigImgBean{
  private static final long serialVersionUID = -4283655792920021932L;
  /** 商品图片地址 */
  private String imageUrl;
  /** 商品大图地址 */
  private String bigImageUrl;

  public String getBigImageUrl() {
    return bigImageUrl;
  }

  public void setBigImageUrl(String bigImageUrl) {
    this.bigImageUrl = bigImageUrl;
  }

  @Override public String getImageUrl() {
    return imageUrl;
  }

  @Override public void setImageUrl(String imgUrl) {
    this.imageUrl = imgUrl;
  }

  @Override public String toString() {
    return "ProductImageArray [imageUrl=" + imageUrl + ", bigImageUrl=" + bigImageUrl + "]";
  }

  @Override public int describeContents() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(imageUrl);
    dest.writeString(bigImageUrl);
  }

  public static final Parcelable.Creator<ProductImageArray> CREATOR =
      new Parcelable.Creator<ProductImageArray>() {
        @Override public ProductImageArray createFromParcel(Parcel source) {
          ProductImageArray infoImg = new ProductImageArray();
          infoImg.setImageUrl(source.readString());
          infoImg.setBigImageUrl(source.readString());
          return infoImg;
        }

        @Override public ProductImageArray[] newArray(int size) {
          return new ProductImageArray[size];
        }
      };

@Override
public String getCommonBigImg() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setCommonBigImg() {
	// TODO Auto-generated method stub
	
}

}
