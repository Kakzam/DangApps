package dang.program.id.dangapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import dang.program.id.dangapps.menu_booking.menu_place_resto.ModelPlaceCity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddDataActivity extends AppCompatActivity {

    String id;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private EditText etNamaResto, etAlamatResto, etStatusResto, etWaktuResto;
    private Button btnAddData, btnPilihImg;
    private ImageView ivImageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDatabase.keepSynced(true);

        etNamaResto = findViewById(R.id.et_nama_resto);
        etAlamatResto = findViewById(R.id.et_address_resto);
        etStatusResto = findViewById(R.id.et_status_resto);
        etWaktuResto = findViewById(R.id.et_time_resto);
        btnAddData = findViewById(R.id.add_data_place);
        btnPilihImg = findViewById(R.id.btn_upload_image);
        ivImageView = findViewById(R.id.img_view);

        btnPilihImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihImg();
            }
        });

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name_resto_place = etNamaResto.getText().toString();
                final String address_resto_place = etAlamatResto.getText().toString();
                final String status_resto_place = etStatusResto.getText().toString();
                final String time_resto_place = etWaktuResto.getText().toString();
                final String img_resto_place = "Gambar";

                String nama = String.valueOf(spinner.getSelectedItem());

                ModelPlaceCity modelDataSantri = new ModelPlaceCity(img_resto_place, name_resto_place, status_resto_place, address_resto_place, time_resto_place);

                String id = mDatabase.push().getKey();

                mDatabase.child("DataPlaceCity").child(nama).child(id).setValue(modelDataSantri);

                //njajal gambar
                uploadGambar(id);
                Toast.makeText(AddDataActivity.this, "Add Data Success", Toast.LENGTH_SHORT).show();
            }
        });

        //Spinner
        addItemsOnSpinner2();
    }

    private void addItemsOnSpinner2() {
        spinner = findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        list.add("Yogyakarta");
        list.add("Semarang");
        list.add("Magelang");
        list.add("Solo");
        list.add("Klaten");
        list.add("Purworejo");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void pilihImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadGambar(String id) {

        final String key = id;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            final StorageReference ref = storageReference.child("images_resto/" + UUID.randomUUID().toString());
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

                                        final Map updadate_hashMap = new HashMap();
                                        updadate_hashMap.put("img_resto_city", download_url);

                                        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        final String current_uid = mCurrentUser.getUid();
                                        //error
                                        final String nama = String.valueOf(spinner.getSelectedItem());

                                        mDatabase.getDatabase().getInstance()
                                                .getReference()
                                                .child("Users")
                                                .child(current_uid)
                                                .child("DataPlaceCity")
                                                .child(nama)
                                                .child(key)
                                                .updateChildren(updadate_hashMap)
                                                .addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {

                                                        if (task.isSuccessful()) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(AddDataActivity.this, "Sukses Menggunggah", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                    } else {
                                        Toast.makeText(AddDataActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AddDataActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddDataActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
