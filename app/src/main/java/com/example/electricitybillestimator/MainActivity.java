package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth, spinnerRebate;
    EditText editTextUnits;
    TextView textTotal, textFinal;
    Button buttonCalculate, buttonSave, buttonHistory, buttonAbout;
    DatabaseHelper db;

    double totalCharges = 0;
    double finalCost = 0;
    int enteredUnits = 0;
    double rebatePercent = 0.0;
    String selectedMonth = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerRebate = findViewById(R.id.spinnerRebate);
        editTextUnits = findViewById(R.id.editTextUnits);
        textTotal = findViewById(R.id.textTotalCharges);
        textFinal = findViewById(R.id.textFinalCost);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonHistory = findViewById(R.id.buttonHistory);
        buttonAbout = findViewById(R.id.buttonAbout);

        db = new DatabaseHelper(this);

        // Setup month spinner
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(monthAdapter);

        // Setup rebate spinner
        String[] rebateOptions = {"0%", "1%", "2%", "3%", "4%", "5%"};
        ArrayAdapter<String> rebateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, rebateOptions);
        spinnerRebate.setAdapter(rebateAdapter);

        // Calculate button
        buttonCalculate.setOnClickListener(view -> {
            if (editTextUnits.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter units", Toast.LENGTH_SHORT).show();
                return;
            }

            enteredUnits = Integer.parseInt(editTextUnits.getText().toString());
            selectedMonth = spinnerMonth.getSelectedItem().toString();

            String selectedRebate = spinnerRebate.getSelectedItem().toString().replace("%", "");
            rebatePercent = Double.parseDouble(selectedRebate);

            totalCharges = calculateCharges(enteredUnits);
            finalCost = calculateFinalCost(totalCharges, rebatePercent);

            textTotal.setText("Total Charges: RM " + String.format("%.2f", totalCharges));
            textFinal.setText("Final Cost after Rebate: RM " + String.format("%.2f", finalCost));
        });

        // Save button
        buttonSave.setOnClickListener(view -> {
            if (selectedMonth.isEmpty() || enteredUnits == 0 || totalCharges == 0) {
                Toast.makeText(MainActivity.this, "Please calculate first", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean inserted = db.insertData(selectedMonth, enteredUnits, rebatePercent, totalCharges, finalCost);
            if (inserted) {
                Toast.makeText(MainActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Save Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // View history button
        buttonHistory.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // About button
        buttonAbout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private double calculateCharges(int units) {
        double total = 0;
        if (units <= 200)
            total = units * 0.218;
        else if (units <= 300)
            total = 200 * 0.218 + (units - 200) * 0.334;
        else if (units <= 600)
            total = 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
        else
            total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
        return total;
    }

    private double calculateFinalCost(double total, double rebate) {
        return total - (total * (rebate / 100));
    }

    private void clearFields() {
        editTextUnits.setText("");
        spinnerRebate.setSelection(0);
        textTotal.setText("Total Charges: RM");
        textFinal.setText("Final Cost after Rebate: RM");
        enteredUnits = 0;
        totalCharges = 0;
        finalCost = 0;
    }
}
