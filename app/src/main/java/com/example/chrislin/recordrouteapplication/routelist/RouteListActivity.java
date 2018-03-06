package com.example.chrislin.recordrouteapplication.routelist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chrislin.recordrouteapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteListActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_routeList_list)
    RecyclerView recyclerViewRouteListList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        ButterKnife.bind(this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewRouteListList.setLayoutManager(linearLayoutManager);
        RouteListAdapter routeListAdapter = new RouteListAdapter(this);
        recyclerViewRouteListList.setAdapter(routeListAdapter);
    }

    public Context getContext() {
        return this;
    }
}
