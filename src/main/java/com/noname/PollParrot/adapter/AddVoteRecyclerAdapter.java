package com.noname.PollParrot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noname.PollParrot.databinding.CustomLayoutVoteListBinding;
import com.noname.PollParrot.dataclass.Choice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class AddVoteRecyclerAdapter extends RecyclerView.Adapter<AddVoteRecyclerAdapter.ViewHolder> {

    private final Context context;
    ArrayList<Choice> choiceList;

    private AddVoteRecyclerAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onSendDataClick(int position);

    }

    public void setOnItemClickListener(AddVoteRecyclerAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemClickListeners(AddVoteRecyclerAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public AddVoteRecyclerAdapter(Context context, ArrayList<Choice> choiceList) {

        this.context = context;
        this.choiceList = choiceList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        CustomLayoutVoteListBinding binding = CustomLayoutVoteListBinding
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
        private final CustomLayoutVoteListBinding binding;

        public ViewHolder(CustomLayoutVoteListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.radio.setOnClickListener(v -> {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                     mListener.onSendDataClick(position);
                }


            }); // not






        }

        public void bind(Choice choice) {
            binding.choiceTv.setText(choice.getChoiceQuestion());

        }
    }


}
