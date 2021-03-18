package com.example.roadsafetyapplication.ui.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roadsafetyapplication.R;
import com.example.roadsafetyapplication.signup.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    ImageView userImage;
    TextView userEmail;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    StorageReference reference;
    Button signOut;
//    FirebaseFirestore firestore;
    String userId;
    public Uri imageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userEmail = view.findViewById(R.id.account_email);
        userEmail.setText(firebaseUser.getEmail());
        userImage = view.findViewById(R.id.account_image);
        signOut = view.findViewById(R.id.logout);

        userId=firebaseUser.getUid();
        reference=FirebaseStorage.getInstance().getReference()
                .child("UserAccountImages");

        if(firebaseUser.getPhotoUrl() != null){
            Glide.with(getActivity())
                    .load(firebaseUser.getPhotoUrl())
                    .into(userImage);
        }

         userImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 imageClicked();
             }
         });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getActivity(),"Signed Out Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                getActivity().finish();
            }
        });
        return  view;
    }
    public void imageClicked(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,19991);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19991 && resultCode == RESULT_OK){
            imageUri = data.getData();
            userImage.setImageURI(imageUri);
            uploadImage();

        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Saving your Display Picture");
        progressDialog.show();

        reference = FirebaseStorage.getInstance().getReference()
                .child("UserAccountImages")
                .child(userId+".jpeg");
        reference.putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Profile Image Set Successfully",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),e.getCause().toString(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double percentage = (snapshot.getBytesTransferred()*100/snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress"+ (int)percentage +"%");
            }
        });
    }


}