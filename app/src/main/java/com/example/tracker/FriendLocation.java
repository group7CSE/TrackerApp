package com.example.tracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Pattern;

public class FriendLocation extends AppCompatActivity {

    Button find, history;
    EditText friend_roll_no,friend_name;
    TextView locate;
    Database database = new Database(FriendLocation.this);
    String rollNo, location, display;
    ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_location);
        locate = (TextView) findViewById(R.id.locate);
        find = (Button)findViewById(R.id.find);
        history = (Button) findViewById(R.id.history);
        friend_roll_no = (EditText)findViewById(R.id.friend_rollno);
        historyList = findViewById(R.id.historyList);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollNo = friend_roll_no.getText().toString().trim().toLowerCase();

                if(TextUtils.isEmpty(rollNo)){
                    Toast.makeText(getApplicationContext(),"Please enter roll no",Toast.LENGTH_LONG).show();
                    return;
                }
                historyList.setVisibility(View.INVISIBLE);
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
                locate.setText("");
                rollNo = friend_roll_no.getText().toString().trim().toLowerCase();
                System.out.println(rollNo);
                if(TextUtils.isEmpty(rollNo)){
                    Toast.makeText(getApplicationContext(),"Please enter roll no",Toast.LENGTH_LONG).show();
                    return;
                }
                historyList.setVisibility(View.VISIBLE);
                display = database.fetch_history(rollNo);
                System.out.println("@@@@@@@@@@@@@@@@@"+display);
                //Splitting
                 if(display != null){
                     String c = "",d="", h="",e = "",j="";
                     String[] arrOfStr = display.split(Pattern.quote("}}},"));
                     for (String a : arrOfStr)
                         if(a.contains(rollNo))
                             c += a;
                     String[] b = c.split(Pattern.quote("}},"));
                     for(String a : b){
                         if(a.contains("ID")){
                             e = a;
                             break;}
                     }
                     String[] f = e.split(Pattern.quote("={"));
                     for(String a : f)
                         h += " "+a;
                     String i[] = h.split(" ");
                     for(String a : i)
                     {
                         if(a.contains("ID"))
                             j += a+" ";
                         if(a.contains("Time"))
                             j += a+" ";
                     }
                     String k[] = j.split(Pattern.quote(", "));
                     String hist[] = new String[k.length/2] ;
                     Arrays.fill(hist,"");
                     int z=-1,x=0;
                     for(String a : k)
                     {
                         if(x%2 == 0)
                             z++;
                         x++;
                         hist[z] += a+"  ";
                         //System.out.println(a);
                     }
                    System.out.println(d);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendLocation.this,android.R.layout.simple_list_item_1,hist);
                    historyList.setAdapter(adapter);
                 }
//Splitting
                locate.setText("Path history of Friend\n\n");
            }
        });
    }
}