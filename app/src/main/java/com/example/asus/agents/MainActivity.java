package com.example.asus.agents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_NAME = "productName";

    private FirebaseAuth mAuth;
    private DatabaseReference rootref;
    private RecyclerView productRecyclerView;
    private final List<Items> productList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private ProductAdapter productAdapter;

    private Toolbar mToolbar;

    Button Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ADMIN");

        mAuth = FirebaseAuth.getInstance();
        rootref = FirebaseDatabase.getInstance().getReference();

        productAdapter = new ProductAdapter(this,productList);
        productRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);

        gridLayoutManager = new GridLayoutManager(this,3);

        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(productAdapter);

        productAdapter.setOnItemClickListener(MainActivity.this);

        Add = (Button) findViewById(R.id.Button02);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddProductActivity.class));
            }
        });

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

            LogOutUser();
        }
    }

    private void LogOutUser() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_button){
            mAuth.signOut();
            LogOutUser();
        }
        else if(item.getItemId() == R.id.main_Account_settings_button){
            Intent profileIntent = new Intent(this,ProfileActivity.class);
            startActivity(profileIntent);
        }

        return true;
    }

    @Override
    public void onItemClick(int postion) {
        Intent detailIntent = new Intent(this,DetailActivity.class);
        Items clickItem = productList.get(postion);

        detailIntent.putExtra(EXTRA_URL,clickItem.getImage());
        detailIntent.putExtra(EXTRA_NAME,clickItem.getName());

        startActivity(detailIntent);

    }
}
