package com.example.diplomaandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Note> list;

    NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.list = notes;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) convertView = layoutInflater.inflate(R.layout.list_notes, viewGroup, false);
        Note note = list.get(position);

        assert convertView != null;
        TextView headline = convertView.findViewById(R.id.list_headline);
        TextView body = convertView.findViewById(R.id.list_note_body);
        TextView date = convertView.findViewById(R.id.list_date);
        TextView time = convertView.findViewById(R.id.list_time);

        headline.setText(note.getHeadline());
        body.setText(note.getBody());
        date.setText(note.getDate());
        time.setText(note.getTime());
        return convertView;
    }
}
