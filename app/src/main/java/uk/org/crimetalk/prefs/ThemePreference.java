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

package uk.org.crimetalk.prefs;

import android.app.Activity;
import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import uk.org.crimetalk.utils.ThemeUtils;

/**
 * {@link android.preference.Preference} that extends a {@link android.preference.CheckBoxPreference}.
 * OnClick is overridden to apply the chosen theme immediately.
 */
public class ThemePreference extends CheckBoxPreference {

    /**
     * Public constructor for the {@link uk.org.crimetalk.prefs.ThemePreference}.
     * Should not be used in code, use in XML instead.
     *
     * @param context      A valid {@link android.content.Context}
     * @param attributeSet The {@link android.util.AttributeSet} from XML
     */
    public ThemePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        // Do nothing

    }

    @Override
    protected void onClick() {
        super.onClick();

        // Set the chosen theme immediately
        ThemeUtils.setThemeImmediately((Activity) getContext());

    }
}