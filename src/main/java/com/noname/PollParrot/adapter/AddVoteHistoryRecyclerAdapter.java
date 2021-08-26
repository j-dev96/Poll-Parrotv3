package com.noname.PollParrot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noname.PollParrot.databinding.CustomLayoutVoteBinding;
import com.noname.PollParrot.dataclass.Choice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class AddVoteHistoryRecyclerAdapter extends RecyclerView.Adapter<AddVoteHistoryRecyclerAdapter.ViewHolder> {

    private final Context context;
    ArrayList<Choice> choiceList;

    private AddVoteHistoryRecyclerAdapter.OnItemClickListener mListener;

    private DatabaseReference createPollRef,historyRef, createdPollHistoryRef;
    private ArrayList<String> totalVote = new ArrayList<>();

    public interface OnItemClickListener {
        void onSendDataClick(int position);
    }

    public void setOnItemClickListener(AddVoteHistoryRecyclerAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemClickListeners(AddVoteHistoryRecyclerAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public AddVoteHistoryRecyclerAdapter(Context context, ArrayList<Choice> choiceList, int choiceCount) {

        this.context = context;
        this.choiceList = choiceList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        CustomLayoutVoteBinding binding = CustomLayoutVoteBinding
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Choice choice = choiceList.get(position);
        holder.bind(choice);

    }


    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CustomLayoutVoteBinding binding;

        public ViewHolder(CustomLayoutVoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Choice choice) {
            binding.choiceTv.setText(choice.getChoiceQuestion());

            createPollRef = FirebaseDatabase.getInstance().getReference("CreatePoll");
            historyRef = FirebaseDatabase.getInstance().getReference("History");
            createdPollHistoryRef = FirebaseDatabase.getInstance().getReference("createdPollHistory");


            // get total
            createdPollHistoryRef.child(choice.getOptionsKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        totalVote.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            totalVote.add(ds.toString());
                        }

                        if (totalVote.size() != 0) {

                            String totalVoteCount = String.valueOf(totalVote.size());

                            binding.voteP.setText(totalVoteCount);
                        }


                    } else {
                        binding.voteP.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });






        }
    }


}
