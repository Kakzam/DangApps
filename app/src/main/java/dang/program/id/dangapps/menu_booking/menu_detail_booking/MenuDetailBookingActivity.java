package dang.program.id.dangapps.menu_booking.menu_detail_booking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import dang.program.id.dangapps.PrefManager;
import dang.program.id.dangapps.R;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment.SnackFragment;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment.DrinkFragment;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment.FoodFragment;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.fragment.PackageFragment;
import dang.program.id.dangapps.menu_booking.menu_detail_booking_order.MenuDetailBookingOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuDetailBookingActivity extends AppCompatActivity {

    TextView tvDetailBooking, tvAlamatBooking, tvWaktuBooking, tvNamaResto;
    Button tvStatusBooking, btnMenuAddDataMenu;
    ImageView ivDetailRestoBooking;
    //Fragment
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail_booking);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Menu Reservation Resto");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        //Set View
        setView();

        //Set Data
//        tvNamaResto.setText("Resto " + getIntent().getStringExtra("name_resto"));
        tvDetailBooking.setText(getIntent().getStringExtra("name"));
        tvAlamatBooking.setText(getIntent().getStringExtra("address"));
        tvWaktuBooking.setText(getIntent().getStringExtra("time"));
        tvStatusBooking.setText(getIntent().getStringExtra("status"));

        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("image_resto"))
                .into(ivDetailRestoBooking);

        //setOnclickListener
        btnMenuAddDataMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddDataMenuActivity.class);
                i.putExtra("keyrestoran", getIntent().getStringExtra("keyrestoran"));
                i.putExtra("name_resto", getIntent().getStringExtra("name_resto"));
                startActivity(i);
            }
        });

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setView() {
//        tvNamaResto = findViewById(R.id.tv_resto_name);
        tvDetailBooking = findViewById(R.id.tv_nama_detail_place);
        tvAlamatBooking = findViewById(R.id.tv_alamat_detail_place);
        tvWaktuBooking = findViewById(R.id.tv_waktu_detail_place);
        tvStatusBooking = findViewById(R.id.tv_status_detail_place);
        btnMenuAddDataMenu = findViewById(R.id.add_menu);
        ivDetailRestoBooking = findViewById(R.id.iv_detail_img);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Food
        FoodFragment foodFragment = new FoodFragment();
        Bundle bundle = new Bundle();
        foodFragment.setArguments(bundle);

        //Drink
        DrinkFragment drinkFragment = new DrinkFragment();
        Bundle bundle_drink = new Bundle();
        drinkFragment.setArguments(bundle_drink);

        //Snack
        SnackFragment snackFragment = new SnackFragment();
        Bundle bundle_snack = new Bundle();
        snackFragment.setArguments(bundle_snack);

         adapter.addFragment(foodFragment, "Food");
         adapter.addFragment(new DrinkFragment(), "Drink");
         adapter.addFragment(new SnackFragment(), "Snack");
         adapter.addFragment(new PackageFragment(), "Package");

         viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                Intent i = new Intent(getApplicationContext(), MenuDetailBookingOrderActivity.class);
                i.putExtra("detail_time", getIntent().getStringExtra("detail_time"));
                i.putExtra("detail_date", getIntent().getStringExtra("detail_date"));
                i.putExtra("resto_booking", getIntent().getStringExtra("name"));
                i.putExtra("place_booking", getIntent().getStringExtra("address"));
                i.putExtra("name_resto", getIntent().getStringExtra("name_resto"));
                i.putExtra("keyrestoran", getIntent().getStringExtra("keyrestoran"));
                i.putExtra("quantity", new PrefManager(getApplicationContext()).getQuantity());
                startActivity(i);
                break;
        }
        return true;
    }
}
