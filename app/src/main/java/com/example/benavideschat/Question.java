package com.example.benavideschat;

public class Question {
    private String question;
    private boolean isOpen;
    private String[] answers;
    private int jumpTo = -1;
    private String externalMessageFormat;
    private String type = "undefined";

    public Question(String question, boolean isOpen, String[] answers, String externalMessageFormat, int jumpTo) {
        this.question = question;
        this.isOpen = isOpen;
        this.answers = answers;
        this.externalMessageFormat = externalMessageFormat;
        this.jumpTo = jumpTo;
    }

    public Question(String question, boolean isOpen, String[] answers, String externalMessageFormat) {
        this.question = question;
        this.isOpen = isOpen;
        this.answers = answers;
        this.externalMessageFormat = externalMessageFormat;
    }

    public Question(String question, boolean isOpen, String externalMessageFormat) {
        this.question = question;
        this.isOpen = isOpen;
        this.externalMessageFormat = externalMessageFormat;
        this.type = type;
    }

    public Question(String question, boolean isOpen, String[] answers, String externalMessageFormat, int jumpTo, String type) {
        this.question = question;
        this.isOpen = isOpen;
        this.answers = answers;
        this.externalMessageFormat = externalMessageFormat;
        this.jumpTo = jumpTo;
        this.type = type;
    }

    public Question(String question, boolean isOpen, String[] answers, String externalMessageFormat, String type) {
        this.question = question;
        this.isOpen = isOpen;
        this.answers = answers;
        this.externalMessageFormat = externalMessageFormat;
        this.type = type;
    }

    public Question(String question, boolean isOpen, String externalMessageFormat, String type) {
        this.question = question;
        this.isOpen = isOpen;
        this.externalMessageFormat = externalMessageFormat;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(int jumpTo) {
        this.jumpTo = jumpTo;
    }

    public String getExternalMessageFormat() {
        return externalMessageFormat;
    }

    public void setExternalMessageFormat(String externalMessageFormat) {
        this.externalMessageFormat = externalMessageFormat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
