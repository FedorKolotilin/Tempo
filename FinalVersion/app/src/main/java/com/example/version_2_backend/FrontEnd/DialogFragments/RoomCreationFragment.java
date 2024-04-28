package com.example.version_2_backend.FrontEnd.DialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.BackEnd.RoomFinder;
import com.example.version_2_backend.R;

public class RoomCreationFragment extends Fragment {
    int layoutResource = R.layout.room_creation;
    View view;
    EditText idEditText, nameEditText, passwordEditText;
    ImageButton roomCreate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(layoutResource, container, false);
        viewsInitializing();
        return view;
    }

    private void viewsInitializing() {
        idEditText = view.findViewById(R.id.room_creation_id);
        nameEditText  = view.findViewById(R.id.room_creation_name);
        passwordEditText = view.findViewById(R.id.room_creation_password);
        roomCreate = view.findViewById(R.id.room_creation_create);
        roomCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RoomFinder.globalFindRoom(idEditText.getText().toString(), nameEditText.getText().toString(),passwordEditText.getText().toString());

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.fragmentSetClickable(((MainActivity)getActivity()).menuFragment, true);
    }

}
