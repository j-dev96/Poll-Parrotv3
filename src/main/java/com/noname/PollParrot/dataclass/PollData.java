package com.noname.PollParrot.dataclass;

public class PollData {

    String VoteId;
    int choiceCount;
    String pin;
    String questionVote;
    String questionVoteAnswer;
    long timestamp;
    String token_id;
    String pollCreatorId;


    public PollData() {
    }

    public PollData(String voteId, int choiceCount, String pin, String questionVote, String questionVoteAnswer, long timestamp, String token_id, String pollCreatorId) {
        VoteId = voteId;
        this.choiceCount = choiceCount;
        this.pin = pin;
        this.questionVote = questionVote;
        this.questionVoteAnswer = questionVoteAnswer;
        this.timestamp = timestamp;
        this.token_id = token_id;
        this.pollCreatorId = pollCreatorId;
    }

    public String getPollCreatorId() {
        return pollCreatorId;
    }

    public void setPollCreatorId(String pollCreatorId) {
        this.pollCreatorId = pollCreatorId;
    }

    public String getVoteId() {
        return VoteId;
    }

    public void setVoteId(String voteId) {
        VoteId = voteId;
    }

    public int getChoiceCount() {
        return choiceCount;
    }

    public void setChoiceCount(int choiceCount) {
        this.choiceCount = choiceCount;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }
}
