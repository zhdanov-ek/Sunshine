package com.example.gek.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;


import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


/**
 * Отображение конкретного дня выбранного в списке
 */

//todo кнопка назад не работает - запускает еще одну копию самого себя

public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = "MyLog: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Добавляем екшен бар
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher);
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
        private final String LOG_TAG = "MyLog: ";

        public DetailFragment() {
            // Указываем, что будем использовать меню в фрагменте.
            // Без этого не вызовется onCreateOptionsMenu
            setHasOptionsMenu(true);
        }


        // Создаем вью нашего фрагмента на основании лаяута
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


        // Наполняем меню с шаблона и подвязываем виджет ShareActionProvider
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Наполнение меню
            inflater.inflate(R.menu.detail_fragment_menu, menu);

            // Ищем наш виджет ShareActionProvider
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Определяем объект для управления нашим виджетом
            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            // Attach an intent to this ShareActionProvider.  You can update this at any time,
            // like when the user selects a new piece of data they might like to share.
            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        // Описываем обработчики на другие кнопки (крроме ShareActionProvider)
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

        // Создание неявного интента для пересылки прогноза суточного
        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            // Флаг позволяет вернутся в саншайн из вызванного приложения по кнопке БЕК
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, forecast + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }
    }

}
