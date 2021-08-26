package com.noname.PollParrot.dataclass;

public class QuestionVote {
    String token_id,VoteId,questionVote,questionVoteAnswer;
    long timestamp;

    public QuestionVote() {
    }

    public QuestionVote(String token_id, String voteId, String questionVote, String questionVoteAnswer, long timestamp) {
        this.token_id = token_id;
        VoteId = voteId;
        this.questionVote = questionVote;
        this.questionVoteAnswer = questionVoteAnswer;
        this.timestamp = timestamp;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getVoteId() {
        return VoteId;
    }

    public void setVoteId(String voteId) {
        VoteId = voteId;
    }

    public String getQuestionVote() {
        return questionVote;
    }

    public void setQuestionVote(String questionVote) {
        this.questionVote = questionVote;
    }

    public String getQuestionVoteAnswer() {
        return questionVoteAnswer;
    }

    public void setQuestionVoteAnswer(String questionVoteAnswer) {
        this.questionVoteAnswer = questionVoteAnswer;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
