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

package uk.org.crimetalk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import uk.org.crimetalk.adapters.NavigationDrawerAdapter;
import uk.org.crimetalk.adapters.items.NavigationDrawerListItem;

/**
 * A specialized {@link android.widget.ListView} used for NavigationDrawers.
 */
public class NavigationDrawerListView extends ListView {

    private NavigationDrawerAdapter mNavigationDrawerAdapter;

    /**
     * Interface that handles {@link uk.org.crimetalk.views.NavigationDrawerListView} selections.
     *
     */
    public interface OnNavigationItemClickListener {

        /**
         * Called when a primary navigation item of a {@link uk.org.crimetalk.views.NavigationDrawerListView}
         * is selected.
         *
         * @param position The position of the item selected
         */
        public void onPrimaryItemClick(int position);

        /**
         * Called when a secondary navigation item of a {@link uk.org.crimetalk.views.NavigationDrawerListView}
         * is selected.
         *
         * @param position The position of the item selected
         */
        public void onSecondaryItemClick(int position);

    }

    /**
     * Empty public constructor.
     *
     * @param context The {@link android.content.Context}
     */
    public NavigationDrawerListView(Context context) {
        super(context);

        // Do nothing

    }

    /**
     * Empty public constructor.
     *
     * @param context The {@link android.content.Context}
     * @param attrs The {@link android.util.AttributeSet} used in the XML
     */
    public NavigationDrawerListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Do nothing

    }

    /**
     * Empty public constructor.
     *
     * @param context The {@link android.content.Context}
     * @param attrs The {@link android.util.AttributeSet} used in the XML
     * @param defStyleAttr The desired style attribute
     */
    public NavigationDrawerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Do nothing

    }

    /**
     * Set the header {@link android.view.View} of the {@link uk.org.crimetalk.views.NavigationDrawerListView}.
     *
     * @param context A valid {@link android.content.Context}
     * @param resourceId The layout resource id of the desired header
     */
    public void setHeader(Context context, int resourceId) {

        // Do not allow multiple headers
        if (getHeaderViewsCount() > 0) {

            Log.e(getClass().getName(), "Cannot add more than one header!");

            return;

        }

        // Inflate the layout using the Context
        final View view = LayoutInflater.from(context).inflate(resourceId, null);

        if (view != null) {

            // Add the inflated View to the ListView
            this.addHeaderView(view);

        } else {

            Log.e(getClass().getName(), "Cannot add null View to NavigationDrawer header");

        }

    }

    /**
     * A worker call to set the {@link uk.org.crimetalk.adapters.NavigationDrawerAdapter}
     * of the {@link uk.org.crimetalk.views.NavigationDrawerListView}. This method will
     * handle the initial setup and selections for the drawer.
     *
     * @param navigationDrawerAdapter A {@link uk.org.crimetalk.adapters.NavigationDrawerAdapter}
     * @param initialCheckedPosition The position of item that should be initially checked/selected
     */
    public void setAdapter(NavigationDrawerAdapter navigationDrawerAdapter, int initialCheckedPosition) {
        super.setAdapter(navigationDrawerAdapter);

        this.mNavigationDrawerAdapter = navigationDrawerAdapter;

        // Use multiple mode to have better control over the highlighted item
        this.setChoiceMode(CHOICE_MODE_MULTIPLE);

        this.setItemChecked(initialCheckedPosition, true);

        // The Adapter does NOT count the header as an item so adjust the position
        final int adapterPosition = initialCheckedPosition - getHeaderViewsCount();
        navigationDrawerAdapter.setSelection(adapterPosition);

    }

    /**
     * Set the {@link uk.org.crimetalk.views.NavigationDrawerListView.OnNavigationItemClickListener}
     * of the {@link uk.org.crimetalk.views.NavigationDrawerListView}.
     *
     * @param onNavigationItemClickListener The desired listener
     */
    public void setOnNavigationItemClickListener(final OnNavigationItemClickListener onNavigationItemClickListener) {

        this.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Header was clicked so do nothing
                if (position == 0) {

                    return;

                }

                // If the item is a primary navigation item, invoke methods specific to primary navigation
                if (((NavigationDrawerListItem)getItemAtPosition(position)).getIconResource() != 0) {

                    // Since we are using CHOICE_MODE_MULTIPLE, remove the current selection to avoid multiple selections
                    clearChoices();

                    // Highlight the current selection
                    setItemChecked(position, true);

                    // The Adapter does NOT count the header as an item so adjust the position
                    final int adapterPosition = position - getHeaderViewsCount();

                    // Notify the adapter of the selection. This will make the icon and text red
                    mNavigationDrawerAdapter.setSelection(adapterPosition);

                    // Invoke the listener
                    onNavigationItemClickListener.onPrimaryItemClick(position);

                } else {

                    // We do NOT want a secondary item to appear highlighted
                    setItemChecked(position, false);

                    // Invoke the listener
                    onNavigationItemClickListener.onSecondaryItemClick(position);

                }

            }

        });

    }

}
