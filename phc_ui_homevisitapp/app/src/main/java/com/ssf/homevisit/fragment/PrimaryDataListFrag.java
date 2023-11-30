package com.ssf.homevisit.fragment;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssf.homevisit.R;
import com.ssf.homevisit.adapters.ListAdapter;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.FragmentPrimaryDataListBinding;
import com.ssf.homevisit.viewmodel.MappingViewModel;


public class PrimaryDataListFrag extends Fragment {
    View view;
    FragmentPrimaryDataListBinding binding;
    ListAdapter adapter;
    public PrimaryDataListFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_primary_data_list, container, false);
        view = binding.getRoot();
        intialiszation();
        return view;
    }

    private void intialiszation() {
        getBundleData();
        callPrimaryDataApi();
        adapter = new ListAdapter();
        binding.list.setAdapter(adapter);
    }

    private void callPrimaryDataApi() {

    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle!=null) {
            String phc = bundle.getString("PHC");
            String subCenter = bundle.getString("SubCenter");
            String village = bundle.getString("Village");
            String message = phc + ">" + subCenter + ">" + village;

            binding.publicHeal.setText(message);
        }
    }
}