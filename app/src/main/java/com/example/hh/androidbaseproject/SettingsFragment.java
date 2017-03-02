package com.example.hh.androidbaseproject;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hh.androidbaseproject.DataAndHelper.DataCache;
import com.example.hh.androidbaseproject.databinding.SettingsFragmentBinding;

/**
 * Created by Administrator on 2016/6/7.
 */
public class SettingsFragment extends Fragment {
    private SettingsFragmentBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding != null){
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.settings_fragment,null,false);
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getInstance().hasLogin = false;
                DataCache.getInstance().loginInfo = null;
                DataCache.getInstance().savePreference();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        binding.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return binding.getRoot();
    }
}
