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

public class RoomEnteringFragment extends Fragment {
    int layoutResource = R.layout.room_entering;
    View view;
    EditText idEditText, passwordEditText;
    ImageButton roomEnter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(layoutResource, container, false);
        viewsInitializing();
        return view;
    }

    private void viewsInitializing() {
        idEditText = view.findViewById(R.id.room_entering_id);
        passwordEditText = view.findViewById(R.id.room_entering_password);
        roomEnter = view.findViewById(R.id.room_entering_enter);
        roomEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RoomFinder.globalFindRoom(idEditText.getText().toString(), passwordEditText.getText().toString());


            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.fragmentSetClickable(((MainActivity)getActivity()).menuFragment, true);
    }

}
