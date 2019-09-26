package com.example.roomdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Note> noteList;
    private RecyclerViewAdapter.ClickListener clickListener;

    public RecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        noteList = new ArrayList<>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.txtName.setText(note.name);
        holder.txtNo.setText("#" + String.valueOf(note.note_id));
        holder.txtDesc.setText(note.description);


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    public void updateNoteList(List<Note> data) {
        noteList.clear();
        noteList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Note data) {
        noteList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtNo;
        public TextView txtDesc;
        public TextView txtCategory;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            txtNo = view.findViewById(R.id.txtNo);
            txtName = view.findViewById(R.id.txtName);
            txtDesc = view.findViewById(R.id.txtDesc);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.launchIntent(noteList.get(getAdapterPosition()).note_id);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}
