package com.gmail.carlos.map524_assignment1_carlosfalconett;

public class Quiz {

    int question_;
    boolean answer;



    public Quiz(int question, boolean option){
        question_ = question;
        answer = option;

    }

    public int showQuestion(){
        return this.question_;
    }

    public boolean showAnswer(){
        return this.answer;
    }

}
