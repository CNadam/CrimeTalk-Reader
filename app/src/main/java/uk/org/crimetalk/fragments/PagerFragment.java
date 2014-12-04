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

package uk.org.crimetalk.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import uk.org.crimetalk.R;
import uk.org.crimetalk.views.TabStrip;

/**
 * {@link android.app.Fragment} that handles {@link android.support.v4.view.ViewPager}
 * navigation.
 *
 * @see {@link uk.org.crimetalk.fragments.LibraryFragment}
 * @see {@link uk.org.crimetalk.fragments.PressCuttingsFragment}
 */
public class PagerFragment extends Fragment {

    // This List will hold the titles of all of the Fragments attached to the PagerFragment
    private final ArrayList<String> mFragmentTitles;

    // This List will hold all of the Fragments attached to the PagerFragment
    private final ArrayList<Fragment> mFragments;

    private ViewPager mViewPager;

    /**
     * Empty public constructor for the {@link uk.org.crimetalk.fragments.PagerFragment}.
     */
    public PagerFragment() {

        this.mFragmentTitles = new ArrayList<>();
        this.mFragments = new ArrayList<>();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Does nothing until overridden in a class that extends PagerFragment
        addFragments();

        // If specified, add a quick navigation Fragment to the beginning of the ViewPager
        if (shouldAddQuickNav()) {

            addQuickNavFragment();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_pager, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new CustomFragmentPagerAdapter(getChildFragmentManager()));

        ((TabStrip) view.findViewById(R.id.tabstrip)).setViewPager(mViewPager);

        if (savedInstanceState == null && shouldAddQuickNav()) {

            mViewPager.setCurrentItem(1);

        }

        return view;

    }

    /**
     * This method should be overridden in any class that extends
     * {@link uk.org.crimetalk.fragments.PagerFragment}.
     */
    @SuppressWarnings("WeakerAccess")
    public void addFragments() {

        // Do nothing

    }

    /**
     * This boolean should be overridden in any class that extends
     * {@link uk.org.crimetalk.fragments.PagerFragment}. If a
     * {@link uk.org.crimetalk.fragments.PagerFragment.QuickNavFragment} should
     * be added, return true.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldAddQuickNav() {

        return false;

    }

    /**
     * Set the position of the {@link uk.org.crimetalk.fragments.PagerFragment}
     * {@link android.support.v4.view.ViewPager}.
     *
     * @param position The position to be navigated to
     */
    public void setViewPagerPosition(int position) {

        position++;
        mViewPager.setCurrentItem(position);

    }

    /**
     * Add a {@link android.app.Fragment} to this {@link uk.org.crimetalk.fragments.PagerFragment}.
     *
     * @param title    The title of the {@link android.app.Fragment} to be added
     * @param fragment The {@link android.app.Fragment} to be added
     * @return The current {@link uk.org.crimetalk.fragments.PagerFragment}
     */
    @SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
    public PagerFragment addFragment(String title, Fragment fragment) {

        this.mFragmentTitles.add(title);
        this.mFragments.add(fragment);

        return this;

    }

    /**
     * Private void.
     * Add a {@link uk.org.crimetalk.fragments.PagerFragment.QuickNavFragment}
     * to this {@link uk.org.crimetalk.fragments.PagerFragment}.
     */
    private void addQuickNavFragment() {

        this.mFragmentTitles.add(0, getResources().getString(R.string.title_quick_nav_fragment));
        this.mFragments.add(0, QuickNavFragment.newInstance(mFragmentTitles));

    }

    /**
     * {@link android.app.Fragment} that displays all of the selections in a
     * {@link uk.org.crimetalk.fragments.PagerFragment}. This {@link android.app.Fragment}
     * will be added in the first position of the {@link uk.org.crimetalk.fragments.PagerFragment}
     * list and provide a way to navigate quickly through large lists.
     */
    public static class QuickNavFragment extends ListFragment implements AdapterView.OnItemClickListener {

        // Arg for the title List. This List will display all the titles in the PagerFragment
        public static final String ARG_TITLE_LIST = "title_list";

        private ArrayList<String> mFragmentTitles;
        private QuickNavFragmentListener mQuickNavFragmentListener;

        /**
         * Callbacks for the {@link uk.org.crimetalk.fragments.PagerFragment.QuickNavFragment}.
         */
        public interface QuickNavFragmentListener {

            /**
             * Item in {@link uk.org.crimetalk.fragments.PagerFragment.QuickNavFragment}
             * has been selected.
             *
             * @param position The position of the item selected
             */
            public void onQuickNavSelected(int position);

        }

        /**
         * Static factory constructor for the {@link uk.org.crimetalk.fragments.PagerFragment.QuickNavFragment}.
         *
         * @param titleList The {@link java.util.List} of {@link android.app.Fragment} titles
         *                  to be ued for quick navigation
         * @return {@link uk.org.crimetalk.fragments.PagerFragment.QuickNavFragment}
         */
        public static QuickNavFragment newInstance(ArrayList<String> titleList) {

            final QuickNavFragment quickNavFragment = new QuickNavFragment();

            final Bundle bundle = new Bundle();
            bundle.putStringArrayList(ARG_TITLE_LIST, titleList);
            quickNavFragment.setArguments(bundle);

            return quickNavFragment;

        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            // Attach callbacks, may cause ClassCastException
            try {

                mQuickNavFragmentListener = (QuickNavFragmentListener) activity;

            } catch (ClassCastException e) {

                throw new ClassCastException(activity.toString()
                        + " must implement OnFragmentInteractionListener");

            }

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {

                this.mFragmentTitles = getArguments().getStringArrayList(ARG_TITLE_LIST);

            }

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Let's remove the QuickNavFragment from its quick navigation list
            this.mFragmentTitles.remove(getResources().getString(R.string.title_quick_nav_fragment));

            setListAdapter(new ArrayAdapter<>(getActivity(),
                    R.layout.row_pager_fragment_categories, mFragmentTitles));

            getListView().setOnItemClickListener(this);
            getListView().setDivider(null);

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Use the callback to handle item clicks
            if (mQuickNavFragmentListener != null) {

                mQuickNavFragmentListener.onQuickNavSelected(position);

            }

        }

    }

    /**
     * {@link android.support.v4.app.FragmentStatePagerAdapter} designed for use with
     * a {@link uk.org.crimetalk.fragments.PagerFragment}.
     */
    private class CustomFragmentPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * Empty public constructor for the
         * {@link uk.org.crimetalk.fragments.PagerFragment.CustomFragmentPagerAdapter}.
         *
         * @param fragmentManager Should use a child {@link android.app.FragmentManager}
         */
        public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

            // Do nothing

        }

        @Override
        public int getCount() {

            return PagerFragment.this.mFragments.size();

        }

        @Override
        public Fragment getItem(int position) {

            return PagerFragment.this.mFragments.get(position);

        }

        @Override
        public CharSequence getPageTitle(int position) {

            return (PagerFragment.this.mFragmentTitles.get(position));

        }

        @Override
        public float getPageWidth(int position) {

            // QuickNav Fragment should not take up a full screen
            if (shouldAddQuickNav()) {

                if (position == 0) {

                    if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                        return 0.25f;

                    }

                    return 0.5f;

                }

            }

            return 1.0f;

        }

    }

}
