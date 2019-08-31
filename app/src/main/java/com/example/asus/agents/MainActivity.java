package com.example.asus.agents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference rootref;
    private RecyclerView productRecyclerView;
    private final List<Items> productList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        rootref = FirebaseDatabase.getInstance().getReference();

        productAdapter = new ProductAdapter(this,productList);
        productRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);

        gridLayoutManager = new GridLayoutManager(this,3);

        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(productAdapter);

        fetchProducts();

    }

    private void fetchProducts() {

        rootref.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Items products = dataSnapshot.getValue(Items.class);
                productList.add(products);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startIntent);
            finish();
        }
    }
}
