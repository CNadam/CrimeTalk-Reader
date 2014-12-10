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
import android.widget.TextView;

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
    public static final String ARG_SEARCH_TEXT = "search_text";

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

        // Set the search text hint
        ((TextView) findViewById(R.id.search)).setText(getIntent().getExtras().getString(ARG_SEARCH_TEXT));

        // Grab the previous query from orientation change
        if (savedInstanceState != null) {

            this.mQuery = savedInstanceState.getString(ARG_QUERY);

            // Restore text in search hint if it exists
            if(mQuery != null && !mQuery.isEmpty()) {

                if (getIntent().getExtras().getString(ARG_SEARCH_TEXT).equals(getResources().getString(R.string.search_library))) {

                    ((TextView) findViewById(R.id.search)).setText(String.format(getResources().getString(R.string.searching_library_for),
                            mQuery));

                } else {

                    ((TextView) findViewById(R.id.search)).setText(String.format(getResources().getString(R.string.searching_press_cuttings_for),
                            mQuery));

                }

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_search, menu);

        // Modify some aspects of the SearchView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQuery(mQuery, false);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {

                SearchActivity.this.mQuery = query;

                return false;

            }

        });

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)
                searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setTextColor(getResources().getColor(R.color.white));

        // Recovering from orientation change, try not to have focus
        if (mQuery != null && !mQuery.isEmpty()) {

           searchView.clearFocus();

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

        // User pressed search so start new SearchFragment
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(getIntent().getExtras()
                            .getParcelableArrayList(ARG_ARTICLE_LIST_HELPER_LIST), intent.getStringExtra(SearchManager.QUERY)))
                    .commit();

            // Change search hint text to reflect new search
            if (getIntent().getExtras().getString(ARG_SEARCH_TEXT).equals(getResources().getString(R.string.search_library))) {

                ((TextView) findViewById(R.id.search)).setText(String.format(getResources().getString(R.string.searching_library_for),
                        intent.getStringExtra(SearchManager.QUERY)));

            } else {

                ((TextView) findViewById(R.id.search)).setText(String.format(getResources().getString(R.string.searching_press_cuttings_for),
                        intent.getStringExtra(SearchManager.QUERY)));

            }

        }

    }

}
