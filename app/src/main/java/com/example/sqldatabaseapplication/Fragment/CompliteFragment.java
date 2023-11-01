package com.example.sqldatabaseapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sqldatabaseapplication.R;


public class CompliteFragment extends Fragment {
    RecyclerView comRcv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_complite, container, false);
      comRcv=view.findViewById(R.id.comRcv);
      return (view);

    }
}