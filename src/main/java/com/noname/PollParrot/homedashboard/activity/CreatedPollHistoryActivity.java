package com.noname.PollParrot.homedashboard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noname.PollParrot.R;
import com.noname.PollParrot.adapter.AddVoteHistoryRecyclerAdapter;
import com.noname.PollParrot.databinding.ActivityCreatedPollHistoryBinding;
import com.noname.PollParrot.dataclass.Choice;
import com.noname.PollParrot.dataclass.PollData;
import com.noname.PollParrot.services.BackgroundSoundService;

import java.util.ArrayList;

public class CreatedPollHistoryActivity extends AppCompatActivity implements AddVoteHistoryRecyclerAdapter.OnItemClickListener {

    private ActivityCreatedPollHistoryBinding binding;
    private final ArrayList<Choice> choiceList = new ArrayList<>();
    private final ArrayList<PollData> pollDataArrayList = new ArrayList<>();
    private AddVoteHistoryRecyclerAdapter mAdapter;
    private String questionForVoteValue = "";
    private String answerVoteId ="";
    private final String pin="";
    private String pollId = "";
    private DatabaseReference createPollRef,historyRef, createdPollHistoryRef;
    private String pollCheck,userId;
    private PollData pollData = new PollData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatedPollHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String action = "PLAY";
        Intent myService = new Intent(CreatedPollHistoryActivity.this, BackgroundSoundService.class);
        myService.setAction(action);
        startService(myService);
        createPollRef = FirebaseDatabase.getInstance().getReference("CreatePoll");
        historyRef = FirebaseDatabase.getInstance().getReference("History");
        createdPollHistoryRef = FirebaseDatabase.getInstance().getReference("createdPollHistory");

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            String pollID = bundle.getString("pollId");
            Toast.makeText(this, pollID, Toast.LENGTH_SHORT).show();
            checkPinFromDataBase(pollID);
        }

        binding.back.setOnClickListener(v ->{
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.small_sms_tone);
                mp.start();
                finish();
        });
        binding.endBtn.setOnClickListener(v -> {
            startActivity(new Intent(CreatedPollHistoryActivity.this,HomeActivity.class));
            finish();
        });

    }

    private void checkPinFromDataBase(String pollID) {

        createPollRef.orderByChild("VoteId").equalTo(pollID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot dss : snapshot.getChildren()) {

                        pollData = dss.getValue(PollData.class);
                        pollDataArrayList.add(pollData);

                        binding.questionForVote.setText(pollData.getQuestionVote());
                        String count = String.valueOf(pollData.getChoiceCount());
                        binding.totalAnswers.setText(count);

                        createPollRef.child(pollData.getVoteId())
                                .child("answerOptions").addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {

                                        Choice choice = ds.getValue(Choice.class);
                                        choiceList.add(choice);

                                    }
                                    setRecyclerView(choiceList);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(CreatedPollHistoryActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }




                }
                else
                {
                    Toast.makeText(CreatedPollHistoryActivity.this, "Incorrect Pin", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void setRecyclerView(ArrayList<Choice> exercisesList) {
        mAdapter = new AddVoteHistoryRecyclerAdapter(CreatedPollHistoryActivity.this, exercisesList,pollData.getChoiceCount());
        LinearLayoutManager llm = new LinearLayoutManager(CreatedPollHistoryActivity.this);
        binding.recycler.setLayoutManager(llm);
        llm.setStackFromEnd(true);
        binding.recycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onSendDataClick(int position) {
        Choice choice = choiceList.get(position);
        questionForVoteValue = choice.getChoiceQuestion();
        answerVoteId = choice.getOptionsKey();
    }
    @Override
    protected void onResume() {
        super.onResume();
        String action = "PLAY";
        Intent myService = new Intent(CreatedPollHistoryActivity.this, BackgroundSoundService.class);
        myService.setAction(action);
        startService(myService);
    }
    @Override
    protected void onPause() {
        super.onPause();
        String action = "PAUSE";
        Intent myService = new Intent(CreatedPollHistoryActivity.this, BackgroundSoundService.class);
        myService.setAction(action);
        startService(myService);
    }
}