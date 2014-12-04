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

package uk.org.crimetalk.adapters.items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that will be used to display items in the {@link uk.org.crimetalk.fragments.ArticleListFragment}.
 */
public class ArticleListItem implements Parcelable {

    private String mTitle;
    private String mDate;
    private String mAuthor;
    private String mHits;
    private String mLink;

    /**
     * Empty public constructor. Items should be accessed by public methods.
     *
     * @see ArticleListItem#setTitle(String)
     * @see ArticleListItem#setDate(String)
     * @see ArticleListItem#setAuthor(String)
     * @see ArticleListItem#setHits(String)
     * @see ArticleListItem#setLink(String)
     * @see ArticleListItem#getTitle()
     * @see ArticleListItem#getTitle()
     * @see ArticleListItem#getTitle()
     * @see ArticleListItem#getTitle()
     * @see ArticleListItem#getTitle()
     */
    public ArticleListItem() {

        /* Do nothing */

    }

    /**
     * Sets the title of the {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     *
     * @param title The title to be displayed
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleListItem}
     */
    public ArticleListItem setTitle(String title) {

        this.mTitle = title;

        return this;

    }

    /**
     * Sets the date of the {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     * The date will appear as "Jan 2014".
     *
     * @param date The date to be displayed
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleListItem}
     */
    public ArticleListItem setDate(String date) {

        this.mDate = date;

        return this;

    }

    /**
     * Sets the author of the {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     * The author will appear as "Written by Bulfinch".
     *
     * @param author The author to be displayed
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleListItem}
     */
    public ArticleListItem setAuthor(String author) {

        this.mAuthor = author;

        return this;

    }

    /**
     * Sets the hits of the {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     * The hits will appear as "Hits: 1234".
     *
     * @param hits The hits to be displayed
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleListItem}
     */
    public ArticleListItem setHits(String hits) {

        this.mHits = hits;

        return this;

    }

    /**
     * Sets the link of the {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     * The link is used for navigation.
     *
     * @param link The link to be navigated to
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleListItem}
     */
    public ArticleListItem setLink(String link) {

        this.mLink = link;

        return this;

    }

    /**
     * Returns the title of the
     * {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     *
     * @return Title as a {@link java.lang.String}
     */
    public String getTitle() {

        return mTitle;

    }

    /**
     * Returns the date of the
     * {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     *
     * @return Date as a {@link java.lang.String}
     */
    public String getDate() {

        return mDate;

    }

    /**
     * Returns the author of the
     * {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     *
     * @return Author as a {@link java.lang.String}
     */
    public String getAuthor() {

        return mAuthor;

    }

    /**
     * Returns the hits of the
     * {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     *
     * @return Hits as a {@link java.lang.String}
     */
    public String getHits() {

        return mHits;

    }

    /**
     * Returns the link of the
     * {@link uk.org.crimetalk.adapters.items.ArticleListItem}.
     *
     * @return Link as a {@link java.lang.String}
     */
    public String getLink() {

        return mLink;

    }

    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(mTitle);
        parcel.writeString(mDate);
        parcel.writeString(mAuthor);
        parcel.writeString(mHits);
        parcel.writeString(mLink);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public ArticleListItem createFromParcel(Parcel parcel) {

            return new ArticleListItem(parcel);

        }

        public ArticleListItem[] newArray(int size) {

            return new ArticleListItem[size];

        }

    };

    private ArticleListItem(Parcel parcel) {

        this.mTitle = parcel.readString();
        this.mDate = parcel.readString();
        this.mAuthor = parcel.readString();
        this.mHits = parcel.readString();
        this.mLink = parcel.readString();

    }

}