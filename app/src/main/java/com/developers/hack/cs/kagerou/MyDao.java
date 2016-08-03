package com.developers.hack.cs.kagerou;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wataru on 16/08/02.
 */
public class MyDao {
    // テーブルの定数
    private static final String TABLE_NAME = "circle";
    private static final String COLUMN_ID = "rowid";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LNG = "lng";
    private static final String COLUMN_R = "radius";
    private static final String COLUMN_INSIDE_COLOR = "iColor";
    private static final String COLUMN_OUTSIDE_COLOR = "oColor";
    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_LAT, COLUMN_LNG, COLUMN_R, COLUMN_INSIDE_COLOR, COLUMN_OUTSIDE_COLOR};
    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     */
    public MyDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 全データの取得   ----------------①
     */
    public List<MyDBEntity> findAll() {
        List<MyDBEntity> entityList = new ArrayList<MyDBEntity>();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                COLUMN_ID);

        while(cursor.moveToNext()) {
            MyDBEntity entity = new MyDBEntity();
            entity.setRowId(cursor.getInt(0));
            entity.setLatValue(cursor.getDouble(1));
            entity.setLngValue(cursor.getDouble(2));
            entity.setRadiusValue(cursor.getInt(3));
            entity.setIColorValue(cursor.getString(4));
            entity.setOColorValue(cursor.getString(5));
            entityList.add(entity);
        }

        return entityList;
    }
    /**
     * データの登録   ----------------③
     */
    public long insert(double lat,double lng,int r,String iColor,String oColor) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LNG, lng);
        values.put(COLUMN_R, r);
        values.put(COLUMN_INSIDE_COLOR, iColor);
        values.put(COLUMN_OUTSIDE_COLOR, oColor);
        return db.insert(TABLE_NAME, null, values);
    }
    /**
     * データの更新   ----------------④
     */
    public int update(MyDBEntity entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, entity.getLatValue());
        values.put(COLUMN_LNG, entity.getLngValue());
        values.put(COLUMN_R, entity.getRadiusValue());
        values.put(COLUMN_INSIDE_COLOR, entity.getIColorValue());
        values.put(COLUMN_OUTSIDE_COLOR, entity.getOColorValue());
        String whereClause = COLUMN_ID + "=" + entity.getRowId();
        return db.update(TABLE_NAME, values, whereClause, null);
    }
}
