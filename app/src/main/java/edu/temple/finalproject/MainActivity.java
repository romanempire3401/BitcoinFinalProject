package edu.temple.finalproject;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.id;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PriceFragment priceFragment;
    GraphFragment graphFragment;
    BlockFragment blockFragment;
    WalletFragment walletFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(findViewById(R.id.drawer_layout) == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            }
            ListView listView = (ListView) findViewById(R.id.contentListView);
            listView.setAdapter(new NavigationAdapter(this, getResources().getStringArray(R.array.navList)));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    switch(position){
                        case 0:
                            getCurrentPrice();
                            break;
                        case 1:
                            getGraphs();
                            break;
                        case 2:
                            getBlocks();
                            break;
                        case 3:
                            getWalletBalance();
                            break;
                        default:
                            getCurrentPrice();
                            break;
                    }
                }
            });
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            assert drawer != null;
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
        getCurrentPrice();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_price:
                getCurrentPrice();
                break;
            case R.id.nav_graph:
                getGraphs();
                break;
            case R.id.nav_blocks:
                getBlocks();
                break;
            case R.id.nav_wallet:
                getWalletBalance();
                break;
            default:
                getCurrentPrice();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(int paneId, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction()
                .replace(paneId, fragment);
        ft.commit();
        fm.executePendingTransactions();
    }

    private void getCurrentPrice() {
        priceFragment = new PriceFragment();
        loadFragment(R.id.mainContent, priceFragment);
        String title = getResources().getString(R.string.price_fragment_title);
        setTitle(title);
    }

    private void getGraphs() {
        graphFragment = new GraphFragment();
        loadFragment(R.id.mainContent, graphFragment);
        String title = getResources().getString(R.string.graph_fragment_title);
        setTitle(title);
    }

    private void getBlocks() {
        blockFragment = new BlockFragment();
        loadFragment(R.id.mainContent, blockFragment);
        String title = getResources().getString(R.string.block_fragment_title);
        setTitle(title);
    }

    private void getWalletBalance() {
        walletFragment = new WalletFragment();
        loadFragment(R.id.mainContent, walletFragment);
        String title = getResources().getString(R.string.wallet_fragment_title);
        setTitle(title);
    }
}
