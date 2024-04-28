package com.example.version_2_backend.FrontEnd.Menu;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.BackEnd.RoomsDataAdapter;
import com.example.version_2_backend.R;

public class MenuFragment extends Fragment
{

    ImageButton accountButton,newRoomButton;
    ListView listView;
    public RoomsDataAdapter adapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainActivity.clickableFragment = this.toString();
        view = inflater.inflate(R.layout.start, container, false);

        listViewInitializing();
        buttonsInitializing();
        return view;

    }


    public void listViewInitializing(){
        Log.d("debug", "list");
        listView = view.findViewById(R.id.start_listview);
        adapter = new RoomsDataAdapter(getContext(), this.toString());
        listView.setAdapter(adapter);

    }
    public void buttonsInitializing(){
        accountButton = view.findViewById(R.id.start_account_button);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(((MainActivity)getActivity()).accountFragment);
                MainActivity.fragmentSetClickable(((MainActivity)getActivity()).menuFragment,false);
            }
        });
        newRoomButton = view.findViewById(R.id.start_new_room_button);
        newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(((MainActivity)getActivity()).chooseFragment);
                MainActivity.fragmentSetClickable(((MainActivity)getActivity()).menuFragment,false);
            }
        });
    }

}
