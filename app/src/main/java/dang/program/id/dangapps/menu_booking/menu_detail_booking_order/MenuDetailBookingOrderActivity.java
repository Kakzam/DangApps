package dang.program.id.dangapps.menu_booking.menu_detail_booking_order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dang.program.id.dangapps.PrefManager;
import dang.program.id.dangapps.R;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.BookingFoodItemAdapter;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.ModelAddDataMenu;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment.FoodFragment;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment.ModelFoodItem;
import dang.program.id.dangapps.menu_booking.menu_detail_order_final.MenuDetailOrderFinalActivity;

public class MenuDetailBookingOrderActivity extends AppCompatActivity {

    private Button mBtnBooking;
    private TextView tvRestoBoking, tvPlaceBooking, tvTimeBooking, tvTotalPriceBooking;
    private FirebaseRecyclerAdapter<ModelFoodItem, FoodFragment.NewsViewHolder> adapter;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private static ListView lv;
    ArrayAdapter<String> list_adapter;
    BookingFoodItemAdapter adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail_booking_order);

        //set access database
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
//        myToolbar.setLogo(R.drawable.ic_food);
        myToolbar.setTitle("DangApps");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        tvRestoBoking = findViewById(R.id.tv_resto_booking);
        tvPlaceBooking = findViewById(R.id.tv_place_booking);
        tvTimeBooking = findViewById(R.id.tv_time_booking);
        tvTotalPriceBooking = findViewById(R.id.tv_total_price_booking);
        mBtnBooking = findViewById(R.id.btn_booking);

        //Data Simpanan Sementara
        final String title_item = new PrefManager(getApplicationContext()).getTitleItem();
        final String price_item = new PrefManager(getApplicationContext()).getPriceItem();
        final String quantity_item = new PrefManager(getApplicationContext()).getQuantity();

        tvRestoBoking.setText(getIntent().getStringExtra("resto_booking"));
        tvPlaceBooking.setText(getIntent().getStringExtra("place_booking"));
        tvTimeBooking.setText(getIntent().getStringExtra("detail_time"));
        tvTotalPriceBooking.setText(price_item);

        mBtnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nama_restoran = getIntent().getStringExtra("resto_booking");
                final String nama_daerah = getIntent().getStringExtra("name_resto");
                final String pesanan = title_item;
                final String total_harga = price_item;
                final String quantity = quantity_item;
                final String date = getIntent().getStringExtra("detail_date");
                final String time = getIntent().getStringExtra("detail_time");

                final ModelMenuDetailBookingOrderActivity modelDataSantri = new ModelMenuDetailBookingOrderActivity(nama_restoran ,nama_daerah ,pesanan ,total_harga ,date ,time , quantity);

                final String id = mDatabase.push().getKey();

                //getId untuk makanan permasing - masing kota.
                mDatabase.child("DataPlaceCity").child("History").child(id).setValue(modelDataSantri);

                Toast.makeText(MenuDetailBookingOrderActivity.this, getIntent().getStringExtra("place_booking"), Toast.LENGTH_SHORT).show();
                //kirim data booking ke final booking
                Intent i = new Intent(getApplicationContext(), MenuDetailOrderFinalActivity.class);
                i.putExtra("resto_final", getIntent().getStringExtra("resto_booking"));
                i.putExtra("place_final", getIntent().getStringExtra("place_booking"));
                i.putExtra("time_final", getIntent().getStringExtra("detail_time"));
                i.putExtra("total_price_final", price_item);
                i.putExtra("quantity", quantity_item);
                startActivity(i);
            }
        });

        adapters = new BookingFoodItemAdapter(this, new String[] {title_item}, new String[] {price_item}, new String[] {quantity_item});
        lv = findViewById(R.id.cart_list_view);
        lv.setAdapter(adapters);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final String id = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Posisi : "+id, Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected void SavePreferences(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(key, value);
        editor.apply();
    }

    protected void LoadPreferences() {
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        String dataSet = data.getString("LISTS", "None Available");
        list_adapter.add(dataSet);
        list_adapter.notifyDataSetChanged();
    }
}
