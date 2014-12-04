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

package uk.org.crimetalk;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import uk.org.crimetalk.fragments.SearchFragment;
import uk.org.crimetalk.utils.ThemeUtils;

/**
 * {@link android.app.Activity} that handles search functions.
 *
 * @see {@link uk.org.crimetalk.fragments.SearchFragment}
 */
public class SearchActivity extends ActionBarActivity {

    // Arg List of articles to search through
    public static final String ARG_ARTICLE_LIST_HELPER_LIST = "article_list_helper_list";

    // Arg text to display before searching
    public static final String ARG_EMPTY_TEXT = "empty_text";

    // Arg used to persist query on orientation change
    private static final String ARG_QUERY = "query";

    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Modify various attributes of the Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // If new instance of SearchActivity, load SearchFragment
        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(getIntent().getExtras()
                            .getParcelableArrayList(ARG_ARTICLE_LIST_HELPER_LIST), getIntent().getExtras().getString(ARG_EMPTY_TEXT)))
                    .commit();

        } else {

            this.mQuery = savedInstanceState.getString(ARG_QUERY);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_search, menu);

        // Modify some aspects of the sSearchView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setTextColor(getResources().getColor(R.color.white));

        // This method ensures the search on orientation changes
        if(mQuery != null && !mQuery.isEmpty()) {

            // Change the SearchView text and send the search through
            searchView.setQuery(mQuery, true);

        }

        return true;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_QUERY, mQuery);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);

        // User pressed search so send search query to SearchFragment
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

            // If user has changed query, restart the loader
            if(mQuery != null && !mQuery.equals(getIntent().getStringExtra(SearchManager.QUERY))) {

                ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.container)).search(getIntent().getStringExtra(SearchManager.QUERY), true);

            // We must be recovering from orientation change so reattach loader callbacks
            } else {

                ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.container)).search(getIntent().getStringExtra(SearchManager.QUERY), false);

            }

            this.mQuery = getIntent().getStringExtra(SearchManager.QUERY);

        }

    }

}
