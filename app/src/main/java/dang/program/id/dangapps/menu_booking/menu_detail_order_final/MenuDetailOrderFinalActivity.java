package dang.program.id.dangapps.menu_booking.menu_detail_order_final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import dang.program.id.dangapps.PrefManager;
import dang.program.id.dangapps.R;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.BookingFoodItemAdapter;

public class MenuDetailOrderFinalActivity extends AppCompatActivity {

    private TextView tvRestoFinal, tvPlaceFinal, tvTimeFinal, tvTotalPriceFinal;
    private static ListView lv;
    BookingFoodItemAdapter adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail_order_final);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("DangApps");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        tvRestoFinal = findViewById(R.id.tv_resto_final);
        tvPlaceFinal = findViewById(R.id.tv_place_final);
        tvTimeFinal = findViewById(R.id.tv_time_final);
        tvTotalPriceFinal = findViewById(R.id.tv_total_price_final);

        tvRestoFinal.setText(getIntent().getStringExtra("resto_final"));
        tvPlaceFinal.setText(getIntent().getStringExtra("place_final"));
        tvTimeFinal.setText(getIntent().getStringExtra("time_final"));
        tvTotalPriceFinal.setText(getIntent().getStringExtra("total_price_final"));


        //Data Simpanan Sementara
        final String title_item = new PrefManager(getApplicationContext()).getTitleItem();
        final String price_item = new PrefManager(getApplicationContext()).getPriceItem();
        final String quantity = new PrefManager(getApplicationContext()).getQuantity();

        adapters = new BookingFoodItemAdapter(this, new String[] {title_item}, new String[] {price_item}, new String[] {quantity});
        lv = findViewById(R.id.cart_list_view);
        lv.setAdapter(adapters);
    }
}
