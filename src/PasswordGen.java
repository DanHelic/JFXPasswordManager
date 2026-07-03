package com.pwmanager.pwmanager;
import java.util.ArrayList;

public class PasswordGen {
    ArrayList<Character> charList;

    public PasswordGen(){
        charList = new ArrayList<Character>();
        for(int i=0;i<26;i++){
            charList.add((char)(i+65));
        }
        for(int i=0;i<26;i++){
            charList.add((char)(i+97));
        }
        for(int i=0;i<10;i++){
            charList.add((char)(i+48));
        }
        int add [] = {33,35,36,37,38,42,43,45,47,61,63,95};
        for(int i=0;i<add.length;i++){
            charList.add((char)(add[i]));
        }
    }

    public String genPassword(int length){
        String randomPassword="";
        for(int i=0;i<length;i++){
            randomPassword = randomPassword+charList.get((int)(Math.random()*charList.size()));
        }
        return randomPassword;
    }


}
