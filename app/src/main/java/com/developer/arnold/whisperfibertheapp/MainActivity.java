package com.developer.arnold.whisperfibertheapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MySQLiteHelper db = new MySQLiteHelper(this);
//        db.destroyDB(this);
    }

    public void viewCustomers(View view) {
        Intent intent = new Intent(this, CustomerList.class);
        startActivity(intent);
    }

    public void viewLogs(View view) {
        Intent intent = new Intent(this, LogList.class);
        startActivity(intent);
    }
}
