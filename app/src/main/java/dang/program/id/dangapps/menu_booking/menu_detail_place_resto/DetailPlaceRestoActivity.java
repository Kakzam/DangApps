package dang.program.id.dangapps.menu_booking.menu_detail_place_resto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import dang.program.id.dangapps.R;
import dang.program.id.dangapps.menu_booking.menu_detail_booking.MenuDetailBookingActivity;

import java.util.Calendar;
import java.util.Date;

public class DetailPlaceRestoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnDatePicker, btnTimePicker;
    ImageView ivDetailRestoPlace;
    TextView txtDate, txtTime;
    Button mBtnContinue;
    TextView tvDetailBooking, tvAlamatBooking, tvWaktuBooking, tvNamaResto;
    Button tvStatusBooking;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_place_resto);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Menu Reservation Resto");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        tvNamaResto = findViewById(R.id.tv_resto_name);
        tvDetailBooking = findViewById(R.id.tv_nama_detail_place);
        tvAlamatBooking = findViewById(R.id.tv_alamat_detail_place);
        tvWaktuBooking = findViewById(R.id.tv_waktu_detail_place);
        tvStatusBooking = findViewById(R.id.tv_status_detail_place);
        ivDetailRestoPlace = findViewById(R.id.iv_detail_resto_place);

        tvNamaResto.setText("Resto " + getIntent().getStringExtra("name_resto"));
        tvDetailBooking.setText(getIntent().getStringExtra("name"));
        tvAlamatBooking.setText(getIntent().getStringExtra("address"));
        tvWaktuBooking.setText(getIntent().getStringExtra("time"));
        tvStatusBooking.setText(getIntent().getStringExtra("status"));

        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("image_resto"))
                .into(ivDetailRestoPlace);

        mBtnContinue = findViewById(R.id.btn_continue);
        btnDatePicker= findViewById(R.id.ib_date);
        btnTimePicker= findViewById(R.id.ib_time);

        txtDate = findViewById(R.id.et_date);
        txtTime = findViewById(R.id.et_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        mBtnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_date:
                // Get Tanggal
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog;
                final Date date = c.getTime();

                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //jika tgl kurang dari hari ini..!!
                                //nanti abis dhuhur
                                if (date.getDay()+2 > dayOfMonth || date.getMonth()+1 > monthOfYear+1 || date.getMonth()-20 > monthOfYear+1) {
                                    Toast.makeText(getApplicationContext(), "Pesanan tidak tersedia untuk hari ini.", Toast.LENGTH_SHORT).show();
                                } else {
                                    txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    Toast.makeText(DetailPlaceRestoActivity.this, "Tanggal : " + txtDate.getText().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

            case R.id.ib_time:
                // Get Waktu
                final Calendar d = Calendar.getInstance();
                mHour = d.get(Calendar.HOUR_OF_DAY);
                mMinute = d.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + " : " + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;

            case R.id.btn_continue:
                // Lanjut kirim data ke MenuDetailBookingActivity.
                if (!TextUtils.isEmpty(txtTime.getText().toString()) && !TextUtils.isEmpty(txtDate.getText().toString())) {

                    Intent i = new Intent(getApplicationContext(), MenuDetailBookingActivity.class);
                    i.putExtra("detail_time", txtTime.getText().toString());
                    i.putExtra("detail_date", txtDate.getText().toString());
                    i.putExtra("name_resto", getIntent().getStringExtra("name_resto"));
                    i.putExtra("name", getIntent().getStringExtra("name"));
                    i.putExtra("address", getIntent().getStringExtra("address"));
                    i.putExtra("time", getIntent().getStringExtra("time"));
                    i.putExtra("status" , getIntent().getStringExtra("status"));
                    i.putExtra("image_resto", getIntent().getStringExtra("image_resto"));
                    i.putExtra("keyrestoran", getIntent().getStringExtra("keyrestoran"));
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Maaf, data harus diisi semua", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
