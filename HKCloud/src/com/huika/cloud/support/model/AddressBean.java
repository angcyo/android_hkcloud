package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

/**
 * 地址信息
 */
public class AddressBean implements Serializable {
	private static String SPACE = "   ";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**地址ID*/
	public String addressId;
	/**地区id*/
	public String areaId;
	
	/**收货人姓名*/
	public String receiverName;
	/**收货人地址*/
	public String receiverAddress;
	/**邮编*/
	public String postCode;
	/**手机*/
	public String receiverPhone;
	/**固话*/
	public String receiverTelephone;
	/**是否默认 0-非默认 1-默认*/
	public int isDefault;
	/**区域id*/
	public String receiverAreaID;
	/**区域信息*/
	public String receiverAreaName;

	
public String provinceId;
public String cityId;
	public String province;
	public String city;
	public String area;

	/**
	 * 获取展示用的邮编
	 */
	public String getShowPost() {
		if (!TextUtils.isEmpty(postCode) && !postCode.matches("^\\d{6}$")) { return ""; }
		return postCode;
	}

	/**
	 * 获取列表用的，详细地址包括区域
	 * @return
	 * @author FAN 创建于Dec 20, 2014
	 */
	public String getShowDetailAddress() {
		if (TextUtils.isEmpty(receiverAreaName)) { return receiverAddress; }
		return receiverAreaName.replaceAll(" ", "") + receiverAddress;
	}

	public static List<AddressBean> getTestData() {
		List<AddressBean> list = new ArrayList();
		for (int i = 0; i < 15; i++) {
			AddressBean addressBean = new AddressBean();
			addressBean.addressId = i + "";
			addressBean.receiverName = "张玲：" + i;
			addressBean.receiverAddress = "设为默认用户收货地址设为默认用户收货地址";
			addressBean.receiverPhone = "13430521631";
			addressBean.postCode = "5800" + i;
			addressBean.isDefault = i == 0 ? 1 : 0;
			list.add(addressBean);
		}
		return list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressId == null) ? 0 : addressId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AddressBean other = (AddressBean) obj;
		if (addressId == null) {
			if (other.addressId != null) return false;
		}
		else if (!addressId.equals(other.addressId)) return false;
		return true;
	}

	public static List<AddressBean> fillDemoData() {
		List<AddressBean> addressBeans=new ArrayList<AddressBean>();
		for (int i = 0; i < 3; i++) {
			AddressBean addressBean = new AddressBean();
			addressBean.receiverAddress="收获地址"+i;
			addressBean.receiverName="张三"+i;
			addressBean.isDefault=1;
			addressBean.receiverAreaName="广东/深圳/龙岗";
			addressBean.receiverPhone=10001+i+"";
			addressBean.postCode=40001+i+"";
			addressBeans.add(addressBean);
		}
		return addressBeans;
	}
	
//	public String addressId;
//	public String areaId;
//	public int isDefault;
//	public String receiverAddress;
//	public String receiverName;
//	public String receiverPhone;

}
