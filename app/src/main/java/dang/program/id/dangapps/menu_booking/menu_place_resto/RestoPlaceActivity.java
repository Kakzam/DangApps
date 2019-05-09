package dang.program.id.dangapps.menu_booking.menu_place_resto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dang.program.id.dangapps.R;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.AddDataMenuActivity;
import dang.program.id.dangapps.menu_booking.menu_detail_place_resto.DetailPlaceRestoActivity;

public class RestoPlaceActivity extends AppCompatActivity {

    private TextView tvRestoName;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<ModelPlaceCity, NewsViewHolder> mAdapter;
    private FirebaseUser mCurrentUser;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton mSearchBtn; //njajal
    private EditText etSearch;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_place);

        Button mBtnAddData = findViewById(R.id.btn_tambah_data);
        mBtnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), dang.program.id.dangapps.AddDataActivity.class);
                startActivity(i);
            }
        });

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Menu Detail Resto");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        Intent o = getIntent();

        final String nama_tempat = o.getStringExtra("name");

        tvRestoName = findViewById(R.id.tv_resto_name);
        tvRestoName.setText("Resto " + nama_tempat);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("DataPlaceCity");
        mDatabase.keepSynced(true);

        recyclerView = findViewById(R.id.rc_place_resto_city);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("DataPlaceCity").child(nama_tempat);
        Query personsQuery = personsRef.orderByKey();

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        final FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ModelPlaceCity>().setQuery(personsQuery, ModelPlaceCity.class).build();

        mAdapter = new FirebaseRecyclerAdapter<ModelPlaceCity, NewsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NewsViewHolder holder, final int position, @NonNull final ModelPlaceCity model) {
                holder.setName_resto_place(model.getName_resto_place());
                holder.setAddress_resto_place(model.getAddress_resto_place());
                holder.setStatus_resto_place(model.getStatus_resto_place());
                holder.setTime_resto_place(model.getTime_resto_place());
                holder.setImg_resto_city(model.getImg_resto_city(), getApplicationContext());

                holder.mContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String keyrestoran = mAdapter.getRef(position).getKey().toString();

                        //kirim data ke class DetailPlaceRestoActivity.
                        Intent i = new Intent(getApplicationContext(), DetailPlaceRestoActivity.class);
                        i.putExtra("name_resto", nama_tempat);
                        i.putExtra("name", model.getName_resto_place());
                        i.putExtra("address", model.getAddress_resto_place());
                        i.putExtra("status", model.getStatus_resto_place());
                        i.putExtra("time", model.getTime_resto_place());
                        i.putExtra("image_resto", model.getImg_resto_city());
                        i.putExtra("keyrestoran", keyrestoran);
                        startActivity(i);

                    }
                });
            }

            @NonNull
            @Override
            public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.row_item_place_city, viewGroup, false);
                return new NewsViewHolder(view);
            }
        };

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });        //nanti lagi ya ..!!


        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        etSearch = findViewById(R.id.search_field);

        mSearchBtn = findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String search_text = etSearch.getText().toString().trim();
                firebaseUserSearchData(search_text);
            }
        });
    }

    //search function
    private void firebaseUserSearchData(final String search_text) {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("DataPlaceCity").child("Yogyakarta");
        final Query personsQuery = personsRef.orderByKey();
        personsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return;
                }

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Query key = child.getRef();

                    Toast.makeText(RestoPlaceActivity.this, "Data "+ key.getRef().child("name_resto_place"), Toast.LENGTH_SHORT).show();

                    FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ModelPlaceCity>().setQuery(key.getRef().child("name_resto_place").equalTo(search_text), ModelPlaceCity.class).build();

                    FirebaseRecyclerAdapter<ModelPlaceCity, NewsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelPlaceCity, NewsViewHolder>(options) {
                        @NonNull
                        @Override
                        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.row_item_place_city, viewGroup, false);
                            return new NewsViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull ModelPlaceCity model) {
                            holder.setName_resto_place(model.getName_resto_place());
                            holder.setAddress_resto_place(model.getAddress_resto_place());
                            holder.setStatus_resto_place(model.getStatus_resto_place());
//                            holder.setImg_resto_item(model.getImg_resto_city());

                        }
                    };

                    firebaseRecyclerAdapter.startListening();
                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RestoPlaceActivity.this, "Data Error " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        LinearLayout mContainer;

        public NewsViewHolder(android.view.View itemView){
            super(itemView);
            mView = itemView;

            mContainer = mView.findViewById(R.id.container);
        }

        public void setName_resto_place(String name_resto_place) {
            TextView name_resto_placee = mView.findViewById(R.id.tv_name_resto_place_city);
            name_resto_placee.setText(name_resto_place);
        }

        public void setAddress_resto_place(String address_resto_place) {
            TextView address_resto_placee = mView.findViewById(R.id.tv_address_resto_place_city);
            address_resto_placee.setText(address_resto_place);
        }

        public void setStatus_resto_place(String status_resto_place) {
            Button status_resto_placee = mView.findViewById(R.id.tv_status);
            status_resto_placee.setText(status_resto_place);
        }

        public void setTime_resto_place(String time_resto_place) {
            TextView time_resto_placee = mView.findViewById(R.id.tv_time_resto_place_city);
            time_resto_placee.setText(time_resto_place);
        }

        public void setImg_resto_city(String img_resto_city, final Context ctx){
            ImageView img_resto_placee = mView.findViewById(R.id.img_resto_place_city);

            Glide.with(ctx).load(img_resto_city).placeholder(R.drawable.food_item_placeholder).into(img_resto_placee);
        }

    }
}
