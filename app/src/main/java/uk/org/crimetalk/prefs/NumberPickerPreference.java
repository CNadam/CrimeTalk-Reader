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

package uk.org.crimetalk.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import uk.org.crimetalk.R;
import uk.org.crimetalk.utils.PreferenceUtils;

/**
 * {@link android.preference.Preference} that uses a {@link android.widget.NumberPicker}.
 * Used for the timeout preference.
 */
public class NumberPickerPreference extends DialogPreference {

    private NumberPicker mNumberPicker;

    /**
     * Public constructor for the {@link uk.org.crimetalk.prefs.NumberPickerPreference}.
     * Should not be used in code, use in XML instead.
     *
     * @param context      A valid {@link android.content.Context}
     * @param attributeSet The {@link android.util.AttributeSet} from XML
     */
    public NumberPickerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        // Do nothing

    }

    @Override
    @SuppressLint("InflateParams")
    protected View onCreateDialogView() {

        final LayoutInflater layoutInflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = layoutInflater.inflate(R.layout.preference_numberpicker, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

        mNumberPicker.setMaxValue(25);
        mNumberPicker.setMinValue(5);
        mNumberPicker.setValue(getPersistedInt(10));
        mNumberPicker.setWrapSelectorWheel(false);
        mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // There's a bug in the text color, correct it manually
        if (mNumberPicker.getChildCount() >= 1) {

            final View childView = mNumberPicker.getChildAt(1);

            if (childView != null && childView instanceof TextView) {

                if (PreferenceUtils.getDarkTheme(getContext())) {

                    ((TextView) childView).setTextColor(getContext().getResources().getColor(R.color.white));

                } else {

                    ((TextView) childView).setTextColor(getContext().getResources().getColor(R.color.black));

                }

            }

        }

        return view;

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        // Dialog was closed so the current value should be saved
        if (positiveResult) {

            persistInt(mNumberPicker.getValue());

        }

    }

}