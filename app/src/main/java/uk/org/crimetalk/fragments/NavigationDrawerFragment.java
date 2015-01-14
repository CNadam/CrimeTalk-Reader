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

package uk.org.crimetalk.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import uk.org.crimetalk.R;
import uk.org.crimetalk.adapters.NavigationDrawerPrimaryAdapter;
import uk.org.crimetalk.adapters.NavigationDrawerSecondaryAdapter;
import uk.org.crimetalk.utils.PreferenceUtils;

/**
 * {@link android.app.Fragment} that handles NavigationDrawer interaction.
 */
public class NavigationDrawerFragment extends Fragment {

    // Arg for selected position. This ensures position over orientation changes
    private static final String ARGS_SELECTED_POSITION = "selected_position";

    /**
     * Interface that will handle NavigationDrawer selections.
     */
    public interface NavigationDrawerCallbacks {

        /**
         * Called when a primary navigation item is selected.
         * Primary items should be the first group of items
         * in the NavigationDrawer.
         *
         * @param position The {@link android.widget.ListView} selection position.
         */
        void onPrimaryItemSelected(int position);

        /**
         * Called when a secondary navigation item is selected.
         * Secondary items should be the second group of items
         * in the NavigationDrawer.
         *
         * @param position The {@link android.widget.ListView} selection position.
         */
        void onSecondaryItemSelected(int position);

    }

    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mPrimaryDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = -1;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Attach callbacks, may cause ClassCastException
        try {

            mNavigationDrawerCallbacks = (NavigationDrawerCallbacks) activity;

        } catch (ClassCastException classCastException) {

            throw new ClassCastException(activity.toString()
                    + " must implement NavigationDrawerCallbacks.");

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This call is needed to open and close the NavigationDrawer via the hamburger icon
        this.setHasOptionsMenu(true);

        mUserLearnedDrawer = PreferenceUtils.getUserLearnedNavigation(getActivity());

        if (savedInstanceState != null) {

            mCurrentSelectedPosition = savedInstanceState.getInt(ARGS_SELECTED_POSITION);
            mFromSavedInstanceState = true;

        } else {

            // Navigate only if new instance
            selectPrimaryItem(0);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // This ListView contains primary navigation items
        mPrimaryDrawerListView = (ListView) view.findViewById(R.id.primary_listview);
        mPrimaryDrawerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mPrimaryDrawerListView.setAdapter(new NavigationDrawerPrimaryAdapter(getActivity(),
                R.layout.row_primary_navigation, getActivity().getResources().getStringArray(R.array.primary_navigation_items)));
        mPrimaryDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        ((NavigationDrawerPrimaryAdapter) mPrimaryDrawerListView.getAdapter()).setSelection(mCurrentSelectedPosition);
        mPrimaryDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Single choice mode so set the position as selected
                ((NavigationDrawerPrimaryAdapter) parent.getAdapter()).setSelection(position);

                selectPrimaryItem(position);

            }

        });

        // This ListView contains secondary navigation items
        final ListView secondaryListView = (ListView) view.findViewById(R.id.secondary_listview);
        secondaryListView.setAdapter(new NavigationDrawerSecondaryAdapter(getActivity(),
                R.layout.row_secondary_navigation, getActivity().getResources().getStringArray(R.array.secondary_navigation_items)));
        secondaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectSecondaryItem(position);

            }

        });

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARGS_SELECTED_POSITION, mCurrentSelectedPosition);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mActionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        mNavigationDrawerCallbacks = null;

    }

    /**
     * Private method.
     * Handles selection of primary navigation items
     */
    private void selectPrimaryItem(int position) {

        // Let's not load anything if the user selects the same screen they're on
        if(mCurrentSelectedPosition == position) {

            if (mDrawerLayout != null) {

                mDrawerLayout.closeDrawer(mFragmentContainerView);

            }

            return;

        }

        mCurrentSelectedPosition = position;

        if (mPrimaryDrawerListView != null) {

            mPrimaryDrawerListView.setItemChecked(position, true);

        }

        if (mDrawerLayout != null) {

            mDrawerLayout.closeDrawer(mFragmentContainerView);

        }

        if (mNavigationDrawerCallbacks != null) {

            mNavigationDrawerCallbacks.onPrimaryItemSelected(position);

        }

        getActivity().invalidateOptionsMenu();

    }

    /**
     * Private method.
     * Handles selection of secondary navigation items
     */
    private void selectSecondaryItem(int position) {

        if (mNavigationDrawerCallbacks != null) {

            mNavigationDrawerCallbacks.onSecondaryItemSelected(position);

        }

    }

    /**
     * Checks if the NavigationDrawer is currently open.
     *
     * @return true if NavigationDrawer is open
     */
    @SuppressWarnings("UnusedDeclaration")
    public boolean isDrawerOpen() {

        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);

    }

    /**
     * Returns the currently selected position of the {@link uk.org.crimetalk.fragments.NavigationDrawerFragment}.
     *
     * @return Currently selected item as int
     */
    public int getSelectedPosition() {

        return mCurrentSelectedPosition;

    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId The android:id of this fragment in its activity's layout.
     * @param view       The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, View view) {

        mFragmentContainerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = (DrawerLayout) view;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Hacky way to stop hamburger icon animation
                if (drawerView != null) {

                    super.onDrawerSlide(drawerView, 0);

                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!isAdded()) {

                    return;

                }

                if (!mUserLearnedDrawer) {

                    mUserLearnedDrawer = true;

                    PreferenceUtils.setUserLearnedNavigation(getActivity(), true);

                }

            }
        };

        // Show user the NavigationDrawer if it has never been seen before
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {

            mDrawerLayout.openDrawer(mFragmentContainerView);

        }

        mDrawerLayout.post(new Runnable() {

            @Override
            public void run() {

                mActionBarDrawerToggle.syncState();

            }

        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

    }

}
