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

package uk.org.crimetalk.adapters.items;

/**
 * Class that will be used to display items in the {@link uk.org.crimetalk.fragments.ArticleContentFragment}.
 */
public class ArticleContentItem {

    private String mText;
    private String mImageUrl;
    private int mTypeface;

    /**
     * Sets the text of the {@link uk.org.crimetalk.adapters.items.ArticleContentItem}.
     *
     * @param text The text to be displayed
     *
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleContentItem}
     */
    public ArticleContentItem setText(String text) {

        this.mText = text;

        return this;

    }

    /**
     * Sets the image URL of the {@link uk.org.crimetalk.adapters.items.ArticleContentItem}.
     *
     * @param imageUrl The URL used by {@link com.squareup.picasso.Picasso} to show an image
     *
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleContentItem}
     */
    public ArticleContentItem setImageUrl(String imageUrl) {

        this.mImageUrl = imageUrl;

        return this;

    }

    /**
     * Sets the {@link android.graphics.Typeface} of the
     * {@link uk.org.crimetalk.adapters.items.ArticleContentItem} text.
     *
     * @param typeface The {@link android.graphics.Typeface} of the text
     *
     * @return The current {@link uk.org.crimetalk.adapters.items.ArticleContentItem}
     */
    public ArticleContentItem setTypeface(int typeface) {

        this.mTypeface = typeface;

        return this;

    }

    /**
     * Returns the text of the
     * {@link uk.org.crimetalk.adapters.items.ArticleContentItem}.
     *
     * @return Text as a {@link java.lang.String}
     */
    public String getText() {

        return mText;

    }

    /**
     * Returns the image URL of the
     * {@link uk.org.crimetalk.adapters.items.ArticleContentItem}.
     *
     * @return URL as a {@link java.lang.String}
     */
    public String getImageUrl() {

        return mImageUrl;

    }

    /**
     * Returns the text {@link android.graphics.Typeface} of the
     * {@link uk.org.crimetalk.adapters.items.ArticleContentItem}.
     *
     * @return {@link android.graphics.Typeface} int
     */
    public int getTypeface() {

        return mTypeface;

    }

}