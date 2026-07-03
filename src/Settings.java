package com.pwmanager.pwmanager;
import javafx.stage.Screen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import static com.pwmanager.pwmanager.Main.*;

public class Settings {
    //load and save Settings
    //saved Settings:
    double xPos, yPos; //latest Position of the pwManager
    double xWidth,yHeight; //latest xWidth/yHeight used
    String lastChosen; //last Profile used
    String reservedName; //a reserved name to create tmp files
    int langInt; //language int
    int pwNoCopyTime; //time until password cant be copied
    int pwDeleteClipboardTime; //time until password deleted from Clipboard
    int autoLogoutTime; //Auto logout time
    boolean saveClipBoard; //recover Clipboard from before copying smth
    boolean showUsernames; //show usernames when logged in
    boolean darkMode; //if Dark Mode is enabled
    boolean createBackup; //if the Programm creates backups of Profiles
    String backupFolder, picFolder, profileFolder;
    String saveTextColor1,saveTextColor2,saveMMColor1, saveMMColor2,saveLIColor1,saveLIColor2,savePWPanelColor1,savePWPanelColor2;
    String textColor1, textColor2, mMColor1, mMColor2, lIColor1, lIColor2, pWPanelColor1, pWPanelColor2;
    String saveDMPanelColor1,saveDMPanelColor2;
    // the save strings are for the non dark-Mode colors in the settingsFile
    // the other ones are the ones that are actually used



    public Settings(){ //load
        reservedName="11111tmpFOkiDoki";
        backupFolder=new java.io.File(".").getAbsolutePath()+"\\ProfileBackups\\";
        picFolder=new java.io.File("").getAbsolutePath()+"\\Pictures\\";
        profileFolder=new java.io.File("").getAbsolutePath()+"\\Profiles\\";
    }

    void checkBounds(){
        boolean isInBounds=false;
        boolean changedX=false;
        List<Screen> screens = Screen.getScreens();
        while(!isInBounds){
            for(Screen s:screens){ //check every screen if window is visible
                if(xPos>s.getBounds().getMinX()&&xPos<s.getBounds().getMaxX()&&yPos>s.getBounds().getMinY()&&yPos<s.getBounds().getMaxY()){
                    isInBounds=true;
                }
            }
            if(!isInBounds){ //if not in bounds change x, if still not in bounds change y
                System.out.println("Was faulty");
                if(!changedX){
                    xPos=500;
                    changedX=true;
                }
                else{
                    yPos=300;
                    isInBounds=true;
                }
            }
        }
        if(xWidth>1900||xWidth<400)xWidth=700;
        if(yHeight>900||yHeight<250)yHeight=480;
    }

    public void setDarkMode(boolean dark){
        darkMode=dark;
        if(dark){
            textColor1 = "#E0E0E0";
            textColor2 = "#E0E0E0";
            mMColor1 = "#36393F";
            mMColor2 = "#2C2D32";
            lIColor1 = "#36393F";
            lIColor2 = "#2C2D32";
            pWPanelColor1 = saveDMPanelColor1;
            pWPanelColor2 = saveDMPanelColor2;
        }
        else{
            textColor1 = saveTextColor1;
            textColor2 = saveTextColor2;
            mMColor1 = saveMMColor1;
            mMColor2 = saveMMColor2;
            lIColor1 = saveLIColor1;
            lIColor2 = saveLIColor2;
            pWPanelColor1 = savePWPanelColor1;
            pWPanelColor2 = savePWPanelColor2;
        }

    }

