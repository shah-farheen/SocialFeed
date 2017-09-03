package com.buggyarts.socialfeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buggyarts.socialfeed.models.DataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Gson gson;
    private Type dataListType = new TypeToken<ArrayList<DataModel>>(){}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_data);
        gson = new Gson();
//        SparseArray<Long> map = new SparseArray<>();

        ArrayList<DataModel> dataList = getData();
//        for(DataModel model : dataList){
//            Log.e("DataModel", model.getName());
//            Log.e("DataModel", model.getText());
//        }
        Collections.sort(dataList, new Comparator<DataModel>() {
            @Override
            public int compare(DataModel dataModel, DataModel t1) {
                return dataModel.getTime().compareTo(t1.getTime());
            }
        });

        long lastTime = -1;
        ListIterator<DataModel> iterator = dataList.listIterator();
        while(iterator.hasNext()){
            DataModel model = iterator.next();
            if(model.getTime() != lastTime){
                lastTime = model.getTime();
                iterator.previous();
                iterator.add(getDateModel(lastTime));
                iterator.next();
            }
        }

//        for(int i=0; i<dataList.size(); i++){
//            if(dataList.get(i).getTime() != lastTime){
//                lastTime = dataList.get(i).getTime();
//                if(i==0){
//                    map.put(0, lastTime);
//                }
//                else {
//                    map.put(i-1, lastTime);
//                }
//            }
//        }

//        for(int i=0; i<map.size(); i++){
//            DataModel model = new DataModel();
//            model.setDate(true);
//            model.setTime(map.get(map.keyAt(i)));
//            dataList.add(map.keyAt(i), model);
//        }

        recyclerView.setAdapter(new DataAdapter(this, dataList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<DataModel> getData(){
        InputStream inputStream = getResources().openRawResource(R.raw.data);
        return gson.fromJson(new InputStreamReader(inputStream), dataListType);
    }

    private DataModel getDateModel(long time){
        DataModel model = new DataModel();
        model.setDate(true);
        model.setTime(time);
        return model;
    }
}
