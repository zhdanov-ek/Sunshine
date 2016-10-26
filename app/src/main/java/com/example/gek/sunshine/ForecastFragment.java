package com.example.gek.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gek on 25.10.16.
 */

public class ForecastFragment extends Fragment {

    public ForecastFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Указываем, что в этом фрагменте будет меню
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // заполняем меню с xml файла
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Проверяем наша ли кнопка нажата и обрабатываем
        if (item.getItemId() == R.id.action_refresh) {
//                Toast.makeText(getContext(), "click Button Refresh", Toast.LENGTH_SHORT).show();
                FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
                fetchWeatherTask.execute(null, null, null);
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        // container  нужен для передачи параметров от родительского конейнера для нашего вью
        // false указывает на то, что не следует полученое вью вставлять в контейнер
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> list = new ArrayList<>();
        list.add("Today Sunny - 88/63");
        list.add("Tomorrow - Foggy - 70/46");
        list.add("Weds - Cloudy - 72/63");
        list.add("Thurs - Rainy - 64/51");
        list.add("Fri - Foggy - 70-46");
        list.add("Sat - Sunny - 76-68");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, list);

        // ищем вью не с корня, а с родительского вью  - rootView, куда и вставлен наш ListView
        ListView lvForecast = (ListView) rootView.findViewById(R.id.listview_forecast);
        lvForecast.setAdapter(adapter);

        return rootView;
    }

    /** AsyncTask для загрузки файла */
    public class FetchWeatherTask extends AsyncTask<String, Integer, String>{
        // Константу-таг для логов формируем по имени класса
//        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        private final String LOG_TAG = "MyLog";

        @Override
        protected String doInBackground(String... params) {
            // Объявляем эти переменные вне try, что бы можно было их закрыт потом
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Сюда получаем ответ с сервера в формате json
            String forecastJson = "";

            try {
                // в gradle.properties добавляем наш ключ, который хотим не бросать на гитхаб
                // В build.gradle (app) добавляем строки инициализации этого ключа

//            String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Cherkassy,ua&mode=json&cnt=7&lang=ru&units=metric&APPIID=";
//            String apiKey = BuildConfig.MY_API_KEY;
//            URL url = new URL(baseUrl.concat(apiKey));

                //на время отладки юзаем другой урл
                String myUrl = "" + BuildConfig.MY_API_KEY;

                URL url = new URL(myUrl);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                forecastJson = buffer.toString();


            } catch (IOException e){
                e.printStackTrace();
                Log.e(LOG_TAG, "IOException: ", e);
                return null;
            } finally {
                // в конце закрываем соединение и поток если они открыты
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

}

