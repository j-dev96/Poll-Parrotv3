package com.noname.PollParrot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.noname.PollParrot.databinding.HistoryLayoutAdapterBinding;
import com.noname.PollParrot.dataclass.QuestionVote;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class HistoryVoteAdapter extends RecyclerView.Adapter<HistoryVoteAdapter.ViewHolder> {

    private final Context context;
    ArrayList<QuestionVote> createdOrderList;

    private HistoryVoteAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onSendDataClick(int position);

    }

    public void setOnItemClickListener(HistoryVoteAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public HistoryVoteAdapter(Context context, ArrayList<QuestionVote> createdOrderList) {

        this.context = context;
        this.createdOrderList = createdOrderList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        HistoryLayoutAdapterBinding binding = HistoryLayoutAdapterBinding
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        QuestionVote exercise = createdOrderList.get(position);
        holder.bind(exercise);

    }


    @Override
    public int getItemCount() {
        return createdOrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final HistoryLayoutAdapterBinding binding;

        public ViewHolder(HistoryLayoutAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(QuestionVote exercise) {
            binding.timeTv.setText(getTime(exercise.getTimestamp()));
            binding.dateTv.setText(getDate(exercise.getTimestamp()));
            binding.voteQuestion.setText(exercise.getQuestionVote());
            binding.answer.setText(exercise.getQuestionVoteAnswer());
        }
    }

    public static String getDate(long timestamp) {
        try {
            Date netDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return sfd.format(netDate);
        } catch (Exception e) {
            return "date";
        }
    }

    public static String getTime(long timestamp) {
        try {
            Date netDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sfd.format(netDate);
        } catch (Exception e) {
            return "date";
        }
    }



}
