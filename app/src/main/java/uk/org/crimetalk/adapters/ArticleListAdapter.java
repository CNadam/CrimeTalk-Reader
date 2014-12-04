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

import java.util.List;

import uk.org.crimetalk.adapters.items.ArticleListItem;
import uk.org.crimetalk.R;

/**
 * {@link android.widget.ArrayAdapter} that will be used to display items in the
 * {@link uk.org.crimetalk.fragments.ArticleListFragment}.
 */
public class ArticleListAdapter extends ArrayAdapter<ArticleListItem> {

    private final LayoutInflater mLayoutInflater;

    /**
     * ViewHolder pattern as described
     * <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">here</a>.
     */
    private static class ViewHolder {

        TextView title;
        TextView date;
        TextView author;
        TextView hits;

    }

    /**
     * Public constructor for the {@link uk.org.crimetalk.adapters.ArticleListAdapter}.
     * The resource parameter is required but it will be ignored.
     *
     * @param context  A valid {@link android.content.Context}
     * @param resource Can be any int as it will be ignored
     */
    @SuppressWarnings("SameParameterValue")
    public ArticleListAdapter(Context context, int resource) {
        super(context, resource);

        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;

        if (view != null) {

            viewHolder = (ViewHolder) view.getTag();

        } else {

            view = mLayoutInflater.inflate(R.layout.row_article_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.author = (TextView) view.findViewById(R.id.author);
            viewHolder.hits = (TextView) view.findViewById(R.id.hits);

            view.setTag(viewHolder);

        }

        viewHolder.title.setText(getItem(position).getTitle());

        /* The getHits() method will return null when the date, author, and hits should
           not be shown. This is used in the PressCuttingsFragment. */
        if (getItem(position).getHits() == null) {

            viewHolder.date.setVisibility(View.GONE);
            viewHolder.author.setVisibility(View.GONE);
            viewHolder.hits.setVisibility(View.GONE);


        } else {

            // Make sure our Views are not GONE from the above statement
            viewHolder.date.setVisibility(View.VISIBLE);
            viewHolder.author.setVisibility(View.VISIBLE);
            viewHolder.hits.setVisibility(View.VISIBLE);

            viewHolder.date.setText(getItem(position).getDate());
            viewHolder.author.setText(getItem(position).getAuthor());
            viewHolder.hits.setText(getItem(position).getHits());

        }

        return view;

    }

    /**
     * Sets a {@link uk.org.crimetalk.adapters.items.ArticleListItem}
     * {@link java.util.List} to the {@link uk.org.crimetalk.adapters.ArticleListAdapter}.
     * This method will also clear any existing list.
     *
     * @param listItemArticleList The {@link uk.org.crimetalk.adapters.items.ArticleListItem}
     *                            {@link java.util.List} to be shown in the {@link uk.org.crimetalk.adapters.ArticleListAdapter}.
     */
    public void setData(List<ArticleListItem> listItemArticleList) {

        this.clear();
        this.addAll(listItemArticleList);

    }

}
