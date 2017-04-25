package com.example.muthuraman.memorableplaces;

import android.app.Activity;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arr ;
    /*ArrayList<Double> lat;
    ArrayList<Double> lon;
    ArrayList<String> address;*/
    double lat[]=new double[20];int l1 =0;
    double lon[]=new double[20];int l2 =0;

    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        arr = new ArrayList<String>();
        Log.i("s","s");
        arr.add("Add a Place");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MyPlaces.class);
                intent.putExtra("val", i);
                if (i != 0) {
                    intent.putExtra("lat", lat[i-1]);
                    intent.putExtra("long", lon[i-1]);
                    intent.putExtra("address", arr.get(i));
                }
                startActivityForResult(intent, i);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
        if(requestCode==0)
        {
            lat[l1++]=(data.getDoubleExtra("lat", -1.1));
            lon[l2++]=(data.getDoubleExtra("long", -1.1));
            arr.add(data.getStringExtra("address"));
        }
        arrayAdapter.notifyDataSetChanged();
    }
    }

}
