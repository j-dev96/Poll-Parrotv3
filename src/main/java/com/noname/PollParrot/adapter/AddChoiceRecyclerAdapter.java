package com.noname.PollParrot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noname.PollParrot.databinding.CustomLayoutAddQuestionsListBinding;
import com.noname.PollParrot.dataclass.Choice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class AddChoiceRecyclerAdapter extends RecyclerView.Adapter<AddChoiceRecyclerAdapter.ViewHolder> {

    private final Context context;
    ArrayList<Choice> choiceList;

    private AddChoiceRecyclerAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);

        void onEditClick(int position);
    }

    public void setOnItemClickListener(AddChoiceRecyclerAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public AddChoiceRecyclerAdapter(Context context, ArrayList<Choice> choiceList) {

        this.context = context;
        this.choiceList = choiceList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        CustomLayoutAddQuestionsListBinding binding = CustomLayoutAddQuestionsListBinding
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
        private final CustomLayoutAddQuestionsListBinding binding;

        public ViewHolder(CustomLayoutAddQuestionsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.cancelButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onDeleteClick(position);

                }
            });
            binding.editButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onEditClick(position);

                }
            });
        }

        public void bind(Choice choice) {
            binding.choiceTv.setText(choice.getChoiceQuestion());

        }
    }


}
