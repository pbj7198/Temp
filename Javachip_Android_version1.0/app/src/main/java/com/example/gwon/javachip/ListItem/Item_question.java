package com.example.gwon.javachip.ListItem;

/**
 * Created by gwon on 2016-06-21.
 */
public class Item_question {
    String questionTitle;
    String questionWriter;
    String questionContents;
    String qid;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuestionContents() {
        return questionContents;
    }

    public void setQuestionContents(String questionContents) {
        this.questionContents = questionContents;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionWriter() {
        return questionWriter;
    }

    public void setQuestionWriter(String questionWriter) {
        this.questionWriter = questionWriter;
    }
}
