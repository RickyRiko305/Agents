package com.example.asus.agents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private FirebaseDatabase firebaseDatabase;

    private TextView totalLead;
    private TextView currentLead;
    private TextView GPayID;
    private EditText newGPayID;
    private Button btnGPay;

    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootRef = firebaseDatabase.getReference().child("state").child("user");//.child(currentUser);

        totalLead = (TextView) findViewById(R.id.InputTotalLead);
        currentLead = (TextView) findViewById(R.id.currentLead);
        GPayID = (TextView) findViewById(R.id.googlePayAccount);

        newGPayID = (EditText) findViewById(R.id.GPay);
        btnGPay = (Button) findViewById(R.id.btnGPayUpdate);

        checkNodes();

        btnGPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAccount = newGPayID.getText().toString();

                updateAccountID(inputAccount);
            }
        });

    }

    public void checkNodes(){
        rootRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rootRef.child(currentUser).child("lead").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                long temp = (long) dataSnapshot.getValue();
                                currentLead.setText(String.valueOf(temp));
                            }
                            else{
                                currentLead.setText("0");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    rootRef.child(currentUser).child("total_lead").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Long temp = (Long) dataSnapshot.getValue();
                                totalLead.setText(String.valueOf(temp));
                            }
                            else{
                                totalLead.setText("0");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    rootRef.child(currentUser).child("GPayID").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String temp = (String) dataSnapshot.getValue();
                                GPayID.setText(temp);
                            }
                            else{
                                GPayID.setText("Empty");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    currentLead.setText("0");
                    totalLead.setText("0");
                    GPayID.setText("Empty");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateAccountID(String account){
        if (TextUtils.isEmpty(account)){
            Toast.makeText(ProfileActivity.this,"please enter new google Pay account", Toast.LENGTH_LONG).show();
        }
        else{

            rootRef.child(currentUser).child("GPayID").setValue(account);
            Toast.makeText(ProfileActivity.this,"Your data has been updated", Toast.LENGTH_LONG).show();
        }
    }
}
