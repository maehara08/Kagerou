package com.developers.hack.cs.kagerou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wataru on 16/08/02.
 */
public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "circle";

    /** 「dbtest」テーブルの作成用SQL */
    private static final String CREATE_TABLE_SQL = "" +
            "create table circle (" +
            "rowid integer primary key autoincrement, " +
            "data text not null " +
            ")";

    /** 「dbtest」テーブルの削除用SQL */
    private static final String DROP_TABLE_SQL = "drop table if exists memo";

    /**
     * コンストラクタ（必須）
     */
    public MyDBHelper(
            Context context,
            SQLiteDatabase.CursorFactory factory,
            int version) {

        super(context, DB_NAME, factory, version);
    }

    /**
     * テーブルの生成（必須）
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    /**
     * テーブルの再作成（必須）
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SQL);
        db.execSQL(CREATE_TABLE_SQL);
    }
}
