package com.example.gek.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                                    .add(R.id.container_main, new PlaceHolderFragment())
                                    .commit();

    }

    public static class PlaceHolderFragment extends Fragment{
        public PlaceHolderFragment(){
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
            return rootView;
        }
    }
}
