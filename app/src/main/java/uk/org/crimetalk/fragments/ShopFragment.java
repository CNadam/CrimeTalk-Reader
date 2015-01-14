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
import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;

import uk.org.crimetalk.R;
import uk.org.crimetalk.fragments.helpers.BookHelper;

/**
 * {@link android.app.Fragment} that will show Book sections in a {@link android.support.v4.view.ViewPager}
 * along with a {@link uk.org.crimetalk.views.TabStrip}. Extends {@link uk.org.crimetalk.fragments.PagerFragment}.
 */
public class ShopFragment extends PagerFragment {

    // Sociology of Crime and Deviance book information
    private static final String SOD_COVER_URL = "http://www.ypdbooks.com/img/p/1064-1196-large.jpg";
    private static final String SOD_INFORMATION_URL = "http://www.crimetalk.org.uk/index.php?option=com_content&view=article&id=780:the-sociology-of-deviance-an-obituary&catid=947:crimetalk-books";

    // Bhopal book information
    private static final String BHOPAL_COVER_URL = "http://www.ypdbooks.com/img/p/672-759-large.jpg";
    private static final String BHOPAL_INFORMATION_URL = "http://www.crimetalk.org.uk/index.php?option=com_content&view=article&id=767:bhopal-flowers-at-the-altar-of-profit-and-power&catid=947:crimetalk-books";

    @Override
    public void addFragments() {

        // Add the Fragments that will be shown in the ViewPager and SlidingTabStrip
        for (BookHelper bookHelper : ShopFragment.getPages(getActivity())) {

            this.addFragment(bookHelper.getTitle(), BookFragment.newInstance(bookHelper));

        }

    }

    /**
     * {@link java.util.List} which holds all of the pages in the
     * {@link uk.org.crimetalk.fragments.ShopFragment}.
     *
     * @param context A valid {@link android.content.Context}
     * @return A {@link java.util.List} of {@link uk.org.crimetalk.fragments.helpers.BookHelper}
     */
    @SuppressWarnings("WeakerAccess")
    public static List<BookHelper> getPages(Context context) {

        final Resources resources = context.getResources();

        return Arrays.asList(
                new BookHelper(SOD_COVER_URL, resources.getString(R.string.sod_title), resources.getString(R.string.sod_author),
                        resources.getString(R.string.sod_format), resources.getString(R.string.sod_summary), SOD_INFORMATION_URL),
                new BookHelper(BHOPAL_COVER_URL, resources.getString(R.string.bhopal_title), resources.getString(R.string.bhopal_author),
                        resources.getString(R.string.bhopal_format), resources.getString(R.string.bhopal_summary), BHOPAL_INFORMATION_URL));

    }

}
