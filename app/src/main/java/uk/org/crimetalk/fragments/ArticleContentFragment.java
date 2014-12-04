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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
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
import uk.org.crimetalk.adapters.ArticleContentAdapter;
import uk.org.crimetalk.adapters.items.ArticleContentItem;
import uk.org.crimetalk.adapters.items.ArticleListItem;
import uk.org.crimetalk.utils.InternetUtils;
import uk.org.crimetalk.utils.PreferenceUtils;

/**
 * {@link android.app.Fragment} that serves as a native article viewer.
 *
 * @see {@link uk.org.crimetalk.ArticleContentActivity}
 * @see {@link uk.org.crimetalk.adapters.ArticleContentAdapter}
 * @see {@link uk.org.crimetalk.adapters.items.ArticleContentItem}
 */
public class ArticleContentFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<ArticleContentItem>> {

    // Arg used to save ListView state on orientation change
    private static final String ARG_LISTVIEW_STATE = "listview_state";

    // Arg used for the Loader
    private static final String ARG_URL = "url";

    // Animation duration of Toolbar hide/show
    private static final int ANIMATION_DURATION = 250;

    private ArticleContentAdapter mArticleContentAdapter;

    private int mLastFirstVisibleItem;

    private Parcelable mListViewState;

    /**
     * Static factory constructor for the {@link uk.org.crimetalk.fragments.ArticleContentFragment}.
     *
     * @param articleListItem The {@link uk.org.crimetalk.adapters.items.ArticleListItem} that should
     *                        be loaded
     * @return {@link uk.org.crimetalk.fragments.ArticleContentFragment}
     */
    public static ArticleContentFragment newInstance(ArticleListItem articleListItem) {

        final ArticleContentFragment articleListFragment = new ArticleContentFragment();

        final Bundle bundle = new Bundle();
        bundle.putParcelable(ArticleContentActivity.ARG_LIST_ITEM, articleListItem);
        articleListFragment.setArguments(bundle);

        return articleListFragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mArticleContentAdapter = new ArticleContentAdapter(getActivity(), R.layout.row_article_content);

        // If returning from orientation change, grab the previous ListView state
        if (savedInstanceState != null) {

            mListViewState = savedInstanceState.getParcelable(ARG_LISTVIEW_STATE);

        }

        // Make sure the Toolbar has been created before making changes to it
        final View view = getActivity().findViewById(R.id.toolbar);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                setupScrollHide(view);

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {

                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                } else {

                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                }

            }

        });

        // Create a Bundle with the ListItem URL to be loaded and pass it to the AsyncTaskLoader
        if (getArguments() != null) {

            final Bundle bundle = new Bundle();

            bundle.putString(ARG_URL, ((ArticleListItem) getArguments()
                    .getParcelable(ArticleContentActivity.ARG_LIST_ITEM)).getLink());

            getLoaderManager().initLoader(0, bundle, this);

        }

        // This ListView is not really a list so let's hide the divider
        getListView().setDivider(null);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Orientation change, save the current ListView state
        mListViewState = getListView().onSaveInstanceState();
        outState.putParcelable(ARG_LISTVIEW_STATE, mListViewState);

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
    public Loader<List<ArticleContentItem>> onCreateLoader(int id, Bundle args) {

        return new ArticleContentLoader(getActivity(), args);

    }

    @Override
    public void onLoadFinished(Loader<List<ArticleContentItem>> loader, List<ArticleContentItem> articleContentItemList) {

        // The Loader returned no results
        if (articleContentItemList.size() == 0) {

            // The device has Internet so something strange happened
            if (InternetUtils.hasInternet(getActivity())) {

                this.setEmptyText(getResources().getString(R.string.internet_error));
                ((TextView) this.getListView().getEmptyView()).setTextColor(Color.GRAY);

                // The device has no Internet so remind the user
            } else {

                this.setEmptyText(getResources().getString(R.string.internet_none));
                ((TextView) this.getListView().getEmptyView()).setTextColor(Color.GRAY);

            }

            // The Loader returned results
        } else {

            /* There is a spacer being used to offset the Progressbar due to the
               Toolbar overlay, get rid of it now that we have content to show */
            getActivity().findViewById(R.id.space).setVisibility(View.GONE);

            this.mArticleContentAdapter.setData(articleContentItemList);

            // There is no header on the ListView, set one to offset the Toolbar overlay
            if (getListView().getHeaderViewsCount() == 0) {

                @SuppressLint("InflateParams")
                final View dummyHeader = getActivity().getLayoutInflater().inflate(R.layout.dummy_header, null);

                getListView().addHeaderView(dummyHeader);

            }

        }

        this.setListAdapter(mArticleContentAdapter);

    }

    @Override
    public void onLoaderReset(Loader<List<ArticleContentItem>> loader) {

        // Nothing to clean up so do nothing

    }

    /**
     * Setup the function that will show and hide the Toolbar when the ListView is scrolled.
     *
     * @param view The Toolbar View
     */
    private void setupScrollHide(final View view) {

        final ObjectAnimator hideObjectAnimator = getHideAnimator(view);
        final ObjectAnimator showObjectAnimator = getShowAnimator(view);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                final int currentFirstVisibleItem = absListView.getFirstVisiblePosition();

                // User is scrolling down so hide the Toolbar
                if (mLastFirstVisibleItem < currentFirstVisibleItem) {

                    if (view.getVisibility() == View.VISIBLE) {

                        hideObjectAnimator.start();

                    }

                    // User is scrolling up so show the Toolbar
                } else if (mLastFirstVisibleItem > currentFirstVisibleItem) {

                    if (view.getVisibility() == View.GONE) {

                        showObjectAnimator.start();

                    }

                }

                mLastFirstVisibleItem = currentFirstVisibleItem;

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // Do nothing

            }

        });

    }

    /**
     * Private method.
     * Returns an {@link android.animation.ObjectAnimator} that is used to
     * hide the {@link android.support.v7.widget.Toolbar}.
     *
     * @param view The {@link android.support.v7.widget.Toolbar} View
     * @return {@link android.animation.ObjectAnimator}
     */
    private ObjectAnimator getHideAnimator(final View view) {

        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f, -view.getHeight());
        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                // Do nothing

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

                // Do nothing

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

                // Do nothing

            }

        });

        return objectAnimator;

    }

    /**
     * Private method.
     * Returns an {@link android.animation.ObjectAnimator} that is used to
     * show the {@link android.support.v7.widget.Toolbar}.
     *
     * @param view The {@link android.support.v7.widget.Toolbar} View
     * @return {@link android.animation.ObjectAnimator}
     */
    private ObjectAnimator getShowAnimator(final View view) {

        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", -view.getHeight(), 0f);
        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                view.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                // Do nothing

            }

            @Override
            public void onAnimationCancel(Animator animator) {

                // Do nothing

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

                // Do nothing

            }

        });

        return objectAnimator;

    }

    /**
     * Private {@link android.content.AsyncTaskLoader} that is used to
     * load the article.
     */
    private static class ArticleContentLoader extends AsyncTaskLoader<List<ArticleContentItem>> {

        private static final String TAG = "ArticleContentLoader";

        private final String mUrl;

        private List<ArticleContentItem> mArticleContentItems;

        /**
         * Public constructor for the {@link uk.org.crimetalk.fragments.ArticleContentFragment.ArticleContentLoader}.
         *
         * @param context A valid {@link android.content.Context}
         * @param args    A {@link android.os.Bundle} that contains the URL
         */
        public ArticleContentLoader(Context context, Bundle args) {
            super(context);

            this.mUrl = args.getString(ARG_URL);

        }

        @Override
        protected void onStartLoading() {

            if (mArticleContentItems != null) {

                deliverResult(mArticleContentItems);

            }

            if (takeContentChanged() || mArticleContentItems == null) {

                forceLoad();

            }

        }

        @Override
        public List<ArticleContentItem> loadInBackground() {

            // Create a new List to keep the ArticleContentItems in
            final List<ArticleContentItem> articleContentItems = new ArrayList<>();

            // May cause IOException
            try {

                /* Connect to the URL via Jsoup. The timeout can be adjusted in the
                   Settings page by the user */
                final Connection.Response response = Jsoup.connect(mUrl)
                        .timeout(PreferenceUtils.getTimeout(getContext()))
                        .execute();

                /* This is what the Jsoup connection will look for when parsing the article.
                   These parameters are specific to CrimeTalk articles */
                final Elements tableElements = response.parse()
                        .getElementsByClass("item-page")
                        .select("p");

                // For loop for each potential ArticleContentItem
                for (Element element : tableElements) {

                    // If the ArticleContentItem has text or an image URL, add it to the list
                    if ((!element.text().isEmpty() && element.text().length() > 1) ||
                            element.select("img").first() != null) {

                        articleContentItems.add(new ArticleContentItem()
                                .setText(element.text().trim())
                                .setTypeface(element.select("strong").first() != null && element //If "strong" than bold the text
                                        .select("strong").first().text().equals(element.text()) ? Typeface.BOLD : Typeface.NORMAL)
                                .setImageUrl(element.select("img").first() != null ? String.format(getContext().getResources()
                                        .getString(R.string.base_url), element.select("img").first().attr("src")) : ""));

                    }

                }

            } catch (IOException ioException) {

                Log.e(TAG, ioException.toString());

            }

            // If there are items in the List and the first one of them contains this dummy text, remove it
            if (articleContentItems.size() > 0 && articleContentItems.get(0).getText().contains("User Rating")) {

                articleContentItems.remove(0);

            }

            return articleContentItems;

        }

        @Override
        public void deliverResult(List<ArticleContentItem> articleContentItems) {

            this.mArticleContentItems = articleContentItems;

            if (isStarted()) {

                super.deliverResult(articleContentItems);

            }

        }

    }

}