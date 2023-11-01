package com.example.sqldatabaseapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqldatabaseapplication.MyDataModel;
import com.example.sqldatabaseapplication.R;

import java.util.List;

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.CompleteViewHolder>{
    Context context;
    List<MyDataModel>compeleteMyDataModelList;

    public CompleteAdapter(Context context, List<MyDataModel> compeleteMyDataModelList) {
        this.context = context;
        this.compeleteMyDataModelList = compeleteMyDataModelList;
    }

    @NonNull
    @Override
    public CompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.completeditem_file ,parent,false);
        return new CompleteAdapter.CompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteViewHolder holder, int position) {
        MyDataModel model=compeleteMyDataModelList.get(position);
        holder.dateTextView.setText(model.getDate());
        holder.timeTextView1.setText(model.getTime());
        holder.descTextView2.setText(model.getDescription());

    }

    @Override
    public int getItemCount() {
        return compeleteMyDataModelList.size();
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
