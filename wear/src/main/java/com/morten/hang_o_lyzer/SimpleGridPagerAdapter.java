package com.morten.hang_o_lyzer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morten on 03-05-2016.
 */

public class SimpleGridPagerAdapter extends FragmentGridPagerAdapter {
    private static final int TRANSITION_DURATION_MILLIS = 100;

    private final Context mContext;
    private List<Row> mRows;
    private ColorDrawable mDefaultBg;

    private ColorDrawable mClearBg;

    public SimpleGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;

        mRows = new ArrayList<Row>();
        CustomFragment cFrag = new CustomFragment();

//        ImageView img = (ImageView) cFrag.getActivity().findViewById(R.id.imageView);
//        img.setImageResource(R.drawable.rochefort10);

//        mRows.add(new Row(new CustomFragment()));
//        mRows.add(new Row(cardFragment(R.string.dismiss_title, R.string.dismiss_text)));
//        mRows.add(new Row(cardFragment(R.string.dismiss_title, R.string.dismiss_text)));
        mRows.add(new Row(
                customFragment(R.drawable.groentuborg,"Grøn Tuborg"),
                customFragment(R.drawable.tuborgguld,"Guld Tuborg"),
                customFragment(R.drawable.rochefort10,"Rochefort 10")
        ));

        mRows.add(new Row(
                customFragment(R.drawable.gintonic,"Gin & Tonic"),
                customFragment(R.drawable.longisland,"Long Island")
        ));

        mRows.add(new Row(
                customFragment(R.drawable.sourz,"Sourz"),
                customFragment(R.drawable.fernementa,"Fernet-Menta")
        ));
        mDefaultBg = new ColorDrawable(R.color.dark_grey);
        mClearBg = new ColorDrawable(android.R.color.transparent);
    }

    private Fragment customFragment(int imgId, String description){
        CustomFragment cFrag = new CustomFragment();
        cFrag.imgid = imgId;
        cFrag.desc = description;
        return cFrag;
    }

//    LruCache<Integer, Drawable> mRowBackgrounds = new LruCache<Integer, Drawable>(3) {
//        @Override
//        protected Drawable create(final Integer row) {
//            int resid = BG_IMAGES[row % BG_IMAGES.length];
//            new DrawableLoadingTask(mContext) {
//                @Override
//                protected void onPostExecute(Drawable result) {
//                    TransitionDrawable background = new TransitionDrawable(new Drawable[] {
//                            mDefaultBg,
//                            result
//                    });
//                    mRowBackgrounds.put(row, background);
//                    notifyRowBackgroundChanged(row);
//                    background.startTransition(TRANSITION_DURATION_MILLIS);
//                }
//            }.execute(resid);
//            return mDefaultBg;
//        }
//    };

    LruCache<Point, Drawable> mPageBackgrounds = new LruCache<Point, Drawable>(3) {
        @Override
        protected Drawable create(final Point page) {
            // place bugdroid as the background at row 2, column 1
            if (page.y == 2 && page.x == 1) {
                int resid = R.drawable.bugbeer;
                new DrawableLoadingTask(mContext) {
                    @Override
                    protected void onPostExecute(Drawable result) {
                        TransitionDrawable background = new TransitionDrawable(new Drawable[] {
                                mClearBg,
                                result
                        });
                        mPageBackgrounds.put(page, background);
                        notifyPageBackgroundChanged(page.y, page.x);
                        background.startTransition(TRANSITION_DURATION_MILLIS);
                    }
                }.execute(resid);
            }
            return GridPagerAdapter.BACKGROUND_NONE;
        }
    };

    private Fragment cardFragment(int titleRes, int textRes) {
        Resources res = mContext.getResources();
        CardFragment fragment =
                CardFragment.create(res.getText(titleRes), res.getText(textRes));
        // Add some extra bottom margin to leave room for the page indicator
        fragment.setCardMarginBottom(
                res.getDimensionPixelSize(R.dimen.card_margin_bottom));
        return fragment;
    }

//    static final int[] BG_IMAGES = new int[] {
//            R.drawable.groentuborg,
//            R.drawable.tuborgguld,
//            R.drawable.rochefort10,
//            R.drawable.gintonic,
//            R.drawable.longisland
//    };

    /** A convenient container for a row of fragments. */
    private class Row {
        final List<Fragment> columns = new ArrayList<Fragment>();

        public Row(Fragment... fragments) {
            for (Fragment f : fragments) {
                add(f);
            }
        }

        public void add(Fragment f) {
            columns.add(f);
        }

        Fragment getColumn(int i) {
            return columns.get(i);
        }

        public int getColumnCount() {
            return columns.size();
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Row adapterRow = mRows.get(row);
        return adapterRow.getColumn(col);
    }

//    @Override
//    public Drawable getBackgroundForRow(final int row) {
//        return mRowBackgrounds.get(row);
//    }
//
//    @Override
//    public Drawable getBackgroundForPage(final int row, final int column) {
//        return mPageBackgrounds.get(new Point(column, row));
//    }

    @Override
    public int getRowCount() {
        return mRows.size();
    }

    @Override
    public int getColumnCount(int rowNum) {
        return mRows.get(rowNum).getColumnCount();
    }

    class DrawableLoadingTask extends AsyncTask<Integer, Void, Drawable> {
        private static final String TAG = "Loader";
        private Context context;

        DrawableLoadingTask(Context context) {
            this.context = context;
        }

        @Override
        protected Drawable doInBackground(Integer... params) {
            Log.d(TAG, "Loading asset 0x" + Integer.toHexString(params[0]));
            return context.getResources().getDrawable(params[0]);
        }
    }
}
