package curtin.edu.ptb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {

   // ActivityUploadBinding binding;
    Uri imageUri;
    ProgressDialog progressDialog;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    String selectedImage, tags;
    TextView tagsView;
    Button uploadimagebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_upload);
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //int selectedImage = Integer.parseInt(intent.getStringExtra("selectedImage"));
        ImageView image = findViewById(R.id.firebaseimage);
        //image.setImageResource(selectedImage);
        selectedImage = intent.getStringExtra("selectedImage");
        tags = intent.getStringExtra("tags");
        tagsView = findViewById(R.id.tagTextUpload);
        tagsView.setText(tags);

        Glide
                .with(this)
                .load(selectedImage)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(image);

        uploadimagebtn = findViewById(R.id.uploadimagebtn);
        uploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(UploadActivity.this);
                progressDialog.setTitle("Uploading Image....");
                progressDialog.show();

                Intent intent = getIntent();

                HashMap<String, Object> map = new HashMap<>();
                map.put("image",intent.getStringExtra("selectedImage"));
                map.put("tags", intent.getStringExtra("tags"));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.UK);
                Date now = new Date();
                String fileName = formatter.format(now);

                firebaseDatabase.getReference("upload images/"+fileName).child("Your Upload").push().setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UploadActivity.this, "UPLOADED!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
             //   Toast.makeText(UploadActivity.this, "UPLOADED!", Toast.LENGTH_SHORT).show();

            }
        });

    }

  /*  private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);


        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        binding.firebaseimage.setImageURI(null);
                        Toast.makeText(UploadActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(UploadActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


                    }
                });

    }

    private void selectImage() {

        Intent intent = getIntent();
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        int selectedImage = Integer.parseInt(intent.getStringExtra("selectedImage"));
        ImageView image = findViewById(R.id.firebaseimage);
        image.setImageResource(selectedImage);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){

            imageUri = data.getData();
            binding.firebaseimage.setImageURI(imageUri);


        }
    }*/
}