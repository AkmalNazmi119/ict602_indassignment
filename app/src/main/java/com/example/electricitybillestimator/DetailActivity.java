package com.example.electricitybillestimator;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView textMonth, textUnit, textRebate, textTotal, textFinal;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textMonth = findViewById(R.id.textMonth);
        textUnit = findViewById(R.id.textUnit);
        textRebate = findViewById(R.id.textRebate);
        textTotal = findViewById(R.id.textTotal);
        textFinal = findViewById(R.id.textFinal);

        db = new DatabaseHelper(this);

        int billId = getIntent().getIntExtra("BILL_ID", -1);

        if (billId != -1) {
            showBillDetails(billId);
        }
    }

    private void showBillDetails(int id) {
        Cursor res = db.getDataById(id);

        if (res.moveToFirst()) {
            String month = res.getString(1);
            int unit = res.getInt(2);
            double rebate = res.getDouble(3);
            double total = res.getDouble(4);
            double finalCost = res.getDouble(5);

            textMonth.setText("Month: " + month);
            textUnit.setText("Units Used: " + unit + " kWh");
            textRebate.setText("Rebate: " + rebate + " %");
            textTotal.setText("Total Charges: RM " + String.format("%.2f", total));
            textFinal.setText("Final Cost: RM " + String.format("%.2f", finalCost));
        }
    }
}
