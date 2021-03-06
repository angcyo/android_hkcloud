package com.huika.cloud.support.db;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.huika.cloud.support.model.City;
import com.huika.cloud.support.model.District;
import com.huika.cloud.support.model.Province;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class AddressDaoSession extends AbstractDaoSession {

	private final DaoConfig provinceDaoConfig;
	private final DaoConfig cityDaoConfig;
	private final DaoConfig districtDaoConfig;

	private final ProvinceDao provinceDao;
	private final CityDao cityDao;
	private final DistrictDao districtDao;

	public AddressDaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		provinceDaoConfig = daoConfigMap.get(ProvinceDao.class).clone();
		provinceDaoConfig.initIdentityScope(type);

		cityDaoConfig = daoConfigMap.get(CityDao.class).clone();
		cityDaoConfig.initIdentityScope(type);

		districtDaoConfig = daoConfigMap.get(DistrictDao.class).clone();
		districtDaoConfig.initIdentityScope(type);

		provinceDao = new ProvinceDao(provinceDaoConfig, this);
		cityDao = new CityDao(cityDaoConfig, this);
		districtDao = new DistrictDao(districtDaoConfig, this);

		registerDao(Province.class, provinceDao);
		registerDao(City.class, cityDao);
		registerDao(District.class, districtDao);
	}

	public void clear() {
		provinceDaoConfig.getIdentityScope().clear();
		cityDaoConfig.getIdentityScope().clear();
		districtDaoConfig.getIdentityScope().clear();
	}

	public ProvinceDao getProvinceDao() {
		return provinceDao;
	}

	public CityDao getCityDao() {
		return cityDao;
	}

	public DistrictDao getDistrictDao() {
		return districtDao;
	}

}
