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

package uk.org.crimetalk.fragments;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import uk.org.crimetalk.R;
import uk.org.crimetalk.utils.DialogUtils;

/**
 * {@link android.app.Fragment} that displays generic information about the application.
 * This can be navigated to in the secondary items of the {@link uk.org.crimetalk.fragments.NavigationDrawerFragment}.
 */
public class AboutFragment extends Fragment {

    private static final String GITHUB_URL = "https://github.com/JohnPersano/CrimeTalk-Reader";

    private int mClickCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_about, container, false);

        /* The code below is a simple easter egg. Long-pressing the icon five times will
           display a message that nods to the developer's graduating classmates. */
        final ImageView imageView = (ImageView) view.findViewById(R.id.icon);

        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(1500);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                mClickCounter++;

                if (mClickCounter == 5) {

                    SuperActivityToast.create(getActivity(), getActivity().getResources().getString(R.string.about_class_nod), SuperToast.Duration.SHORT,
                            Style.getStyle(Style.BLACK)).show();

                    return true;

                }

                objectAnimator.start();

                return true;

            }

        });

        // This Button navigates to the libraries used in this project
        final Button librariesButton = (Button) view.findViewById(R.id.libraries);
        librariesButton.setOnClickListener(mOnClickListener);

        // This Button navigates to the Github project page
        final Button githubButton = (Button) view.findViewById(R.id.github);
        githubButton.setOnClickListener(mOnClickListener);

        return view;

    }

    // Private OnCLickListener which handles Button clicks in a switch statement
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.libraries:

                    DialogUtils.getLibrariesDialog(getActivity()).show();

                    break;

                case R.id.github:

                    final Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(GITHUB_URL));
                    startActivity(intent);

                    break;

            }

        }
    };

}
