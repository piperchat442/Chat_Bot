package com.example.vaibhav.chat_bot;

/**
 * Created by vaibhav on 16/12/17.
 */

public class Information {
    int type;
    String message;
    String time_message;

    String name;
    String no;

    public Information(){

    }

    public Information(int type, String message, String time_message) {
        this.type = type;
        this.message = message;
        this.time_message = time_message;
    }

    public Information(String name,String no){
        this.name=name;
        this.no=no;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime_message() {
        return time_message;
    }

    public void setTime_message(String time_message) {
        this.time_message = time_message;
    }
}
