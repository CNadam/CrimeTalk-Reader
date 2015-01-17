/*
 * Copyright 2015 John Persano
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.org.crimetalk;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uk.org.crimetalk.adapters.NavigationDrawerAdapter;
import uk.org.crimetalk.adapters.items.NavigationDrawerListItem;
import uk.org.crimetalk.fragments.ErrorFragment;
import uk.org.crimetalk.fragments.LibraryFragment;
import uk.org.crimetalk.fragments.PagerFragment;
import uk.org.crimetalk.fragments.PressCuttingsFragment;
import uk.org.crimetalk.fragments.ShopFragment;
import uk.org.crimetalk.utils.PreferenceUtils;
import uk.org.crimetalk.utils.ThemeUtils;
import uk.org.crimetalk.views.NavigationDrawerContainer;
import uk.org.crimetalk.views.NavigationDrawerListView;

/**
 * {@link android.app.Activity} that shows the main content.
 *
 * @see {@link uk.org.crimetalk.fragments.LibraryFragment}
 * @see {@link uk.org.crimetalk.fragments.PressCuttingsFragment}
 */
public class MainActivity extends ActionBarActivity implements PagerFragment.QuickNavFragment.QuickNavFragmentListener {

    // Start Activity for result request code for SettingsActivity
    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 1;

    private static final String ARGS_SELECTED_POSITION = "selected_position";
    private static final String ARGS_TITLE = "title";

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private NavigationDrawerContainer mNavigationDrawerContainer;

    private int mCurrentSelectedPosition = -1;
    private boolean mUserLearnedNavigation;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Modify various attributes of the Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mUserLearnedNavigation = PreferenceUtils.getUserLearnedNavigation(MainActivity.this);

        mNavigationDrawerContainer = (NavigationDrawerContainer) findViewById(R.id.navigation_container);

        final NavigationDrawerAdapter navigationDrawerAdapter = new NavigationDrawerAdapter(MainActivity.this,
                R.layout.row_primary_navigation, getNavigationItems());

        // Set up the DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.crimetalk_dark_red));

