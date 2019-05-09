package dang.program.id.dangapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvKlikSini;
    private Button mBtnSignUp, mbtnSignIn;
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private ProgressDialog progressDialog;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //activity yg dilewati ketika sudah login (gak perlu login lagi)
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            prefManager.setIsFirstTimeLaunch(false);
            sendTo(getApplicationContext(), MainActivity.class);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        tvKlikSini = findViewById(R.id.tv_disini);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        progressDialog = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mbtnSignIn = findViewById(R.id.btn_login);
        mbtnSignIn.setOnClickListener(this);
        mBtnSignUp = findViewById(R.id.btn_register);
        mBtnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:

                sendTo(getApplicationContext(), SignupActivity.class);
                break;
            case R.id.btn_login:

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    progressDialog.setMessage("Mohon tunggu sebentar...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    loginUser(email, password);
                } else {
                    Toast.makeText(this, "Email atau password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    progressDialog.dismiss();

                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            //set pref manager
                            prefManager.setIsFirstTimeLaunch(false);
                            sendTo(getApplicationContext(), MainActivity.class);
                            finish();
                        }
                    });
                } else {

                    progressDialog.hide();

                    String task_result = task.getException().getMessage().toString();
                    Toast.makeText(LoginActivity.this, "Error : " + task_result, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //berpindah activity
    private void sendTo(Context ctx, Class kelas) {
        Intent i = new Intent(ctx, kelas);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    //off back animation
    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }
}
