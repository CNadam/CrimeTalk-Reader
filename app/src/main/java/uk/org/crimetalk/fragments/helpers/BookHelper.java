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
 * to load a {@link uk.org.crimetalk.fragments.BookFragment}.
 */
@SuppressWarnings("UnusedDeclaration")
public class BookHelper implements Parcelable {

    private String mCoverUrl;
    private String mTitle;
    private String mAuthor;
    private String mFormat;
    private String mSummary;
    private String mButtonUrl;

    /**
     * Public constructor for the {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @param coverUrl  The URL used by {@link com.squareup.picasso.Picasso} to show the book cover
     * @param title     The title of the book. This is shown in the {@link uk.org.crimetalk.views.TabStrip}
     * @param author    The author of the book
     * @param format    The available book formats (i.e. E-book/Paperback)
     * @param summary   The summary of the book
     * @param buttonUrl The URL that should be navigated to if the "More Information" Button is pressed
     */
    public BookHelper(String coverUrl, String title, String author, String format, String summary, String buttonUrl) {

        this.mCoverUrl = coverUrl;
        this.mTitle = title;
        this.mAuthor = author;
        this.mFormat = format;
        this.mSummary = summary;
        this.mButtonUrl = buttonUrl;

    }

    /**
     * Set the book cover URL of the {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     * This URL is used by {@link com.squareup.picasso.Picasso} to load an image in a
     * {@link uk.org.crimetalk.fragments.BookFragment}.
     *
     * @param coverUrl The book cover URL
     * @return The current {@link uk.org.crimetalk.fragments.helpers.BookHelper}
     */
    public BookHelper setCoverUrl(String coverUrl) {

        this.mCoverUrl = coverUrl;

        return this;

    }

    /**
     * Set the book title of the {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     * This title is shown in the {@link uk.org.crimetalk.views.TabStrip}.
     *
     * @param title The book title
     * @return The current {@link uk.org.crimetalk.fragments.helpers.BookHelper}
     */
    public BookHelper setTitle(String title) {

        this.mTitle = title;

        return this;

    }

    /**
     * Set the book author of the {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @param author The book author
     * @return The current {@link uk.org.crimetalk.fragments.helpers.BookHelper}
     */
    public BookHelper setAuthor(String author) {

        this.mAuthor = author;

        return this;

    }

    /**
     * Set the book format of the {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     * The book format refers to the available formats of the book (i.e. E-book/Paperback).
     *
     * @param format The available book format
     * @return The current {@link uk.org.crimetalk.fragments.helpers.BookHelper}
     */
    public BookHelper setFormat(String format) {

        this.mFormat = format;

        return this;

    }

    /**
     * Set the book summary of the {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     * The book summary should be succinct and summarize the main content of the book.
     *
     * @param summary The book summary
     * @return The current {@link uk.org.crimetalk.fragments.helpers.BookHelper}
     */
    public BookHelper setSummary(String summary) {

        this.mSummary = summary;

        return this;

    }

    /**
     * Set the Button URL of the {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     * The Button URL is navigated to when the "More Information" Button is pressed and
     * should link to a website that offers the book for sale.
     *
     * @param buttonUrl The URL that should be navigated to.
     * @return The current {@link uk.org.crimetalk.fragments.helpers.BookHelper}
     */
    public BookHelper setButtonUrl(String buttonUrl) {

        this.mButtonUrl = buttonUrl;

        return this;

    }

    /**
     * Returns the book cover URL of the
     * {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @return Book cover URL as a {@link java.lang.String}
     */
    public String getCoverUrl() {

        return mCoverUrl;

    }

    /**
     * Returns the book title of the
     * {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @return Book title as a {@link java.lang.String}
     */
    public String getTitle() {

        return mTitle;

    }

    /**
     * Returns the book author of the
     * {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @return Book author as a {@link java.lang.String}
     */
    public String getAuthor() {

        return mAuthor;

    }

    /**
     * Returns the available book format of the
     * {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @return Available book format as a {@link java.lang.String}
     */
    public String getFormat() {

        return mFormat;

    }

    /**
     * Returns the book summary of the
     * {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @return Book summary as a {@link java.lang.String}
     */
    public String getSummary() {

        return mSummary;

    }

    /**
     * Returns the Button URL of the
     * {@link uk.org.crimetalk.fragments.helpers.BookHelper}.
     *
     * @return Button URLa as a {@link java.lang.String}
     */
    public String getButtonUrl() {

        return mButtonUrl;

    }

    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(mCoverUrl);
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);
        parcel.writeString(mFormat);
        parcel.writeString(mSummary);
        parcel.writeString(mButtonUrl);

    }

    public static final Creator CREATOR = new Creator() {

        public BookHelper createFromParcel(Parcel parcel) {

            return new BookHelper(parcel);

        }

        public BookHelper[] newArray(int size) {

            return new BookHelper[size];

        }

    };

    private BookHelper(Parcel parcel) {

        this.mCoverUrl = parcel.readString();
        this.mTitle = parcel.readString();
        this.mAuthor = parcel.readString();
        this.mFormat = parcel.readString();
        this.mSummary = parcel.readString();
        this.mButtonUrl = parcel.readString();

    }

}