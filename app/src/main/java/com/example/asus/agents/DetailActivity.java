package com.example.asus.agents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private DatabaseReference targetProduct;
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
    private Button delete;

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
        targetProduct = firebaseDatabase.getReference().child("Product").child(productName);

        loadingBar = new ProgressDialog(this);

        customerName = (TextView) findViewById(R.id.txtName);
        customerEmail = (EditText) findViewById(R.id.txtEmail);

        send = (Button) findViewById(R.id.btnSend);
        delete = (Button) findViewById(R.id.btnDelete);

        fetchlead();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((customerEmail==null)){
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirm();

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


    public void Confirm() {
//        AlertDialog dialog = new AlertDialog.Builder(context).create();
//        dialog.setTitle("Confirmation");
//        dialog.setMessage("Choose Yes or No");
//        dialog.setCancelable(false);
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int buttonId) {
//                myInterface.getListener().onDialogCompleted(true);
//            }
//        });
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int buttonId) {
//                myInterface.getListener().onDialogCompleted(false);
//            }
//        });
//        dialog.setIcon(android.R.drawable.ic_dialog_alert);
//        dialog.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.delete_message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.show();
    }

    public void deleteProduct(){
        targetProduct.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DetailActivity.this,"product is deleted successfully",Toast.LENGTH_LONG).show();
                            Intent startIntent = new Intent(DetailActivity.this, MainActivity.class);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(startIntent);
                            finish();
                        }

                    }
                });
    }



}
