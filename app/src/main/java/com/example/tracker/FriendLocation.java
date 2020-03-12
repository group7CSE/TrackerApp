package com.example.tracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FriendLocation extends AppCompatActivity {

    Button find, history;
    EditText friend_roll_no,friend_name;
    TextView locate;
    Database database = new Database(FriendLocation.this);
    String rollNo, location, fetch_history;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_location);
        locate = (TextView) findViewById(R.id.locate);
        find = (Button)findViewById(R.id.find);
        history = (Button) findViewById(R.id.history);
        friend_roll_no = (EditText)findViewById(R.id.friend_rollno);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollNo = friend_roll_no.getText().toString().trim().toLowerCase();

                if(TextUtils.isEmpty(rollNo)){
                    Toast.makeText(getApplicationContext(),"Please enter roll no",Toast.LENGTH_LONG).show();
                    return;
                }
                location = database.find_friendLocation(rollNo);
                System.out.println(rollNo);
                if(location.contains("null"))
                    locate.setText("Location Not available");
                else
                    locate.setText(location);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollNo = friend_roll_no.getText().toString().trim().toLowerCase();
                System.out.println(rollNo);
                if(TextUtils.isEmpty(rollNo)){
                    Toast.makeText(getApplicationContext(),"Please enter roll no",Toast.LENGTH_LONG).show();
                    return;
                }
                fetch_history = database.fetch_history(rollNo);
                if(fetch_history.contains("null"))
                    locate.setText("History Not available");
                else
                    locate.setText(fetch_history);


            }
        });
    }
}
