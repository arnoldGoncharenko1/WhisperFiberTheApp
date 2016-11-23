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

public class CustomerList extends AppCompatActivity {

    ListView CustomerList;
    ArrayAdapter<String> arrayAdapter;
    List<Customer> listOfCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        CustomerList = (ListView) findViewById(R.id.lstCustomers);

        MySQLiteHelper db = new MySQLiteHelper(this);

        List<String> listOfCustomerNames = new ArrayList<String>();

        listOfCustomers = db.getAllCustomers();

        for (int i= 0; i < listOfCustomers.size(); i++) {
            listOfCustomerNames.add(listOfCustomers.get(i).custName);
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listOfCustomerNames);

        CustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CustomerAdd.class);
                intent.putExtra("Mode", 1);
                int retreivedCustId = com.developer.arnold.whisperfibertheapp.CustomerList.this.listOfCustomers.get(position).custID;
                intent.putExtra("ID", retreivedCustId);
                startActivityForResult(intent, 100);
            }
        });

        CustomerList.setAdapter(arrayAdapter);

        db.close();
    }

    public void createNewCustomer(View view) {
        Intent intent = new Intent(this, CustomerAdd.class);
        intent.putExtra("Mode", 0);
        intent.putExtra("ID", listOfCustomers.size() + 1);
        startActivityForResult(intent, 100);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                MySQLiteHelper db = new MySQLiteHelper(this);
                List<String> listOfCustomerNames = new ArrayList<String>();

                listOfCustomers.clear();
                listOfCustomers = db.getAllCustomers();

                for (int i= 0; i < listOfCustomers.size(); i++) {
                    listOfCustomerNames.add(listOfCustomers.get(i).custName);
                }

                arrayAdapter.clear();
                arrayAdapter.addAll(listOfCustomerNames);
                arrayAdapter.notifyDataSetChanged();

                db.close();
            }
        }
    }
}