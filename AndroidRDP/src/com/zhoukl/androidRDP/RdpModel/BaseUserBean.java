package com.zhoukl.androidRDP.RdpModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/** 
 * @description：
 * @author zhoukl(67073753@qq.com)
 * @date 2015-6-25 上午9:15:00
 */
public class BaseUserBean implements Serializable, Parcelable {
    private static final long serialVersionUID = -6618630209605916889L;
    public String userId;   //:用户ID
    public String memberId; // 会员id
    public String account;

	@Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        
    }

}
