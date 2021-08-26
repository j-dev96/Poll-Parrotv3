package com.noname.PollParrot.homedashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.noname.PollParrot.R;
import com.noname.PollParrot.databinding.ActivityHomeBinding;
import com.noname.PollParrot.services.BackgroundSoundService;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //music from: https://www.bensound.com/royalty-free-music/track/ukulele
        String action = "PLAY";
        Intent myService = new Intent(HomeActivity.this, BackgroundSoundService.class);
        myService.setAction(action);
        startService(myService);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_home_container);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
    }
    //to navigate to back screens, starts here
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (Openable) null);
    }
//ends here
    @Override
    protected void onResume() {
        super.onResume();
        String action = "PLAY";
        Intent myService = new Intent(HomeActivity.this, BackgroundSoundService.class);
        myService.setAction(action);
        startService(myService);
    }
    @Override
    protected void onPause() {
        super.onPause();
        String action = "PAUSE";
        Intent myService = new Intent(HomeActivity.this, BackgroundSoundService.class);
        myService.setAction(action);
        startService(myService);
    }
}