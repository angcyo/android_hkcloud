package com.zhoukl.androidRDP.RdpModel;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/** 
 * @description：
 * @author zhoukl(67073753@qq.com)
 * @date 2015-6-25 上午9:15:00
 */
public class BaseUserBean implements Serializable, Parcelable {
    private static final long serialVersionUID = -6618630209605916889L;
    
    protected String memberId;
    protected String account;
    
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

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
