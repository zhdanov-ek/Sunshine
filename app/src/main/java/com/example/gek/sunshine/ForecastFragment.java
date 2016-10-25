package com.example.gek.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gek on 25.10.16.
 */

public class ForecastFragment extends Fragment {
    private String TAG = "ForecastFragment: ";

    public ForecastFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        // container  нужен для передачи параметров от родительского конейнера для нашего вью
        // false указывает на то, что не следует полученое вью вставлять в контейнер
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> list = new ArrayList<String>();
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

        // Объявляем эти переменные вне try, что бы можно было их закрыт потом
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;

        // Сюда получаем ответ с сервера в формате json
        String forecastJson = null;

        try {
            // TODO: 25.10.16 Вынести за рамки проекта ключ, что бы не светить на гитхабе
            //      buildTypes.each {
            //      it.buildConfigField 'String', 'OPEN_WEATHER_MAP_API_KEY', key123456superkey

//                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Cherkassy,ua&mode=json&cnt=7&lang=ru&units=metric&");
            String baseUrl = "http://google.com.ua";
            String apiKey = "&APPIID=";

            URL url = new URL("http://google.com.ua");
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            String line = null;
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while (line == br.readLine()){
                forecastJson += line;
            }

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } finally {
            Toast.makeText(getContext(), forecastJson, Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    //todo Создать асинтакс и перенести в него загрузку файла
}