        // Set up the ActionBarDrawerToggle
        mActionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Hacky way to stop hamburger icon to arrow animation
                if (drawerView != null) {

                    super.onDrawerSlide(drawerView, 0);

                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                // When the user first opens the NavigationDrawer remember he/she learned it
                if (!mUserLearnedNavigation) {

                    mUserLearnedNavigation = true;

                    PreferenceUtils.setUserLearnedNavigation(MainActivity.this, true);

                }

            }
        };

        // Show user the NavigationDrawer if it has never been seen before
        if (!mUserLearnedNavigation && savedInstanceState == null) {

            mDrawerLayout.openDrawer(mNavigationDrawerContainer);

        }

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        // If new instance select the Library item
        if (savedInstanceState == null) {

            selectPrimaryItem(1);

        // Otherwise we are returning from a previous instance and should display the previous items
        } else {

            mCurrentSelectedPosition = savedInstanceState.getInt(ARGS_SELECTED_POSITION);

            mTitle = savedInstanceState.getString(ARGS_TITLE);
            getSupportActionBar().setTitle(mTitle);

        }

        // Set up a ListView specialized for NavigationDrawers
        final NavigationDrawerListView navigationDrawerListView = (NavigationDrawerListView) findViewById(R.id.navigation_list);
        navigationDrawerListView.setHeader(MainActivity.this, R.layout.header_navigation_drawer);
        navigationDrawerListView.setAdapter(navigationDrawerAdapter, mCurrentSelectedPosition > 0 ? mCurrentSelectedPosition : 1);
        navigationDrawerListView.setOnNavigationItemClickListener(new NavigationDrawerListView.OnNavigationItemClickListener() {

            @Override
            public void onPrimaryItemClick(int position) {

                selectPrimaryItem(position);

            }

            @Override
            public void onSecondaryItemClick(int position) {

                selectSecondaryItem(position);

            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Open the drawer when the hamburger icon is clicked
        return mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred
        mActionBarDrawerToggle.syncState();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the selected primary item and the title
        outState.putInt(ARGS_SELECTED_POSITION, mCurrentSelectedPosition);
        outState.putCharSequence(ARGS_TITLE, mTitle);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mActionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public void onQuickNavSelected(int position) {

        // QuickNav was selected from the PressCuttingsFragment, navigate appropriately
        ((PagerFragment) getSupportFragmentManager().findFragmentById(R.id.container)).setViewPagerPosition(position);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // The SettingsActivity will rigger this when the theme is changed
        if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                /* This is used when the user navigates back from changing the theme
                    For information on the Handler, see
                    http://stackoverflow.com/questions/13136263/concurrentmodificationexception-when-adding-entries-to-arraylist */
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        MainActivity.this.recreate();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }

                }, 10);

            }

        }

    }

    /**
     * Private method.
     * Handles selection of primary navigation items
     *
     * Order of items in the list:
     *
     * 0 = NavigationDrawer header view
     *
     * 1 = LibraryFragment
     * 2 = PressCuttingsFragment
     * 3 = ShopFragment
     *
     * 4 = Separator
     *
     * 5 = SettingsActivity
     * 6 = AboutActivity
     */
    private void selectPrimaryItem(int position) {

        // Let's not load anything if the user selects the same screen they're on
        if(mCurrentSelectedPosition == position) {

            if (mDrawerLayout != null) {

                mDrawerLayout.closeDrawer(mNavigationDrawerContainer);

            }

            return;

        }

        this.mCurrentSelectedPosition = position;

        // Adjust the position for the header view
        final int listPosition = position - 1;

        // Change the title
        this.mTitle = getNavigationItems().get(listPosition).getTitle();
        this.getSupportActionBar().setTitle(mTitle);

        if (mDrawerLayout != null) {

            mDrawerLayout.closeDrawer(mNavigationDrawerContainer);

        }

        // The Fragment to be loaded
        Fragment fragment;

        /* Load the Fragment appropriate for the navigation selection.
           Note the case differentiation. This is because the header in the
           NavigationDrawer counts as item '0' so the first item in the list
           is actually item '1' */
        switch (position) {

            case 1:

                fragment = new LibraryFragment();

                break;

            case 2:

                fragment = new PressCuttingsFragment();

                break;

            case 3:

                fragment = new ShopFragment();

                break;

            default:

                fragment = new ErrorFragment();

                break;

        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    /**
     * Private method.
     * Handles selection of secondary navigation items
     */
    private void selectSecondaryItem(int position) {

        /* Load the Activity appropriate for the navigation selection.
           Note the case differentiation. Since the primary items are in the same list,
           these items must come after it. The numbers are further skewed by the
           header view and separator which both count as an item */
        switch (position) {

            case 5:

                // Listen for result signifying theme was changed
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_ACTIVITY_REQUEST_CODE);

                break;

            case 6:

                startActivity(new Intent(this, AboutActivity.class));

                break;

        }

    }

    /**
     * Private method.
     * Used for the creation of a {@link uk.org.crimetalk.adapters.NavigationDrawerAdapter}.
     *
     * @return A {@link uk.org.crimetalk.adapters.items.NavigationDrawerListItem} {@link java.util.List}
     */
    private List<NavigationDrawerListItem> getNavigationItems() {

        final List<NavigationDrawerListItem> navigationListItems = new ArrayList<>();

        // Add primary navigation items
        navigationListItems.add(NavigationDrawerListItem.createPrimaryNavigationItem(getResources()
                .getString(R.string.library), R.drawable.ic_library_light));
        navigationListItems.add(NavigationDrawerListItem.createPrimaryNavigationItem(getResources()
                .getString(R.string.press_cuttings), R.drawable.ic_world_light));
        navigationListItems.add(NavigationDrawerListItem.createPrimaryNavigationItem(getResources()
                .getString(R.string.shop), R.drawable.ic_shop_light));

        // Add separator
        navigationListItems.add(NavigationDrawerListItem.createSeparatorItem());

        // Add secondary navigation items
        navigationListItems.add(NavigationDrawerListItem.createSecondaryNavigationItem(getResources()
                .getString(R.string.settings)));
        navigationListItems.add(NavigationDrawerListItem.createSecondaryNavigationItem(getResources()
                .getString(R.string.about)));

        return navigationListItems;

    }

}
