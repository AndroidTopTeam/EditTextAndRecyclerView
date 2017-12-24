package ru.mipt.feofanova.foodapp;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ru.mipt.feofanova.foodapp.fragments.FavoriteFragment;
import ru.mipt.feofanova.foodapp.fragments.IngredientsInputFragment;
import ru.mipt.feofanova.foodapp.fragments.PropertiesFragment;

public class NavigationActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationDrawer;

    public static Fragment fragment;
    public static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            try {
                fragment = IngredientsInputFragment.class.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationDrawer = findViewById(R.id.navigation_view);

        setupDrawerContent(navigationDrawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                fragment = mFragmentManager.findFragmentById(R.id.main_content);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                View view = findViewById(R.id.ingredients_relative);
                if (view != null) {
                    inputMethodManager.showSoftInput(view,InputMethodManager.SHOW_FORCED);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.favorite_fragment:
                fragmentClass = FavoriteFragment.class;
                break;
            case R.id.properties_fragment:
                fragmentClass = PropertiesFragment.class;
                break;
            case R.id.search_fragment:
                fragmentClass = IngredientsInputFragment.class;
                break;
            default:
                fragmentClass = IngredientsInputFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mFragmentManager.beginTransaction().replace(R.id.main_content, fragment).addToBackStack(null).commit();

        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
    }

    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mFragmentManager.putFragment(outState,"myfragment", fragment);
    }
    public void onRestoreInstanceState(Bundle inState){
        fragment = mFragmentManager.getFragment(inState,"myfragment");
    }
}
