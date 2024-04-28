package com.example.version_2_backend.FrontEnd.Account;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.Account;
import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.BackEnd.utils.Constants;
import com.example.version_2_backend.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class AccountFragment extends Fragment {

    View view;
    ImageView fon, avatar;
    Button exitButton;
    ImageButton  logOut;
    TextView firstname, lastname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.clickableFragment = ((MainActivity)getActivity()).accountFragment.toString();


        view = inflater.inflate(R.layout.account, container, false);
        fon = view.findViewById(R.id.account_shapka);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(Constants.storageURL).child(Account.getAccountUser().getEmail() + "fon" + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    fon.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        fon.setForeground(getActivity().getResources().getDrawable(R.drawable.shapka_account));
                    }
                }
            });
        } catch (IOException e ) {

        }
        fon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).chooseImage((ImageView) view,Account.getAccountUser().getEmail() + "fon" + ".jpg" );
            }
        });
        avatar = view.findViewById(R.id.account_avatar);
        storageRef = storage.getReferenceFromUrl(Constants.storageURL).child(Account.getAccountUser().getEmail()+ "avatar" + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    avatar.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        avatar.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.avatar_account));
                    }
                }
            });
        } catch (IOException e ) {}
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).chooseImage((ImageView) view,Account.getAccountUser().getEmail() + "avatar" + ".jpg" );
            }
        });
        firstname = view.findViewById(R.id.account_firstname);
        firstname.setText(Account.getAccountUser().getFirstName());
        lastname = view.findViewById(R.id.account_lastname);
        lastname.setText(Account.getAccountUser().getLastName());
        exitButton = view.findViewById(R.id.account_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        logOut = view.findViewById(R.id.account_log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.logOut();
            }
        });
        return view ;
    }
    public void exit(){
        MainActivity.deleteFragment(((MainActivity)getActivity()).accountFragment);
        MainActivity.fragmentSetClickable(((MainActivity)getActivity()).menuFragment, true);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        exit();
    }
}
