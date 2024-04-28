package com.example.version_2_backend.FrontEnd.DialogFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.R;

public class ChooseFragment extends Fragment {

    int layoutResource = R.layout.choose;
    Button roomCreate, roomEnter;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainActivity.clickableFragment = ((MainActivity)getActivity()).chooseFragment.toString();
        view = inflater.inflate(layoutResource, container, false);
        viewsInitializing();
        return view;
    }

    private void viewsInitializing() {
        roomCreate = view.findViewById(R.id.choose_room_create);
        roomEnter = view.findViewById(R.id.choose_room_enter);
        roomCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(((MainActivity)getActivity()).roomCreationFragment);
                MainActivity.deleteFragment(((MainActivity)getActivity()).chooseFragment);
            }
        });
        roomEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(((MainActivity)getActivity()).roomEnteringFragment);
                MainActivity.deleteFragment(((MainActivity)getActivity()).chooseFragment);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("destr", "destr");
        MainActivity.fragmentSetClickable(((MainActivity)getActivity()).menuFragment, true);
    }

}
