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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.org.crimetalk.R;
import uk.org.crimetalk.adapters.items.ArticleContentItem;

/**
 * {@link android.widget.ArrayAdapter} that will be used to display items in the
 * {@link uk.org.crimetalk.fragments.ArticleContentFragment}.
 */
public class ArticleContentAdapter extends ArrayAdapter<ArticleContentItem> {

    private final LayoutInflater mLayoutInflater;

    /**
     * ViewHolder pattern as described
     * <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">here</a>.
     */
    private static class ViewHolder {

        TextView textView;
        ImageView imageView;

    }

    /**
     * Public constructor for the {@link uk.org.crimetalk.adapters.ArticleContentAdapter}.
     * The resource parameter is required but it will be ignored.
     *
     * @param context  A valid {@link android.content.Context}
     * @param resource Can be any int as it will be ignored
     */
    @SuppressWarnings("SameParameterValue")
    public ArticleContentAdapter(Context context, int resource) {
        super(context, resource);

        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;

        if (view != null) {

            viewHolder = (ViewHolder) view.getTag();

        } else {

            view = mLayoutInflater.inflate(R.layout.row_article_content, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);

            view.setTag(viewHolder);

        }

        viewHolder.textView.setText(getItem(position).getText());
        viewHolder.textView.setTypeface(null, getItem(position).getTypeface());

        // If URL is not empty, load the image with Picasso
        if (!this.getItem(position).getImageUrl().isEmpty()) {

            viewHolder.imageView.setVisibility(View.VISIBLE);

            Picasso.with(getContext())
                    .load(getItem(position).getImageUrl())
                    .placeholder(R.drawable.picasso_placeholder)
                    .into(viewHolder.imageView);

        } else {

            viewHolder.imageView.setImageDrawable(null);
            viewHolder.imageView.setVisibility(View.GONE);

        }

        return view;

    }

    @Override
    public boolean isEnabled(int position) {

        // The ArticleContentItems should not be selectable

        return false;

    }

    /**
     * Sets a {@link uk.org.crimetalk.adapters.items.ArticleContentItem}
     * {@link java.util.List} to the {@link uk.org.crimetalk.adapters.ArticleContentAdapter}.
     * This method will also clear any existing list.
     *
     * @param articleContentItems The {@link uk.org.crimetalk.adapters.items.ArticleContentItem}
     *                            {@link java.util.List} to be shown in the {@link uk.org.crimetalk.adapters.ArticleContentAdapter}.
     */
    public void setData(List<ArticleContentItem> articleContentItems) {

        this.clear();
        this.addAll(articleContentItems);

    }

}