    public void saveSettings(){
        File settingFile = new File("Settings.txt");
        try{
            ioS.backupFile(settingFile);
            settingFile.createNewFile();
            String path = settingFile.getAbsolutePath().substring(0,settingFile.getAbsolutePath().length()-settingFile.getName().length());
            File newFile = new File(path+reservedName);
            newFile.createNewFile();
            FileWriter fileWriter = new FileWriter(newFile.getPath());

            fileWriter.write(xPos+"\n"); //double
            fileWriter.write(yPos+"\n"); //double
            fileWriter.write(xWidth+"\n"); //double
            fileWriter.write(yHeight+"\n"); //double
            fileWriter.write(lastChosen+"\n"); //String
            fileWriter.write(langInt +"\n"); //int
            fileWriter.write(pwNoCopyTime+"\n"); //int
            fileWriter.write(pwDeleteClipboardTime+"\n"); //int
            fileWriter.write(saveClipBoard+"\n"); //boolean
            fileWriter.write(showUsernames+"\n"); //boolean
            fileWriter.write(darkMode+"\n"); //boolean
            fileWriter.write(createBackup+"\n"); //boolean
            fileWriter.write(autoLogoutTime +"\n"); //int
            fileWriter.write(saveTextColor1+"\n"); //String
            fileWriter.write(saveTextColor2+"\n"); //String
            fileWriter.write(saveMMColor1+"\n"); //String
            fileWriter.write(saveMMColor2+"\n"); //String
            fileWriter.write(saveLIColor1+"\n"); //String
            fileWriter.write(saveLIColor2+"\n"); //String
            fileWriter.write(savePWPanelColor1+"\n"); //String
            fileWriter.write(savePWPanelColor2+"\n"); //String
            fileWriter.write(saveDMPanelColor1+"\n"); //String
            fileWriter.write(saveDMPanelColor2+"\n"); //String

            fileWriter.flush();
            fileWriter.close();
            settingFile.delete();
            newFile.renameTo(settingFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getSettings(){ //else = default
        boolean success = false; //create Settings if error while reading them
        File settingFile = new File("Settings.txt");
        try{
            settingFile.createNewFile();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try(BufferedReader sFile =new BufferedReader(new FileReader(settingFile.getPath()));){
            String s;

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0)))xPos=Double.parseDouble(s);
            else xPos=550;

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0)))yPos=Double.parseDouble(s);
            else yPos=275;

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0)))xWidth=Double.parseDouble(s);
            else xWidth=790;

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0)))yHeight=Double.parseDouble(s);
            else yHeight=505;

            s = sFile.readLine();
            if(s!=null)lastChosen=s;
            else lastChosen="no Profile";

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0))) langInt =Integer.parseInt(s);
            else langInt =0;

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0)))pwNoCopyTime=Integer.parseInt(s);
            else pwNoCopyTime=10;

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0)))pwDeleteClipboardTime=Integer.parseInt(s);
            else pwDeleteClipboardTime=10;

            s = sFile.readLine();
            if(s!=null)saveClipBoard=Boolean.parseBoolean(s);
            else saveClipBoard = true;

            s = sFile.readLine();
            if(s!=null)showUsernames=Boolean.parseBoolean(s);
            else showUsernames=true;

            s = sFile.readLine();
            if(s!=null)darkMode=Boolean.parseBoolean(s);
            else darkMode=true;

            s = sFile.readLine();
            if(s!=null)createBackup=Boolean.parseBoolean(s);
            else createBackup=true;

            s = sFile.readLine();
            if(s!=null&&Character.isDigit(s.charAt(0))) autoLogoutTime =Integer.parseInt(s);
            else autoLogoutTime =300;

            s = sFile.readLine();
            if(s!=null)saveTextColor1=s;
            else saveTextColor1="#000000";

            s = sFile.readLine();
            if(s!=null)saveTextColor2=s;
            else saveTextColor2="#000000";

            s = sFile.readLine();
            if(s!=null)saveMMColor1=s;
            else saveMMColor1="#83D6FF";

            s = sFile.readLine();
            if(s!=null)saveMMColor2=s;
            else saveMMColor2="#F3F3F3";

            s = sFile.readLine();
            if(s!=null)saveLIColor1=s;
            else saveLIColor1="#83D6FF";

            s = sFile.readLine();
            if(s!=null)saveLIColor2=s;
            else saveLIColor2="#F3F3F3";

            s = sFile.readLine();
            if(s!=null)savePWPanelColor1=s;
            else savePWPanelColor1="#FF8383";

            s = sFile.readLine();
            if(s!=null)savePWPanelColor2=s;
            else savePWPanelColor2="#FF7070";

            s = sFile.readLine();
            if(s!=null)saveDMPanelColor1=s;
            else saveDMPanelColor1="#FF7F50";

            s = sFile.readLine();
            if(s!=null)saveDMPanelColor2=s;
            else saveDMPanelColor2="#FA8072";

            setDarkMode(darkMode);
            if(s!=null) success=true;
        }
        catch(Exception e){
            e.printStackTrace();
            success=false;
        }
        if(!success)saveSettings();
        checkBounds();
    }

}
