package dang.program.id.dangapps.menu_setting;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dang.program.id.dangapps.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private dang.program.id.dangapps.PrefManager prefManager;
    private Button mBtnExit, mBtnChange;
    private EditText etUserName, etPhone, etPassword, etNewPassword;
    private TextView tvChangeFoto;
    private CircleImageView ivImgProfile;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    //firebase
    private StorageReference mImageStorage;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    FirebaseStorage storage;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mBtnExit = view.findViewById(R.id.btn_exit);
        mBtnExit.setOnClickListener(this);
        mBtnChange = view.findViewById(R.id.btn_change);
        tvChangeFoto = view.findViewById(R.id.change_foto);
        tvChangeFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pilih image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        ivImgProfile = view.findViewById(R.id.user_edit);


        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        myToolbar.setTitle("Menu Setting");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        etUserName = view.findViewById(R.id.et_username);
        etPhone = view.findViewById(R.id.et_phone);
        etPassword = view.findViewById(R.id.et_password);
        etNewPassword = view.findViewById(R.id.et_newpassword);

        //User
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        //Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDatabase.keepSynced(true);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                String image = "default";

                if (dataSnapshot.hasChild("image")) {
                    image = dataSnapshot.child("image").getValue().toString();
                }
                String user_name = dataSnapshot.child("name").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                final String password=dataSnapshot.child("password").getValue().toString();

                etUserName.setText(user_name);
                etPhone.setText(phone);
                etPassword.setText(password);

                mBtnChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_edit= etUserName.getText().toString();
                        dataSnapshot.getRef().child("name").setValue(user_edit);
                        String phone_edit=etPhone.getText().toString();
                        dataSnapshot.getRef().child("phone").setValue(phone_edit);
                        String password_edit=etNewPassword.getText().toString();
                        //upload gambar
                        uploadGambar();

                        if (password_edit.isEmpty()) {
                            Toast.makeText(getActivity(), "Maaf, password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        } else {
                            dataSnapshot.getRef().child("password").setValue(password_edit);
                            Toast.makeText(getActivity(), "Update Data Successfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //image
                if(!image.equals("default")) {

                    final String finalImage = image;
                    Picasso.with(getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.profile).into(ivImgProfile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(getContext()).load(finalImage).placeholder(R.drawable.profile).into(ivImgProfile);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error : "+databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendTo(Context context, Class kelas) {
        Intent i = new Intent(context, kelas);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
             case R.id.btn_exit:
                prefManager = new dang.program.id.dangapps.PrefManager(getActivity());
                if (!prefManager.isFirstTimeLaunch()) {
                    prefManager.setIsFirstTimeLaunch(true);
                    sendTo(getActivity(), dang.program.id.dangapps.LoginActivity.class);
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                ivImgProfile.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    //upload gambar
    private void uploadGambar() {

        storage = FirebaseStorage.getInstance();
        mImageStorage = storage.getReference();

        if(filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = mImageStorage.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                //njajal
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (task.isSuccessful()) {
                                    final String download_url = uri.toString();

                                    Map updadate_hashMap = new HashMap();
                                    updadate_hashMap.put("image", download_url);

                                    mDatabase.updateChildren(updadate_hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Sukses Menggunggah", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                })

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
        }
    }
}
