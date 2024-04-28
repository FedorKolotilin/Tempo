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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.Account;
import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.BackEnd.utils.Constants;
import com.example.version_2_backend.BackEnd.utils.Message;
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
import java.util.ArrayList;

public class RoomTranslationFragment extends Fragment {
    EditText editText;
    ImageButton button;
    DatabaseReference db;
    View view;
    ArrayList<Message> messages = new ArrayList<>();
    ArrayAdapter<Message> arrayAdapter ;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.room_translation,container, false);
        ListView listView = view.findViewById(R.id.room_translation_list_view);
        context = getContext();
        arrayAdapter = new ArrayAdapter<Message>(getActivity(), android.R.layout.simple_list_item_1,messages){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View itemView = new View(getContext());
                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    itemView = layoutInflater.inflate(R.layout.send_item, parent, false);
                } else {
                    itemView = convertView;
                }
                TextView textView = itemView.findViewById(R.id.text1);
                textView.setText(getItem(position).getText());
                ImageView imageView = itemView.findViewById(R.id.send_item_avatar);
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
        listView.setAdapter(arrayAdapter);

        db = FirebaseDatabase.getInstance().getReference("Room" +  ((MainActivity)getActivity()).roomFragment.room.getID() + "chat");
        editText = view.findViewById(R.id.room_translation_edit_text);
        button = view.findViewById(R.id.room_translation_send_messege);
        getDataFromDB();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.setText(editText.getText().toString());
                message.setEmail(Account.getAccountUser().getEmail());
                db.push().setValue(message);
                getDataFromDB();
            }
        });
        return view;
    }
    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(messages.size() != 0){
                    messages.clear();
                }
                for(DataSnapshot ds : snapshot.getChildren()){
                    Message m = ds.getValue(Message.class);
                    messages.add(m);

                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addValueEventListener(vListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.fragmentSetClickable(((MainActivity)getActivity()).roomFragment,true);
    }
}
