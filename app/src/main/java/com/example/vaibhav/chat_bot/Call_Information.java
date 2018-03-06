package com.example.vaibhav.chat_bot;

/**
 * Created by vaibhav on 2/2/18.
 */

public class Call_Information {
    public String name;
    public String number;


    public Call_Information(){

    }

    public Call_Information(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
