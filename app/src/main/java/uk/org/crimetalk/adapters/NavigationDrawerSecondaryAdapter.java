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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uk.org.crimetalk.R;

/**
 * {@link android.widget.ArrayAdapter} that will be used to display secondary items in the
 * {@link uk.org.crimetalk.fragments.NavigationDrawerFragment}.
 */
public class NavigationDrawerSecondaryAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mLayoutInflater;
    private final String[] mStrings;

    /**
     * ViewHolder pattern as described
     * <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">here</a>.
     */
    private static class ViewHolder {

        TextView textView;

    }

    /**
     * Public constructor for the {@link uk.org.crimetalk.adapters.NavigationDrawerSecondaryAdapter}.
     * The resource parameter is required but it will be ignored.
     *
     * @param context  A valid {@link android.content.Context}
     * @param resource Can be any int as it will be ignored
     * @param strings  The items to ce shown in the adapter
     */
    @SuppressWarnings("SameParameterValue")
    public NavigationDrawerSecondaryAdapter(Context context, int resource, String[] strings) {
        super(context, resource, strings);

        this.mLayoutInflater = LayoutInflater.from(context);
        this.mStrings = strings;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;

        if (view != null) {

            viewHolder = (ViewHolder) view.getTag();

        } else {

            view = mLayoutInflater.inflate(R.layout.row_secondary_navigation, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text);

            view.setTag(viewHolder);

        }

        viewHolder.textView.setText(mStrings[position]);

        return view;

    }

}
