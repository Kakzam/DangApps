package dang.program.id.dangapps.menu_booking.menu_detail_booking;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dang.program.id.dangapps.R;

public class AddDataMenuActivity extends AppCompatActivity {

    private EditText etJudul, etHarga;
    private Button mBtnAddData;
    android.widget.Spinner spinner_place, spinner_food;
    private ImageView ivImgFoodItem;
    private Button mBtnAddImage;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_menu);

        //set access database
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDatabase.keepSynced(true);

        setView();
        setOnclickListener();
        addPlaceSpinner();
        addDataItemsSpinner();
    }

    private void setView() {
        etJudul = findViewById(R.id.et_send_data_judul);
        etHarga = findViewById(R.id.et_send_data_harga);
        mBtnAddData = findViewById(R.id.btn_tambah_data);
        //njajal
        ivImgFoodItem = findViewById(R.id.iv_img_food_item);
        mBtnAddImage = findViewById(R.id.btn_add_img);
    }

    private void setOnclickListener() {
        mBtnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String menu_title = etJudul.getText().toString();
                final String menu_price = etHarga.getText().toString();
                final String img_resto_item = "img_resto_item";

                final String place = String.valueOf(spinner_place.getSelectedItem());
                final String food = String.valueOf(spinner_food.getSelectedItem());

                final ModelAddDataMenu modelDataSantri = new ModelAddDataMenu(menu_title, menu_price, img_resto_item);

                final String id = mDatabase.push().getKey();


                //getId untuk makanan permasing - masing kota.
                mDatabase.child("DataPlaceCity").child(place).child(getIntent().getStringExtra("keyrestoran")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Toast.makeText(getApplicationContext(), "Add Data Success" + getIntent().getStringExtra("keyrestoran"), Toast.LENGTH_SHORT).show();

                            mDatabase.child("DataPlaceCity")
                                    .child(place)
                                    .child(getIntent().getStringExtra("keyrestoran"))
                                    .child(food).child(id)
                                    .setValue(modelDataSantri);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Db Error :" + databaseError, Toast.LENGTH_SHORT).show();
                    }
                });
                uploadGambar(id);
            }
        });
        //njajal
        mBtnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    //Nama Tempat
    private void addPlaceSpinner() {
        spinner_place = findViewById(R.id.spinner_add_data_place);
        List<String> list = new ArrayList<>();
        list.add(getIntent().getStringExtra("name_resto"));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_place.setAdapter(dataAdapter);
    }

    //Nama Makanan
    private void addDataItemsSpinner() {
        spinner_food = findViewById(R.id.spinner_add_data_food);
        List<String> list = new ArrayList<>();
        list.add("Food");
        list.add("Drink");
        list.add("Snack");
        list.add("Package");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_food.setAdapter(dataAdapter);
    }

    private void uploadGambar(final String id) {

        final String key = id;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //upload gambar ke firebase
            final StorageReference ref = storageReference.child("images_resto_food_item/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (task.isSuccessful()) {
                                        final String download_url = uri.toString();

                                        //dapat data dari spinner.
                                        final String nama_tempat = String.valueOf(spinner_place.getSelectedItem());
                                        final String nama_tipe = String.valueOf(spinner_food.getSelectedItem());

                                        final Map updadate_hashMap = new HashMap();
                                        updadate_hashMap.put("img_resto_item", download_url);

                                        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


                                        mDatabase.child("DataPlaceCity")
                                                .child(nama_tempat)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (final DataSnapshot child : dataSnapshot.getChildren()) {
                                                            Toast.makeText(getApplicationContext(), "Add Data Success", Toast.LENGTH_SHORT).show();

                                                            mDatabase.child("DataPlaceCity")
                                                                    .child(nama_tempat)
                                                                    .child(getIntent().getStringExtra("keyrestoran"))
                                                                    .child(nama_tipe)
                                                                    .child(key)
                                                                    .updateChildren(updadate_hashMap)
                                                                    .addOnCompleteListener(new OnCompleteListener() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task task) {

                                                                            if (task.isSuccessful()) {
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(AddDataMenuActivity.this, "Sukses Menggunggah " + id, Toast.LENGTH_LONG).show();
                                                                            }

                                                                        }
                                                                    });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Toast.makeText(getApplicationContext(), "Db Error :" + databaseError, Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(AddDataMenuActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AddDataMenuActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddDataMenuActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                ivImgFoodItem.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
