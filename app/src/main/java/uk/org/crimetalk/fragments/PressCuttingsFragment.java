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
 * {@link android.app.Fragment} that will show Press Cuttings sections in a {@link android.support.v4.view.ViewPager}
 * along with a {@link uk.org.crimetalk.views.TabStrip}. Extends {@link PagerFragment}.
 */
public class PressCuttingsFragment extends PagerFragment {

    // URLs to be loaded
    private static final String ECONOMIC_CORPORATE_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=944&Itemid=299";
    private static final String MEDIA_STIGMA_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=941&Itemid=287";
    private static final String VIOLENCE_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=942&Itemid=289";
    private static final String DRUGS_MIND_CORPORATE_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=919&Itemid=269";
    private static final String PRISONS_PUNISHMENTS_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=916&Itemid=246";
    private static final String YOUTH_CHILDREN_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=918&Itemid=248";
    private static final String POLICING_GOVERNANCE_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=940&Itemid=286";
    private static final String SOCIETY_POLICY_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=921&Itemid=272";
    private static final String CRIMETALK_URL = "http://crimetalk.org.uk/index.php?option=com_content&view=category&id=928&Itemid=278";
    private static final String SUPPORT_EDUCATION_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=128&Itemid=288";
    private static final String CULTURE_IDEOLOGY_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=124&Itemid=298";
    private static final String RACE_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=915&Itemid=245";
    private static final String STATE_WAR_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=951&Itemid=300";
    private static final String VICTIMS_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=922&Itemid=273";
    private static final String SEX_GENDER_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=917&Itemid=247";
    private static final String LAW_JUSTICE_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=129&Itemid=297";
    private static final String HISTORY_URL = "http://crimetalk.org.uk/index.php?option=com_weblinks&view=category&id=109&Itemid=504";

    // Jsoup information used for most PressCuttings Fragments
    private static final String JSOUP_CLASS = "weblink-category";
    private static final String JSOUP_SELECTION = "li";

    @Override
    public void addFragments() {

        // Add the Fragments that will be shown in the ViewPager and SlidingTabStrip
        for (ArticleListHelper articleListHelper : PressCuttingsFragment.getPages(getActivity())) {

            this.addFragment(articleListHelper.getTitle(), ArticleListFragment.newInstance(articleListHelper, true));

        }

    }

    @Override
    public boolean shouldAddQuickNav() {

        // There are a lot of items in this Fragment, add a quick navigation Fragment
        return true;

    }

    /**
     * {@link java.util.List} which holds all of the pages in the
     * {@link uk.org.crimetalk.fragments.PressCuttingsFragment}.
     *
     * @param context A valid {@link android.content.Context}
     * @return A {@link java.util.List} of {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     */
    public static List<ArticleListHelper> getPages(Context context) {

        final Resources resources = context.getResources();

        return Arrays.asList(
                new ArticleListHelper(resources.getString(R.string.presscuttings_economic_corporate),
                        ECONOMIC_CORPORATE_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_media_stigma),
                        MEDIA_STIGMA_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_violence),
                        VIOLENCE_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_drugs_mind),
                        DRUGS_MIND_CORPORATE_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_prisons_punishment),
                        PRISONS_PUNISHMENTS_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_youth_children),
                        YOUTH_CHILDREN_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_policing_governance),
                        POLICING_GOVERNANCE_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_society_policy),
                        SOCIETY_POLICY_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_crimetalk),
                        CRIMETALK_URL, "category", "tr", ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_support_education),
                        SUPPORT_EDUCATION_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_culture_ideology),
                        CULTURE_IDEOLOGY_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_race),
                        RACE_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_state_war),
                        STATE_WAR_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_victims),
                        VICTIMS_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_sex_gender),
                        SEX_GENDER_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_law_justice),
                        LAW_JUSTICE_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT),
                new ArticleListHelper(resources.getString(R.string.presscuttings_history),
                        HISTORY_URL, JSOUP_CLASS, JSOUP_SELECTION, ArticleListHelper.PRESSCUTTINGS_FRAGMENT));

    }

}
