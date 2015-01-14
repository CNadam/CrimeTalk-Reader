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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
 * {@link android.app.Fragment} that shows an {@link uk.org.crimetalk.adapters.items.ArticleListItem}
 * {@link android.widget.ListView}.
 *
 * @see {@link uk.org.crimetalk.fragments.LibraryFragment}
 * @see {@link uk.org.crimetalk.fragments.PressCuttingsFragment}
 */
public class ArticleListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<List<ArticleListItem>>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    // Arg for an ArticleListHelper. This object holds information required for loading the List
    private static final String ARG_ARTICLE_LIST_HELPER = "article_list_helper";

    // Arg for a boolean. True if the article should be loaded in a local browser when clicked
    private static final String ARG_LOAD_IN_BROWSER = "load_in_browser";

    // Arg used to save ListView state on orientation change
    private static final String ARG_LISTVIEW_STATE = "listview_state";

    // Arg used to check if SwipeRefreshLayout was refreshing before orientation change
    private static final String ARG_SWIPE_REFRESH_REFRESHING = "swipe_refresh_refreshing";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleListAdapter mArticleListAdapter;

    private boolean mLoadInBrowser;

    private Parcelable mListViewState;

    /**
     * Static factory constructor for the {@link uk.org.crimetalk.fragments.ArticleListFragment}.
     *
     * @param articleListHelper {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper} which holds
     *                          information required to load this {@link uk.org.crimetalk.fragments.ArticleListFragment}
     * @return {@link uk.org.crimetalk.fragments.ArticleListFragment}
     */
    public static ArticleListFragment newInstance(ArticleListHelper articleListHelper, boolean loadInBrowser) {

        final ArticleListFragment articleListFragment = new ArticleListFragment();

        final Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_ARTICLE_LIST_HELPER, articleListHelper);
        bundle.putBoolean(ARG_LOAD_IN_BROWSER, loadInBrowser);
        articleListFragment.setArguments(bundle);

        return articleListFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This Fragment has its own options menu
        this.setHasOptionsMenu(true);

    }

    @Override
    @SuppressWarnings("NullableProblems")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the ListFragment View and add a SwipeRefreshLayout to it
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);

        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.crimetalk_red);
        mSwipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (listFragmentView != null) {

            mSwipeRefreshLayout.addView(listFragmentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }

        return mSwipeRefreshLayout;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mArticleListAdapter = new ArticleListAdapter(getActivity(), R.layout.row_article_item);

        // If returning from orientation change, grab the previous ListView state
        if (savedInstanceState != null) {

            mListViewState = savedInstanceState.getParcelable(ARG_LISTVIEW_STATE);

            // Reshow the SwipeRefresh icon if it was showing before orientation change
            if (savedInstanceState.getBoolean(ARG_SWIPE_REFRESH_REFRESHING)) {

                // Have to do it in a Runnable or else it won't show
                mSwipeRefreshLayout.post(new Runnable() {

                    @Override
                    public void run() {

                        mSwipeRefreshLayout.setRefreshing(true);

                    }

                });

           }

        }

        final Bundle bundle = new Bundle();

        if (getArguments() != null) {

            this.mLoadInBrowser = getArguments().getBoolean(ARG_LOAD_IN_BROWSER);

            bundle.putParcelable(ARG_ARTICLE_LIST_HELPER, getArguments().getParcelable(ARG_ARTICLE_LIST_HELPER));

        }

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        // Force load to make sure the SwipeRefresh icon disappears on orientation change
        getLoaderManager().initLoader(0, bundle, this).forceLoad();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Orientation change, save the current ListView state
        mListViewState = getListView().onSaveInstanceState();
        outState.putParcelable(ARG_LISTVIEW_STATE, mListViewState);

        // Orientation change, save the state of the SwipeRefreshLayout
        outState.putBoolean(ARG_SWIPE_REFRESH_REFRESHING, mSwipeRefreshLayout.isRefreshing());

    }

    @Override
    public void onResume() {
        super.onResume();

        // Might have recovered from an orientation change so load the previous ListView state
        if (mListViewState != null) {

            getListView().onRestoreInstanceState(mListViewState);

        }

        mListViewState = null;

    }

    @Override
    public void onRefresh() {

        getLoaderManager().getLoader(0).forceLoad();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // If should load in a local browser, load in a local browser
        if (mLoadInBrowser || PreferenceUtils.getLoadInBrowser(getActivity())) {

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_article_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:

                startSearchActivity();

                return true;

            case R.id.refresh:

                mSwipeRefreshLayout.setRefreshing(true);

                getLoaderManager().getLoader(0).forceLoad();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }


    }

    @Override
    public Loader<List<ArticleListItem>> onCreateLoader(int id, Bundle args) {

        return new ArticleListLoader(getActivity(), args);

    }

    @Override
    public void onLoadFinished(Loader<List<ArticleListItem>> loader, List<ArticleListItem> data) {

        this.setListAdapter(mArticleListAdapter);

        // The Loader returned no results
        if (data.size() == 0) {

            // The device has Internet so something strange happened
            if (InternetUtils.hasInternet(getActivity())) {

                this.setEmptyText(getResources().getString(R.string.internet_error));
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

        // Load is finished, stop refreshing
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoaderReset(Loader<List<ArticleListItem>> loader) {

        // Nothing to clean up so do nothing

    }

    /**
     * Private method.
     * Starts a new {@link uk.org.crimetalk.SearchActivity}.
     */
    private void startSearchActivity() {

        /* SearchActivity searches the articles of the outer Fragment (i.e. LibraryFragment and PressCuttingsFragment)
           so we must know what outer Fragment we are in before starting the Activity */
        switch (((ArticleListHelper) getArguments().getParcelable(ARG_ARTICLE_LIST_HELPER)).getFragmentIdentifier()) {

            case ArticleListHelper.LIBRARY_FRAGMENT:

                final ArrayList<ArticleListHelper> libraryArticleListHelperList = new ArrayList<>(LibraryFragment.getPages(getActivity()));

                final Intent librarySearchIntent = new Intent(getActivity(), SearchActivity.class);
                librarySearchIntent.putParcelableArrayListExtra(SearchActivity.ARG_ARTICLE_LIST_HELPER_LIST, libraryArticleListHelperList);
                librarySearchIntent.putExtra(SearchActivity.ARG_SEARCH_TEXT, getResources().getString(R.string.search_library));

                startActivity(librarySearchIntent);

                break;

            case ArticleListHelper.PRESSCUTTINGS_FRAGMENT:

                final ArrayList<ArticleListHelper> pressCuttingsArticleListHelperList = new ArrayList<>(PressCuttingsFragment.getPages(getActivity()));

                final Intent pressCuttingsSearchIntent = new Intent(getActivity(), SearchActivity.class);
                pressCuttingsSearchIntent.putParcelableArrayListExtra(SearchActivity.ARG_ARTICLE_LIST_HELPER_LIST, pressCuttingsArticleListHelperList);
                pressCuttingsSearchIntent.putExtra(SearchActivity.ARG_SEARCH_TEXT, getResources().getString(R.string.search_press_cuttings));

                startActivity(pressCuttingsSearchIntent);

                break;

        }

    }

    /**
     * Private {@link android.content.AsyncTaskLoader} that is used to
     * load the article List.
     */
    private static class ArticleListLoader extends AsyncTaskLoader<List<ArticleListItem>> {

        private static final String TAG = "ArticleListLoader";

        private final String mUrl;
        private final String mJsoupClass;
        private final String mJsoupSelection;

        private List<ArticleListItem> mListItemArticleList;

        /**
         * Public constructor for the {@link uk.org.crimetalk.fragments.ArticleListFragment.ArticleListLoader}.
         *
         * @param context A valid {@link android.content.Context}
         * @param args    A {@link android.os.Bundle} that contains the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
         */
        public ArticleListLoader(Context context, Bundle args) {
            super(context);

            final ArticleListHelper articleListHelper = args.getParcelable(ARG_ARTICLE_LIST_HELPER);

            this.mUrl = articleListHelper.getUrl();
            this.mJsoupClass = articleListHelper.getJsoupClass();
            this.mJsoupSelection = articleListHelper.getJsoupSelection();

        }

        @Override
        protected void onStartLoading() {

            if (mListItemArticleList != null) {

                deliverResult(mListItemArticleList);

            }

            if (takeContentChanged() || mListItemArticleList == null) {

                forceLoad();

            }

        }

        @Override
        public List<ArticleListItem> loadInBackground() {

            // Create a new List to keep the ArticleListItems in
            final List<ArticleListItem> articleListItems = new ArrayList<>();

            // May cause IOException
            try {

                /* Connect to the URL via Jsoup. The data posted ensures the entire
                   list of articles can be grabbed by Jsoup. The timeout can be adjusted in the
                   Settings page by the user */
                final Connection.Response response = Jsoup.connect(mUrl)
                        .data("limit", "0")
                        .method(Connection.Method.POST)
                        .timeout(PreferenceUtils.getTimeout(getContext()))
                        .execute();

                /* This is what the Jsoup connection will look for when parsing the article.
                   These parameters are specific to CrimeTalk article lists */
                final Elements tableElements = response.parse()
                        .getElementsByClass(mJsoupClass)
                        .select(mJsoupSelection);

                // For loop for each potential ArticleListItem
                for (Element element : tableElements) {

                    articleListItems.add(new ArticleListItem()
                            .setTitle(element.getElementsByClass("list-title").text().trim())
                            .setDate(element.getElementsByClass("list-date").text().trim())
                            .setAuthor(element.getElementsByClass("list-author").text().trim())
                            .setHits(element.getElementsByClass("list-hits").text().contains("Hits:") ?
                                    null : String.format(getContext().getResources().getString(R.string.hits), element.getElementsByClass("list-hits").text().trim()))
                            .setLink(String.format(getContext().getResources()
                                    .getString(R.string.base_url), element.select("a").first().attr("href"))));

                }

            } catch (IOException ioException) {

                Log.e(TAG, ioException.toString());

            }

            // If there are items in the List and the first one of them contains this dummy text, remove it
            if (articleListItems.size() > 0 && articleListItems.get(0).getTitle().equalsIgnoreCase("Title")) {

                articleListItems.remove(0);

            }

            return articleListItems;

        }

        @Override
        public void deliverResult(List<ArticleListItem> listItemArticleList) {

            this.mListItemArticleList = listItemArticleList;

            if (isStarted()) {

                super.deliverResult(listItemArticleList);

            }

        }

    }

    /**
     * Private class.
     * {@link android.support.v4.widget.SwipeRefreshLayout} designed specifically for a
     * {@link android.app.ListFragment}. Code found
     * <a href="https://developer.android.com/samples/SwipeRefreshListFragment/index.html">here</a>.
     */
    private class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {

        /**
         * Empty public constructor for the
         * {@link uk.org.crimetalk.fragments.ArticleListFragment.ListFragmentSwipeRefreshLayout}.
         *
         * @param context A valid {@link android.content.Context}
         */
        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);

            //Do nothing

        }

        @Override
        public boolean canChildScrollUp() {

            final ListView listView = getListView();

            return listView.getVisibility() != View.VISIBLE || canListViewScrollUp(listView);

        }

        private boolean canListViewScrollUp(ListView listView) {

            return ViewCompat.canScrollVertically(listView, -1);

        }

    }

}
