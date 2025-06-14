package com.example.electricitybillestimator;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayList<String> listData;
    ArrayList<Integer> listIds; // To store corresponding IDs for each row
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listView);
        db = new DatabaseHelper(this);
        listData = new ArrayList<>();
        listIds = new ArrayList<>();

        loadData();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            int billId = listIds.get(i);
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("BILL_ID", billId);
            startActivity(intent);
        });

        // Long press to delete
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            int billId = listIds.get(i);
            new AlertDialog.Builder(HistoryActivity.this)
                    .setTitle("Delete Record")
                    .setMessage("Are you sure you want to delete this record?")
                    .setPositiveButton("Yes", (dialogInterface, which) -> {
                        boolean deleted = db.deleteData(billId);
                        if (deleted) {
                            Toast.makeText(HistoryActivity.this, "Record deleted", Toast.LENGTH_SHORT).show();
                            loadData(); // Refresh list
                        } else {
                            Toast.makeText(HistoryActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void loadData() {
        listData.clear();
        listIds.clear();

        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            listData.add("No data found");
        } else {
            while (res.moveToNext()) {
                int id = res.getInt(0);
                String month = res.getString(1);
                double finalCost = res.getDouble(5);
                listData.add(month + " - RM " + String.format("%.2f", finalCost));
                listIds.add(id);
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
    }
}
