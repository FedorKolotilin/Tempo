package com.example.version_2_backend.BackEnd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.version_2_backend.BackEnd.utils.Constants;
import com.example.version_2_backend.BackEnd.utils.PreferenceManager;
import com.example.version_2_backend.FrontEnd.Account.AccountFragment;
import com.example.version_2_backend.FrontEnd.DialogFragments.ChooseFragment;
import com.example.version_2_backend.FrontEnd.DialogFragments.RoomCreationFragment;
import com.example.version_2_backend.FrontEnd.DialogFragments.RoomEnteringFragment;
import com.example.version_2_backend.FrontEnd.Menu.MenuFragment;
import com.example.version_2_backend.FrontEnd.Registration.SignInFragment;
import com.example.version_2_backend.FrontEnd.Registration.SignUpFragment;
import com.example.version_2_backend.FrontEnd.Room.RoomFragment;
import com.example.version_2_backend.FrontEnd.Room.RoomInfoFragment;
import com.example.version_2_backend.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private static FragmentManager fm;
    private static FragmentTransaction tr;

    static ActivityResultLauncher<Intent> resultLauncherImage, resultLauncherAudio;

    public static MenuFragment menuFragment = new MenuFragment();
    public static AccountFragment accountFragment = new AccountFragment();
    public static ChooseFragment chooseFragment = new ChooseFragment();
    public static RoomCreationFragment roomCreationFragment = new RoomCreationFragment();
    public static RoomEnteringFragment roomEnteringFragment = new RoomEnteringFragment();
    public static RoomFragment roomFragment = new RoomFragment();
    public static RoomInfoFragment roomInfoFragment = new RoomInfoFragment();
    public static SignUpFragment signUpFragment = new SignUpFragment();
    public static SignInFragment signInFragment = new SignInFragment();

    public static String clickableFragment;
    public static MainActivity mainActivity;

    ImageView imageView;
    String imageName;

    public static int mainView = R.id.main_view;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initialize();
        addFragment(signUpFragment);

        initResultLaunchers();
    }

    private void initResultLaunchers() {
        resultLauncherImage = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(
                            ActivityResult result) {

                        Intent data = result.getData();
                        if (data != null) {
                            Uri sUri = data.getData();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                imageView.setForeground(null);
                            }
                            imageView.setImageURI(sUri);
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageReference = storage.getReferenceFromUrl(Constants.storageURL).child(imageName);

                            UploadTask uploadTask = storageReference.putFile(sUri);
                        }
                    }
                });
        resultLauncherAudio = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(
                            ActivityResult result) {


                        Intent data = result.getData();
                        if (data != null) {
                            Uri sUri = data.getData();
                            roomFragment.createPlayer(sUri);

                        }
                    }
                });
    }



    public void chooseImage(ImageView imageView, String imageName) {
        this.imageView = imageView;
        this.imageName = imageName;
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission
                        .READ_EXTERNAL_STORAGE)
                != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{
                            Manifest.permission
                                    .READ_EXTERNAL_STORAGE},
                    1);
        } else {
            selectImage();
        }

    }

    public void chooseAudio() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission
                        .READ_EXTERNAL_STORAGE)
                != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{
                            Manifest.permission
                                    .READ_EXTERNAL_STORAGE},
                    2);
        } else {
            selectAudio();
        }

    }

    private void selectImage() {
        Intent intent
                = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        resultLauncherImage.launch(intent);
    }

    private void selectAudio() {
        Intent intent
                = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        resultLauncherAudio.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else if (requestCode == 2 && grantResults.length > 0
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            selectAudio();
        } else {
            Toast
                    .makeText(getApplicationContext(),
                            "Permission Denied",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void initialize() {
        clickableFragment = menuFragment.toString();
        fm = getSupportFragmentManager();
    }

    static public void deleteFragment(Fragment fragment) {
        tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }

    static public void addFragment(Fragment fragment) {
        tr = fm.beginTransaction();
        tr.add(mainView, fragment);
        tr.addToBackStack(null);
        tr.commit();

    }

    static public void addFragment(int xmlSource, Fragment fragment) {

        tr = fm.beginTransaction();
        tr.add(xmlSource, fragment);
        tr.addToBackStack(null);
        tr.commit();

    }

    static public void replaceFragment(int xmlSource, Fragment fragment) {

        tr = fm.beginTransaction();
        tr.replace(xmlSource, fragment, "SomeTag");
        tr.addToBackStack(null);
        tr.commit();

    }

    static public void replaceFragment(Fragment fragment) {

        tr = fm.beginTransaction();
        tr.replace(mainView, fragment, "SomeTag");
        tr.commit();

    }

    static public void fragmentSetClickable(Fragment fragment, boolean fl) {

        ViewGroup viewGroup = (ViewGroup) fragment.getView();
        int n = viewGroup.getChildCount();

        for (int i = 0; i < n; i++) {

            View v = viewGroup.getChildAt(i);
            if (v.hasOnClickListeners()) {
                v.setClickable(fl);
            }

        }
    }

    static public void enterRoom(Room room) {
        if (room == null) {
            Toast toast = Toast.makeText(mainActivity, "Wrong ID or Password, please Try again", Toast.LENGTH_LONG);
            toast.show();
        } else {
            if (!room.getUsers().contains(Account.getAccountUser())) {
                room.addUser(Account.getAccountUser());


                DatabaseReference database = FirebaseDatabase.getInstance().getReference("ROOM");

                Map<String, Object> roomValues = room.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(room.ID, roomValues);

                database.updateChildren(childUpdates);
            }
            RoomFragment.room = room;
            MainActivity.replaceFragment(MainActivity.roomFragment);

        }
    }

    static public void createRoom(Room room) {
        RoomFragment.room = room;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("ROOM");
        database.child(room.getID()).setValue(room);
    }

    static public void endRegistration() {
        PreferenceManager preferenceManager = new PreferenceManager(signUpFragment.getContext());
        String firstName, lastName, email, password;
        firstName = preferenceManager.getString("first_name");
        lastName = preferenceManager.getString("last_name");
        email = preferenceManager.getString("email");
        password = preferenceManager.getString("password");
        User user = new User(firstName, lastName, password, email);
        Account.setAccountUser(user);
        replaceFragment(menuFragment);
        RoomsData.getDataFromDB();
    }

    static public void logOut() {
        PreferenceManager preferenceManager = new PreferenceManager(signUpFragment.getContext());

        Account.setAccountUser(null);
        replaceFragment(signUpFragment);
        preferenceManager.clearPreferences();
    }


}