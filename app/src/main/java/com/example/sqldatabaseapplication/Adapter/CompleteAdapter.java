package com.example.sqldatabaseapplication.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqldatabaseapplication.R;

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.CompleteViewHolder>{


    @NonNull
    @Override
    public CompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CompleteViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView,timeTextView1,descTextView2;
        public CompleteViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView=itemView.findViewById(R.id.dateTextView);
            timeTextView1=itemView.findViewById(R.id.timeTextView1);
            descTextView2=itemView.findViewById(R.id.descTextView2);

        }
    }
}
