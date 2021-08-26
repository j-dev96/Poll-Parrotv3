package com.noname.PollParrot.homedashboard.fragments;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.noname.PollParrot.R;
import com.noname.PollParrot.adapter.AddChoiceRecyclerAdapter;
import com.noname.PollParrot.adapter.AddVoteRecyclerAdapter;
import com.noname.PollParrot.adapter.AddVoteRecyclerAdapter.OnItemClickListener;
import com.noname.PollParrot.databinding.CustomDialogEditQuestionBinding;
import com.noname.PollParrot.databinding.FragmentCreatePollBinding;
import com.noname.PollParrot.dataclass.Choice;
import com.noname.PollParrot.dataclass.PollData;
import com.noname.PollParrot.homedashboard.activity.CreatedPollHistoryActivity;
import com.noname.PollParrot.homedashboard.activity.HomeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreatePollFragment extends Fragment  implements View.OnClickListener, AddChoiceRecyclerAdapter.OnItemClickListener, OnItemClickListener {

    FragmentCreatePollBinding binding;
    private final ArrayList<Choice> choiceList = new ArrayList<>();
    private final ArrayList<PollData> pollDataArrayList = new ArrayList<>();
    private AddChoiceRecyclerAdapter adapter;
    private AddVoteRecyclerAdapter mAdapter;
    private String questionForVoteValue = "";
    private String answerVoteId ="";
    private final String pin="";
    private String pollId = "";
    private DatabaseReference createPollRef,historyRef, createdPollHistoryRef;
    private String pollCheck,userId;
    private PollData pollData = new PollData();



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreatePollBinding.inflate(inflater, container, false);

        createPollRef = FirebaseDatabase.getInstance().getReference("CreatePoll");
        historyRef = FirebaseDatabase.getInstance().getReference("History");
        createdPollHistoryRef = FirebaseDatabase.getInstance().getReference("createdPollHistory");

        binding.addButton.setOnClickListener(this);
        binding.createButton.setOnClickListener(this);
        binding.back.setOnClickListener(this);

        assert getArguments() != null;
        pollCheck = CreatePollFragmentArgs.fromBundle(getArguments()).getPollCheck();
        if (pollCheck.equals("addPoll"))
        {
            binding.createPollView.setVisibility(View.VISIBLE);
            binding.pinLinear.setVisibility(View.GONE);
        }
        if (pollCheck.equals("pinCard"))
        {
            binding.createPollView.setVisibility(View.GONE);
            binding.pinLinear.setVisibility(View.VISIBLE);
            checkPinFromDataBase();
        }


        binding.voteBtn.setOnClickListener(v -> {
            final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
            mp.start();

            savePollResult();

        });
        binding.endPollBtn.setOnClickListener(v -> {
            final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
                    mp.start();
            Intent intent = new Intent(getContext(), CreatedPollHistoryActivity.class);
            intent.putExtra("pollId",pollData.getVoteId());
            startActivity(intent);
            requireActivity().finish();
        });
        binding.showResultsBtn.setOnClickListener(v -> {
            final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
            mp.start();
            Intent intent = new Intent(getContext(), CreatedPollHistoryActivity.class);
            intent.putExtra("pollId",pollData.getVoteId());
            startActivity(intent);
            requireActivity().finish();
        });

        checkPollCreator();

        return binding.getRoot();
    }

    private void checkPollCreator()
    {

        String tokenId = FirebaseInstanceId.getInstance().getToken();
        createPollRef.orderByChild("pollCreatorId").equalTo(tokenId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    binding.endPollBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.endPollBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkPinFromDataBase() {

        binding.passwordButton.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.passwordEt.getText()).toString().isEmpty())
            {
                String pin = binding.passwordEt.getText().toString();
                createPollRef.orderByChild("pin").equalTo(pin).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            binding.pinLinear.setVisibility(View.GONE);
                            binding.voteLinerView.setVisibility(View.VISIBLE);
                            for (DataSnapshot dss : snapshot.getChildren()) {

                                pollData = dss.getValue(PollData.class);
                                pollDataArrayList.add(pollData);

                                binding.questionForVote.setText(pollData.getQuestionVote());
                                binding.progressBar.setVisibility(View.VISIBLE);
                                createPollRef.child(pollData.getVoteId())
                                        .child("answerOptions").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            binding.progressBar.setVisibility(View.GONE);
                                            for (DataSnapshot ds : snapshot.getChildren()) {

                                                Choice choice = ds.getValue(Choice.class);
                                                choiceList.add(choice);

                                            }
                                            setRecyclerView(choiceList);


                                        }else {
                                            binding.progressBar.setVisibility(View.GONE);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                        binding.progressBar.setVisibility(View.GONE);
                                        Toast.makeText(requireContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }




                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Incorrect Pin", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else
            {
                Toast.makeText(requireContext(), "Please Enter Pin", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void setRecyclerView(ArrayList<Choice> exercisesList) {
        mAdapter = new AddVoteRecyclerAdapter(requireContext(), exercisesList);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        binding.recycler.setLayoutManager(llm);
        llm.setStackFromEnd(true);
        binding.recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
            if (view == binding.addButton) {
                final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
            mp.start();
            if (Objects.requireNonNull(binding.choiceOptionEt.getText()).toString().isEmpty()) {
                Toast toast = Toast.makeText(requireContext(), "Enter Choice Option", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            } else {
                initRecycler();
            }
        }

        if (view == binding.createButton)
        {
            final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
            mp.start();
            if (choiceList.size() != 0) {

                if (Objects.requireNonNull(binding.pinEt.getText()).toString().isEmpty())
                {
                    Toast.makeText(requireContext(), "Please Enter a 6 digit pin", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showConfirmationDialog();
                }



            } else
            {
                Toast.makeText(requireContext(), "Add Choice", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == binding.back)
        {
            final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
            mp.start();
           requireActivity().onBackPressed();
        }

    }

    private void initRecycler() {

        choiceList.add(new Choice(Objects.requireNonNull(binding.choiceOptionEt.getText()).toString()));
        adapter = new AddChoiceRecyclerAdapter(requireContext(), choiceList);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        binding.exercisesRecycler.setLayoutManager(llm);
        llm.setStackFromEnd(true);
        binding.exercisesRecycler.setAdapter(adapter);
//            binding.exercisesRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(this);
        adapter.notifyItemInserted(choiceList.size() - 1);

        binding.choiceOptionEt.setText("");
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show();
        choiceList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onEditClick(int position) {
        Choice choice = choiceList.get(position);
        showEditProductDialog(choice, position);
    }

    private void showEditProductDialog(Choice choice, int position) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        CustomDialogEditQuestionBinding editDialogBinding = CustomDialogEditQuestionBinding.bind(
                LayoutInflater.from(requireContext()).inflate(
                        R.layout.custom_dialog_edit_question,
                        null
                ));
        builder.setView(editDialogBinding.getRoot());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        editDialogBinding.weightKgsEt.setText(choice.getChoiceQuestion());


        final int[] count = new int[1];

        editDialogBinding.confirmButton.setOnClickListener(view -> {


            choiceList.set(position,
                    new Choice(
                            editDialogBinding.weightKgsEt.getText().toString()));
            adapter.notifyDataSetChanged();

            alertDialog.dismiss();
            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show();
        });

        editDialogBinding.cancelButton.setOnClickListener(view -> alertDialog.dismiss());

    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create Poll?");
        builder.setIcon(R.drawable.ic_baseline_check_circle);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.small_sms_tone);
            mp.start();
            createPoll();
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    private void savePollResult() {

        if (questionForVoteValue.isEmpty()) {
            Toast.makeText(getContext(), "Please Select the Answer", Toast.LENGTH_SHORT).show();
        } else {

            String tokenId = FirebaseInstanceId.getInstance().getToken();
            final String pushKey = createPollRef.push().getKey();
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("token_id", tokenId);
            tokenMap.put("VoteId", pollData.getVoteId());
            tokenMap.put("questionVote", binding.questionForVote.getText().toString());
            tokenMap.put("questionVoteAnswer", questionForVoteValue);
            tokenMap.put("questionVoteAnswerID", answerVoteId);
            tokenMap.put("timestamp", ServerValue.TIMESTAMP);
            assert tokenId != null;
            assert pushKey != null;
            historyRef.child(pushKey)
                    .setValue(tokenMap).addOnCompleteListener(task -> {

                String tokenId1 = FirebaseInstanceId.getInstance().getToken();
                final String pushKey1 = createPollRef.push().getKey();
                Map<String, Object> historyMap = new HashMap<>();
                historyMap.put("token_id", tokenId1);
                historyMap.put("VoteId", pollData.getVoteId());
                historyMap.put("questionVote", binding.questionForVote.getText().toString());
                historyMap.put("questionVoteAnswer", questionForVoteValue);
                historyMap.put("questionVoteAnswerID", answerVoteId);
                historyMap.put("timestamp", ServerValue.TIMESTAMP);
                assert tokenId1 != null;
                assert pushKey1 != null;
                createdPollHistoryRef.child(answerVoteId).child(tokenId1).setValue(historyMap);
                Toast.makeText(getContext(), "Vote Successfully Submit", Toast.LENGTH_SHORT).show();
            });
        }
    }


    private void createPoll() {
        binding.progressBar.setVisibility(View.VISIBLE);
        String question = Objects.requireNonNull(binding.questionEt.getText()).toString();

        String tokenId = FirebaseInstanceId.getInstance().getToken();
        final String pushKey = createPollRef.push().getKey();
        Map<String, Object> tokenMap = new HashMap<>();

        tokenMap.put("token_id", tokenId);
        tokenMap.put("VoteId", pushKey);
        tokenMap.put("questionVote", question);
        tokenMap.put("pin", Objects.requireNonNull(binding.pinEt.getText()).toString());
        tokenMap.put("timestamp", ServerValue.TIMESTAMP);
        tokenMap.put("choiceCount", choiceList.size());
        tokenMap.put("pollCreatorId", tokenId);

        assert pushKey != null;
        createPollRef = createPollRef.child(pushKey);
        createPollRef.setValue(tokenMap)
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    System.out.println("CreateOrderActivity.createOrder: " + e);

                }).addOnCompleteListener(task -> {

            createPollRef = createPollRef.child("answerOptions");
            for (int i = 0; i < adapter.getItemCount(); i++) {

                final String optionsKey = createPollRef.child(pushKey).push().getKey();
                Choice exercise;
                exercise = choiceList.get(i);

                Map<String, Object> map = new HashMap<>();
                map.put("choiceQuestion", exercise.getChoiceQuestion());
                map.put("optionsKey", optionsKey);

                assert optionsKey != null;
                createPollRef.child(optionsKey)
                        .setValue(map).addOnCompleteListener(task1 -> {
                            startActivity(new Intent(requireContext(),HomeActivity.class));
                            requireActivity().finish();
                        });

            }

//            Toast.makeText(requireContext(), "Successfully Add", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);

        });



    }

    @Override
    public void onSendDataClick(int position) {
        Choice choice = choiceList.get(position);
        questionForVoteValue = choice.getChoiceQuestion();
        answerVoteId = choice.getOptionsKey();

    }
}