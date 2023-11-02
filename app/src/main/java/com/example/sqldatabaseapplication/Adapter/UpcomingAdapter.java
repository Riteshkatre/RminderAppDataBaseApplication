package com.example.sqldatabaseapplication.Adapter;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqldatabaseapplication.Fragment.UpcomingFragment;
import com.example.sqldatabaseapplication.MyDataBaseHelper;
import com.example.sqldatabaseapplication.MyDataModel;
import com.example.sqldatabaseapplication.R;

import java.util.ArrayList;


public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {
    private ArrayList<MyDataModel> dataList;
    private OnEditItemClickListener editItemClickListener;
    private OnDeleteItemClickListener deleteItemClickListener;
    MyDataBaseHelper myDataBaseHelper;

    public UpcomingAdapter(ArrayList<MyDataModel> dataList, OnEditItemClickListener editItemClickListener, OnDeleteItemClickListener deleteItemClickListener) {
        this.dataList = dataList;
        this.editItemClickListener = editItemClickListener;
        this.deleteItemClickListener = deleteItemClickListener;
    }

    public interface OnEditItemClickListener {
        void onEditClick(int position);
    }

    public interface OnDeleteItemClickListener {
        void onDeleteClick(int position);
    }

    public void updateData(ArrayList<MyDataModel> newDataList) {
        dataList.clear();
        dataList.addAll(newDataList);
        notifyDataSetChanged(); // Notify adapter that the whole dataset has changed
    }


    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.upcomingitem_file, parent, false);
        return new UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        myDataBaseHelper = new MyDataBaseHelper(holder.itemView.getContext());

        MyDataModel myDataModel = dataList.get(position);
        holder.dateTextView.setText(myDataModel.getDate());
        holder.timeTextView1.setText(myDataModel.getTime());
        holder.descTextView2.setText(myDataModel.getDescription());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItemClickListener.onEditClick(position);

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemClickListener.onDeleteClick(position);

            }
        });
        holder.btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Move Task to Completed?");
                builder.setMessage("Do you want to mark this task as completed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDataBaseHelper.deleteData(myDataModel.getId());
                        myDataBaseHelper.addNewCptReminder(myDataModel.getDate(), myDataModel.getTime(), myDataModel.getDescription());
                        dataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, dataList.size());
                        Toast.makeText(v.getContext(), "Task marked as completed", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Dismiss the dialog if "No" is clicked
                    }
                });
                builder.create().show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class UpcomingViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView, timeTextView1, descTextView2;
        ImageView btnEdit, btnDelete, btnRead;

        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView1 = itemView.findViewById(R.id.timeTextView1);
            descTextView2 = itemView.findViewById(R.id.descTextView2);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnRead = itemView.findViewById(R.id.btnRead);
        }
    }
}
