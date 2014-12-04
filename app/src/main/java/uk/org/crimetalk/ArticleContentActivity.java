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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import uk.org.crimetalk.adapters.items.ArticleListItem;
import uk.org.crimetalk.fragments.ArticleContentFragment;
import uk.org.crimetalk.utils.ThemeUtils;

/**
 * {@link android.app.Activity} that shows the content of an article.
 *
 * @see {@link uk.org.crimetalk.fragments.ArticleContentFragment}
 */
public class ArticleContentActivity extends ActionBarActivity {

    // Arg for the ArticleListItem that was clicked
    public static final String ARG_LIST_ITEM = "list_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // Modify various attributes of the Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_up_indicator));

        // Get the ArticleListItem that was sent as an extra
        if (getIntent().getExtras() != null) {

            final ArticleListItem articleListItem = getIntent().getExtras().getParcelable(ArticleContentActivity.ARG_LIST_ITEM);

            ((TextView) findViewById(R.id.title)).setText(articleListItem.getTitle());
            ((TextView) findViewById(R.id.author)).setText(articleListItem.getAuthor());

            // If new instance of ArticleContentActivity, load the ArticleContentFragment
            if (savedInstanceState == null) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, ArticleContentFragment.newInstance((ArticleListItem) getIntent().getExtras()
                                .getParcelable(ARG_LIST_ITEM)))
                        .commit();

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.getMenuInflater().inflate(R.menu.activity_article_content, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final ArticleListItem articleListItem = getIntent().getExtras().getParcelable(ArticleContentActivity.ARG_LIST_ITEM);

        switch (item.getItemId()) {

            case android.R.id.home:

                this.finish();

                return true;

            case R.id.action_share:

                final Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.check_out_article));
                shareIntent.putExtra(Intent.EXTRA_TEXT, articleListItem.getLink());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_via)));

                return true;

            case R.id.action_browser:

                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(articleListItem.getLink()));
                startActivity(intent);

                return true;

            case R.id.action_clipboard:

                final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                final ClipData clipData = ClipData.newPlainText("article", String.format(getResources()
                        .getString(R.string.clipboard_article), articleListItem.getTitle(), articleListItem.getAuthor(), articleListItem.getLink()));
                clipboard.setPrimaryClip(clipData);

                SuperActivityToast.create(this, getResources().getString(R.string.clipboard_toast),
                        SuperToast.Duration.SHORT, Style.getStyle(Style.RED)).show();

                return true;

        }

        return false;

    }

}
