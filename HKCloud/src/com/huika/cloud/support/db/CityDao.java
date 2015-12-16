package com.huika.cloud.support.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.huika.cloud.support.model.City;
import com.huika.cloud.support.model.Province;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table City.
*/
public class CityDao extends AbstractDao<City, Integer> {

	public static final String TABLENAME = "City";

	/**
	 * Properties of entity City.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	*/
	public static class Properties {
		public final static Property CityID = new Property(0, int.class, "cityID", true, "cityID");
		public final static Property CityName = new Property(1, String.class, "cityName", false, "cityName");
		public final static Property ZipCode = new Property(2, String.class, "zipCode", false, "zipCode");
		public final static Property ProvinceID = new Property(3, int.class, "provinceID", false, "provinceID");
	};

	private AddressDaoSession daoSession;

	public CityDao(DaoConfig config) {
		super(config);
	}

	public CityDao(DaoConfig config, AddressDaoSession daoSession) {
		super(config, daoSession);
		this.daoSession = daoSession;
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'City' (" + //
				"'cityID' INTEGER PRIMARY KEY NOT NULL ," + // 0: cityID
				"'cityName' TEXT," + // 1: cityName
				"'zipCode' TEXT," + // 2: zipCode
				"'provinceID' INTEGER NOT NULL );"); // 3: provinceID
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'City'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, City entity) {
		stmt.clearBindings();
		stmt.bindLong(1, entity.getCityID());

		String cityName = entity.getCityName();
		if (cityName != null) {
			stmt.bindString(2, cityName);
		}

		String zipCode = entity.getZipCode();
		if (zipCode != null) {
			stmt.bindString(3, zipCode);
		}
		stmt.bindLong(4, entity.getProvinceID());
	}

	@Override
	protected void attachEntity(City entity) {
		super.attachEntity(entity);
		entity.__setDaoSession(daoSession);
	}

	/** @inheritdoc */
	@Override
	public Integer readKey(Cursor cursor, int offset) {
		return cursor.getInt(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public City readEntity(Cursor cursor, int offset) {
		City entity = new City( //
				cursor.getInt(offset + 0), // cityID
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cityName
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // zipCode
				cursor.getInt(offset + 3) // provinceID
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, City entity, int offset) {
		entity.setCityID(cursor.getInt(offset + 0));
		entity.setCityName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
		entity.setZipCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
		entity.setProvinceID(cursor.getInt(offset + 3));
	}

	/** @inheritdoc */
	@Override
	protected Integer updateKeyAfterInsert(City entity, long rowId) {
		return entity.getCityID();
	}

	/** @inheritdoc */
	@Override
	public Integer getKey(City entity) {
		if (entity != null) {
			return entity.getCityID();
		}
		else {
			return null;
		}
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	private String selectDeep;

	protected String getSelectDeep() {
		if (selectDeep == null) {
			StringBuilder builder = new StringBuilder("SELECT ");
			SqlUtils.appendColumns(builder, "T", getAllColumns());
			builder.append(',');
			SqlUtils.appendColumns(builder, "T0", daoSession.getProvinceDao().getAllColumns());
			builder.append(" FROM City T");
			builder.append(" LEFT JOIN PROVINCE T0 ON T.'provinceID'=T0.'provinceID'");
			builder.append(' ');
			selectDeep = builder.toString();
		}
		return selectDeep;
	}

	protected City loadCurrentDeep(Cursor cursor, boolean lock) {
		City entity = loadCurrent(cursor, 0, lock);
		int offset = getAllColumns().length;

		Province province = loadCurrentOther(daoSession.getProvinceDao(), cursor, offset);
		if (province != null) {
			entity.setProvince(province);
		}

		return entity;
	}

	public City loadDeep(Long key) {
		assertSinglePk();
		if (key == null) { return null; }

		StringBuilder builder = new StringBuilder(getSelectDeep());
		builder.append("WHERE ");
		SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
		String sql = builder.toString();

		String[] keyArray = new String[] { key.toString() };
		Cursor cursor = db.rawQuery(sql, keyArray);

		try {
			boolean available = cursor.moveToFirst();
			if (!available) {
				return null;
			}
			else if (!cursor.isLast()) { throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount()); }
			return loadCurrentDeep(cursor, true);
		}
		finally {
			cursor.close();
		}
	}

	/** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
	public List<City> loadAllDeepFromCursor(Cursor cursor) {
		int count = cursor.getCount();
		List<City> list = new ArrayList<City>(count);

		if (cursor.moveToFirst()) {
			if (identityScope != null) {
				identityScope.lock();
				identityScope.reserveRoom(count);
			}
			try {
				do {
					list.add(loadCurrentDeep(cursor, false));
				}
				while (cursor.moveToNext());
			}
			finally {
				if (identityScope != null) {
					identityScope.unlock();
				}
			}
		}
		return list;
	}

	protected List<City> loadDeepAllAndCloseCursor(Cursor cursor) {
		try {
			return loadAllDeepFromCursor(cursor);
		}
		finally {
			cursor.close();
		}
	}

	/** A raw-style query where you can pass any WHERE clause and arguments. */
	public List<City> queryDeep(String where, String... selectionArg) {
		Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
		return loadDeepAllAndCloseCursor(cursor);
	}

}
