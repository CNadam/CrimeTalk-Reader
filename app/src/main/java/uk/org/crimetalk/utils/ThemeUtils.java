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

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.WindowManager;

import uk.org.crimetalk.R;

/**
 * Utility class that handles the app theme.
 *
 * @see {@link uk.org.crimetalk.SettingsActivity.SettingsFragment}
 */
public class ThemeUtils {

    /**
     * Sets the theme of the current {@link android.app.Activity}.
     * This should be called before the super method and
     * {@link android.app.Activity#setContentView(int)}.
     *
     * @param activity The current {@link android.app.Activity}
     */
    public static void setTheme(Activity activity) {

        if (PreferenceUtils.getDarkTheme(activity)) {

            activity.setTheme(R.style.Theme_CrimeTalk);

            return;

        }

        activity.setTheme(R.style.Theme_CrimeTalk_Light);

    }

    /**
     * Sets the theme of the current {@link android.app.Activity}.
     * This should be called before the super method and
     * {@link android.app.Activity#setContentView(int)}.
     *
     * @param activity The current {@link android.app.Activity}
     * @param shouldColorStatusBar True if the status bar should be colored in devices running at least API 21
     */
    public static void setTheme(Activity activity, boolean shouldColorStatusBar) {

        if (PreferenceUtils.getDarkTheme(activity)) {

            activity.setTheme(R.style.Theme_CrimeTalk);

            return;

        }

        activity.setTheme(R.style.Theme_CrimeTalk_Light);

        // Lint is strange here so nest the 'if' statements
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if(shouldColorStatusBar) {

                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.crimetalk_dark_red));

            }

        }

    }

    /**
     * Sets the theme of the current {@link android.app.Activity}.
     * This will close and restart the calling {@link android.app.Activity}.
     *
     * @param activity The current {@link android.app.Activity}
     */
    public static void setThemeImmediately(Activity activity) {

        Intent _result = new Intent();
        activity.setResult(Activity.RESULT_OK, _result);

        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}
