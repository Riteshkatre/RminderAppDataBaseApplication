package com.example.sqldatabaseapplication.Fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sqldatabaseapplication.Adapter.UpcomingAdapter;
import com.example.sqldatabaseapplication.AlarmReceiver;
import com.example.sqldatabaseapplication.MyDataBaseHelper;
import com.example.sqldatabaseapplication.MyDataModel;
import com.example.sqldatabaseapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UpcomingFragment extends Fragment {
    RecyclerView rcv;
    FloatingActionButton btnAdd;

   MyDataBaseHelper dbHandler;
   UpcomingAdapter adapter;
    private AlarmReceiver alarmReceiver;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        rcv = view.findViewById(R.id.rcv);
        btnAdd = view.findViewById(R.id.btnAdd);

        alarmReceiver = new AlarmReceiver();

        dbHandler=new MyDataBaseHelper(requireContext());
        ArrayList<MyDataModel> dataList = dbHandler.getAllData();





        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button cancel, ok;
                EditText etDate, etTime, etDesc;


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.aad_dialogfile, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();


                ok = dialogView.findViewById(R.id.ok);
                cancel = dialogView.findViewById(R.id.cancel);
                etDate = dialogView.findViewById(R.id.etDate);
                etTime = dialogView.findViewById(R.id.etTime);
                etDesc = dialogView.findViewById(R.id.etDesc);

                etDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar currentDate = Calendar.getInstance();
                        int year = currentDate.get(Calendar.YEAR);
                        int month = currentDate.get(Calendar.MONTH);
                        int day = currentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                        String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                                        etDate.setText(selectedDate);
                                    }
                                },
                                year,
                                month,
                                day
                        );
                        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

                        datePickerDialog.show();
                    }


                });
                etTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the current time
                        Calendar currentTime = Calendar.getInstance();
                        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = currentTime.get(Calendar.MINUTE);

                        // Create a TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                        // Handle the selected time
                                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                                        etTime.setText(selectedTime);
                                    }
                                },
                                currentHour,
                                currentMinute,
                                false
                        );

                        // Show the TimePickerDialog
                        timePickerDialog.show();
                    }




            });
                etDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String date = etDate.getText().toString();
                        String time = etTime.getText().toString();
                        String desc = etDesc.getText().toString();

                        // validating if the text fields are empty or not.
                        if (date.isEmpty() || time.isEmpty() || desc.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter all the data..", Toast.LENGTH_SHORT).show();
                            return;
                        }else{



                        dbHandler.addNewCourse(date, time, desc);


                        // after adding the data we are displaying a toast message.
                        Toast.makeText(getContext(), "Course has been added.", Toast.LENGTH_SHORT).show();
                        etDate.setText("");
                        etTime.setText("");
                        etDesc.setText("");
                            ArrayList<MyDataModel> newDataList = dbHandler.getAllData();
                            adapter.updateData(newDataList);
                        dialog.dismiss();}

                    }
                });



                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });



            }

        });
        adapter = new UpcomingAdapter(dataList, position -> {
            // Handle Edit Click
            MyDataModel selectedData = dataList.get(position);
            openEditDialog(selectedData);
        }, position -> {
            showDeleteConfirmationDialog(position);
            // Handle Delete Click
//            MyDataModel selectedData = dataList.get(position);
//            MyDatabaseHandler myDatabaseHandler = new MyDatabaseHandler(requireContext());
         /*   dbHandler.deleteData(selectedData.getId());
            dataList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();*/
        });

        rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rcv.setAdapter(adapter);
        return (view);



    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Do you want to delete this reminder?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            ArrayList<MyDataModel> dataList = dbHandler.getAllData();
            // Handle the delete operation
            MyDataModel selectedData = dataList.get(position);
            dbHandler.deleteData(selectedData.getId());
            dataList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.updateData(dataList);
            Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();


        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void openEditDialog(MyDataModel selectedData) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View editDialogView = getLayoutInflater().inflate(R.layout.edit_dialog_layout, null);
        builder.setView(editDialogView);
        builder.setTitle("Edit Reminder");

        EditText etvEditDate = editDialogView.findViewById(R.id.etvEditDate);
        EditText etvEditTime = editDialogView.findViewById(R.id.etvEditTime);
        EditText etvEditDescription = editDialogView.findViewById(R.id.etvEditDescription);

        etvEditDate.setText(selectedData.getDate());
        etvEditTime.setText(selectedData.getTime());
        etvEditDescription.setText(selectedData.getDescription());

        // Add an OnClickListener to etvEditDate to open DatePicker
        etvEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                                etvEditDate.setText(selectedDate);
                            }
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.show();
            }
        });

        // Add an OnClickListener to etvEditTime to open TimePicker
        etvEditTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int currentMinute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        requireContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                                etvEditTime.setText(selectedTime);
                            }
                        },
                        currentHour,
                        currentMinute,
                        false
                );
                timePickerDialog.show();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editedDate = etvEditDate.getText().toString();
                String editedTime = etvEditTime.getText().toString();
                String editedDescription = etvEditDescription.getText().toString();

                // Update the data in the database
                MyDataBaseHelper myDatabaseHandler = new MyDataBaseHelper(requireContext());
                myDatabaseHandler.updateData(selectedData.getId(), editedDate, editedTime, editedDescription);

                selectedData.setDate(editedDate);
                selectedData.setTime(editedTime);
                selectedData.setDescription(editedDescription);
                ArrayList<MyDataModel> newDataList = dbHandler.getAllData();
                adapter.updateData(newDataList);

                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "Item updated", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }






    }