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

package uk.org.crimetalk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uk.org.crimetalk.R;
import uk.org.crimetalk.adapters.items.NavigationDrawerListItem;
import uk.org.crimetalk.utils.PreferenceUtils;

/**
 * {@link android.widget.ArrayAdapter} that will be used to display primary items in the
 * {@link uk.org.crimetalk.views.NavigationDrawerListView}.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerListItem> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<NavigationDrawerListItem> mNavigationDrawerListItems;

    private int mSelection;

    /**
     * ViewHolder pattern as described
     * <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">here</a>.
     */
    private static class ViewHolder {

        TextView textView;
        View dividerView;

    }

    /**
     * Public constructor for the {@link uk.org.crimetalk.adapters.NavigationDrawerAdapter}.
     * The resource parameter is required but it will be ignored.
     *
     * @param context             A valid {@link android.content.Context}
     * @param resource            Can be any int as it will be ignored
     * @param navigationDrawerListItems The items to be shown in the adapter
     */
    public NavigationDrawerAdapter(Context context, int resource, List<NavigationDrawerListItem> navigationDrawerListItems) {
        super(context, resource, navigationDrawerListItems);

        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mNavigationDrawerListItems = navigationDrawerListItems;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;

        if (view != null) {

            viewHolder = (ViewHolder) view.getTag();

        } else {

            view = mLayoutInflater.inflate(R.layout.row_navigation_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text);
            viewHolder.dividerView = view.findViewById(R.id.divider);

            view.setTag(viewHolder);

        }

        // If the item is a separator set visibility and return the view
        if (mNavigationDrawerListItems.get(position).isSeparator()) {

            viewHolder.textView.setVisibility(View.GONE);
            viewHolder.dividerView.setVisibility(View.VISIBLE);

            return view;

        } else {

            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.dividerView.setVisibility(View.GONE);

        }

        viewHolder.textView.setText(mNavigationDrawerListItems.get(position).getTitle());

        // Item is a primary navigation item and is the selected item
        if (mNavigationDrawerListItems.get(position).getIconResource() != 0 && position == mSelection) {

            // Current item is selected so bold the text, make it red, and change the icon to red
            viewHolder.textView.setTypeface(viewHolder.textView.getTypeface(), Typeface.BOLD);
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.crimetalk_red));
            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(getSelectedIcon(mNavigationDrawerListItems
                    .get(position).getIconResource()), 0, 0, 0);

            // Item is a primary navigation item and is NOT the selected item
        } else if (mNavigationDrawerListItems.get(position).getIconResource() != 0 && position != mSelection) {

            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(getThemedIcon(mNavigationDrawerListItems
                    .get(position).getIconResource()), 0, 0, 0);

            viewHolder.textView.setTypeface(Typeface.create(viewHolder.textView.getTypeface(), Typeface.NORMAL));

            // If user has selected the dark theme, set text color as white
            if (PreferenceUtils.getDarkTheme(getContext())) {

                viewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.white));

            } else {

                viewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.black));

            }

        } else {

            viewHolder.textView.setTypeface(Typeface.create(viewHolder.textView.getTypeface(), Typeface.NORMAL));

            // If user has selected the dark theme, set text color as white
            if (PreferenceUtils.getDarkTheme(getContext())) {

                viewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.white));

            } else {

                viewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.black));

            }

        }

        return view;

    }

    /**
     * Private method.
     * Returns a black or white icon depending on the theme preference.
     * Throws a {@link IllegalArgumentException} if the position
     * is not within the required index.
     *
     * @param resource The resource for a specific {@link uk.org.crimetalk.adapters.items.NavigationDrawerListItem}
     * @return The themed icon resource of the current item
     */
    private int getThemedIcon(int resource) {

        switch (resource) {

            case R.drawable.ic_library_light:

                if (PreferenceUtils.getDarkTheme(getContext())) {

                    return R.drawable.ic_library_dark;

                }

                return R.drawable.ic_library_light;

            case R.drawable.ic_world_light:

                if (PreferenceUtils.getDarkTheme(getContext())) {

                    return R.drawable.ic_world_dark;

                }

                return R.drawable.ic_world_light;

            case R.drawable.ic_shop_light:

                if (PreferenceUtils.getDarkTheme(getContext())) {

                    return R.drawable.ic_shop_dark;

                }

                return R.drawable.ic_shop_light;

            default:

                throw new IllegalArgumentException("No icon can be produced for the selection.");

        }

    }

    /**
     * Private method.
     * Returns a red icon for the selected item.
     * Throws a {@link IllegalArgumentException} if the position
     * is not within the required index.
     *
     * @param resource The resource for a specific {@link uk.org.crimetalk.adapters.items.NavigationDrawerListItem}
     * @return The icon resource of the selected item
     */
    private int getSelectedIcon(int resource) {

        switch (resource) {

            case R.drawable.ic_library_light:

                return R.drawable.ic_library_selected;

            case R.drawable.ic_world_light:

                return R.drawable.ic_world_selected;

            case R.drawable.ic_shop_light:

                return R.drawable.ic_shop_selected;

            default:

                throw new IllegalArgumentException("No icon can be produced for the selection.");

        }

    }

    /**
     * Sets the currently selected item of the {@link uk.org.crimetalk.adapters.NavigationDrawerAdapter}.
     *
     * @param selection The current selection
     */
    public void setSelection(int selection) {

        this.mSelection = selection;
        notifyDataSetChanged();

    }

}
