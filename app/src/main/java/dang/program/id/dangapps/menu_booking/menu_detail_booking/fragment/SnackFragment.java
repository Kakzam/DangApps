package dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import dang.program.id.dangapps.PrefManager;
import dang.program.id.dangapps.R;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment.ModelFoodItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class SnackFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<ModelFoodItem, SnackFragment.NewsViewHolder> mAdapter;
    private FirebaseUser mCurrentUser;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SnackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_snack, container, false);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(current_uid)
                .child("DataPlaceCity")
                .child(getActivity().getIntent().getStringExtra("name_resto"))
                .child(getActivity().getIntent().getStringExtra("keyrestoran"))
                .child("Snack");
        mDatabase.keepSynced(true);

        recyclerView = view.findViewById(R.id.rc_place_package_item);

        DatabaseReference personsRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(current_uid)
                .child("DataPlaceCity")
                .child(getActivity().getIntent().getStringExtra("name_resto"))
                .child(getActivity().getIntent().getStringExtra("keyrestoran"))
                .child("Snack");
        Query personsQuery = personsRef.orderByKey();

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        //nanti lagi ya ..!!
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ModelFoodItem>().setQuery(personsQuery, ModelFoodItem.class).build();

        mAdapter = new FirebaseRecyclerAdapter<ModelFoodItem, SnackFragment.NewsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SnackFragment.NewsViewHolder holder, final int position, @NonNull final ModelFoodItem model) {
                holder.setMenu_title(model.getMenu_title());
                holder.setMenu_price(model.getMenu_price());
                holder.setImg_resto_item(model.getImg_resto_item());
                holder.mBtnAddCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                        new PrefManager(getContext()).setTitleItem(model.getMenu_title()+"Coba");
                        new PrefManager(getContext()).setPriceItem(model.getMenu_price());
                    }
                });
            }

            @NonNull
            @Override
            public SnackFragment.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.row_item_food, viewGroup, false);
                return new NewsViewHolder(view);
            }
        };

        swipeRefreshLayout = view.findViewById(R.id.swipe_package);
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
        });

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button mBtnAddCart;

        public NewsViewHolder(android.view.View itemView){
            super(itemView);
            mView = itemView;

            mBtnAddCart = itemView.findViewById(R.id.btn_add_cart);
        }

        public void setMenu_title(String menu_title) {
            TextView name_resto_placee = mView.findViewById(R.id.tv_title_food_item);
            name_resto_placee.setText(menu_title);
        }

        @SuppressLint("SetTextI18n")
        public void setMenu_price(String menu_price) {
            TextView address_resto_placee = mView.findViewById(R.id.tv_price_food_item);
            address_resto_placee.setText("Rp. "+menu_price);
        }

        public void setImg_resto_item(String img_resto_item){
            ImageView img_resto_itemm = mView.findViewById(R.id.iv_food_item);

            Glide.with(getContext())
                    .load(img_resto_item)
                    .placeholder(R.drawable.food_item_placeholder)
                    .into(img_resto_itemm);
        }
    }
}
