package com.example.gek.sunshine;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MyLog: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getActionBar();
//                getActivity().getActionBar().setIcon(getResources().getDrawable(R.mipmap.ic_launcher));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new ForecastFragment())
                    .commit();
        }
        if (!isOnline()) {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT);
        }
    }

    /** Проверяем есть ли интернет */
    public boolean isOnline() {
        // Обращаемся к сервису, который отвечает за соединение с интернетом
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }
}
