package com.example.personalfinanceapp;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.AlphaVantageException;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;

import static com.example.personalfinanceapp.BuildConfig.AlphaVantageKey;

public class InvestmentPerformanceActivity extends AppCompatActivity {
    private Spinner spinnerTickerSymbols;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag2_investment_performance_layout);
        Button buttonRequestStockData = (Button) findViewById(R.id.buttonRetrieveStockData);
        spinnerTickerSymbols = (Spinner) findViewById(R.id.spinnerTickerSymbol);

        //arrayOfTickerSymbols.add("LGND");
        //spinnerTickerSymbols.setAdapter(R.id.);

        //ArrayList<String> arrayOfTickerSymbols = new ArrayList<>();
        //arrayOfTickerSymbols.add("LGND");
        //ArrayAdapter<String> arrayAdapter =

        buttonRequestStockData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveStockData();
            }
        });

    }

    private void retrieveStockData() {
        String tickerSymbol = this.spinnerTickerSymbols.getSelectedItem().toString();

        Config cfg = Config.builder()
                .key(AlphaVantageKey)
                .timeOut(10)
                .build();

        AlphaVantage.api().init(cfg);

        AlphaVantage.api()
                .timeSeries()
                .daily()
                .forSymbol(tickerSymbol)
                .outputSize(OutputSize.FULL)
                .onSuccess(e->handleSuccess(e))
                .onFailure(e->handleFailure(e))
                .fetch();
    }

    private void handleFailure(AlphaVantageException e) {
        Looper.prepare();
        Toast.makeText(this, "Failed to retrieve stock data", Toast.LENGTH_LONG).show();
    }

    private void handleSuccess(Object e) {
        //TODO: Make selectedDate dynamic
        String selectedDate = "2021-08-11";
        Pattern p1 = Pattern.compile("open=([\\d\\.]*), high=([\\d\\.]*), low=([\\d\\.]*), close=([\\d\\.]*), adjustedClose=([\\d\\.]*), volume=([\\d]*), dividendAmount=([\\d\\.]*), splitCoefficient=([\\d\\.]*), " +
                "date=" + selectedDate);
        Matcher m1 = p1.matcher(e.toString());
        TextView textViewOpen = (TextView) findViewById(R.id.textViewOpeningPrice);
        TextView textViewClose = (TextView) findViewById(R.id.textViewClosingPrice);
        TextView textViewDate = (TextView) findViewById(R.id.textViewDate);

        if (!m1.find()) {
            Looper.prepare();
            Toast.makeText(this, "Failed to retrieve stock data", Toast.LENGTH_LONG).show();
        } else {
            textViewOpen.setText(m1.group(1));
            textViewClose.setText(m1.group(4));
            textViewDate.setText(selectedDate);
            }
        }
    }
