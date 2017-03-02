package com.example.hh.androidbaseproject;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hh.androidbaseproject.databinding.MainFragmentBinding;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MainFragment extends Fragment {

    private MainFragmentBinding binding = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding != null){
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.main_fragment,null, false);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
