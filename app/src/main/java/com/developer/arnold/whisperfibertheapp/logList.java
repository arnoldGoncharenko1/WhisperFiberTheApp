package com.developer.arnold.whisperfibertheapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LogList extends AppCompatActivity {

    ListView logList;
    ArrayAdapter<String> arrayAdapter;
    List<Log> listOfLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_list);

        logList = (ListView) findViewById(R.id.lstLogs);

        MySQLiteHelper db = new MySQLiteHelper(this);

        List<String> listOfLogNames = new ArrayList<>();

        listOfLogs = db.getAllLogs();

        for (int i= 0; i < listOfLogs.size(); i++) {
            listOfLogNames.add(listOfLogs.get(i).logName);
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listOfLogNames );

        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), LogAdd.class);
                intent.putExtra("Mode", 1);
                int retreivedLogId = LogList.this.listOfLogs.get(position).logID;
                intent.putExtra("ID", retreivedLogId);
                startActivityForResult(intent, 100);
            }
        });

        logList.setAdapter(arrayAdapter);

        db.close();
    }

    public void createNewLog(View view) {
        Intent intent = new Intent(this, LogAdd.class);
        intent.putExtra("Mode", 0);
        startActivityForResult(intent, 100);
    }

    public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                MySQLiteHelper db = new MySQLiteHelper(this);

                List<String> listOfLogNames = new ArrayList<>();

                listOfLogs.clear();
                listOfLogs = db.getAllLogs();

                for (int i= 0; i < listOfLogs.size(); i++) {
                    listOfLogNames.add(listOfLogs.get(i).logName);
                }

                arrayAdapter.clear();
                arrayAdapter.addAll(listOfLogNames);
                arrayAdapter.notifyDataSetChanged();

                db.close();
            }
        }
    }

}
