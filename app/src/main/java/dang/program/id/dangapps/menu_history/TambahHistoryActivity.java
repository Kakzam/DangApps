package dang.program.id.dangapps.menu_history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dang.program.id.dangapps.R;

public class TambahHistoryActivity extends AppCompatActivity {

    private EditText etTitleHistory, etInfoHistory, etDateHistory;
    private Button mBtnAddHistory;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_history);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDatabase.keepSynced(true);

        setView();

        mBtnAddHistory = findViewById(R.id.add_menu_history);
        mBtnAddHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title_history = etTitleHistory.getText().toString();
                final String info_history = etInfoHistory.getText().toString();
                final String date_history = etDateHistory.getText().toString();


//                ModelMenuHistory modelMenuHistory = new ModelMenuHistory(title_history, info_history, "Belum ada", date_history);

                String id = mDatabase.push().getKey();

//                mDatabase.child("History").child(id).setValue(modelMenuHistory);

                Toast.makeText(getApplicationContext(), "Add Data Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        etTitleHistory = findViewById(R.id.et_title_history);
        etInfoHistory = findViewById(R.id.et_info_history);
        etDateHistory = findViewById(R.id.et_date_history);
    }
}
