package com.example.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Database {
     private DatabaseReference databaseReference;
     private final Context mcontext;
     public FirebaseAuth firebaseAuth;
     String roll_no,currentLocation, mac, location, location1, block, pos1, pos2, pos3, time, date;
     String mac1;
    boolean avail = false;


     public Database(Context context){
         this.mcontext=context;
     }

     //To store users information
     public void add_UserDetails(final String name,final String rollno,final String email,final String pass){

         databaseReference = FirebaseDatabase.getInstance().getReference();

         UserDetails users = new UserDetails(name,rollno,email,pass);

         databaseReference.child("User Details").child(users.Name).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 Toast.makeText(mcontext,"Success",Toast.LENGTH_SHORT).show();
             }
         });
     }

     //To store tracked data
     public String track(String Id,String mac,String date, String time){

         final String email;
         firebaseAuth = FirebaseAuth.getInstance();
         FirebaseUser user = firebaseAuth.getCurrentUser();
         email = user.getEmail();


         DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User Details");

         Query zonesQuery = ref.orderByChild("Email").equalTo(email);

         zonesQuery.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {

                     roll_no = zoneSnapshot.child("Rollno").getValue(String.class);
                     System.out.println("@@@@@@@@@@  "+ roll_no);
                 }
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {
                 System.out.println("@@@ onCancelled"+ databaseError.toException());
             }
         });

         databaseReference = FirebaseDatabase.getInstance().getReference();

         if(roll_no == null)
             roll_no = "1";

         tracking_detail detail = new tracking_detail(roll_no,Id,mac,date,time);
         currentLocation = detail.ID;

         System.out.println("*********  "+ roll_no);

         if(!(roll_no == "1")) {

             databaseReference.child("Tracking").child(detail.RollNo).setValue(detail).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     Toast.makeText(mcontext, "Success", Toast.LENGTH_SHORT).show();
                 }
             });
             history(Id, mac, date, time);
         }
         return currentLocation;
     }

     //Store history
    public void history(String Id,String mac,String date, String time){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        history_detail hd = new history_detail(roll_no,Id,mac,date,time);
        databaseReference.child("History").child(hd.RollNo).child(hd.Date).child(hd.Time).setValue(hd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(mcontext,"History Success",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Check User available
    public boolean checkUser(String roll_no){
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Student Details");

        Query zonesQuery = ref2.orderByChild("Rollno").equalTo(roll_no);

        zonesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {

                    avail = zoneSnapshot.child("Rollno").exists();
                    System.out.println("xxx "+avail);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("@@@ onCancelled"+ databaseError.toException());
            }
        });
        return avail;
     }

    //Fetch History of friend
    public String fetch_history(String roll){
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("History");
        System.out.println("***Find Friend History***"+roll);

        Ref.orderByChild("Date").limitToLast(1);

        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        mac1 = dataSnapshot.getValue().toString();
                        //   time = ds.child("id").getValue(String.class);
                        System.out.println("$$ " + mac1);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return mac1;
    }
     //Friend location
     public String find_friendLocation(String roll){

         DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tracking");

         Query zonesQuery = ref.orderByChild("RollNo").equalTo(roll);

         System.out.println("***Find Friend***");

         zonesQuery.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {

                     mac = zoneSnapshot.child("MAC").getValue(String.class);
                     time = zoneSnapshot.child("Time").getValue(String.class);
                     date = zoneSnapshot.child("Date").getValue(String.class);

                     System.out.println("@@@@friend location  "+ mac);
                 }
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {
                 System.out.println("@@@ onCancelled"+ databaseError.toException());
             }
         });

         //fetching location from access point table using mac id
         DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Access Point Location");

         Query zonesQuery1 = ref1.orderByChild("mac").equalTo(mac);

         zonesQuery1.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {

                     location = zoneSnapshot.child("ssid").getValue(String.class);
                     block = zoneSnapshot.child("block").getValue(String.class);
                     pos1 = zoneSnapshot.child("pos1").getValue(String.class);
                     pos2 = zoneSnapshot.child("pos2").getValue(String.class);
                     pos3 = zoneSnapshot.child("pos3").getValue(String.class);

                     System.out.println("@@@@@@@@@@  "+ location);
                 }
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {
                 System.out.println("@@@ onCancelled"+ databaseError.toException());
             }
         });

         location1 = "Your friend's location \n"+"\nBlock : "+block+"\nNear or in room \n"+pos1+"\n"+pos2+"\n"+pos3+ "\nDate : "+ date+"\nTime : "+time;

         System.out.println("*********  "+ location1);

         return location1;
     }
}