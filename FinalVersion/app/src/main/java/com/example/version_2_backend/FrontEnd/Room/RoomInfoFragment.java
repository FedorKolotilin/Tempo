package com.example.version_2_backend.FrontEnd.Room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.BackEnd.Room;
import com.example.version_2_backend.BackEnd.User;
import com.example.version_2_backend.BackEnd.utils.Constants;
import com.example.version_2_backend.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class RoomInfoFragment extends Fragment {

    Context context;
    int layoutResource = R.layout.room_info;
    View view;
    Room room;
    TextView id, password, name;
    ListView listView;
    ArrayAdapter<User> adapter;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(layoutResource, container, false);
        context = getContext();
        viewsInitializing();
        return view;
    }

    private void viewsInitializing() {
        room = RoomFragment.room;
        listView = view.findViewById(R.id.room_info_listview);
        id = view.findViewById(R.id.room_info_id);
        password = view.findViewById(R.id.room_info_password);
        name = view.findViewById(R.id.room_info_name);
        id.setText("" + room.getID());
        password.setText(room.getPassword());
        name.setText(room.getRoomName());



        adapter = new ArrayAdapter<User>(context, R.layout.user_item, room.users) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View itemView = new View(getContext());
                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    itemView = layoutInflater.inflate(R.layout.user_item, parent, false);
                } else {
                    itemView = convertView;
                }
                TextView textView = itemView.findViewById(R.id.user_item_text);
                textView.setText(getItem(position).getEmail());
                ImageView imageView = itemView.findViewById(R.id.user_item_icon);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(Constants.storageURL).child(getItem(position).getEmail() + "avatar" + ".jpg");
                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                imageView.setForeground(getActivity().getResources().getDrawable(R.drawable.avatar_account));
                            }
                        }
                    });
                } catch (IOException e ) {

                }
                return itemView;
            }
        };
        listView.setAdapter(adapter);
        getDataFromDB();
    }

    public void getDataFromDB() {
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("ROOM");
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (room.users.size() != 0) {
                    room.users.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Room nRoom = ds.getValue(Room.class);
                    if (nRoom.getID().equals(room.getID())) {
                        room.users = nRoom.users;
                    }

                }
                if(getContext() != null) {
                    adapter = new ArrayAdapter<User>(getContext(), R.layout.user_item, room.users) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View itemView = new View(getContext());
                            if (convertView == null) {
                                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                itemView = layoutInflater.inflate(R.layout.user_item, parent, false);
                            } else {
                                itemView = convertView;
                            }
                            TextView textView = itemView.findViewById(R.id.user_item_text);
                            textView.setText(getItem(position).getEmail());
                            ImageView imageView = itemView.findViewById(R.id.user_item_icon);
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReferenceFromUrl(Constants.storageURL).child(getItem(position).getEmail() + "avatar" + ".jpg");
                            try {
                                final File localFile = File.createTempFile("images", "jpg");
                                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageView.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            imageView.setForeground(getActivity().getResources().getDrawable(R.drawable.avatar_account));
                                        }
                                    }
                                });
                            } catch (IOException e ) {

                            }
                            return itemView;
                        }
                    };
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database.addValueEventListener(vListener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.fragmentSetClickable(((MainActivity) getActivity()).roomFragment, true);
    }
}
