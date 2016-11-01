package com.example.gek.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gek on 31.10.16.
 */

//todo кнопка назад не работает - запускает еще одну копию самого себя

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, new DetailFragment())
                    .commit();
        }

    }


    public static class DetailFragment extends Fragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Указываем, что в этом фрагменте будет меню
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View viewRoot = inflater.inflate(R.layout.fragment_detail, container, false);

            // Интент получаем через активити в которое он и передавался
            Intent intentList = getActivity().getIntent();
            if (intentList != null && intentList.hasExtra(Intent.EXTRA_TEXT)) {
                TextView tvDetail = (TextView) viewRoot.findViewById(R.id.tvDetail);
                String forecast = intentList.getStringExtra(Intent.EXTRA_TEXT);
                tvDetail.setText(forecast);
            }
            return viewRoot;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment, menu);
        }

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


}
