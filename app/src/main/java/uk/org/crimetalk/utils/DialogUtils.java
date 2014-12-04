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

package uk.org.crimetalk.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import uk.org.crimetalk.AboutActivity;
import uk.org.crimetalk.R;
import uk.org.crimetalk.adapters.items.ArticleListItem;

/**
 * Utility class used to show various {@link com.afollestad.materialdialogs.MaterialDialog}
 * throughout the app.
 */
public class DialogUtils {

    private static final String JSOUP_URL = "http://jsoup.org";
    private static final String PICASSO_URL = "http://square.github.io/picasso/";
    private static final String SUPERTOASTS_URL = "https://github.com/JohnPersano/SuperToasts";
    private static final String MATERIALDIALOGS_URL = "https://github.com/afollestad/material-dialogs";
    private static final String PAGERSLIDINGTABSTRIP_URL = "https://github.com/astuetz/PagerSlidingTabStrip";

    /**
     * {@link com.afollestad.materialdialogs.MaterialDialog} used to display generic
     * article options.
     *
     * @param context         A valid {@link android.content.Context}
     * @param articleListItem The {@link uk.org.crimetalk.adapters.items.ArticleListItem} that
     *                        called the MaterialDialog
     * @return {@link com.afollestad.materialdialogs.MaterialDialog.Builder}
     * @see {@link uk.org.crimetalk.fragments.ArticleListFragment}
     */
    public static MaterialDialog.Builder getGenericArticleDialog(final Context context, final ArticleListItem articleListItem) {

        return new MaterialDialog.Builder(context)
                .title(context.getResources().getString(R.string.dialog_generic_article_title))
                .items(context.getResources().getStringArray(R.array.dialog_generic_article_items))
                .itemsCallback(new MaterialDialog.ListCallback() {

                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int selection, CharSequence text) {

                        switch (selection) {

                            case 0:

                                final Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.check_out_article));
                                shareIntent.putExtra(Intent.EXTRA_TEXT, articleListItem.getLink());
                                shareIntent.setType("text/plain");
                                context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.share_via)));

                                break;

                            case 1:

                                final Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(articleListItem.getLink()));
                                context.startActivity(intent);

                                break;

                            case 2:

                                final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                final ClipData clipData = ClipData.newPlainText("article", String.format(context.getResources()
                                        .getString(R.string.clipboard_article), articleListItem.getTitle(), articleListItem.getAuthor(), articleListItem.getLink()));
                                clipboard.setPrimaryClip(clipData);

                                SuperActivityToast.create((Activity) context, context.getResources().getString(R.string.clipboard_toast),
                                        SuperToast.Duration.SHORT, Style.getStyle(Style.RED)).show();

                                break;

                        }

                    }

                });

    }

    /**
     * {@link com.afollestad.materialdialogs.MaterialDialog} used to display the
     * third party libraries used in this app.
     *
     * @param context A valid {@link android.content.Context}
     * @return {@link com.afollestad.materialdialogs.MaterialDialog.Builder}
     * @see {@link uk.org.crimetalk.fragments.AboutFragment}
     */
    public static MaterialDialog.Builder getLibrariesDialog(final Context context) {

        return new MaterialDialog.Builder(context)
                .title(context.getResources().getString(R.string.dialog_libraries_title))
                .items(context.getResources().getStringArray(R.array.dialog_libraries_items))
                .itemsCallback(new MaterialDialog.ListCallback() {

                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int selection, CharSequence text) {

                        switch (selection) {

                            case 0:

                                final Intent jsoupIntent = new Intent(Intent.ACTION_VIEW);
                                jsoupIntent.setData(Uri.parse(JSOUP_URL));
                                context.startActivity(jsoupIntent);

                                break;

                            case 1:

                                final Intent picassoIntent = new Intent(Intent.ACTION_VIEW);
                                picassoIntent.setData(Uri.parse(PICASSO_URL));
                                context.startActivity(picassoIntent);

                                break;

                            case 2:

                                final Intent supertoastsIntent = new Intent(Intent.ACTION_VIEW);
                                supertoastsIntent.setData(Uri.parse(SUPERTOASTS_URL));
                                context.startActivity(supertoastsIntent);

                                break;

                            case 3:

                                final Intent materialdialogsIntent = new Intent(Intent.ACTION_VIEW);
                                materialdialogsIntent.setData(Uri.parse(MATERIALDIALOGS_URL));
                                context.startActivity(materialdialogsIntent);

                                break;

                            case 4:

                                final Intent pagerslidingtabstripIntent = new Intent(Intent.ACTION_VIEW);
                                pagerslidingtabstripIntent.setData(Uri.parse(PAGERSLIDINGTABSTRIP_URL));
                                context.startActivity(pagerslidingtabstripIntent);

                                break;

                        }

                    }

                });

    }

    /**
     * {@link com.afollestad.materialdialogs.MaterialDialog} used to display the
     * third party libraries used in this app.
     *
     * @param context A valid {@link android.content.Context}
     * @return {@link com.afollestad.materialdialogs.MaterialDialog.Builder}
     * @see {@link uk.org.crimetalk.fragments.AboutFragment}
     */
    public static MaterialDialog.Builder getPressCuttingsWarningDialog(final Context context) {

        return new MaterialDialog.Builder(context)
                .title(context.getResources().getString(R.string.dialog_press_cuttings_warning_title))
                .content(context.getResources().getString(R.string.dialog_press_cuttings_warning_message))
                .negativeText(context.getResources().getString(R.string.dialog_press_cuttings_warning_negative))
                .negativeColor(context.getResources().getColor(R.color.material_blue_grey_950))
                .positiveText(context.getResources().getString(R.string.dialog_press_cuttings_warning_positive))
                .positiveColor(context.getResources().getColor(R.color.material_deep_teal_500))
                .callback(new MaterialDialog.FullCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        PreferenceUtils.setUserLearnedPressCuttingsWarning(context, true);

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                        final Intent faqIntent = new Intent(Intent.ACTION_VIEW);
                        faqIntent.setData(Uri.parse(AboutActivity.FAQ_URL));
                        context.startActivity(faqIntent);

                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {

                        // Do nothing

                    }

                });

    }

}
