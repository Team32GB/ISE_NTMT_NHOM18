package com.example.team32gb.jobit.View.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.UnActiveUser.UnactiveUserModel;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.team32gb.jobit.Utility.Config.UN_ACTIVE_USER;

public class AdminUnactiveUserActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private ActionBar acitonBar;
    private RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<UnactiveUserModel,AdminUnactiveUserViewHolder> adaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_unactive_user);
        recyclerView = findViewById(R.id.recyclerViewUnactiveUser);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải dữ liệu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        toolbar = findViewById(R.id.tbUnactiveUser);
        toolbar.setTitle("User bị khóa tài khoản");
        setSupportActionBar(toolbar);
        acitonBar = getSupportActionBar();
        acitonBar.setDisplayHomeAsUpEnabled(true);

        DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference().child(UN_ACTIVE_USER);
        FirebaseRecyclerOptions<UnactiveUserModel> options = new FirebaseRecyclerOptions.Builder<UnactiveUserModel>()
                .setQuery(refDatabase, UnactiveUserModel.class)
                .build();

        adaptor = new FirebaseRecyclerAdapter<UnactiveUserModel, AdminUnactiveUserViewHolder>(options) {

            @NonNull
            @Override
            public AdminUnactiveUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_unactive_user_item_row, null);
                final AdminUnactiveUserViewHolder viewHolder = new AdminUnactiveUserViewHolder(view);

                viewHolder.SetOnClickListener(new AdminUnactiveUserViewHolder.ClickListner(){
                    @Override
                    public void onItemClick(View view, int position) {
//                        UnactiveUserModel model = getItem(viewHolder.getAdapterPosition());
//                        Intent intent = new Intent(AdminUnactiveUserActivity.this, .class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString(UID_USER, model.getId());
//                        bundle.putInt(USER_TYPE, model.getTypeUser());
//
//                        intent.putExtra("bundle", bundle);
//                        startActivity(intent);
                        Toast.makeText(AdminUnactiveUserActivity.this, "Tài khoản này đã bị khóa", Toast.LENGTH_SHORT).show();
                    }
                });


                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull AdminUnactiveUserViewHolder viewHolder, int i, @NonNull UnactiveUserModel model) {
                viewHolder.txtUnactiveUserName.setText(model.getName());
                viewHolder.txtDateUnactiveUser.setText(Util.getSubTime(model.getDateUnactive()));
                Log.e("Kiem tra unactive user",model.getName() +", " + model.getDateUnactive());
                progressDialog.dismiss();
            }
        };

        adaptor.startListening();
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeAdmin:
                Intent intent = new Intent(this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
