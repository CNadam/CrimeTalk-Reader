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
import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;

import uk.org.crimetalk.R;
import uk.org.crimetalk.fragments.helpers.ArticleListHelper;

/**
 * {@link android.app.Fragment} that will show Library {@link uk.org.crimetalk.fragments.ArticleListFragment}s
 * in a {@link android.support.v4.view.ViewPager} along with a {@link uk.org.crimetalk.views.TabStrip}.
 * Extends {@link PagerFragment}.
 */
public class LibraryFragment extends PagerFragment {

    // URLs to be loaded
    private static final String FEATURED_ARTICLES_URL = "http://crimetalk.org.uk/index.php?option=com_content&view=category&id=38&Itemid=41";
    private static final String IN_BRIEF_URL = "http://crimetalk.org.uk/index.php?option=com_content&view=category&id=926&Itemid=275";
    private static final String EDITORS_BLOG_URL = "http://crimetalk.org.uk/index.php?option=com_content&view=category&id=946&Itemid=292";

    // Jsoup information used for all Library Fragments
    private static final String JSOUP_CLASS = "category";
    private static final String JSOUP_SELECTION = "tr";

    @Override
    public void addFragments() {

        // Add the Fragments that will be shown in the ViewPager and SlidingTabStrip
        for (ArticleListHelper articleListHelper : LibraryFragment.getPages(getActivity())) {

            this.addFragment(articleListHelper.getTitle(), ArticleListFragment.newInstance(articleListHelper, false));

        }

    }

    /**
     * {@link java.util.List} which holds all of the pages in the
     * {@link uk.org.crimetalk.fragments.LibraryFragment}.
     *
     * @param context A valid {@link android.content.Context}
     * @return A {@link java.util.List} of {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     */
    public static List<ArticleListHelper> getPages(Context context) {

        final Resources resources = context.getResources();

        return Arrays.asList(
                new ArticleListHelper(resources.getString(R.string.library_featured_articles),
                        FEATURED_ARTICLES_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.LIBRARY_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.library_in_brief),
                        IN_BRIEF_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.LIBRARY_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.library_editors_blog),
                        EDITORS_BLOG_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.LIBRARY_FRAGMENT));

    }

}
