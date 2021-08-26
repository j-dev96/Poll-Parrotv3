package com.noname.PollParrot.homedashboard.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.noname.PollParrot.R;
import com.noname.PollParrot.adapter.HistoryVoteAdapter;
import com.noname.PollParrot.databinding.FragmentHistoryBinding;
import com.noname.PollParrot.dataclass.QuestionVote;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {


    FragmentHistoryBinding binding;
    private DatabaseReference historyRef;
    String tokenId;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        historyRef = FirebaseDatabase.getInstance().getReference("History");
        tokenId = FirebaseInstanceId.getInstance().getToken();
        getHistory();

        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
        binding.back.setOnClickListener(v -> {
            mp.start();
           requireActivity().onBackPressed();

        });


        return binding.getRoot();


    }

    private void getHistory() {

        binding.recentViewProgressBarLayout.setVisibility(View.VISIBLE);
        ArrayList<QuestionVote> remindersList = new ArrayList<>();

        assert tokenId != null;
        historyRef.orderByChild("token_id").equalTo(tokenId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recentViewProgressBarLayout.setVisibility(View.GONE);
                for (DataSnapshot dss : snapshot.getChildren()) {

                    QuestionVote questionVote = dss.getValue(QuestionVote.class);

                    remindersList.add(questionVote);

                }

                initRecyclerView(remindersList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.recentViewProgressBarLayout.setVisibility(View.GONE);

                Toast.makeText(requireContext(), "" + error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initRecyclerView(ArrayList<QuestionVote> remindersList) {
        if (remindersList.isEmpty())
        {
            binding.recentlyNothingFoundLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            HistoryVoteAdapter adapter = new HistoryVoteAdapter(getContext(), remindersList);
            binding.recyclerView.setAdapter(adapter);
        }


    }


}