package com.example.asus.agents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView allUsersList;
    private DatabaseReference allDatabaseUserreference;
    String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        allUsersList = (RecyclerView) findViewById(R.id.all_users_list);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));

        allDatabaseUserreference = FirebaseDatabase.getInstance().getReference().child("state").child("user");
        allDatabaseUserreference.keepSynced(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(AllUsers.class,R.layout.all_users_display_layout,AllUsersViewHolder.class,allDatabaseUserreference) {
            @Override
            protected void populateViewHolder(AllUsersViewHolder viewHolder, AllUsers model, final int position) {

                viewHolder.setName(model.getName());
                viewHolder.setLead(model.getLead());
                viewHolder.setTotal_lead(model.getTotal_lead());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String visit_user_id = getRef(position).getKey();

                        Intent profileIntent = new Intent(ProfileActivity.this , UserProfileActivity.class);
                        profileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(profileIntent);
                    }
                });

            }
        };

        allUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class AllUsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public AllUsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView user_name = (TextView) mView.findViewById(R.id.all_users_username);
            user_name.setText(name);
        }

        public void setLead(long lead) {
            String value = Long.toString(lead);
            TextView Lead = (TextView) mView.findViewById(R.id.all_users_current_lead);
            Lead.setText(value);
        }

        public void setTotal_lead(long total_lead){
            String value = Long.toString(total_lead);
            TextView totalLead = (TextView) mView.findViewById(R.id.all_users_total_lead);
            totalLead.setText(value);
        }

    }


}
