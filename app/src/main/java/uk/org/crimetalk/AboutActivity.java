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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import uk.org.crimetalk.fragments.AboutFragment;
import uk.org.crimetalk.utils.ThemeUtils;

/**
 * {@link android.app.Activity} that shows information about the app.
 *
 * @see {@link uk.org.crimetalk.fragments.AboutFragment}
 */
public class AboutActivity extends ActionBarActivity {

    // URLs for the options menu
    private static final String RATE_URL = "market://details?id=uk.org.crimetalk";
    private static final String ISSUE_URL = "https://github.com/JohnPersano/CrimeTalk-Reader/issues";
    private static final String CONTACT_URL = "https://plus.google.com/+JohnPersano";

    // Used in DialogUtils
    public static final String FAQ_URL = "https://github.com/JohnPersano/CrimeTalk-Reader/wiki/FAQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Modify various attributes of the Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getResources().getString(R.string.about_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_up_indicator));

        // If new instance of AboutActivity, load the AboutFragment
        if (savedInstanceState == null) {

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new AboutFragment())
                    .commit();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_about, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                this.finish();

                return true;

            case R.id.action_rate:

                final Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                rateIntent.setData(Uri.parse(RATE_URL));
                startActivity(rateIntent);

                return true;

            case R.id.action_faq:

                final Intent faqIntent = new Intent(Intent.ACTION_VIEW);
                faqIntent.setData(Uri.parse(FAQ_URL));
                startActivity(faqIntent);

                return true;

            case R.id.action_issue:

                final Intent issueIntent = new Intent(Intent.ACTION_VIEW);
                issueIntent.setData(Uri.parse(ISSUE_URL));
                startActivity(issueIntent);

                return true;

            case R.id.action_contact:

                final Intent contactIntent = new Intent(Intent.ACTION_VIEW);
                contactIntent.setData(Uri.parse(CONTACT_URL));
                startActivity(contactIntent);
                return true;

        }

        return false;

    }

}
