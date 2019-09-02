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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.asus.agents.MainActivity.EXTRA_NAME;
import static com.example.asus.agents.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String currentUser;

    private EditText customerName;
    private EditText customerEmail;
    private EditText customerMobileNo;
    private EditText customerAdharcard;
    private EditText customerPanCard;
    private Button send;

    private ProgressDialog loadingBar;

    customer customerDetails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String productName = intent.getStringExtra(EXTRA_NAME);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("Product_details").child(productName).child(currentUser);

        loadingBar = new ProgressDialog(this);

        customerName = (EditText) findViewById(R.id.txtName);
        customerEmail = (EditText) findViewById(R.id.txtEmail);
        customerMobileNo = (EditText) findViewById(R.id.txtMob);
        customerAdharcard = (EditText) findViewById(R.id.txtReg);
        customerPanCard = (EditText) findViewById(R.id.txtPancard);
        send = (Button) findViewById(R.id.btnSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((customerName==null) || (customerEmail==null) || (customerMobileNo==null) || (customerAdharcard==null) || (customerPanCard==null)){
                    Toast.makeText(DetailActivity.this,"Please fill all the requied details", Toast.LENGTH_SHORT).show();;
                }
                else{
                    String Name = customerName.getText().toString();
                    String Email = customerEmail.getText().toString();
                    String MobileNo = customerMobileNo.getText().toString();
                    String Adhardcard = customerAdharcard.getText().toString();
                    String Pancard = customerPanCard.getText().toString();

                    loadData(Name,Email,MobileNo,Adhardcard,Pancard);
                }
            }
        });


    }

    private void loadData(String Name,String Email,String MobileNo,String Adhardcard,String Pancard){

        customerDetails = new customer();

        customerDetails.setName(Name);
        customerDetails.setEmail(Email);
        customerDetails.setMobileNo(MobileNo);
        customerDetails.setAdharcard(Adhardcard);
        customerDetails.setPancard(Pancard);

        loadingBar.setTitle("Customer Data");
        loadingBar.setMessage("Please wait, while we are updating your customer data...");
        loadingBar.show();

        //DatabaseReference newCustomer = databaseReference.push();
        //String newCustomerKey = newCustomer.getKey();

        databaseReference.push().setValue(customerDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(DetailActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(DetailActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }



}
