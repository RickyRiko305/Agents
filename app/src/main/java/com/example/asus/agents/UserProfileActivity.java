package com.example.asus.agents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.asus.agents.MainActivity.EXTRA_URL;

public class UserProfileActivity extends AppCompatActivity {

    private DatabaseReference rootRef;
    private FirebaseDatabase firebaseDatabase;

    private TextView totalLead;
    private TextView currentLead;

    private EditText inputLeadValue;
    private Button btnLeadAdd;

    private String userUID;
    private long current_lead_value;
    private long total_lead_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        userUID = intent.getExtras().get("visit_user_id").toString();

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootRef = firebaseDatabase.getReference().child("state").child("user");//.child(currentUser);

        totalLead = (TextView) findViewById(R.id.InputTotalLead);
        currentLead = (TextView) findViewById(R.id.currentLead);

        inputLeadValue = (EditText) findViewById(R.id.InputAddLead);
        btnLeadAdd = (Button) findViewById(R.id.btnAddLead);

        checkNodes();

        btnLeadAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAmount = inputLeadValue.getText().toString();

                BonusAdd(inputAmount);
            }
        });

    }

    public void checkNodes(){
        rootRef.child(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rootRef.child(userUID).child("lead").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                long temp = (long) dataSnapshot.getValue();
                                current_lead_value = temp;
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
                    rootRef.child(userUID).child("total_lead").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                long temp1 = (long) dataSnapshot.getValue();
                                total_lead_value = temp1;
                                totalLead.setText(String.valueOf(temp1));
                            }
                            else{
                                totalLead.setText("0");
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void BonusAdd(String leadAmount){
        if (TextUtils.isEmpty(leadAmount)){
            Toast.makeText(UserProfileActivity.this,"please enter the amount", Toast.LENGTH_LONG).show();
        }
        else{
            long num = Long.valueOf(leadAmount);
            long newLeadupdate = current_lead_value + num;
            long newTotalLeadupdate = total_lead_value + num;
            rootRef.child(userUID).child("lead").setValue(newLeadupdate);
            rootRef.child(userUID).child("total_lead").setValue(newTotalLeadupdate);
            Toast.makeText(UserProfileActivity.this,"Your data has been updated", Toast.LENGTH_LONG).show();
            inputLeadValue.getText().clear();
        }
    }
}
