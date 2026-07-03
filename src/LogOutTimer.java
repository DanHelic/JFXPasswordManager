package com.pwmanager.pwmanager;
import javafx.application.Platform;

import static com.pwmanager.pwmanager.Main.*;

public class LogOutTimer extends Thread{

    boolean stop;
    long startTime;
    public LogOutTimer(){
        stop=false;
        if(settingFile.autoLogoutTime !=0)start();
    }

    public void stopTimer(){
        stop=true;
    }

    public void run(){
        try{
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime< settingFile.autoLogoutTime *1000&&!stop){
                sleep(20);
            }
            if(!stop){
                Platform.runLater(new Runnable(){ public void run() { //copy Username
                    mainClass.logOut();
                }});
            }
            stop=true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
