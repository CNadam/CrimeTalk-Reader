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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.org.crimetalk.R;
import uk.org.crimetalk.fragments.helpers.BookHelper;

/**
 * {@link android.app.Fragment} that shows CrimeTalk book information.
 *
 * @see {@link uk.org.crimetalk.fragments.helpers.BookHelper}
 * @see {@link uk.org.crimetalk.fragments.ShopFragment}
 */
public class BookFragment extends Fragment {

    // Arg for a BookHelper. This holds information about the book
    private static final String ARG_BOOK_HELPER = "book_helper";

    /**
     * Static factory constructor for the {@link uk.org.crimetalk.fragments.BookFragment}.
     *
     * @param bookHelper {@link uk.org.crimetalk.fragments.helpers.BookHelper} which holds
     *                   information pertinent to a book
     * @return {@link uk.org.crimetalk.fragments.BookFragment}
     */
    public static BookFragment newInstance(BookHelper bookHelper) {

        final BookFragment bookFragment = new BookFragment();

        final Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BOOK_HELPER, bookHelper);
        bookFragment.setArguments(bundle);

        return bookFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_book, container, false);

        if (getArguments() != null) {

            final BookHelper bookHelper = getArguments().getParcelable(ARG_BOOK_HELPER);

            // Load the book cover with Picasso
            Picasso.with(getActivity())
                    .load(bookHelper.getCoverUrl())
                    .placeholder(R.drawable.picasso_placeholder)
                    .into(((ImageView) view.findViewById(R.id.cover)));

            ((TextView) view.findViewById(R.id.title)).setText(bookHelper.getTitle());
            ((TextView) view.findViewById(R.id.author)).setText(bookHelper.getAuthor());
            ((TextView) view.findViewById(R.id.format)).setText(String.format(getResources().getString(R.string.book_format), bookHelper.getFormat()));
            ((TextView) view.findViewById(R.id.summary)).setText(bookHelper.getSummary());

            // Set the "More Information" Button to navigate to a specific URL
            view.findViewById(R.id.information).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    final Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(bookHelper.getButtonUrl()));

                    startActivity(intent);

                }

            });

        }

        return view;

    }

}
