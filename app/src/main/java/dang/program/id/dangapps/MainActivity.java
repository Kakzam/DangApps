package dang.program.id.dangapps;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new dang.program.id.dangapps.menu_booking.BookingFragment());
    }

    /**
     * Fragment
     **/
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Menu Bottom Navigation Drawer
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_chats:
                fragment = new dang.program.id.dangapps.menu_booking.BookingFragment();
                break;
            case R.id.navigation_contacts:
                fragment = new dang.program.id.dangapps.menu_setting.SettingFragment();
                break;
            case R.id.navigation_feeds:
                fragment = new dang.program.id.dangapps.menu_history.HistoryFragment();
                break;
        }
        return loadFragment(fragment);
    }

    //off back animation
    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }
}
