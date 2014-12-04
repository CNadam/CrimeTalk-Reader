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

package uk.org.crimetalk.fragments.helpers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Helper class that holds references to various background information necessary
 * to load an {@link uk.org.crimetalk.fragments.ArticleListFragment}.
 */
@SuppressWarnings("UnusedDeclaration")
public class ArticleListHelper implements Parcelable {

    public static final int LIBRARY_FRAGMENT = 0;
    public static final int PRESSCUTTINGS_FRAGMENT = 1;

    private String mTitle;
    private String mUrl;
    private String mJsoupClass;
    private String mJsoupSelection;
    private int mFragmentIdentifier;

    /**
     * Public constructor for the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     *
     * @param title              The title of the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     *                           This is used in the {@link uk.org.crimetalk.views.TabStrip}.
     * @param url                The URL of the {@link uk.org.crimetalk.fragments.ArticleListFragment} data.
     * @param jsoupClass         The {@link org.jsoup.Jsoup} class which holds list items.
     * @param jsoupSelection     The {@link org.jsoup.Jsoup} items within the class.
     * @param fragmentIdentifier The outer {@link uk.org.crimetalk.fragments.PagerFragment}. Use the
     *                           public variables of this class.
     */
    public ArticleListHelper(String title, String url, String jsoupClass, String jsoupSelection, int fragmentIdentifier) {

        this.mTitle = title;
        this.mUrl = url;
        this.mJsoupClass = jsoupClass;
        this.mJsoupSelection = jsoupSelection;
        this.mFragmentIdentifier = fragmentIdentifier;

    }

    /**
     * Set the title of the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     * This is used in the {@link uk.org.crimetalk.fragments.PagerFragment} and is shown
     * in the {@link uk.org.crimetalk.views.TabStrip}.
     *
     * @param title The title of the {@link uk.org.crimetalk.fragments.ArticleListFragment}.
     * @return The current {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     */
    public ArticleListHelper setTitle(String title) {

        this.mTitle = title;

        return this;

    }

    /**
     * Set the URL of the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     * This is used by {@link org.jsoup.Jsoup} as the primary URL.
     *
     * @param url The url of the {@link uk.org.crimetalk.fragments.ArticleListFragment}.
     * @return The current {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     */
    public ArticleListHelper setUrl(String url) {

        this.mUrl = url;

        return this;

    }

    /**
     * Set the {@link org.jsoup.Jsoup} class of the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     * This class should hold all of the items that are displayed in an
     * {@link uk.org.crimetalk.fragments.ArticleListFragment}.
     *
     * @param jsoupClass The {@link org.jsoup.Jsoup} class of the {@link uk.org.crimetalk.fragments.ArticleListFragment}.
     * @return The current {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     */
    public ArticleListHelper setJsoupClass(String jsoupClass) {

        this.mJsoupClass = jsoupClass;

        return this;

    }

    /**
     * Set the {@link org.jsoup.Jsoup} selection of the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     * This selection should reference all of the items within the {@link org.jsoup.Jsoup} class.
     *
     * @param jsoupSelection The {@link org.jsoup.Jsoup} selection of the {@link uk.org.crimetalk.fragments.ArticleListFragment}.
     * @return The current {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     * @see {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper#setJsoupClass(String)}
     * @see {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper#getJsoupClass()}
     */
    public ArticleListHelper setJsoupSelection(String jsoupSelection) {

        this.mJsoupSelection = jsoupSelection;

        return this;

    }

    /**
     * Set the Fragment identifier of the {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     * This identifier is used to determine which outer {@link uk.org.crimetalk.fragments.PagerFragment}
     * is being used.
     *
     * @param fragmentIdentifier The Fragment identifier of the {@link uk.org.crimetalk.fragments.ArticleListFragment}.
     *                           Should use a public int defined in this class
     * @return The current {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}
     * @see {@link uk.org.crimetalk.fragments.LibraryFragment}
     * @see {@link uk.org.crimetalk.fragments.PressCuttingsFragment}
     */
    public ArticleListHelper setFragmentIdentifier(int fragmentIdentifier) {

        this.mFragmentIdentifier = fragmentIdentifier;

        return this;

    }

    /**
     * Returns the title of the
     * {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     *
     * @return Title as a {@link java.lang.String}
     */
    public String getTitle() {

        return mTitle;

    }

    /**
     * Returns the URL of the
     * {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     *
     * @return URL as a {@link java.lang.String}
     */
    public String getUrl() {

        return mUrl;

    }

    /**
     * Returns the {@link org.jsoup.Jsoup} class of the
     * {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     *
     * @return {@link org.jsoup.Jsoup} class as a {@link java.lang.String}
     */
    public String getJsoupClass() {

        return mJsoupClass;

    }

    /**
     * Returns the {@link org.jsoup.Jsoup} selection of the
     * {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     *
     * @return {@link org.jsoup.Jsoup} selection as a {@link java.lang.String}
     */
    public String getJsoupSelection() {

        return mJsoupSelection;

    }

    /**
     * Returns the Fragment identifier of the
     * {@link uk.org.crimetalk.fragments.helpers.ArticleListHelper}.
     *
     * @return Fragment identifier as an int
     */
    public int getFragmentIdentifier() {

        return mFragmentIdentifier;

    }

    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(mTitle);
        parcel.writeString(mUrl);
        parcel.writeString(mJsoupClass);
        parcel.writeString(mJsoupSelection);
        parcel.writeInt(mFragmentIdentifier);

    }

    public static final Creator CREATOR = new Creator() {

        public ArticleListHelper createFromParcel(Parcel parcel) {

            return new ArticleListHelper(parcel);

        }

        public ArticleListHelper[] newArray(int size) {

            return new ArticleListHelper[size];

        }

    };

    private ArticleListHelper(Parcel parcel) {

        this.mTitle = parcel.readString();
        this.mUrl = parcel.readString();
        this.mJsoupClass = parcel.readString();
        this.mJsoupSelection = parcel.readString();
        this.mFragmentIdentifier = parcel.readInt();

    }

}