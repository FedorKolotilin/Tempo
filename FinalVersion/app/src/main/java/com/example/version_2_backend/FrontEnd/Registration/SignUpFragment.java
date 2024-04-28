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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignUpFragment extends Fragment {
    View view;
    EditText firstName, lastName, eMail, password1, password2;
    ImageButton signIn, signUp;
    private PreferenceManager preferenceManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.registration_sign_up, container, false);
        viewsInitializing();
        return view;
    }

    private void viewsInitializing() {
        preferenceManager = new PreferenceManager(getContext());

        if (preferenceManager.getBoolean("isSignedIn")){
            MainActivity.endRegistration();
        }

        firstName = view.findViewById(R.id.registration_sig_up_firstname);
        lastName = view.findViewById(R.id.registration_sig_up_lastname);
        eMail = view.findViewById(R.id.registration_sig_up_email);
        password1 = view.findViewById(R.id.registration_sig_up_pass1);
        password2 = view.findViewById(R.id.registration_sig_up_pass2);
        signIn = view.findViewById(R.id.registration_sig_up_buttonsignin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.replaceFragment(MainActivity.signInFragment);
            }
        });
        signUp = view.findViewById(R.id.registration_sig_up_buttonsignup);
        signUp.setOnClickListener(view -> {
            if (firstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Enter first name", Toast.LENGTH_SHORT).show();
            } else if (lastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (eMail.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            }else if (!Patterns.EMAIL_ADDRESS.matcher(eMail.getText().toString()).matches()) {
                Toast.makeText(getContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
            } else if (password1.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            } else if (password2.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Confirm your password", Toast.LENGTH_SHORT).show();
            } else if (!password1.getText().toString().equals(password2.getText().toString())) {
                Toast.makeText(getContext(), "Password & confirm password must be same", Toast.LENGTH_SHORT).show();
            } else {
                signup();
            }
        });
    }

    private void signup() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put("first_name", firstName.getText().toString());
        user.put("last_name", lastName.getText().toString());
        user.put("email", eMail.getText().toString());
        user.put("password", password1.getText().toString());

        database.collection("users").whereEqualTo("email", eMail.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().getDocuments().size()  == 0){
                    database.collection("users")
                            .add(user)
                            .addOnSuccessListener(documentReference -> {
                                preferenceManager.putBoolean("isSignedIn", true);
                                preferenceManager.putString("first_name", firstName.getText().toString());
                                preferenceManager.putString("last_name", lastName.getText().toString());
                                preferenceManager.putString("email", eMail.getText().toString());
                                preferenceManager.putString("password", password1.getText().toString());
                                MainActivity.endRegistration();

                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show())
                    ;
                }
                else{
                    Toast.makeText(getContext(),"choose another email, this one is used", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
