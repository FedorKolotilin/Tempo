package com.example.version_2_backend.FrontEnd.Room;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.version_2_backend.BackEnd.MainActivity;
import com.example.version_2_backend.BackEnd.Room;
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
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomFragment extends Fragment {

    int layoutResource = R.layout.room;
    View view;
    public static Room room;
    SeekBar seekBar;
    ImageButton info, startAudio, pauseAudio, chooseFile, exit, chat, previousSong, nextSong;
    MediaPlayer mediaPlayer;
    ImageView imageView;
    private final Handler handler = new Handler();
    ArrayList<String> tracks;
    int playableTrack;
    TextView trackNum;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(layoutResource, container, false);
        viewsInitializing();
        //downloadTracks();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ROOM" ).child(room.getID());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("audio!", "something changed");
                room = snapshot.getValue(Room.class);
                updateTracks();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
        return view;

    }

    private void updateTracks(){
        Toast.makeText(getContext(), "Идет загрузка треков, подождите", Toast.LENGTH_SHORT).show();
        if(tracks == null){
            tracks = new ArrayList<>();
        }
        for (String s : tracks){

            Log.d("audio!",s );
        }
        Log.d("audio!",""+ tracks.size() + " " +  room.getTrackCount() );
        for (int i = tracks.size(); i < room.getTrackCount(); i ++){
            Log.d("audio!", "" + i);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(Constants.storageURL).child("room" + room.getID() + "/" + i);
            try {
                final File localFile = File.createTempFile("audio", ".mpeg");
                int finalI = i;
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("audio!", "added track " + finalI);
                        tracks.add(localFile.getAbsolutePath());
                        if (finalI == 0){
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build());
                            try {
                                mediaPlayer.setDataSource(tracks.get(0));
                                playableTrack = 0;
                                trackNum.setText("Трек номер: " + (playableTrack+1));
                                mediaPlayer.setLooping(true);
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (finalI == room.getTrackCount() - 1){
                            Toast.makeText(getContext(), "Треки обновлены", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            } catch (IOException e) {

            }
        }


    }

    private void downloadTracks(){
        tracks = new ArrayList<>();
        for (int i = 0; i < room.getTrackCount(); i++) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(Constants.storageURL).child("room" + room.getID() + "/" + i);
            try {
                final File localFile = File.createTempFile("audio", ".mpeg");
                int finalI = i;
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        tracks.add(localFile.getAbsolutePath());
                        if(finalI == 0){
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build());
                            try {
                                mediaPlayer.setDataSource(tracks.get(0));
                                playableTrack = 0;
                                trackNum.setText("Трек номер: " + (playableTrack+1));
                                mediaPlayer.setLooping(true);
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            } catch (IOException e) {

            }
        }

    }
    private void viewsInitializing() {
        trackNum = view.findViewById(R.id.treck_num);
        trackNum.setText("Трек номер: " + (playableTrack+1));
        imageView = view.findViewById(R.id.room_picture);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(Constants.storageURL).child("room" + room.getID() + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        imageView.setForeground(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        imageView.setForeground(getActivity().getDrawable(R.drawable.kartina_komnata));
                    }
                }
            });
        } catch (IOException e) {

        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).chooseImage((ImageView) view, "room" + room.getID() + ".jpg");
            }
        });
        info = view.findViewById(R.id.room_info);
        exit = view.findViewById(R.id.room_exit);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(((MainActivity) getActivity()).roomInfoFragment);
                MainActivity.fragmentSetClickable(((MainActivity) getActivity()).roomFragment, false);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.deleteFragment(((MainActivity) getActivity()).roomFragment);
                MainActivity.deleteFragment(((MainActivity) getActivity()).roomInfoFragment);
                MainActivity.addFragment(((MainActivity) getActivity()).menuFragment);
            }
        });
        chooseFile = view.findViewById(R.id.room_files_button);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).chooseAudio();
            }
        });
        startAudio = view.findViewById(R.id.room_translation_button);
        startAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlayer();
            }
        });
        pauseAudio = view.findViewById(R.id.room_pause_button);
        pauseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlayer();
            }
        });
        switchButtons(true);
        seekBar = view.findViewById(R.id.seekBar);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                seekChange(view);
                return false;
            }
        });
        chat = view.findViewById(R.id.room_chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(new RoomTranslationFragment());
                MainActivity.fragmentSetClickable(((MainActivity) getActivity()).roomFragment, false);
            }
        });

        nextSong = view.findViewById(R.id.room_next_song);
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlayer();
                playableTrack +=1;
                if (playableTrack == room.getTrackCount()){
                    playableTrack =0 ;
                }
                setSong(playableTrack );
                trackNum.setText("Трек номер: " + (playableTrack+1));

            }
        });
        previousSong = view.findViewById(R.id.room_previous_song);
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlayer();
                playableTrack -=1;
                if (playableTrack == -1){
                    playableTrack = room.getTrackCount() - 1 ;
                }
                setSong(playableTrack );
                trackNum.setText("Трек номер: " + (playableTrack+1));

            }
        });

    }

    private void seekChange(View view) {
        if (mediaPlayer.isPlaying()) {
            SeekBar sb = (SeekBar) view;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void setSong(int playableTrack) {
        if (mediaPlayer == null) {
            Toast.makeText(getContext(), "Choose Music", Toast.LENGTH_SHORT).show();
        } else {
            seekBar.setProgress(0);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build());
            try {
                mediaPlayer.setDataSource(tracks.get(playableTrack));
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void switchButtons(boolean fl){
        if(fl){
            startAudio.setVisibility(View.VISIBLE);
            startAudio.setClickable(true);
            pauseAudio.setVisibility(View.INVISIBLE);
            pauseAudio.setClickable(false);
        }
        else {
            startAudio.setVisibility(View.INVISIBLE);
            startAudio.setClickable(false);
            pauseAudio.setVisibility(View.VISIBLE);
            pauseAudio.setClickable(true);
        }
    }
    public void startPlayer() {
        if (mediaPlayer == null) {
            Toast.makeText(getContext(), "Choose Music", Toast.LENGTH_SHORT).show();
        } else {
            mediaPlayer.start();
            switchButtons(false);
            if(seekBar.getProgress()!=0){
                mediaPlayer.seekTo(seekBar.getProgress());
            }
            seekBar.setMax(mediaPlayer.getDuration());
            startPlayProgressUpdater();
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startAudio.setForeground(null);
            }*/
        }
    }

    public void createPlayer(Uri audioFile) {
        /*mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build());
        try {
            mediaPlayer.setDataSource(getContext(), audioFile);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(Constants.storageURL).child("room" + room.getID() + "/" + room.getTrackCount() );
        room.setTrackCount(room.getTrackCount() + 1);
        //tracks.add(audioFile.getPath());
        UploadTask uploadTask = storageReference.putFile(audioFile);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("ROOM");
                Map<String, Object> roomValues = room.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(room.getID(), roomValues);
                database.updateChildren(childUpdates);
            }
        });
    }


    public void pausePlayer() {
        mediaPlayer.pause();
        switchButtons(true);
    }

    public void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();

            switchButtons(true);
        }
    }


    public void startPlayProgressUpdater() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        } else {
            //mediaPlayer.pause();
            //seekBar.setProgress(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //MainActivity.addFragment(((MainActivity)getActivity()).menuFragment);
        stopPlayer();
        releasePlayer();
    }


}
