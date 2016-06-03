package com.vexonelite.fragmentleftmenu;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (Build.VERSION.SDK_INT < 23) {
            drawer.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            drawer.setBackgroundColor(getResources().getColor(R.color.white, null));
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initEntryFragment();
        initLeftMenu();
    }

    @Override
    public void onBackPressed() {
        if (closeNavigationViewIfNeeded()) {
            return;
        }
        else {
            super.onBackPressed();
        }
    }

    private void initEntryFragment () {
        Bundle args = new Bundle();
        args.putString("MENU_TITLE", "This is Entry Fragment");
        SingleTextFragment fragment = new SingleTextFragment();
        fragment.setArguments(args);
        replaceFragment(fragment);
    }

    private void initLeftMenu () {
        LeftMenuFragment fragment = new LeftMenuFragment();
        fragment.setMenuActionCallback(new MyMenuActionCallback());

        FragmentManager tFragManager = getSupportFragmentManager();
        FragmentTransaction tFragTransaction = tFragManager.beginTransaction();
        tFragTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        tFragTransaction.replace(R.id.left_menu_container, fragment, fragment.getClass().getSimpleName());
        try {
            tFragTransaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyMenuActionCallback implements LeftMenuFragment.MenuActionCallback {
        @Override
        public void onMenuItemClicked(Object info) {
            if (null == info) {
                Log.w("MainActivity", "onMenuItemClicked - Object info is null!");
                return;
            }
            if (info instanceof MenuData) {
                handleMenuItem ((MenuData) info);
            }
            else {
                Log.w("MainActivity", "onMenuItemClicked - unknown Object info!");
            }
            MainActivity.this.closeNavigationViewIfNeeded();
        }

        void handleMenuItem (MenuData menuItem) {
            Bundle args = new Bundle();
            args.putString("MENU_TITLE", menuItem.getMenuTitle());
            SingleTextFragment fragment = new SingleTextFragment();
            fragment.setArguments(args);
            MainActivity.this.replaceFragment(fragment);
            MainActivity.this.closeNavigationViewIfNeeded();
        }
    }

    private boolean closeNavigationViewIfNeeded () {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
            return true;
        }
        else {
            return false;
        }
    }


    private void replaceFragment (final Fragment nextFragment) {
        if (null == nextFragment) {
            return;
        }
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragTransaction.replace(R.id.fragment_container, nextFragment);

        try {
            int len = fragManager.getBackStackEntryCount();
            if (len > 0) {
                for(int i = 0; i < len; ++i) {
                    //fragManager.popBackStack();
                    fragManager.popBackStackImmediate();
                }
            }
            fragTransaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


}
