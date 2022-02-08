package com.aysu.traveldaily.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.aysu.traveldaily.databinding.ActivityMain3Binding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity3 extends AppCompatActivity {


    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher ;
    private ActivityMain3Binding main3Binding;
    Uri imageData;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main3Binding=ActivityMain3Binding.inflate(getLayoutInflater());
        View view=main3Binding.getRoot();
        setContentView(view);

        registerLauncher();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
    }
    public void save_buttom(View view) {
         UUID uuid=UUID.randomUUID();
         String imageName="images/"+uuid+".jpg";
        if(imageData!=null){
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference=FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            String Url=uri.toString();
                            String comment=main3Binding.aciklama.getText().toString();
                            String email=user.getEmail();
                            String country=main3Binding.country.getText().toString();
                            String city=main3Binding.city.getText().toString();
                            String PlaceName=main3Binding.PlaceName.getText().toString();

                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("comment",comment);
                            hashMap.put("url",Url);
                            hashMap.put("email",comment);
                            hashMap.put("country",country);
                            hashMap.put("city",city);
                            hashMap.put("PlaceName",PlaceName);
                            hashMap.put("Date", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity3.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                 Toast.makeText(MainActivity3.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }






    }

    public void selectimage(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }


        }else{

            Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
             activityResultLauncher.launch(intentToGallery);
        }

    }

    private void registerLauncher(){
         activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
             @Override
             public void onActivityResult(ActivityResult result) {
                 if(result.getResultCode()==RESULT_OK){
                  Intent intentResult=result.getData();
                   if(intentResult!=null){
                      imageData= intentResult.getData();
                      main3Binding.imageView.setImageURI(imageData);
                   }
                 }
             }
         });

         permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
             @Override
             public void onActivityResult(Boolean result) {
               if(result){
                   Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   activityResultLauncher.launch(intentToGallery);
               }else{
                   Toast.makeText(MainActivity3.this,"Permission needded !",Toast.LENGTH_LONG).show();
               }
             }
         });


    }

}