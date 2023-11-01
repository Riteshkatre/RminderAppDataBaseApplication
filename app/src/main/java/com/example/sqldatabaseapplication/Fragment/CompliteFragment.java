package com.example.sqldatabaseapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sqldatabaseapplication.Adapter.CompleteAdapter;
import com.example.sqldatabaseapplication.MyDataBaseHelper;
import com.example.sqldatabaseapplication.MyDataModel;
import com.example.sqldatabaseapplication.R;

import java.util.ArrayList;
import java.util.List;


public class CompliteFragment extends Fragment {
    RecyclerView comRcv;
    CompleteAdapter completeAdapter;
    MyDataBaseHelper dbHelper;

    SwipeRefreshLayout swipeRefresh;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_complite, container, false);
      comRcv=view.findViewById(R.id.comRcv);
      swipeRefresh=view.findViewById(R.id.swipeRefresh);
      swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
              dbHelper = new MyDataBaseHelper(requireContext());
              ArrayList<MyDataModel> completedDataList = dbHelper.getAllCompletedData();
              completeAdapter = new CompleteAdapter(requireContext(), completedDataList);



              comRcv.setAdapter(completeAdapter);

              comRcv.setLayoutManager(new LinearLayoutManager(requireContext()));
              swipeRefresh.setRefreshing(false);



          }
      });






        return (view);

    }
}