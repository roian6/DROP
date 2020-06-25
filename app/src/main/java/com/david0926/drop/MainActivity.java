package com.david0926.drop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.david0926.drop.databinding.ActivityMainBinding;
import com.david0926.drop.fragment.FabFragment;
import com.david0926.drop.fragment.MainFragment1;
import com.david0926.drop.fragment.MainFragment2;
import com.david0926.drop.fragment.MainFragment3;
import com.david0926.drop.fragment.MainFragment4;
import com.google.android.material.bottomappbar.BottomAppBar;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //fullscreen

        binding.fabMain.setColorFilter(Color.WHITE);
        binding.fabMain.setOnClickListener(view -> {
            binding.fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            binding.fabMain.setColorFilter(getColor(R.color.colorPrimary));
            FabFragment fabFragment = FabFragment.newInstance();
            fabFragment.setParentFab(binding.fabMain);
            fabFragment.show(getSupportFragmentManager(), fabFragment.getTag());
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("invalidate_fab")) {
                    binding.fabMain.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                    binding.fabMain.setColorFilter(Color.WHITE);
                    binding.bottombarMain.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                    binding.bottombarMain.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("invalidate_fab"));

        binding.bottomnavMain.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_1:
                    switchFragment(MainFragment1.newInstance());
                    break;
                case R.id.action_2:
                    switchFragment(MainFragment2.newInstance());
                    break;
                case R.id.action_3:
                    switchFragment(MainFragment3.newInstance());
                    break;
                case R.id.action_4:
                    switchFragment(MainFragment4.newInstance());
                    break;
            }
            return true;
        });
        switchFragment(MainFragment1.newInstance());
    }

    void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
