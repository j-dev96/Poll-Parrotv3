package com.noname.PollParrot.dataclass;

public class Choice {

    String choiceQuestion, optionsKey;

    public Choice(){}


    public Choice(String choiceQuestion) {
        this.choiceQuestion = choiceQuestion;
    }

    public Choice(String choiceQuestion, String optionsKey) {
        this.choiceQuestion = choiceQuestion;
        this.optionsKey = optionsKey;
    }

    public String getChoiceQuestion() {
        return choiceQuestion;
    }

    public void setChoiceQuestion(String choiceQuestion) {
        this.choiceQuestion = choiceQuestion;
    }

    public String getOptionsKey() {
        return optionsKey;
    }

    public void setOptionsKey(String optionsKey) {
        this.optionsKey = optionsKey;
    }
}
