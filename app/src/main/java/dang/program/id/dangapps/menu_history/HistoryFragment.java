package dang.program.id.dangapps.menu_history;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<dang.program.id.dangapps.menu_history.ModelMenuHistory, NewsViewHolder> mAdapter;
    private FirebaseUser mCurrentUser;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button mBtnHistory;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_history, container, false);

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        myToolbar.setTitle("Menu History");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        recyclerView = view.findViewById(R.id.rc_history);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("DataPlaceCity").child("History");
        mDatabase.keepSynced(true);

        DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("DataPlaceCity").child("History");

        Query personsQuery = personRef.orderByKey();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ModelMenuHistory>().setQuery(personsQuery, ModelMenuHistory.class).build();

        mAdapter = new FirebaseRecyclerAdapter<ModelMenuHistory, HistoryFragment.NewsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryFragment.NewsViewHolder holder, int position, @NonNull final ModelMenuHistory model) {
                holder.setNama_daerah(model.getNama_daerah());
                holder.setNama_restoran(model.getNama_restoran());
                holder.setPesanan(model.getPesanan());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());
                holder.setTotal_harga(model.getTotal_harga());
            }

            @NonNull
            @Override
            public HistoryFragment.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.row_item_history, viewGroup, false);
                return new NewsViewHolder(view);
            }
        };

        swipeRefreshLayout = view.findViewById(R.id.swipe_history);
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

        public NewsViewHolder(android.view.View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setNama_restoran(String nama_restoran) {
            TextView nama_restorann = mView.findViewById(R.id.tv_wilayah);
            nama_restorann.setText(nama_restoran);
        }

        public void setNama_daerah(String nama_daerah) {
            TextView nama_daerahh = mView.findViewById(R.id.tv_nama_restoran);
            nama_daerahh.setText(nama_daerah);
        }

        public void setPesanan(String pesanan) {
//            TextView pesanann = mView.findViewById(R.id.tv_date_history);
//            pesanann.setText(pesanan);
        }

        public void setTotal_harga(String total_harga) {
//            TextView total_hargaa = mView.findViewById(R.id.tv_date_history);
//            total_hargaa.setText(total_harga);
        }

        public void setDate(String date) {
//            TextView datee = mView.findViewById(R.id.tv_date_history);
//            datee.setText(date);
        }

        public void setTime(String time) {
//            TextView timee = mView.findViewById(R.id.tv_date_history);
//            timee.setText(time);
        }
    }
}
