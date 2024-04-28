package com.example.version_2_backend.BackEnd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.version_2_backend.R;

public class RoomsDataAdapter extends BaseAdapter {

    Context context;
    String fragment;

    public RoomsDataAdapter(Context context, String fragment) {
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public int getCount() {
        return RoomsData.rooms.size();
    }

    @Override
    public Room getItem(int i) {
        return RoomsData.rooms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return ((Room) getItem(i)).getID().hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View itemView = new View(context);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(R.layout.start_listview_item, viewGroup, false);
        } else {
            itemView = view;
        }
        TextView textView1, textView2;
        textView1 = itemView.findViewById(R.id.room_item_textView1);
        textView2 = itemView.findViewById(R.id.room_item_textView2);
        textView1.setText(((Room) getItem(i)).getRoomName());
        textView2.setText("" + ((Room) getItem(i)).getID());

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity.enterRoom(getItem(i));
            }
        });
        return itemView;
    }
}
