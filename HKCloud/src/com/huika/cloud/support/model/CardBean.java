package com.huika.cloud.support.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class CardBean implements Parcelable {
	public String cardId;
	public String bankName;
	public String cardNumber;
	public String logoImg;
	public String realName;
	public String subbranchBank;

	@Override
	public int describeContents() {
		return 0;
	}

	
	public CardBean() {
		super();
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(cardId);
		dest.writeString(bankName);
		dest.writeString(cardNumber);
		dest.writeString(logoImg);
		dest.writeString(realName);
		dest.writeString(subbranchBank);
		
	}

	public static final Parcelable.Creator<CardBean> CREATOR = new Parcelable.Creator<CardBean>() {
		public CardBean createFromParcel(Parcel in) {
			return new CardBean(in);
		}

		public CardBean[] newArray(int size) {
			return new CardBean[size];
		}
	};

	private CardBean(Parcel in) {
		cardId = in.readString();
		bankName = in.readString();
		cardNumber=in.readString();
		logoImg=in.readString();
		realName=in.readString();
		subbranchBank=in.readString();
	}

	public static ArrayList<CardBean> fillDemoData() {
		ArrayList<CardBean> cardBeans = new ArrayList<CardBean>();
		for (int i = 0; i < 3; i++) {
			CardBean cardBean = new CardBean();
			cardBean.bankName="中国银行"+i;
			cardBean.cardNumber=10001+i+"";
			cardBeans.add(cardBean);
		}
		return cardBeans;
	}


	@Override
	public String toString() {
		return "CardBean [cardId=" + cardId + ", bankName=" + bankName + ", cardNumber=" + cardNumber + ", logoImg=" + logoImg + ", realName=" + realName + ", subbranchBank=" + subbranchBank + "]";
	}
}
