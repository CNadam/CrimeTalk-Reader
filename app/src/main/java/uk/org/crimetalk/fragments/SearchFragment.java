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

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.org.crimetalk.ArticleContentActivity;
import uk.org.crimetalk.R;
import uk.org.crimetalk.SearchActivity;
import uk.org.crimetalk.adapters.ArticleListAdapter;
import uk.org.crimetalk.adapters.items.ArticleListItem;
import uk.org.crimetalk.fragments.helpers.ArticleListHelper;
import uk.org.crimetalk.utils.DialogUtils;
import uk.org.crimetalk.utils.InternetUtils;
import uk.org.crimetalk.utils.PreferenceUtils;

/**
 * {@link android.app.Fragment} that handles search functions.
 */
public class SearchFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<ArticleListItem>>,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "SearchFragment";

    // Arg for the search query
    private static final String ARG_QUERY = "query";

    private ArticleListAdapter mArticleListAdapter;

    /**
     * Static factory constructor for the {@link uk.org.crimetalk.fragments.SearchFragment}.
     *
     * @param parcelableArrayList A List of {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     *                            to be searched through
     * @return {@link uk.org.crimetalk.fragments.SearchFragment}
     */
    public static SearchFragment newInstance(ArrayList<Parcelable> parcelableArrayList, String query) {

        final SearchFragment searchFragment = new SearchFragment();

        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SearchActivity.ARG_ARTICLE_LIST_HELPER_LIST, parcelableArrayList);
        bundle.putString(ARG_QUERY, query);
        searchFragment.setArguments(bundle);

        return searchFragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mArticleListAdapter = new ArticleListAdapter(getActivity(), R.layout.row_article_item);

        final ArrayList<ArticleListHelper> articleListHelperList = getArguments()
                .getParcelableArrayList(SearchActivity.ARG_ARTICLE_LIST_HELPER_LIST);

        // Let users know this search will take a long time
        if (articleListHelperList.get(0).getFragmentIdentifier() == ArticleListHelper.PRESSCUTTINGS_FRAGMENT
                && !PreferenceUtils.getUserLearnedPressCuttingsWarning(getActivity())) {

            DialogUtils.getPressCuttingsWarningDialog(getActivity()).show();

        }

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SearchActivity.ARG_ARTICLE_LIST_HELPER_LIST, articleListHelperList);
        bundle.putString(ARG_QUERY, getArguments().getString(ARG_QUERY));

        // Start a load with the new query
        getLoaderManager().initLoader(0, bundle, this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final ArrayList<ArticleListHelper> articleListHelperList = getArguments()
                .getParcelableArrayList(SearchActivity.ARG_ARTICLE_LIST_HELPER_LIST);

        // If should load in a local browser, load in a local browser
        if (articleListHelperList.get(0).getFragmentIdentifier() == ArticleListHelper.PRESSCUTTINGS_FRAGMENT
                || PreferenceUtils.getLoadInBrowser(getActivity())) {

            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(((ArticleListItem) parent.getAdapter().getItem(position)).getLink()));

            startActivity(intent);

            return;

        }

        // Otherwise, load in a native content viewer
        final Intent intent = new Intent(getActivity(), ArticleContentActivity.class);
        intent.putExtra(ArticleContentActivity.ARG_LIST_ITEM, (ArticleListItem) parent.getAdapter().getItem(position));

        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

        // Create and show a dialog when an item is long pressed
        DialogUtils.getGenericArticleDialog(getActivity(), (ArticleListItem) getListAdapter().getItem(position)).show();

        return true;

    }

    @Override
    public Loader<List<ArticleListItem>> onCreateLoader(int id, Bundle args) {

        return new SearchQueryLoader(getActivity(), args);

    }

    @Override
    public void onLoadFinished(Loader<List<ArticleListItem>> loader, List<ArticleListItem> data) {

        this.setListAdapter(mArticleListAdapter);

        // Sometimes this method is called before content view is created
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                try {

                    setListShown(true);

                } catch (IllegalStateException illegalStateException) {

                    Log.e(TAG, illegalStateException.toString());

                }

            }

        }, 1000);

        // The Loader returned no results
        if (data.size() == 0) {

            // The device has Internet so something strange happened
            if (InternetUtils.hasInternet(getActivity())) {

                this.setEmptyText(getResources().getString(R.string.search_error));
                ((TextView) this.getListView().getEmptyView()).setTextColor(Color.GRAY);
                ((TextView) this.getListView().getEmptyView()).setTextSize(22);

                // The device has no Internet so remind the user
            } else {

                this.setEmptyText(getResources().getString(R.string.internet_none));
                ((TextView) this.getListView().getEmptyView()).setTextColor(Color.GRAY);
                ((TextView) this.getListView().getEmptyView()).setTextSize(22);

            }

        // The Loader returned results
        } else {

            this.mArticleListAdapter.setData(data);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<ArticleListItem>> loader) {

        // Nothing to clean up so do nothing

    }

    /**
     * Private {@link android.content.AsyncTaskLoader} that is used to
     * search through the article List.
     */
    private static class SearchQueryLoader extends AsyncTaskLoader<List<ArticleListItem>> {

        private static final String TAG = "SearchQueryLoader";

        private final String mSearchQuery;

        public final List<ArticleListHelper> mArticleListHelperList;
        private List<ArticleListItem> mArticleListItems;

        /**
         * Public constructor for the {@link uk.org.crimetalk.fragments.SearchFragment.SearchQueryLoader}.
         *
         * @param context A valid {@link android.content.Context}
         * @param args    A {@link android.os.Bundle} that contains the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
         *                to be searched through as well as a search query
         */
        public SearchQueryLoader(Context context, Bundle args) {
            super(context);

            this.mArticleListHelperList = args.getParcelableArrayList(SearchActivity.ARG_ARTICLE_LIST_HELPER_LIST);
            this.mSearchQuery = args.getString(ARG_QUERY);

        }

        @Override
        protected void onStartLoading() {

            if (mArticleListItems != null) {

                deliverResult(mArticleListItems);

            }

            if (takeContentChanged() || mArticleListItems == null) {

                forceLoad();

            }

        }

        @Override
        public List<ArticleListItem> loadInBackground() {

            // Create a new List to keep the ArticleListItems in
            final List<ArticleListItem> articleListItems = new ArrayList<>();

            // For each item to be searched through
            for (ArticleListHelper articleListHelper : mArticleListHelperList) {

                // May cause IOException
                try {

                    /* Connect to the URL via Jsoup. The data posted ensures the entire
                       list of articles can be grabbed by Jsoup. The timeout can be adjusted in the
                       Settings page by the user */
                    final Connection.Response response = Jsoup.connect(articleListHelper.getUrl())
                            .data("limit", "0")
                            .method(Connection.Method.POST)
                            .timeout(PreferenceUtils.getTimeout(getContext()))
                            .execute();

                    /* This is what the Jsoup connection will look for when parsing the article.
                       These parameters are specific to CrimeTalk article lists */
                    final Elements tableElements = response.parse()
                            .getElementsByClass(articleListHelper.getJsoupClass())
                            .select(articleListHelper.getJsoupSelection());

                    // For loop for each potential ArticleListItem
                    for (Element element : tableElements) {

                        // Compare ArticleListItems with search query ignoring text case
                        if (element.getElementsByClass("list-title").text().toLowerCase().contains(mSearchQuery.toLowerCase()) ||
                                element.getElementsByClass("list-date").text().toLowerCase().contains(mSearchQuery.toLowerCase()) ||
                                element.getElementsByClass("list-author").text().toLowerCase().contains(mSearchQuery.toLowerCase())) {

                            final ArticleListItem articleListItem = new ArticleListItem()
                                    .setTitle(element.getElementsByClass("list-title").text().trim())
                                    .setDate(element.getElementsByClass("list-date").text().trim())
                                    .setAuthor(element.getElementsByClass("list-author").text().trim())
                                    .setHits(element.getElementsByClass("list-hits").text().contains("Hits:") ?
                                            null : String.format(getContext().getResources().getString(R.string.hits), element.getElementsByClass("list-hits").text().trim()))
                                    .setLink(String.format(getContext().getResources()
                                            .getString(R.string.base_url), element.select("a").first().attr("href")));

                            articleListItems.add(articleListItem);

                        }

                    }

                } catch (IOException ioException) {

                    Log.e(TAG, ioException.toString());

                }

            }

            // If there are items in the List and the first one of them contains this dummy text, remove it
            if (articleListItems.size() > 0 && articleListItems.get(0).getTitle().equalsIgnoreCase("Title")) {

                articleListItems.remove(0);

            }

            return articleListItems;

        }

        @Override
        public void deliverResult(List<ArticleListItem> articleListItems) {

            this.mArticleListItems = articleListItems;

            if (isStarted()) {

                super.deliverResult(articleListItems);

            }

        }

    }

}