package com.example.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

//necessary for mail to admin


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail, editTextName, editTextRollno;
    private Button buttonSignup;
    private TextView textViewSignin, textViewOther;

    ImageView bgapp;
    Animation frombottom;
    LinearLayout register, textsplash;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    String name, email, rollno, pass, designation = "Select";

    Database database = new Database(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        bgapp = (ImageView) findViewById(R.id.bgapp);
        register = findViewById(R.id.register);
        textsplash = findViewById(R.id.textsplash);

        bgapp.animate().translationY(-1900).setDuration(2500).setStartDelay(3000);
        textsplash.animate().translationY(-1900).setDuration(2500).setStartDelay(3000);
        register.startAnimation(frombottom);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null

        Log.e("FirebaseAuth", "" + firebaseAuth.getCurrentUser());
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), UserActivity.class));

        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextRollno = (EditText) findViewById(R.id.editTextRollno);


        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        textViewOther = (TextView)findViewById(R.id.textViewOther);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    designation = "Select";
                if (i == 1) {
                    designation = "Student";
                } else if (i == 2) {
                    designation = "Staff";
                }
                System.out.println(designation);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //user details table
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        progressDialog = new ProgressDialog(this);

        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        textViewOther.setOnClickListener(this);
    }

    //Password generation
    // Java code to generate random password
    // Here we are using random() method of util class in Java
    // This our Password generating method
    // We have use static here, so that we not to make any object for it
    static String geek_Password(int len) {
        Log.e("Geek _password", "Generating password using random() : ");
        Log.e("", "Your new password is : ");

        // A strong password has Cap_chars, Lower_chars,
        // numeric value and symbols. So we are using all of
        // them to generate our password
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String pass = "";

        String values = Capital_chars + Small_chars +
                numbers ;

        // Using random method
        Random rndm_method = new Random();

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            pass += values.charAt(rndm_method.nextInt(values.length()));

        }
        Log.e("", "" + pass);
        return pass;
    }

    private void registerUser() {

        name = editTextName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim().toLowerCase();
        rollno = editTextRollno.getText().toString().trim().toLowerCase();
        pass = geek_Password(8);

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }
       if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(rollno)) {
            Toast.makeText(this, "Please enter designation", Toast.LENGTH_LONG).show();
            return;
        }

        if(database.checkUser(rollno)) {

            Toast.makeText(this, "After successful verification your email and password will be sent to your mail!", Toast.LENGTH_LONG).show();

            //if the email and password are not empty
            //displaying a progress dialog

            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();

            //creating a new user
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.e("Register", "password");
                            //checking if success
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                                //sending mail to admin

                                //Getting content for email
                                String email1 = email;//+","+"dheepigaraja@gmail.com".toString().trim();
                                System.out.println(email1);
                                String subject = "Tracking Request Accepted".toString().trim();
                                String message = "Name : " + name + "\nEmail : " + email + "\nRollno : " + rollno
                                        + "\nYour Password : " + pass.toString().trim();

                                //Creating SendMail object
                                SendMail sm = new SendMail(email1, subject, message);
                                sm.execute();

                                database.add_UserDetails(name, rollno, email, pass);

                                //Executing sendmail to send email
                            } else {
                                //display some message here
                                Toast.makeText(MainActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }

                    });
        }
        else
            Toast.makeText(MainActivity.this, "Roll Number Not Available", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {

        if (view == buttonSignup) {
                registerUser();
        }
        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
                startActivity(new Intent(this, LoginActivity.class));
        }
        if(view == textViewOther)
            startActivity(new Intent(this,OtherOTPLogin.class));

    }
}