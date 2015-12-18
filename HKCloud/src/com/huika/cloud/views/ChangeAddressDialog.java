package com.huika.cloud.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.support.helps.AreaInfoDbHelper;
import com.huika.cloud.support.model.AreaInfo;
import com.huika.cloud.views.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.huika.cloud.views.wheel.widget.views.OnWheelChangedListener;
import com.huika.cloud.views.wheel.widget.views.OnWheelScrollListener;
import com.huika.cloud.views.wheel.widget.views.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 更改封面对话框
 * 
 * @author ywl
 *
 */
public class ChangeAddressDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView wvProvince;
	private WheelView wvCitys;
	private View lyChangeAddress;
	private View lyChangeAddressChild;
	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private JSONObject mJsonObj;
	private String[] mProvinceDatas;

	private ArrayList<AreaInfo> arrProvinces = new ArrayList<AreaInfo>();
	private ArrayList<AreaInfo> arrCitys = new ArrayList<AreaInfo>();
	private ArrayList<AreaInfo> arrAreas= new ArrayList<AreaInfo>();
	private AddressTextAdapter provinceAdapter;
	private AddressTextAdapter cityAdapter;
	private AddressTextAdapter areaAdapter;

	private int selectedProvince = 0;
	private int selectedCity = 0;
	private int selectedArea= 0;
	
	private String strProvince= "北京市";
	private String strCity= "东城区";
	private String strArea= "";

	private OnAddressCListener onAddressCListener;

	private int maxsize = 24;
	private int minsize = 14;
	private WheelView wvAreas;
	private AreaInfoDbHelper mAreaInfoDbHelper;
	
	private String selectedAreaId="110100";

	public ChangeAddressDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changeaddress);
		mAreaInfoDbHelper = new AreaInfoDbHelper();
		wvProvince = (WheelView) findViewById(R.id.wv_address_province);
		wvCitys = (WheelView) findViewById(R.id.wv_address_city);
		wvAreas = (WheelView) findViewById(R.id.wv_address_area);
		lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
		lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeAddress.setOnClickListener(this);
		lyChangeAddressChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initJsonData();
		initDatas();
		initProvinces();
		provinceAdapter = new AddressTextAdapter(context, arrProvinces,selectedProvince, maxsize, minsize);
		wvProvince.setVisibleItems(7);
		wvProvince.setViewAdapter(provinceAdapter);
		wvProvince.setCurrentItem(selectedProvince);

		initCitys(mAreaInfoDbHelper.getAreaListByParentID(arrProvinces.get(0).getAreaId()));
		cityAdapter = new AddressTextAdapter(context, arrCitys, selectedCity, maxsize, minsize);
		wvCitys.setVisibleItems(7);
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(selectedCity);
		
		initAreas(mAreaInfoDbHelper.getAreaListByParentID(arrCitys.get(0).getAreaId()));
		areaAdapter = new AddressTextAdapter(context, arrAreas,selectedArea, maxsize, minsize);
		wvAreas.setVisibleItems(7);
		wvAreas.setViewAdapter(areaAdapter);
		wvAreas.setCurrentItem(selectedArea);

		wvProvince.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				strProvince = currentText;
				setTextviewSize(currentText, provinceAdapter);

				initCitys(mAreaInfoDbHelper.getAreaListByParentID(arrProvinces.get(wheel.getCurrentItem()).getAreaId()));
				cityAdapter = new AddressTextAdapter(context, arrCitys, 0, maxsize, minsize);
				wvCitys.setVisibleItems(7);
				wvCitys.setViewAdapter(cityAdapter);
				wvCitys.setCurrentItem(0);
				strCity = (String) cityAdapter.getItemText(0);
				selectedAreaId=cityAdapter.getItemId(0);
				
				initAreas(mAreaInfoDbHelper.getAreaListByParentID(arrCitys.get(0).getAreaId()));
				areaAdapter = new AddressTextAdapter(context, arrAreas, 0, maxsize, minsize);
				wvAreas.setVisibleItems(7);
				wvAreas.setViewAdapter(areaAdapter);
				wvAreas.setCurrentItem(0);
				if(arrAreas.size()>0){
					strArea=(String) areaAdapter.getItemText(0);
					selectedAreaId=areaAdapter.getItemId(0);
				}else{
					strArea="";
				}
				
			}
		});

		wvProvince.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, provinceAdapter);
			}
		});

		wvCitys.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				strCity = currentText;
				setTextviewSize(currentText, cityAdapter);
								
				initAreas(mAreaInfoDbHelper.getAreaListByParentID(arrCitys.get(wheel.getCurrentItem()).getAreaId()));
				areaAdapter = new AddressTextAdapter(context, arrAreas, 0, maxsize, minsize);
				wvAreas.setVisibleItems(7);
				wvAreas.setViewAdapter(areaAdapter);
				wvAreas.setCurrentItem(0);
				if(areaAdapter.list.size()>0){
					strArea=areaAdapter.list.get(0).getAreaName();
					selectedAreaId=areaAdapter.getItemId(0);
				}else{
					strArea="";
					selectedAreaId=cityAdapter.getItemId(wheel.getCurrentItem());
				}
			}
		});

		wvCitys.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, cityAdapter);
			}
		});
		
		wvAreas.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
				strArea = currentText;
				selectedAreaId = areaAdapter.getItemId(wheel.getCurrentItem());
				setTextviewSize(currentText, areaAdapter);
				
			}
		});

		wvAreas.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				if(areaAdapter.list.size()>0){
					String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
					setTextviewSize(currentText, areaAdapter);
				}
			}
		});
	}

	/**
	 * 设置字体大小
	 *
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(24);
			} else {
				textvew.setTextSize(14);
			}
		}
	}

	public void setAddresskListener(OnAddressCListener onAddressCListener) {
		this.onAddressCListener = onAddressCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onAddressCListener != null) {
				onAddressCListener.onClick(strProvince,strCity,strArea,selectedAreaId);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeAddressChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	/**
	 * 从文件中读取地址数据
	 */
	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = context.getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析数据
	 */
	private void initDatas() {

	}

	/**
	 * 初始化省会
	 */
	public void initProvinces() {
		 arrProvinces = (ArrayList<AreaInfo>) mAreaInfoDbHelper.getProvinceList();
	}

	/**
	 * 根据省会，生成该省会的所有城市
	 *
	 * @param list
	 */
	public void initCitys(List<AreaInfo> list) {
		if (list != null) {
			arrCitys.clear();
			arrCitys.addAll(list);
		} else {
			List<AreaInfo> arrList = mAreaInfoDbHelper.getAreaListByParentID(arrProvinces.get(selectedProvince).getAreaId());
			arrCitys.clear();
			arrCitys.addAll(arrList);
		}
	}

	/**根据城市生成地区*/
	public void initAreas(List<AreaInfo> list){
		if (list != null) {
			arrAreas.clear();
			arrAreas.addAll(list);
		} else {
			List<AreaInfo> arrList = mAreaInfoDbHelper.getAreaListByParentID(arrCitys.get(selectedCity).getAreaId());
			arrAreas.clear();
			arrAreas.addAll(arrList);
		}
	}

	/**
	 * 回调接口
	 *
	 * @author Administrator
	 */
	public interface OnAddressCListener {
		void onClick(String province, String city, String area, String lastAreaId);
	}

	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<AreaInfo> list;

		protected AddressTextAdapter(Context context, ArrayList<AreaInfo> arrCitys, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = arrCitys;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index).getAreaName();
		}

		protected String getItemId(int index) {
			return list.get(index).getAreaId() + "";
		}

	}

}