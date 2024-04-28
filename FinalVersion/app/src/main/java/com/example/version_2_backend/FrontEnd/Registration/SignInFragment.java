package com.example.version_2_backend.FrontEnd.Registration;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.BackEnd.utils.PreferenceManager;
import com.example.version_2_backend.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignInFragment  extends Fragment {
    View view;
    EditText eMail, password;
    ImageButton signIn, signUp;
    private PreferenceManager preferenceManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.registration_sign_in, container, false);
        viewsInitializing();
        return view;
    }

    private void viewsInitializing() {
        eMail = view.findViewById(R.id.registration_sig_in_email);
        password = view.findViewById(R.id.registration_sig_in_pass);
        signIn = view.findViewById(R.id.registration_sig_in_buttonsignin);
        signUp = view.findViewById(R.id.registration_sig_in_buttonsignup);
        signIn.setOnClickListener(view -> {
            if (eMail.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(eMail.getText().toString()).matches()) {
                Toast.makeText(getContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
            }else if (password.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            }  else {
                signin();
            }
        });
        signUp.setOnClickListener(view -> MainActivity.replaceFragment(MainActivity.signUpFragment));
    }

    private void signin() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getContext());

        database.collection("users")
                .whereEqualTo("email", eMail.getText().toString())
                .whereEqualTo("password", password.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult()!= null && task.getResult().getDocuments().size() > 0){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            preferenceManager.putBoolean("isSignedIn", true);
                            preferenceManager.putString("first_name", documentSnapshot.getString("first_name"));
                            preferenceManager.putString("last_name", documentSnapshot.getString("last_name"));
                            preferenceManager.putString("email", documentSnapshot.getString("email"));
                            preferenceManager.putString("password", password.getText().toString());
                            MainActivity.endRegistration();
                        }
                        else {
                            Toast.makeText(getContext(), "Unable to sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

