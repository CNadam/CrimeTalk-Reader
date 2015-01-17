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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that will be used to display items in the {@link uk.org.crimetalk.views.NavigationDrawerListView}.
 */
@SuppressWarnings("WeakerAccess")
public class NavigationDrawerListItem implements Parcelable {

    private final String mTitle;
    private final int mIconResource;
    private final boolean mIsSeparator;

    /**
     * Public constructor. This default constructor creates a separator item within
     * a {@link uk.org.crimetalk.views.NavigationDrawerListView}
     */
    public NavigationDrawerListItem() {

        this.mTitle = "";
        this.mIconResource = 0;
        this.mIsSeparator = true;

    }

    /**
     * Public constructor. This default constructor creates a secondary navigation item within
     * a {@link uk.org.crimetalk.views.NavigationDrawerListView}
     */
    public NavigationDrawerListItem(String title) {

        this.mTitle = title;
        this.mIconResource = 0;
        this.mIsSeparator = false;

    }

    /**
     * Public constructor. This default constructor creates a primary navigation item within
     * a {@link uk.org.crimetalk.views.NavigationDrawerListView}
     */
    public NavigationDrawerListItem(String title, int iconResource) {

        this.mTitle = title;
        this.mIconResource = iconResource;
        this.mIsSeparator = false;

    }

    /**
     * Returns the title of the
     * {@link NavigationDrawerListItem}.
     *
     * @return Title as a {@link String}
     */
    public String getTitle() {

        return mTitle;

    }

    /**
     * Returns the icon resource of the
     * {@link NavigationDrawerListItem}.
     *
     * @return Resource as an {@link java.lang.Integer}
     */
    public int getIconResource() {

        return mIconResource;

    }

    /**
     * Returns true if {@link NavigationDrawerListItem}
     * is a separator.
     *
     * @return Is a separator as a {@link java.lang.Boolean}
     */
    public boolean isSeparator() {

        return mIsSeparator;

    }

    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(mTitle);
        parcel.writeInt(mIconResource);
        parcel.writeByte((byte) (mIsSeparator ? 1 : 0));

    }

    public static final Creator CREATOR = new Creator() {

        public NavigationDrawerListItem createFromParcel(Parcel parcel) {

            return new NavigationDrawerListItem(parcel);

        }

        public NavigationDrawerListItem[] newArray(int size) {

            return new NavigationDrawerListItem[size];

        }

    };

    private NavigationDrawerListItem(Parcel parcel) {

        this.mTitle = parcel.readString();
        this.mIconResource = parcel.readInt();
        this.mIsSeparator = parcel.readByte() != 0;

    }

    /**
     * Public static method which returns a separator item for a
     * {@link uk.org.crimetalk.views.NavigationDrawerListView}.
     *
     * @return A separator {@link uk.org.crimetalk.adapters.items.NavigationDrawerListItem}
     */
    public static NavigationDrawerListItem createSeparatorItem() {

        return new NavigationDrawerListItem();

    }

    /**
     * Public static method which returns a secondary navigation item for a
     * {@link uk.org.crimetalk.views.NavigationDrawerListView}.
     *
     * @param title The title of the item
     *
     * @return A secondary navigation {@link uk.org.crimetalk.adapters.items.NavigationDrawerListItem}
     */
    public static NavigationDrawerListItem createSecondaryNavigationItem(String title) {

        return new NavigationDrawerListItem(title);

    }

    /**
     * Public static method which returns a primary navigation item for a
     * {@link uk.org.crimetalk.views.NavigationDrawerListView}.
     *
     * @param title The title of the item
     * @param iconResource The icon resource of the item
     *
     * @return A primary navigation {@link uk.org.crimetalk.adapters.items.NavigationDrawerListItem}
     */
    public static NavigationDrawerListItem createPrimaryNavigationItem(String title, int iconResource) {

        return new NavigationDrawerListItem(title, iconResource);

    }

}