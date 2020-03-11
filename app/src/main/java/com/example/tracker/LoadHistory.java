package com.example.tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoadHistory extends AppCompatActivity {

    String fetch_history;
    TextView historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_history);

        historyList = (TextView)findViewById(R.id.historyList);
        fetch_history = database.load_history(rollNo);
        if(fetch_history.contains("null"))
            fetch_history.setText("Not available");
        else
            historyList.setText(fetch_history);
    }
}
