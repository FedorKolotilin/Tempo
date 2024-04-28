package com.example.version_2_backend.BackEnd;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomsData {
    static public ArrayList<Room> rooms = new ArrayList<>();

    public static void getDataFromDB(){
        DatabaseReference database ;
        database = FirebaseDatabase.getInstance().getReference("ROOM");
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(rooms.size() != 0){
                    rooms.clear();
               }
                for(DataSnapshot ds : snapshot.getChildren()){
                    Room room = ds.getValue(Room.class);
                    ArrayList<String> eMails = new ArrayList<>();
                    for (User u : room.getUsers()){
                        eMails.add(u.getEmail());
                    }
                     if (eMails.contains(Account.getAccountUser().getEmail())){
                         Log.d("audio!", room.toString());
                         rooms.add(room);
                    }
                }
                MainActivity.menuFragment.adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database.addValueEventListener(vListener);

    }





}
