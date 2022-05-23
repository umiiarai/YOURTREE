package com.example.yourtree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// 노트 목록
public class NoteListAdapter extends BaseAdapter {

    private Context context;
    private List<Note> noteList;

    public NoteListAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int i) {
        return noteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.note, null);
        TextView et_note_content = (TextView) v.findViewById(R.id.et_note_content);
        TextView et_note_name = (TextView) v.findViewById(R.id.et_note_name);
        TextView et_note_date = (TextView) v.findViewById(R.id.et_note_date);

        et_note_content.setText(noteList.get(i).getNote());
        et_note_name.setText(noteList.get(i).getName());
        et_note_date.setText(noteList.get(i).getDate());

        v.setTag(noteList.get(i).getNote());
        return v;
    }


}
