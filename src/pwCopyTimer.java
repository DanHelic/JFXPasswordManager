package com.pwmanager.pwmanager;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;

import static com.pwmanager.pwmanager.Main.*;

public class pwCopyTimer extends Thread{
    Password pwObject;
    boolean stop, pressedTab,altActive;
    String profilePW;
    NativeKeyListener keyListener;

    public pwCopyTimer(Password pwOb,String pw){
        if (mainClass.activeClipboardTimers==null){
            profilePW=pw;
            pwObject=pwOb;
            stop=false;
            pressedTab=false;
            sem=new AdditiveSemaphore(0);
            keyListener=new NativeKeyListener() {
                @Override
                public void nativeKeyPressed(NativeKeyEvent nativeEvent) { //or pressed
                    if(nativeEvent.getRawCode()==KeyCode.TAB.getCode()){
                        if(!altActive)pressedTab=true;
                    }
                    if(nativeEvent.getRawCode()==164){
                        altActive=true;
                    }
                }
                @Override
                public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
                    if(nativeEvent.getRawCode()==164){
                        altActive=false;
                    }
                }
            };
            GlobalScreen.addNativeKeyListener(keyListener);
            mainClass.activeClipboardTimers=this;
            this.start();
        }
    }


    Object contentBefore;
    DataFormat contentBeforeFormat;
    public void getContentBefore(){ //sadly only text works (no pictures)
        Platform.runLater(new Runnable(){ public void run() { //get content before
            boolean found=false;
            for(DataFormat df: mainClass.fxCB.getContentTypes()){
                if(mainClass.fxCB.hasContent(df)&&!found){
                    contentBefore=mainClass.fxCB.getContent(df);
                    contentBeforeFormat=df;
                    found=true;
                }
                System.out.println(df);
            }
            sem.v();
        }});
    }

    public void setContentBefore(){
        Platform.runLater(new Runnable(){ public void run() {
            ClipboardContent content = new ClipboardContent();
            content.putIfAbsent(contentBeforeFormat,contentBefore);
            if(contentBeforeFormat!=null)mainClass.fxCB.setContent(content);
        }});
    }

    AdditiveSemaphore sem;
    public void run(){
        try{
            long startTime;
            if(settingFile.saveClipBoard){
                getContentBefore();
            }
            else{
                sem.v();
            }

            if(!stop&&!pwObject.username.equals("")){
                Platform.runLater(new Runnable(){ public void run() { //copy Username
                    sem.p(); //to be sure that the CB-content before has been saved if wished
                    ClipboardContent content = new ClipboardContent();
                    content.putString(pwObject.username);
                    mainClass.fxCB.setContent(content);
                }});
            }
            mainClass.loggedInPane.clipboardStatusPane.setStatus(1);

            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime< settingFile.pwNoCopyTime*1000&&!stop&&!pressedTab&&!pwObject.username.equals("")){
                sleep(20);
            }

            if(!stop){
                Platform.runLater(new Runnable(){ public void run() { //copy Password
                    ClipboardContent content = new ClipboardContent();
                    content.putString(enDeCryptV2.decrypt(profilePW,pwObject.password));
                    mainClass.fxCB.setContent(content);
                }});
            }
            mainClass.loggedInPane.clipboardStatusPane.setStatus(2);

            startTime = System.currentTimeMillis();
            while((System.currentTimeMillis()-startTime<settingFile.pwDeleteClipboardTime*1000)&&!stop){
                sleep(20);
            }

            mainClass.loggedInPane.clipboardStatusPane.setStatus(0);
            if(settingFile.saveClipBoard){
                setContentBefore();
            }
            else{
                Platform.runLater(new Runnable(){ public void run() {
                    mainClass.fxCB.clear();
                }});
            }
            pwObject.setReady(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        mainClass.activeClipboardTimers=null;
        GlobalScreen.removeNativeKeyListener(keyListener);
    }
}
