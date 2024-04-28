package com.example.version_2_backend.BackEnd;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoomFinder {

    public static void globalFindRoom(String ID, String name, String password) {
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("ROOM").child(ID);
        database.get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().getValue(Room.class)== null) {
                            Room room = new Room(ID, name, password);
                            room.addUser(Account.accountUser);

                            MainActivity.createRoom(room);

                            MainActivity.replaceFragment(MainActivity.roomFragment);

                            MainActivity.deleteFragment(MainActivity.roomCreationFragment);
                        }
                        else {
                            Toast.makeText(MainActivity.roomCreationFragment.getContext(), "Room with same ID already yet", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    public static void globalFindRoom(String ID ,String password){
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("ROOM").child(ID);
        database.get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Room room = task.getResult().getValue(Room.class);
                        if(room == null || !room.password.equals(password)){
                            Toast toast = Toast.makeText(MainActivity.roomEnteringFragment.getContext(), "Wrong ID or Password, please Try again", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else {
                            MainActivity.enterRoom(room);
                        }
                    }
                });
    }
}
