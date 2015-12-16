package com.huika.cloud.support.db.areainfo;

import java.util.ArrayList;
import java.util.List;

import com.huika.cloud.support.model.AreaInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Area.
*/
public class AreaInfoDao extends AbstractDao<AreaInfo, Integer> {

	public static final String TABLENAME = "AreaInfo";

	/**
	 * Properties of entity Area.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	*/
	public static class Properties {
		public final static Property AreaID = new Property(0, int.class, "areaID", true, "areaID");
		public final static Property AreaName = new Property(1, String.class, "areaName", false, "areaName");
		public final static Property ParentID = new Property(2, int.class, "parentID", true, "parentID");
		public final static Property ShortName = new Property(3, String.class, "shortName", false, "shortName");
		public final static Property AreaType = new Property(4, int.class, "areaType", false, "areaType");
//        public final static Property NamePath = new Property(5, String.class, "namePath", false, "namePath");
//        public final static Property IDPath = new Property(6, String.class, "IDPath", false, "IDPath");
		// public final static Property Initial = new Property(2, String.class, "initial", false, "initial"); // 首字母
		// public final static Property ShortSpelling = new Property(2, String.class, "shortSpelling", false, "shortSpelling");
		// public final static Property FullSpelling = new Property(2, String.class, "fullSpelling", false, "fullSpelling");

		// private String levelNum;//城市级别2：省份，3：市，4：区
		// private String type;//类型，0表示直辖市
	};

	private AreaInfoDaoSession daoSession;

	public AreaInfoDao(DaoConfig config) {
		super(config);
	}

	public AreaInfoDao(DaoConfig config, AreaInfoDaoSession daoSession) {
		super(config, daoSession);
		this.daoSession = daoSession;
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'AreaInfo' (" + "'AreaId' INTEGER PRIMARY KEY NOT NULL ," + "'AreaName' TEXT," + "'ParentId' INTEGER NOT NULL," + "'AreaType' INTEGER);");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'AreaInfo'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, AreaInfo entity) {
		stmt.clearBindings();
		stmt.bindLong(1, entity.getAreaId());

		String areaName = entity.getAreaName();
		if (areaName != null) {
			stmt.bindString(2, areaName);
		}
		stmt.bindLong(3, entity.getParentId());

		String shortName = entity.getShortName();
		if (shortName != null) {
			stmt.bindString(4, shortName);
		}
		stmt.bindLong(3, entity.getAreaType());
	}

	@Override
	protected void attachEntity(AreaInfo entity) {
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
	public AreaInfo readEntity(Cursor cursor, int offset) {
		AreaInfo entity = new AreaInfo( //
				cursor.getInt(offset + 0), // areaId
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // areaName
				cursor.getInt(offset + 2), // parentId
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // shortName
				cursor.isNull(offset + 4) ? 0 : cursor.getInt(offset + 4) // areaType
//	            cursor.isNull(offset + 5) ? "" : cursor.getString(offset + 5), // namepath
//                cursor.isNull(offset + 6) ? "" : cursor.getString(offset + 6) // idpath

		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, AreaInfo entity, int offset) {
		entity.setAreaId(cursor.getInt(offset + 0));
		entity.setAreaName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
		entity.setParentId(cursor.getInt(offset + 2));
		entity.setShortName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
		entity.setAreaType(cursor.isNull(offset + 4) ? 0 : cursor.getInt(offset + 4));
	}

	/** @inheritdoc */
	@Override
	protected Integer updateKeyAfterInsert(AreaInfo entity, long rowId) {
		return entity.getAreaId();
	}

	/** @inheritdoc */
	@Override
	public Integer getKey(AreaInfo entity) {
		if (entity != null) {
			return entity.getAreaId();
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
			// SqlUtils.appendColumns(builder, "T0", daoSession.getProvinceDao().getAllColumns());
			builder.append(" FROM Area T");
			builder.append(" LEFT JOIN PROVINCE T0 ON T.'provinceID'=T0.'provinceID'");
			builder.append(' ');
			selectDeep = builder.toString();
		}
		return selectDeep;
	}

	protected AreaInfo loadCurrentDeep(Cursor cursor, boolean lock) {
		AreaInfo entity = loadCurrent(cursor, 0, lock);

		return entity;
	}

	public AreaInfo loadDeep(Long key) {
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
	public List<AreaInfo> loadAllDeepFromCursor(Cursor cursor) {
		int count = cursor.getCount();
		List<AreaInfo> list = new ArrayList<AreaInfo>(count);

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

	protected List<AreaInfo> loadDeepAllAndCloseCursor(Cursor cursor) {
		try {
			return loadAllDeepFromCursor(cursor);
		}
		finally {
			cursor.close();
		}
	}

	/** A raw-style query where you can pass any WHERE clause and arguments. */
	public List<AreaInfo> queryDeep(String where, String... selectionArg) {
		Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
		return loadDeepAllAndCloseCursor(cursor);
	}

}