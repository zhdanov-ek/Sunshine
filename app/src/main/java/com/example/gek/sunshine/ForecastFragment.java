/**
 *  Фрагмент отображающий список
 */

package com.example.gek.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> adapter;

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
        // Проверяем наша ли кнопка нажата и запускаем AsyncTask на запрос данных с сервера и парсинг
        if (item.getItemId() == R.id.action_refresh) {
//                Toast.makeText(getContext(), "click Button Refresh", Toast.LENGTH_SHORT).show();
                FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
                fetchWeatherTask.execute("Cherkassy");
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle saveInstanceState){
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


        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, list);

        // ищем вью не с корня, а с родительского вью  - rootView, куда и вставлен наш ListView
        ListView lvForecast = (ListView) rootView.findViewById(R.id.listview_forecast);
        lvForecast.setAdapter(adapter);

        lvForecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "Click on item " + position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), DetailActivity.class));
            }
        });

        return rootView;
    }

    // Форматируем под нужный нам шаблон вывод нашу дату
    private String getReadableDateString(long time){
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }


    /**
     *  AsyncTask для загрузки файла
     * */
    private class FetchWeatherTask extends AsyncTask<String, Integer, String[]>{
        // Константу-таг для логов формируем по имени класса
//        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        private final String LOG_TAG = "MyLog";

        @Override
        protected String[] doInBackground(String... params) {
            // Объявляем эти переменные вне try, что бы можно было их закрыт потом
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                //todo gradle.properties все еще выгружается на github вместе с ключом
                // в gradle.properties добавляем наш ключ, который хотим не бросать на гитхаб
                // В build.gradle (app) добавляем строки инициализации этого ключа

                String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String apiKey = BuildConfig.MY_API_KEY;

                // Параметры доступные для формирования запроса
                final String QUERY_PARAM = "q";
                final String LANG_PARAM = "lang";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String KEY_PARAM = "APPID";

                // Собираем URI формирующий общий запрос на сервер
                Uri fullPathUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(LANG_PARAM, "ru")
                        .appendQueryParameter(FORMAT_PARAM, "json")
                        .appendQueryParameter(UNITS_PARAM, "metric")
                        .appendQueryParameter(DAYS_PARAM, "7")
                        .appendQueryParameter(KEY_PARAM, apiKey)
                        .build();

                URL url = new URL(fullPathUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Если данных нет то выходим и адаптер не обновится в onPostExecute
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // оформляем текст по строчно для удобства чтения во время дебага
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Если данных нет то выходим и адаптер не обновится в onPostExecute
                    return null;
                }

                // Текстовый ответ с сервера в форамате json
                String forecastJson = buffer.toString();

                // Парсим полученную строку json и в случае успеха возвращает в onPostExecute массив
                try {
                    return getWeatherDataFromJson(forecastJson, 7);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }


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

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null){
                adapter.clear();
                for (String oneDay: result){
                    adapter.add(oneDay);
                }

            }
        }
    }

    // Метод для парсинга нашей json строки
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        final String LIST = "list";
        final String WEATHER = "weather";
        final String TEMPERATURE = "temp";
        final String MAX = "max";
        final String MIN = "min";
        final String DESCRIPTION = "main";

        JSONObject jsonObjectForecast = new JSONObject(forecastJsonStr);
        JSONArray jsonArrayListDay = jsonObjectForecast.getJSONArray(LIST);

        Time dayTime = new Time();
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        dayTime = new Time();


        // Перебираем каждую запись (день) в массиве json "list" и формируем инфу за день
        String[] resultDays = new String[numDays];
        for (int i = 0; i < jsonArrayListDay.length(); i++) {
            String day;
            String description;
            String maxAndMin;

            // Определяем интересующий нас день и форматируем его в нужный нам формат
            long dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            // Получаем текущий (i) день со всеми вложенными данными
            JSONObject jsonObjectDay = jsonArrayListDay.getJSONObject(i);

            // Получаем из текущего дня вложенный объект "weather"
            JSONObject weatherObject = jsonObjectDay.getJSONArray(WEATHER).getJSONObject(0);
            description = weatherObject.getString(DESCRIPTION);

            // Получаем максимальную и минимальную температуру с объекта "temp"
            double max = jsonObjectDay.getJSONObject(TEMPERATURE).getDouble(MAX);
            double min = jsonObjectDay.getJSONObject(TEMPERATURE).getDouble(MIN);

            maxAndMin = Math.round(max) + " / " + Math.round(min);

            resultDays[i] = day + " - " + description + " - " + maxAndMin;

        }
        return resultDays;
    }

}

