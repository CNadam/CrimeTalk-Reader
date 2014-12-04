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

package uk.org.crimetalk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uk.org.crimetalk.R;
import uk.org.crimetalk.utils.PreferenceUtils;

/**
 * {@link android.widget.ArrayAdapter} that will be used to display primary items in the
 * {@link uk.org.crimetalk.fragments.NavigationDrawerFragment}.
 */
public class NavigationDrawerPrimaryAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final String[] mStrings;

    private int mSelection;

    /**
     * ViewHolder pattern as described
     * <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">here</a>.
     */
    private static class ViewHolder {

        TextView textView;

    }

    /**
     * Public constructor for the {@link uk.org.crimetalk.adapters.NavigationDrawerPrimaryAdapter}.
     * The resource parameter is required but it will be ignored.
     *
     * @param context  A valid {@link android.content.Context}
     * @param resource Can be any int as it will be ignored
     * @param strings  The items to ce shown in the adapter
     */
    @SuppressWarnings("SameParameterValue")
    public NavigationDrawerPrimaryAdapter(Context context, int resource, String[] strings) {
        super(context, resource, strings);

        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mStrings = strings;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;

        if (view != null) {

            viewHolder = (ViewHolder) view.getTag();

        } else {

            view = mLayoutInflater.inflate(R.layout.row_primary_navigation, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text);

            view.setTag(viewHolder);

        }

        viewHolder.textView.setText(mStrings[position]);
        viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(getIcon(position), 0, 0, 0);

        // If current item is not selected, use default text and icon color values
        if (position != mSelection) {

            viewHolder.textView.setTypeface(Typeface.create(viewHolder.textView.getTypeface(), Typeface.NORMAL));

            // If user has selected the dark theme, set text color as white
            if (PreferenceUtils.getDarkTheme(getContext())) {

                viewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.white));

            } else {

                viewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.black));

            }

        } else {

            // Current item is selected so bold the text, make it red, and change the icon to red
            viewHolder.textView.setTypeface(viewHolder.textView.getTypeface(), Typeface.BOLD);
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.crimetalk_red));
            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(getSelectedIcon(position), 0, 0, 0);

        }

        return view;

    }

    /**
     * Private method.
     * Returns a black or white icon depending on the theme preference.
     * Throws a {@link java.lang.IllegalArgumentException} if the position
     * is not within the required index.
     *
     * @param position The current {@link uk.org.crimetalk.adapters.NavigationDrawerPrimaryAdapter#getView(int,
     *                 android.view.View, android.view.ViewGroup)} position
     * @return The icon resource of the current item
     */
    private int getIcon(int position) {

        switch (position) {

            case 0:

                if (PreferenceUtils.getDarkTheme(getContext())) {

                    return R.drawable.ic_library_dark;

                }

                return R.drawable.ic_library_light;

            case 1:

                if (PreferenceUtils.getDarkTheme(getContext())) {

                    return R.drawable.ic_world_dark;

                }

                return R.drawable.ic_world_light;

            case 2:

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
     * Returns a red icon for the selected position.
     * Throws a {@link java.lang.IllegalArgumentException} if the position
     * is not within the required index.
     *
     * @param position The current {@link uk.org.crimetalk.adapters.NavigationDrawerPrimaryAdapter#getView(int,
     *                 android.view.View, android.view.ViewGroup)} position
     * @return The icon resource of the selected item
     */
    private int getSelectedIcon(int position) {

        switch (position) {

            case 0:

                return R.drawable.ic_library_selected;

            case 1:

                return R.drawable.ic_world_selected;

            case 2:

                return R.drawable.ic_shop_selected;

            default:

                throw new IllegalArgumentException("No icon can be produced for the selection.");

        }

    }

    /**
     * Sets the currently selected item of the {@link uk.org.crimetalk.adapters.NavigationDrawerPrimaryAdapter}.
     *
     * @param selection The current selection
     */
    public void setSelection(int selection) {

        this.mSelection = selection;
        notifyDataSetChanged();

    }

}
