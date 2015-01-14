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

package uk.org.crimetalk.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Utility class that handles calls to {@link android.content.SharedPreferences}.
 */
public class PreferenceUtils {

    // Various Preference keys
    private static final String KEY_LOAD_IN_BROWSER = "load_in_browser";
    private static final String KEY_TIMEOUT = "timeout";
    private static final String KEY_DARK_THEME = "dark_theme";
    private static final String KEY_USER_LEARNED_NAVIGATION = "user_learned_navigation";
    private static final String KEY_USER_LEARNED_PRESS_CUTTINGS_WARING= "user_learned_press_cuttings_warning";

    /**
     * Returns the network timeout requested by the user as found in
     * {@link uk.org.crimetalk.SettingsActivity.SettingsFragment}.
     *
     * @return Desired network timeout in milliseconds
     */
    public static int getTimeout(Context context) {

        return (PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_TIMEOUT, 10) * 1000);

    }

    /**
     * Returns the load in browser setting requested by the user as found in
     * {@link uk.org.crimetalk.SettingsActivity.SettingsFragment}.
     *
     * @return true if user desires to view all articles in a local browser
     */
    public static boolean getLoadInBrowser(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(KEY_LOAD_IN_BROWSER, false);

    }

    /**
     * Returns the theme requested by the user as found in
     * {@link uk.org.crimetalk.SettingsActivity.SettingsFragment}.
     *
     * @return true if user desires a dark theme
     */
    public static boolean getDarkTheme(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_DARK_THEME, false);

    }

    /**
     * Sets the value for user learning the NavigationDrawer.
     *
     * @param context           A valid {@link android.content.Context}
     * @param learnedNavigation true if user has learned the NavigationDrawer
     */
    @SuppressWarnings("SameParameterValue")
    public static void setUserLearnedNavigation(Context context, boolean learnedNavigation) {

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(KEY_USER_LEARNED_NAVIGATION, learnedNavigation).commit();

    }

    /**
     * Returns the user learned NavigationDrawer value.
     *
     * @return true if user has learned NavigationDrawer
     */
    public static boolean getUserLearnedNavigation(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_USER_LEARNED_NAVIGATION, false);

    }

    /**
     * Sets the value for user learning the PressCuttings search warning.
     *
     * @param context           A valid {@link android.content.Context}
     * @param learnedWarning true if user has learned the warning
     */
    @SuppressWarnings("SameParameterValue")
    public static void setUserLearnedPressCuttingsWarning(Context context, boolean learnedWarning) {

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(KEY_USER_LEARNED_PRESS_CUTTINGS_WARING, learnedWarning).commit();

    }

    /**
     * Returns the user learned PressCuttings search value.
     *
     * @return true if user has learned the warning
     */
    public static boolean getUserLearnedPressCuttingsWarning(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_USER_LEARNED_PRESS_CUTTINGS_WARING, false);

    }


}
