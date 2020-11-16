package com.example.diplomaandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_notes, viewGroup, false);
            viewHolder = new ViewHolder();
        } else viewHolder = (ViewHolder) convertView.getTag();
        Note note = list.get(position);

        assert convertView != null;
        viewHolder.headline = convertView.findViewById(R.id.list_headline);
        viewHolder.body = convertView.findViewById(R.id.list_note_body);
        viewHolder.linearLayout = convertView.findViewById(R.id.list_deadline_layout);
        viewHolder.date = convertView.findViewById(R.id.list_date);
        viewHolder.time = convertView.findViewById(R.id.list_time);

        viewHolder.headline.setText(note.getHeadline());
        viewHolder.body.setText(note.getBody());
        viewHolder.date.setText(note.getDate());
        viewHolder.time.setText(note.getTime());

        if (note.getHeadline().equals("")) viewHolder.headline.setVisibility(View.GONE);
        if (note.getBody().equals("")) viewHolder.body.setVisibility(View.GONE);
        if (!note.hasDeadline()) viewHolder.linearLayout.setVisibility(View.GONE);
        return convertView;
    }

    private static class ViewHolder {
        TextView headline;
        TextView body;
        TextView date;
        TextView time;
        LinearLayout linearLayout;
    }
}
