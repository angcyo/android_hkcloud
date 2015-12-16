package com.huika.cloud.support.helps;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.support.db.areainfo.AreaInfoDao;
import com.huika.cloud.support.db.areainfo.AreaInfoDao.Properties;
import com.huika.cloud.support.db.areainfo.AreaInfoDaoMaster;
import com.huika.cloud.support.db.areainfo.AreaInfoDaoSession;
import com.huika.cloud.support.model.AreaInfo;

public class AreaInfoDbHelper {

	private AreaInfoDaoSession mAreaInfoDaoSession;
	private AreaInfoDao mAreaInfoDao;
	private SQLiteDatabase db;
	private Context mContext;

	public AreaInfoDbHelper() {
		AreaInfoDaoMaster daoMaster = HKCloudApplication.getAreaInfoDaoMaster();
		mAreaInfoDaoSession = daoMaster.newSession();
		mAreaInfoDao = mAreaInfoDaoSession.getAreaInfoDao();
		db = daoMaster.getDatabase();
	}

	public AreaInfoDbHelper(Context mContext) {
		this();
		this.mContext = mContext;
	}

	public long getTotalCount() {
		return mAreaInfoDao.queryBuilder().count();
	}

	public List<AreaInfo> getProvinceList() {
		return mAreaInfoDao.queryBuilder().where(Properties.ParentID.eq(1)).orderAsc(Properties.AreaID).list();
	}

	public List<AreaInfo> getAreaListByParentID(int parentID) {
		return mAreaInfoDao.queryBuilder().where(Properties.ParentID.eq(parentID)).orderAsc(Properties.AreaID).list();
	}
	public AreaInfo getAreaListByID(int areaID) {
		List<AreaInfo> list = mAreaInfoDao.queryBuilder().where(Properties.AreaID.eq(areaID)).orderAsc(Properties.AreaID).list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

//    public int getAreaIDByNamePath(String namePath) {
//        List<AreaInfo> list = mAreaInfoDao.queryBuilder().where(Properties.NamePath.like("%" + namePath + "%")).orderAsc(Properties.AreaID).list();
//        if (list.size() > 0) {
//            return list.get(0).getAreaId();
//        }
//        return -1;
//    }
    
	/**
	 * @description：获取地区Id
	 * @author jxh
	 * @date 2015-7-24 上午11:31:15
	 * @param provinceName 省份
	 * @param cityName 城市
	 */
	public int getAreaIdByParentName(String provinceName,String cityName){
	    List<AreaInfo> citylist = mAreaInfoDao.queryBuilder().where(Properties.AreaName.eq(cityName)).orderAsc(Properties.AreaID).list();
	    if (citylist.size() > 1) {
	    	 List<AreaInfo> provinceNameList = mAreaInfoDao.queryBuilder().where(Properties.AreaName.eq(cityName)).orderAsc(Properties.AreaID).list();
	    	 if(provinceNameList.size()>0){
	    		 int provinceId =  provinceNameList.get(0).getAreaId();
	    		 citylist = mAreaInfoDao.queryBuilder().where(Properties.AreaName.eq(cityName),Properties.ParentID.eq(provinceId)).orderAsc(Properties.AreaID).list();
	    		 if(citylist.size()>0){
	    			 return citylist.get(0).getAreaId();
	    		 }
	    	 }
        }else if (citylist.size() == 1){
        	 return citylist.get(0).getAreaId();
        }
		return -1;
	}
	
	public String getAreaByAreaId(String areaId){
		List<AreaInfo> citylist = mAreaInfoDao.queryBuilder().where(Properties.AreaID.eq(areaId)).orderAsc(Properties.AreaID).list();
		if(null!= citylist &&citylist.size()>0){
			return  citylist.get(0).getAreaName();
		}
		return null;
	}
}
