package com.developers.hack.cs.kagerou.model;

import com.google.android.gms.maps.model.Circle;

/**
 * Created by Wataru on 16/08/04.
 */
public class KagerouCircle  {
    private String name;
    private int user_id;
    private int circle_id;
    private String title;
    private String content;
    private int radius;
    private int help_count;
    private String created_at;
    private double lng;
    private double lat;

    private static KagerouCircle ourInstance = new KagerouCircle();

    public static KagerouCircle getInstance() {
        return ourInstance;
    }

    private KagerouCircle() {
    }

    public KagerouCircle(
            String name_value,
            int user_id_value,
            int circle_id_value,
            String title_value,
            String content_value,
            int radius_value,
            int help_count_value,
            String created_at_value,
            double lng_value,
            double lat_value){
        name=name_value;
        user_id=user_id_value;
        circle_id=circle_id_value;
        title=title_value;
        content=content_value;
        radius=radius_value;
        help_count=help_count_value;
        created_at=created_at_value;
        lng=lng_value;
        lat=lat_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(int circle_id) {
        this.circle_id = circle_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getHelp_count() {
        return help_count;
    }

    public void setHelp_count(int help_count) {
        this.help_count = help_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public static KagerouCircle getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(KagerouCircle ourInstance) {
        KagerouCircle.ourInstance = ourInstance;
    }
}
