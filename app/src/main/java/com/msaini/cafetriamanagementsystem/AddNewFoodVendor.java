package com.msaini.cafetriamanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AddNewFoodVendor extends AppCompatActivity {


    String imageName = "";
    FoodList uploadFood = null;
    int PICK_IMAGE_REQUEST = 101;
    Uri filePath;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_vendor);

        storageReference = firebaseStorage.getReference();
        databaseReference  = firebaseDatabase.getReference();;
        if (isConnected()) {
            findViewById(R.id.newFoodImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();

                }
            });

            findViewById(R.id.logoutButtonVendor).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddNewFoodVendor.this, VendorActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddNewFoodVendor.this, VendorOrders.class);
                    startActivity(intent);
                    finish();
                }
            });

            findViewById(R.id.saveFood).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   String foodDesc =  ((TextView)findViewById(R.id.newFoodDesc)).getText().toString();
                   String foodPrice =  ((TextView)findViewById(R.id.newFoodPrice)).getText().toString();


                   uploadFood = new FoodList(foodDesc,imageName,foodPrice);

                    uploadImage();

                    ((TextView)findViewById(R.id.newFoodDesc)).setText("");
                    ((TextView)findViewById(R.id.newFoodPrice)).setText("");
                    ((ImageView)findViewById(R.id.newFoodImage)).setImageResource(R.drawable.addimage);


                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageViewUpload =findViewById(R.id.newFoodImage);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            File file= new File(filePath.getPath());
            imageName =  file.getName();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewUpload.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // final StorageReference ref = storageReference.child("images/"+ UPLOAD_MEEASAGE.imageName);
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID()+".png");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddNewFoodVendor.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uriImage = uri.toString();
                                    uploadFood.imageUrl = uriImage;
                                    databaseReference.child("Food").child("Bojang").push().setValue(uploadFood);
                                    //GoToMessageAdapter();
                                    //UPLOAD_MEEASAGE = null;
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddNewFoodVendor.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            //GoToMessageAdapter();
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
