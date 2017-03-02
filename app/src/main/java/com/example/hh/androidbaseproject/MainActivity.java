package com.example.hh.androidbaseproject;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.example.hh.androidbaseproject.DataAndHelper.DataCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private static Map<Integer, Integer> fragmentsMap = new HashMap<Integer, Integer>();
    private RadioGroup rg;
    public boolean gotoFirstPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataCache.getInstance().mainActivity = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            gotoFirstPage = bundle.getBoolean("gotoFirstPage");
        }
        setContentView(R.layout.activity_main);
        rg = (RadioGroup) findViewById(R.id.tabbar);
        rg.check(rg.getChildAt(0).getId());
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("me", "checkId:" + checkedId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content, fragmentList.get(fragmentsMap
                        .get(checkedId)));
                transaction.commit();
            }
        });

        Fragment fragment = new MainFragment();
        Fragment settingsFrag = new SettingsFragment();
        fragmentList.add(fragment);
        fragmentList.add(settingsFrag);
        fragmentsMap.put(new Integer(R.id.first_page), new Integer(0));
        fragmentsMap.put(new Integer(R.id.settings_page), new Integer(1));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gotoFirstPage) {
            rg.check(R.id.first_page);
            gotoFirstPage = false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


        // 检测是否有新版，强制更新

    }
}
