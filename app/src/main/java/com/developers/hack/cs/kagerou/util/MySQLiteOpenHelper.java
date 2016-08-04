package com.developers.hack.cs.kagerou.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import com.developers.hack.cs.kagerou.model.KagerouCircle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/08/04.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = MySQLiteOpenHelper.class.getSimpleName();
    private String mDBName;
    private OnLoadFinishListener mListener;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mDBName = name;
//        mListener=listener;
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
            Log.d(TAG,lng+"---"+lat);
            String sex = '"' + jsonObject.getString("sex") + '"';
            int age = jsonObject.getInt("age");
            double distance = jsonObject.getDouble("distance");

            String query = String.format("INSERT INTO circles (name, user_id, circle_id, title, content, radius, move_to_x, move_to_y," +
                            "help_count, view_count, from_merge, draw, created_at, lng, lat, sex, age, distance) " +
                            "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
                    name, user_id, circle_id, title, content, radius, move_to_x, move_to_y, help_count,
                    view_count, from_merge, draw, created_at, lng, lat, sex, age, distance);

            Log.d(TAG, "insertCircleDB lng + " + lng);
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
                "sex text not null,"+
                "age int not null," +
                "distance blog not null" +
                ");";

        circleDB.execSQL(query);
        circleDB.execSQL(resetQuery);
    }

    public ArrayList<KagerouCircle> loadCircleDB(SQLiteDatabase circleDB) {
        ArrayList<KagerouCircle> arrayList = new ArrayList<KagerouCircle>();
        Cursor cursor = circleDB.query(
                "circles", new String[]{
                        "name",
                        "user_id",
                        "circle_id",
                        "title",
                        "content",
                        "radius",
                        "help_count",
                        "created_at",
                        "lng",
                        "lat",
                        "sex",
                        "age"
                }, null, null, null, null, "circle_id");
        // 参照先を一番始めに
        boolean isEof = cursor.moveToFirst();
        // データを取得していく
        while (isEof) {
            Log.d(TAG, "loadCircleDB + " + cursor.getString(cursor.getColumnIndex("name")));
            KagerouCircle kagerouCircle = new KagerouCircle(
                    cursor.getString(0),
                    Integer.valueOf(cursor.getString(1)),
                    Integer.valueOf(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4),
                    Integer.valueOf(cursor.getString(5)),
                    Integer.valueOf(cursor.getString(6)),
                    cursor.getString(7),
                    Double.valueOf(cursor.getString(8)),
                    Double.valueOf(cursor.getString(9)));

            arrayList.add(kagerouCircle);

            Log.d(TAG, "loadCircleDB sex: " + cursor.getString(cursor.getColumnIndex("sex")));
            Log.d(TAG, "loadCircleDB age: " + cursor.getString(cursor.getColumnIndex("age")));
//            Log.d(TAG, "loadCircleDB lat: " + cursor.getString(cursor.getColumnIndex("lat")));
//            Log.d(TAG, "loadCircleDB move_to_y: " + cursor.getString(cursor.getColumnIndex("move_to_y")));
            isEof = cursor.moveToNext();
        }
        // 忘れずに閉じる
        cursor.close();
        Log.d(TAG,"End LOAD");
        return arrayList;
    }

    public void updateCircleDB(SQLiteDatabase circleDB){
        Log.d(TAG, "updateCircleDB: in method");
//        loadCircleDB(circleDB);
        String updateQuery = "update circles set lat = lat + move_to_y/10," +
                "lng = lng + move_to_x/10;";
        circleDB.execSQL(updateQuery);
        Log.d(TAG,"updateCircleDB: END");
//        loadCircleDB(circleDB);
    }

    public void resetCommentDB(SQLiteDatabase commentDB){
        Log.d(TAG, "resetCommentDB");
        String deleteQuery = "DROP TABLE IF EXISTS comments";
        String resetQuery = "create table comments" + "(" +
                "name text," +
                "circle_id int," +
                "comment_id int," +
                "content text," +
                "created_at text" +
                ");";

        commentDB.execSQL(deleteQuery);
        commentDB.execSQL(resetQuery);
    }

    public void loadCommentDB(SQLiteDatabase commentDB){
        Cursor cursor = commentDB.query(
                "comments", new String[]{"name", "circle_id"}, null, null, null, null, "name");
        // 参照先を一番始めに
        boolean isEof = cursor.moveToFirst();
        // データを取得していく
        while (isEof) {
            Log.d(TAG, "loadCommentDB + " + cursor.getString(cursor.getColumnIndex("name")));
            isEof = cursor.moveToNext();
        }
        // 忘れずに閉じる
        cursor.close();
    }
    public void insertCommentDB(String jsondata, SQLiteDatabase commentDB) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsondata);
        
        for(int i = 0; i < jsonArray.length(); i++){
            Log.d(TAG, "insertCommentDB");
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = '"' + jsonObject.getString("name")+'"';
            int circle_id = jsonObject.getInt("circle_id");
            int comment_id = jsonObject.getInt("comment_id");
            String content = '"' + jsonObject.getString("content") + '"';
            String created_at = '"' + jsonObject.getString("created_at") + '"';

            String query = String.format("insert into comments (name, circle_id, comment_id, content, created_at)" +
                    "values(%s, %s, %s, %s, %s);",
                    name, circle_id, comment_id, content, created_at);
            Log.d(TAG,"insertCommentDB + " + name);
            commentDB.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public interface OnLoadFinishListener {
        void onLoadFinish();
    }
}
