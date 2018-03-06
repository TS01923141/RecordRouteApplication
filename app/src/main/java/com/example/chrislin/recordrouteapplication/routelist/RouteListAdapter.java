package com.example.chrislin.recordrouteapplication.routelist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chrislin.recordrouteapplication.R;
import com.example.chrislin.recordrouteapplication.database.dao.RouteDao;
import com.example.chrislin.recordrouteapplication.database.entity.Route;
import com.example.chrislin.recordrouteapplication.main.MapsActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by Chrislin on 2018/3/6.
 */

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {
    private RouteDao routeDao;
    private RealmResults<Route> routes;
    RouteListActivity routeListActivity;

    private static final String TAG = "RouteListAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView_routeList_routeName;
        public ViewHolder(View v){
            super(v);
            textView_routeList_routeName = v.findViewById(R.id.textView_routeList_routeName);
        }
    }

    public RouteListAdapter(RouteListActivity routeListActivity) {
        this.routeListActivity = routeListActivity;
        routeDao = new RouteDao();
        routes = routeDao.searchAll();
    }

    private void refresh(){
        routes = routeDao.searchAll();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView_routeList_routeName.setText(routes.get(position).getRouteName());
        holder.textView_routeList_routeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(routeListActivity.getContext(), MapsActivity.class);
                intent.putExtra("position", position);
                routeListActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, String.valueOf(routes.size()));
        return routes.size();
    }
}
