package com.example.chrislin.recordrouteapplication.database.dao;

import android.util.Log;

import com.example.chrislin.recordrouteapplication.database.entity.Route;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Chrislin on 2018/3/6.
 */

public class RouteDao {
    private static final String TAG = "RouteDao";
    protected Realm db;

    public RouteDao() {
        if (db == null){
            db = Realm.getDefaultInstance();
        }
    }
    public void insert(final String newPrimaryKey, final String routeName, final RealmList<String> routeLocationList) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Route route = realm.createObject(Route.class, newPrimaryKey);
                route.setRouteName(routeName);
                route.setRouteLocationList(routeLocationList);
            }
        });
    }

    public void update(final String primaryKey, final String routeName, final RealmList<String> routeLocationList) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Route route = where().equalTo("id", primaryKey).findFirst();
                route.setRouteName(routeName);
                route.setRouteLocationList(routeLocationList);
            }
        });
    }

    public RealmResults<Route> searchAll() {
        return db.where(Route.class).findAll();
    }


    public RealmResults<Route> searchData(final String searchType, final String searchEqual) {
        return where().equalTo(searchType, searchEqual).findAll();
    }

    public void delete(final String position) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Route> users = realm.where(Route.class).findAll();
                users.deleteFromRealm(Integer.parseInt(position));
            }
        });
    }

    public void close() {
        db.close();
    }

    private RealmQuery<Route> where() {
        return db.where(Route.class);
    }
}
