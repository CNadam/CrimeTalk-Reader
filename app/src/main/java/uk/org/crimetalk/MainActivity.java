/*
 * Copyright 2014 John Persano
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
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import uk.org.crimetalk.fragments.LibraryFragment;
import uk.org.crimetalk.fragments.NavigationDrawerFragment;
import uk.org.crimetalk.fragments.PagerFragment;
import uk.org.crimetalk.fragments.PressCuttingsFragment;
import uk.org.crimetalk.fragments.ShopFragment;
import uk.org.crimetalk.utils.ThemeUtils;

/**
 * {@link android.app.Activity} that shows the main content.
 *
 * @see {@link uk.org.crimetalk.fragments.NavigationDrawerFragment}
 * @see {@link uk.org.crimetalk.fragments.LibraryFragment}
 * @see {@link uk.org.crimetalk.fragments.PressCuttingsFragment}
 */
public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        PagerFragment.QuickNavFragment.QuickNavFragmentListener {

    // Start Activity for result request code for SettingsActivity
    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 1;

    private NavigationDrawerFragment mNavigationDrawerFragment;
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

        // Setup the NavigationDrawerFragment
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, findViewById(R.id.drawer_layout));

        // Set the title to the correct navigation item
        mTitle = getResources().getStringArray(R.array.primary_navigation_items)[mNavigationDrawerFragment.getSelectedPosition()];

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Set the title of the Toolbar
        if (!mNavigationDrawerFragment.isDrawerOpen()) {

            if (getSupportActionBar() != null) {

                getSupportActionBar().setTitle(mTitle);

            }

            return true;

        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onPrimaryItemSelected(int position) {

        // Item was selected so change the title
        this.mTitle = getResources().getStringArray(R.array.primary_navigation_items)[position];

        // Load the Fragment appropriate for the navigation selection
        switch (position) {

            case 0:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new LibraryFragment())
                        .commit();

                break;

            case 1:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new PressCuttingsFragment())
                        .commit();

                break;

            case 2:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new ShopFragment())
                        .commit();

                break;

        }

    }

    @Override
    public void onSecondaryItemSelected(int position) {

        // Load the Activity appropriate for the navigation selection
        switch (position) {

            case 0:

                // Listen for result signifying theme was changed
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_ACTIVITY_REQUEST_CODE);

                break;

            case 1:

                startActivity(new Intent(this, AboutActivity.class));

                break;

        }

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

}
