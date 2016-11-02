package com.example.gek.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;


import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


/**
 * Created by gek on 31.10.16.
 */

//todo кнопка назад не работает - запускает еще одну копию самого себя

public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = "MyLog: ";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider();

//        ShareActionProvider mShareActionProvider =
//                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, forecast + FORECAST_SHARE_HASHTAG);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "sdfg" + "dfgdfg");

        mShareActionProvider.setShareIntent(shareIntent);




        // Get the provider and hold onto it to set/change the share intent.
//            ShareActionProvider mShareActionProvider =
//                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//            // Attach an intent to this ShareActionProvider.  You can update this at any time,
//            // like when the user selects a new piece of data they might like to share.
//            if (mShareActionProvider != null ) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
//            } else {
//                Log.d(LOG_TAG, "Share Action Provider is null?");
//            }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Добавляем акшен бар
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, new DetailFragment())
                    .commit();
        }

    }


    /** -------------------------------  FRAGMENT  ----------------------------------- */
    public static class DetailFragment extends Fragment{

        private String forecast;
        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            // Указываем, что в этом фрагменте будет меню
//            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View viewRoot = inflater.inflate(R.layout.fragment_detail, container, false);

            // Интент получаем через активити в которое он и передавался
            Intent intentList = getActivity().getIntent();
            if (intentList != null && intentList.hasExtra(Intent.EXTRA_TEXT)) {
                TextView tvDetail = (TextView) viewRoot.findViewById(R.id.tvDetail);
                forecast = intentList.getStringExtra(Intent.EXTRA_TEXT);
                tvDetail.setText(forecast);
            }
            return viewRoot;
        }

//        @Override
//        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.detailfragment, menu);
//
//            // Retrieve the share menu item
//            MenuItem menuItem = menu.findItem(R.id.action_share);
//
//            // Get the provider and hold onto it to set/change the share intent.
//            ShareActionProvider mShareActionProvider =
//                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//            // Attach an intent to this ShareActionProvider.  You can update this at any time,
//            // like when the user selects a new piece of data they might like to share.
//            if (mShareActionProvider != null ) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
//            } else {
//                Log.d(LOG_TAG, "Share Action Provider is null?");
//            }
//        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                // окно настроек
                case R.id.action_settings:
                    startActivity(new Intent(getActivity(), SettingsActivity.class), null);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
//    private Intent createShareForecastIntent() {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        shareIntent.setType("text/plain");
////        shareIntent.putExtra(Intent.EXTRA_TEXT, forecast + FORECAST_SHARE_HASHTAG);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "sdfg" + "dfgdfg");
//        return shareIntent;
//    }


}
