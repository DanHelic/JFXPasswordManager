package com.pwmanager.pwmanager;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.github.kwhat.jnativehook.*;
import com.github.kwhat.jnativehook.keyboard.*;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main extends Application {
    public static Main mainClass;
    public static Language lang;
    public static EnAnDecryption enDeCrypt;
    public static EnAnDecryptionV2 enDeCryptV2;
    public static Settings settingFile;
    public static IOStuff ioS;
    public LogInScreenPane logInPane;
    public LoggedInPane loggedInPane;
    public Clipboard fxCB;
    public pwCopyTimer activeClipboardTimers;
    public Stage addEditStages;
    public LogOutTimer logOutTimer;
    public Stage mainStage;
    public Scene scene;
    boolean aPressed;
    boolean cPressed;
    ArrayList<Stage> autoClickers;
    EventHandler<KeyEvent> autoClickerEvent;
    //To have them Available everywhere need to: import static com.pwmanager.pwmanager.Main.*;

    /*
    someday when im bored:
    Image/other Data encryption
    auto clicker
     */



    @Override
    public void start(Stage stage) throws IOException {
        try{GlobalScreen.registerNativeHook();}
        catch(Exception e){e.printStackTrace();}

        //Initiations
        autoClickers=new ArrayList<Stage>();
        addEditStages=new Stage();
        fxCB = Clipboard.getSystemClipboard();
        enDeCrypt = new EnAnDecryption();
        enDeCryptV2 = new EnAnDecryptionV2();
        mainClass=this;
        mainStage =stage;
        ioS = new IOStuff();
        settingFile=new Settings();
        settingFile.getSettings();
        lang=new Language(0); //0 = deutsch, 1 = Englisch, 2 = GIBBY

        logInPane = new LogInScreenPane();

        scene = new Scene(logInPane, settingFile.xWidth, settingFile.yHeight-1);
        try{stage.getIcons().add(new Image(new File("PwManager.png").getAbsolutePath()));}
        catch(Exception e){e.printStackTrace();}
        stage.setTitle("The PasswordManager");
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(200);
        stage.setMinWidth(350);
        stage.setX(settingFile.xPos);
        stage.setY(settingFile.yPos);
        stage.setHeight(settingFile.yHeight); //To Fix bug in Scrollpane

        stage.widthProperty().addListener(e->{
            settingFile.xWidth=stage.getWidth();
            if(logInPane!=null)logInPane.paintIt();
            if(loggedInPane!=null) loggedInPane.pwPane.paintIt();
        });
        stage.heightProperty().addListener(e->{
            settingFile.yHeight=stage.getHeight();
            if(logInPane!=null)logInPane.paintIt();
            if(loggedInPane!=null) loggedInPane.pwPane.paintIt();
        }); //tell LogIn pane width and height changes

        //to open Autoclicker
        aPressed=false;
        cPressed=false;
        autoClickerEvent = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()==KeyCode.A){
                    aPressed=true;
                }
                if(keyEvent.getCode()==KeyCode.C){
                    cPressed=true;
                }
                if(aPressed&&cPressed){
                    AutoClicker ac=new AutoClicker();
                    Stage x = new Stage();
                    x.setTitle(lang.getText(52));
                    Scene scene = new Scene(ac,335,325);
                    x.setX(mainStage.getX()+settingFile.xWidth/2-5-335/2);
                    x.setY(mainStage.getY()+settingFile.yHeight/2-5-325/2);
                    x.setScene(scene);
                    autoClickers.add(x);
                    x.show();
                }
            }
        };
        scene.setOnKeyPressed(autoClickerEvent);

        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeEvent) { //or pressed
                if(nativeEvent.getRawCode()==KeyCode.A.getCode()){
                    aPressed=false;
                }
                if(nativeEvent.getRawCode()==KeyCode.C.getCode()){
                    cPressed=false;
                }
            }
        });
        stage.setOnCloseRequest(e->{
            for(Stage s:autoClickers){s.close();}
            addEditStages.close();
        });
        //createManyPasswords(150,"manyPasswords","123");
    }

    private void createManyPasswords(int count,String profileName,String password){
        File profile = new File(settingFile.profileFolder,profileName+".pwp");
        try{
            profile.createNewFile(); //not case-sensitive
            FileWriter fr = new FileWriter(profile.getAbsolutePath());
            fr.write(enDeCryptV2.newPwHash(password)+"\n");
            File f = new File(settingFile.picFolder+"PwManager.png");
            f = new File(settingFile.picFolder+ioS.checkIfNewPic(f));
            fr.write(f.getName()+"\n"+"\n"+"\n");
            fr.flush();
            fr.close();
        }
        catch(Exception x){
            x.printStackTrace();
        }
        mainClass.logInPane.checkForProfiles();

        ArrayList<String>picNames=new ArrayList<String>();
        for(File f:logInPane.pictures){
            picNames.add(f.getName());
        }

        for(int i=0;i<count;i++){
            ioS.addPassword("Many_Profiles",""+i,""+i,picNames.get(i%picNames.size()),profile,"123");
        }
    }

    public void logIn(String pw, File profile){
        logOutTimer=new LogOutTimer();
        scene = new Scene(new LoggedInPane(pw,profile), mainStage.getWidth()-16, mainStage.getHeight());
        mainStage.setScene(scene);
        scene.setOnKeyPressed(autoClickerEvent);
        mainStage.setTitle("The PasswordManager - "+profile.getName().substring(0,profile.getName().length()-4));
        ioS.somethingChanged =true;
        addEditStages.close();
        logInPane=null;
    }

    public void logOut(){
        logInPane=new LogInScreenPane();
        scene = new Scene(logInPane, mainStage.getWidth()-16, mainStage.getHeight());
        mainStage.setScene(scene);
        scene.setOnKeyPressed(autoClickerEvent);
        mainStage.setTitle("The PasswordManager");
        addEditStages.close();
        if(activeClipboardTimers!=null)activeClipboardTimers.stop=true;
        logOutTimer.stopTimer();
        loggedInPane=null;
        ioS.somethingChanged=true;
    }

    @Override
    public void stop(){
        try{
            settingFile.yPos= mainStage.getY();
            settingFile.xPos= mainStage.getX();
            settingFile.saveSettings();
            if(activeClipboardTimers!=null)activeClipboardTimers.stop=true;
            if(logOutTimer!=null)logOutTimer.stopTimer();
            GlobalScreen.unregisterNativeHook();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public String timeString(){
        String timeStamp = new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss").format(new java.util.Date());
        return timeStamp;
    }

    public void newStage(Stage stage){
        addEditStages.close();
        addEditStages = stage;
        addEditStages.getIcons().add(new Image(new File("PwManager.png").getAbsolutePath()));
    }


    public static void main(String[] args) {launch();}
}
