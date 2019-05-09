package dang.program.id.dangapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText etUserame, etEmail, etPhone, etPassword, etTglLahir;
    private Button mBtnRegister;
    private TextView tvDisini;
    private Toolbar mToolbar;
    private DatabaseReference mDatabase;
    //ProgressDialog
    private ProgressDialog mRegProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //loading progress
        mRegProgress = new ProgressDialog(this);
        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        etUserame = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etTglLahir = findViewById(R.id.et_tgl_lahir);
        mBtnRegister = findViewById(R.id.btn_register);
        tvDisini = findViewById(R.id.tv_login_disini);
        tvDisini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUserame.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                String tgl_lahir = etTglLahir.getText().toString();

                if(!TextUtils.isEmpty(username) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(phone)
                        || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(tgl_lahir)){

                    mRegProgress.setMessage("Mohon tunggu sebentar...");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(username, email, phone, password, tgl_lahir);

                } else {
                    Toast.makeText(SignupActivity.this, "Data tidak boleh ada yg kosong..!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void register_user(final String username, final String email, final String phone, final String password, final String tgl_lahir) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", username);
                    userMap.put("email", email);
                    userMap.put("phone", phone);
                    userMap.put("password", password);
                    userMap.put("tgl_lahir", tgl_lahir);
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mRegProgress.dismiss();
                                Intent mainIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(mainIntent);
                                finish();

                                Toast.makeText(SignupActivity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {

                    String task_result = task.getException().getMessage().toString();

                    mRegProgress.hide();
                    Toast.makeText(SignupActivity.this, "Registrasi tidak berhasil. Silakan periksa formulir dan coba lagi." + task_result, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //off back animation
    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }
}
