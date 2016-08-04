package com.developers.hack.cs.kagerou.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/08/04.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = MySQLiteOpenHelper.class.getSimpleName();
    private String mDBName;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mDBName = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }

    public void insertCircleDB(String jsondata, SQLiteDatabase circleDB) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsondata);

        for (int i = 0; i < jsonArray.length(); i++) {
            Log.d(TAG, "insertCirclrDB");
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = '"' + jsonObject.getString("name") + '"';
            int user_id = jsonObject.getInt("user_id");
            int circle_id = jsonObject.getInt("circle_id");
            String title = '"' + jsonObject.getString("title") + '"';
            String content = '"' + jsonObject.getString("content") + '"';
            int radius = jsonObject.getInt("radius");
            double move_to_x = jsonObject.getDouble("move_to_x");
            double move_to_y = jsonObject.getDouble("move_to_y");
            int help_count = jsonObject.getInt("help_count");
            int view_count = jsonObject.getInt("view_count");
//                int from_merge = jsonObject.getInt("from_merge");
            //TODO
            int from_merge = 0;
//                int draw = jsonObject.getInt("draw");
            // TODO
            int draw = 0;
            String created_at = '"' + jsonObject.getString("created_at") + '"';
            double lng = jsonObject.getDouble("lng");
            double lat = jsonObject.getDouble("lat");
            double distance = jsonObject.getDouble("distance");

            String query = String.format("INSERT INTO circles (name, user_id, circle_id, title, content, radius, move_to_x, move_to_y," +
                            "help_count, view_count, from_merge, draw, created_at, lng, lat, distance) " +
                            "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
                    name, user_id, circle_id, title, content, radius, move_to_x, move_to_y, help_count,
                    view_count, from_merge, draw, created_at, lng, lat, distance);

            Log.d(TAG, "insertCircleDB + " + name);
            circleDB.execSQL(query);
        }
    }

    public void resetCircleTable(SQLiteDatabase circleDB) {
        String query = "DROP TABLE IF EXISTS circles";
        String resetQuery = "CREATE TABLE circles" + "(" +
                "name text," +
                "user_id int," +
                "circle_id int primary key," +
                "title text not null," +
                "content text not null," +
                "radius int not null," +
                "move_to_x blog not null," +
                "move_to_y blog not null," +
                "help_count int default 0," +
                "view_count int default 0," +
                "from_merge int ," +
                "draw int ," +
                "created_at text," +
                "lng blog not null," +
                "lat blog not null," +
                "distance blog not null" +
                ");";

        circleDB.execSQL(query);
        circleDB.execSQL(resetQuery);
    }

    public void loadCircleDB(SQLiteDatabase circleDB) {
        Cursor cursor = circleDB.query(
                "circles", new String[]{"name", "user_id"}, null, null, null, null, "name");
        // 参照先を一番始めに
        boolean isEof = cursor.moveToFirst();
        // データを取得していく
        while (isEof) {
            Log.d(TAG, "loadCircleDB + " + cursor.getString(cursor.getColumnIndex("name")));
            isEof = cursor.moveToNext();
        }
        // 忘れずに閉じる
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
