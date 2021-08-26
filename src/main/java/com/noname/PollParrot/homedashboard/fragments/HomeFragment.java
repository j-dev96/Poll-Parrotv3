package com.noname.PollParrot.homedashboard.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.noname.PollParrot.R;
import com.noname.PollParrot.databinding.FragmentHomeBinding;
import com.noname.PollParrot.homedashboard.fragments.HomeFragmentDirections;

import org.jetbrains.annotations.NotNull;

import static com.noname.PollParrot.homedashboard.fragments.HomeFragmentDirections.actionHomeFragmentToCreatePollFragment;


public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding ;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);


        clickListeners();



        return binding.getRoot();
    }

    private void clickListeners() {
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
        binding.addPoll.setOnClickListener(v -> {
            mp.start();
            HomeFragmentDirections.ActionHomeFragmentToCreatePollFragment action = actionHomeFragmentToCreatePollFragment("addPoll");
            Navigation.findNavController(requireView()).navigate(action);


        });
        binding.history.setOnClickListener(v -> {
            mp.start();
            Navigation.findNavController(binding.history).navigate(R.id.action_homeFragment_to_historyFragment);
        });

        binding.pinCard.setOnClickListener(v -> {
            mp.start();
            HomeFragmentDirections.ActionHomeFragmentToCreatePollFragment action = actionHomeFragmentToCreatePollFragment("pinCard");
            Navigation.findNavController(requireView()).navigate(action);

        });
        binding.discord.setOnClickListener(v ->{
            Uri uriUrl = Uri.parse("https://discord.gg/2s3KdzdNrH");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        });
    }
}