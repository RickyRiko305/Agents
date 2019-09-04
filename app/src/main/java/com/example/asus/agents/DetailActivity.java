package com.example.asus.agents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.asus.agents.MainActivity.EXTRA_NAME;
import static com.example.asus.agents.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference productLead;
    private DatabaseReference userLead;
    private FirebaseDatabase firebaseDatabase;

    private String currentUser;

    private TextView customerName;
    private EditText customerEmail;
    private EditText customerMobileNo;
    private EditText customerAdharcard;
    private EditText customerPanCard;
    private Button send;

    private ProgressDialog loadingBar;

    customer customerDetails;
    String product;
    private long lead;
    private long total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String productName = intent.getStringExtra(EXTRA_NAME);
        product = intent.getStringExtra(EXTRA_NAME);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("Product").child(productName).child("lead");

        loadingBar = new ProgressDialog(this);

        customerName = (TextView) findViewById(R.id.txtName);
        customerEmail = (EditText) findViewById(R.id.txtEmail);

        send = (Button) findViewById(R.id.btnSend);

        fetchlead();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((customerName==null) || (customerEmail==null) || (customerMobileNo==null) || (customerAdharcard==null) || (customerPanCard==null)){
                    Toast.makeText(DetailActivity.this,"Please fill all the requied details", Toast.LENGTH_SHORT).show();;
                }
                else{
                    //String Name = customerName.getText().toString();
                    String Email = customerEmail.getText().toString();
//                    String MobileNo = customerMobileNo.getText().toString();
//                    String Adhardcard = customerAdharcard.getText().toString();
//                    String Pancard = customerPanCard.getText().toString();

                    loadData(Email);
                }
            }
        });


    }

    private void fetchlead() {
        productLead = firebaseDatabase.getReference().child("Product").child(product).child("lead");

        productLead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long temp = (long)dataSnapshot.getValue();
                lead = temp;
                String leadString = String.valueOf(lead);
                customerName.setText(leadString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userLead = firebaseDatabase.getReference().child("state").child("user").child(mAuth.getCurrentUser().getUid()).child("lead");
        userLead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long temp = (long) dataSnapshot.getValue();
                    total = temp;
                }
                else{
                    total = 0;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadData(String Email){

        //customerDetails = new customer();

        //customerDetails.setName(Name);
        //customerDetails.setEmail(Email);
//        customerDetails.setMobileNo(MobileNo);
//        customerDetails.setAdharcard(Adhardcard);
//        customerDetails.setPancard(Pancard);

        loadingBar.setTitle("Customer Data");
        loadingBar.setMessage("Please wait, while we are updating your customer data...");
        loadingBar.show();

        //DatabaseReference newCustomer = databaseReference.push();
        //String newCustomerKey = newCustomer.getKey();
        long Value = Integer.parseInt(Email);
        //databaseReference.setValue(Value);
        databaseReference.setValue(Value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(DetailActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                            //total = total + lead;
                            //userLead.setValue(total);

                        }
                        else {
                            Toast.makeText(DetailActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }



}
