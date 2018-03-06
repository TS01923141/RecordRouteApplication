package com.example.chrislin.recordrouteapplication.database.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Chrislin on 2018/3/6.
 */

public class Route extends RealmObject {
    @PrimaryKey
    String id;

    RealmList<String> routeLocationList;
    //format "lat,lng"

    String routeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<String> getRouteLocationList() {
        return routeLocationList;
    }

    public void setRouteLocationList(RealmList<String> routeLocationList) {
        this.routeLocationList = routeLocationList;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }


}
