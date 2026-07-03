package com.pwmanager.pwmanager;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;

import static com.pwmanager.pwmanager.Main.*;

public class IOStuff {
    File profileBackupFile;
    ArrayList<Password> passWordList;
    boolean somethingChanged; //Performance (to not perform io Operations when window resizes)

    public IOStuff(){
        profileBackupFile = new File("ProfileBackups");
        if(!profileBackupFile.exists()) profileBackupFile.mkdir();
        somethingChanged =true;
    }

    public ArrayList<Password> getPwList(File prof,String pw){
        if(somethingChanged){ //only do if something has changed because of performance
            passWordList =getPasswords(prof,pw);
            somethingChanged =false;
        }
        return passWordList;
    }

    public ArrayList<Profile> getProfiles(List<File> profileFilesArray){
        ArrayList<Profile> returnList = new ArrayList<Profile>();
        File profileFile;
        String picName;
        BufferedReader bReader;
        for(int i=0;i<profileFilesArray.size();i++){
            profileFile = profileFilesArray.get(i);
            picName="";
            try{
                bReader =new BufferedReader(new FileReader(profileFile.getAbsolutePath()));
                picName=bReader.readLine();
                picName=bReader.readLine();
                picName=bReader.readLine();
                bReader.close();
            }
            catch (Exception e){e.printStackTrace();}
            picName=settingFile.picFolder+picName; //to picPath
            returnList.add(new Profile(profileFile.getName().substring(0,profileFile.getName().length()-4),picName,i));
        }
        return returnList;
    }

    public String checkIfNewPic(File pic){ //if picture in in pictures folder copy it there and check name for doubles
        String newPicName=pic.getName();
        if(!pic.getAbsolutePath().contains(settingFile.picFolder)){
            boolean gotName=false;
            int i=2;
            while(!gotName){
                gotName=true;
                for(File f:new File(settingFile.picFolder).listFiles()){ //look through all files in pictures
                    if(f.getName().equals(newPicName)){ //if pic with same name exists
                        gotName=false; //add number to the picname and recheck
                        newPicName=pic.getName().substring(0,pic.getName().length()-4)+(i++)+pic.getName().substring(pic.getName().length()-4,pic.getName().length());
                    }
                }
            }
            try{ //copy pic to pictureFolder
                Files.copy(pic.toPath(),new File(settingFile.picFolder+newPicName).toPath());
            }
            catch (Exception x){
                x.printStackTrace();
            }
        }
        return newPicName;
    }

    public ImageView tryGetImage(String url){ //if picture not found create image with "No image Found"
        try{
            return new ImageView(url);
        }
        catch (Exception e){
            //System.out.println("No pic found");
            //e.printStackTrace();
            //System.out.println(url);
        }
        WritableImage wImage = new WritableImage(50,50);
        Label label= new Label("No Image found");
        label.setWrapText(true);
        label.setFont(new Font(11));
        label.setMinSize(50,50);
        label.setMaxSize(50,50);
        label.setPrefSize(50,50);
        Scene imgScene = new Scene(new Group(label));
        imgScene.snapshot(wImage);
        ImageView retPic = new ImageView(wImage);
        return retPic;
    }

    public void changePassword(String userName, String passwordPW, String description, String picName, File profileFile, String profilePW, int passwordNumber){
        somethingChanged =true;
        try{
            backupFile(profileFile);
            File newFile = new File(profileFile.getParentFile()+"/"+settingFile.reservedName);
            newFile.createNewFile();
            BufferedReader oldFile =new BufferedReader(new FileReader(profileFile.getAbsolutePath()));
            FileWriter fr = new FileWriter(newFile.getAbsolutePath());
            fr.write(oldFile.readLine()+"\n");//Version
            fr.write(oldFile.readLine()+"\n");//Hash
            fr.write(oldFile.readLine()+"\n");//profilePic
            fr.write(oldFile.readLine()+"\n");//emptyLine

            for(int i=1;i<passwordNumber;i++){
                fr.write(oldFile.readLine()+"\n"); //username
                fr.write(oldFile.readLine()+"\n"); //password
                fr.write(oldFile.readLine()+"\n"); //description
                fr.write(oldFile.readLine()+"\n"); //pic
                fr.write(oldFile.readLine()+"\n"); //emptyLine
            }

            fr.write(enDeCryptV2.encrypt(profilePW,userName)+"\n");
            fr.write(enDeCryptV2.encrypt(profilePW,passwordPW)+"\n");
            fr.write(enDeCryptV2.encrypt(profilePW,description)+"\n");
            fr.write(enDeCryptV2.encrypt(profilePW,picName)+"\n");
            fr.write("\n");
            fr.flush();

            oldFile.readLine(); //delete username
            oldFile.readLine(); //delete password
            oldFile.readLine(); //delete description
            oldFile.readLine(); //delete pic
            oldFile.readLine(); //delete emptyLine

            String s=oldFile.readLine();
            while(s!=null){
                fr.write(s+"\n");
                s=oldFile.readLine();
            }
            fr.flush();
            oldFile.close();
            fr.close();
            profileFile.delete();
            newFile.renameTo(profileFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Password> getPasswords(File prof,String pw){
        ArrayList <Password> returnList= new ArrayList<Password>();
        String username,password,picUrl,description;
        try(BufferedReader file =new BufferedReader(new FileReader(prof.getAbsolutePath()))){
            String s;
            s=file.readLine(); //Version
            s=file.readLine(); //hash
            s=file.readLine(); //profilepic
            s=file.readLine(); //empty Line
            int passwordNumber=1;
            while(s!=null){
                if(!s.isEmpty()){
                    //s=file.readLine(); //username
                    username=enDeCryptV2.decrypt(pw,s);
                    s=file.readLine(); //password
                    password=s;
                    s=file.readLine(); //description
                    description = enDeCryptV2.decrypt(pw,s);
                    s=file.readLine(); //pic
                    picUrl= enDeCryptV2.decrypt(pw,s);
                    returnList.add(new Password(passwordNumber, description, username, picUrl, password));
                    passwordNumber++;
                }
                s=file.readLine(); //get empty line
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return returnList;
    }


    public void addPassword(String userName, String passwordPW, String description, String picName, File profileFile, String profilePW){
        somethingChanged =true;
        try{
            backupFile(profileFile);
            File newFile = new File(profileFile.getParentFile()+"/"+settingFile.reservedName);
            newFile.createNewFile();
            BufferedReader oldFile =new BufferedReader(new FileReader(profileFile.getAbsolutePath()));
            FileWriter fileWriter = new FileWriter(newFile.getAbsolutePath());
            fileWriter.write(oldFile.readLine()+"\n");//Version
            fileWriter.write(oldFile.readLine()+"\n");//Hash
            fileWriter.write(oldFile.readLine()+"\n");//profilePic
            fileWriter.write(oldFile.readLine()+"\n");//emptryLine
            //new pw:
            fileWriter.write(enDeCryptV2.encrypt(profilePW,userName)+"\n");
            fileWriter.write(enDeCryptV2.encrypt(profilePW,passwordPW)+"\n");
            fileWriter.write(enDeCryptV2.encrypt(profilePW,description)+"\n");
            fileWriter.write(enDeCryptV2.encrypt(profilePW,picName)+"\n");
            fileWriter.write("\n");
            fileWriter.flush();
            //other pw:
            String s=oldFile.readLine();
            while(s!=null){
                fileWriter.write(s+"\n");
                s=oldFile.readLine();
            }
            fileWriter.flush();

            oldFile.close();
            fileWriter.close();
            profileFile.delete();
            newFile.renameTo(profileFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void delPassword(int pwNumber, File profileFile){
        somethingChanged =true;
        try{
            backupFile(profileFile);
            File newFile = new File(profileFile.getParentFile()+"/"+settingFile.reservedName);
            newFile.createNewFile();
            BufferedReader oldFile =new BufferedReader(new FileReader(profileFile.getAbsolutePath()));
            FileWriter fileWriter = new FileWriter(newFile.getAbsolutePath());
            fileWriter.write(oldFile.readLine()+"\n");//Version
            fileWriter.write(oldFile.readLine()+"\n");//Hash
            fileWriter.write(oldFile.readLine()+"\n");//profilePic
            fileWriter.write(oldFile.readLine()+"\n");//emptryLine

            for(int i=1;i<pwNumber;i++){
                fileWriter.write(oldFile.readLine()+"\n"); //username
                fileWriter.write(oldFile.readLine()+"\n"); //password
                fileWriter.write(oldFile.readLine()+"\n"); //description
                fileWriter.write(oldFile.readLine()+"\n"); //pic
                fileWriter.write(oldFile.readLine()+"\n"); //emptyLine
            }
            oldFile.readLine(); //delete username
            oldFile.readLine(); //delete password
            oldFile.readLine(); //delete description
            oldFile.readLine(); //delete pic
            oldFile.readLine(); //delete emptyLine

            fileWriter.flush();
            String s=oldFile.readLine();
            while(s!=null){
                fileWriter.write(s+"\n");
                s=oldFile.readLine();
            }
            fileWriter.flush();

            oldFile.close();
            fileWriter.close();
            profileFile.delete();
            newFile.renameTo(profileFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void movePw(int oldNumber, int newNumber, File profileFile){
        somethingChanged =true;
        try{
            backupFile(profileFile);
            ArrayList<String> linesToMove= new ArrayList<>();
            ArrayList<String> newFileStrings= new ArrayList<>();
            int newFileStringsIndex=0;
            File newFile = new File(profileFile.getParentFile()+"/"+settingFile.reservedName);
            newFile.createNewFile();
            BufferedReader oldFile =new BufferedReader(new FileReader(profileFile.getAbsolutePath()));
            FileWriter fileWriter = new FileWriter(newFile.getAbsolutePath());
            fileWriter.write(oldFile.readLine()+"\n");//Version
            fileWriter.write(oldFile.readLine()+"\n");//Hash
            fileWriter.write(oldFile.readLine()+"\n");//profilePic
            fileWriter.write(oldFile.readLine()+"\n");//emptryLine

            for(int i=1;i<oldNumber;i++){
                newFileStrings.add(oldFile.readLine()+"\n"); //username
                newFileStrings.add(oldFile.readLine()+"\n"); //password
                newFileStrings.add(oldFile.readLine()+"\n"); //description
                newFileStrings.add(oldFile.readLine()+"\n"); //pic
                newFileStrings.add(oldFile.readLine()+"\n"); //emptyLine
            }

            linesToMove.add(oldFile.readLine()+"\n"); //delete username
            linesToMove.add(oldFile.readLine()+"\n"); //delete password
            linesToMove.add(oldFile.readLine()+"\n"); //delete description
            linesToMove.add(oldFile.readLine()+"\n"); //delete pic
            linesToMove.add(oldFile.readLine()+"\n"); //delete emptyLine

            String tmpString=oldFile.readLine();
            while(tmpString!=null){
                newFileStrings.add(tmpString+"\n");
                tmpString=oldFile.readLine();
            }

            for(int i=1;i<newNumber;i++){
                fileWriter.write(newFileStrings.get(newFileStringsIndex++)); //username
                fileWriter.write(newFileStrings.get(newFileStringsIndex++)); //password
                fileWriter.write(newFileStrings.get(newFileStringsIndex++)); //description
                fileWriter.write(newFileStrings.get(newFileStringsIndex++)); //pic
                fileWriter.write(newFileStrings.get(newFileStringsIndex++)); //emptyLine
            }
            fileWriter.flush();

            for(String s:linesToMove){
                fileWriter.write(s);
            }
            fileWriter.flush();

            while(newFileStringsIndex<newFileStrings.size()){
                fileWriter.write(newFileStrings.get(newFileStringsIndex++));
            }
            fileWriter.flush();

            oldFile.close();
            fileWriter.close();
            profileFile.delete();
            newFile.renameTo(profileFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }


    public boolean backupFile(File file){
        try{
            if(settingFile.createBackup){
                String backupFName = file.getName().substring(0,file.getName().length()-4)+mainClass.timeString()+".pwp";
                File backupFile = new File(profileBackupFile.getAbsolutePath(),backupFName);
                FileWriter backupFileWriter = new FileWriter(backupFile.getAbsolutePath());
                BufferedReader originalFileReader =new BufferedReader(new FileReader(file.getAbsolutePath()));
                String nextLine=originalFileReader.readLine();
                while(nextLine!=null){
                    backupFileWriter.write(nextLine);
                    backupFileWriter.flush();
                    nextLine=originalFileReader.readLine();
                    if(nextLine!=null){
                        backupFileWriter.write("\n");
                        backupFileWriter.flush();
                    }
                }
                originalFileReader.close();
                backupFileWriter.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean changeProfile(File file, String profileName, String profilePW, File picFile, String profilePWOld){
        somethingChanged =true;
        File tmpFile= new File(file.getParentFile()+"/"+profileName+settingFile.reservedName);
        try{
            FileWriter fileWriter = new FileWriter(tmpFile.getAbsolutePath());
            BufferedReader fileReader =new BufferedReader(new FileReader(file.getAbsolutePath()));
            fileWriter.write("V2.0"+"\n");
            fileWriter.write(enDeCryptV2.newPwHash(profilePW)+"\n");
            fileWriter.write(picFile.getName()+"\n");
            fileWriter.write("\n");
            fileWriter.flush();
            String nextLine=fileReader.readLine();
            nextLine=fileReader.readLine();
            nextLine=fileReader.readLine();
            nextLine=fileReader.readLine();
            nextLine=fileReader.readLine(); //first encrypted line
            if(nextLine!=null){ //if there are 0 passwords safed
                if(nextLine.equals(""))nextLine=null;
            }
            int isLineEmpty = 0;
            while(nextLine!=null){
                if(isLineEmpty%5==4){ //write empty line after 4 lines
                    isLineEmpty++;
                    fileWriter.write(nextLine);
                }
                else{
                    isLineEmpty++;
                    if(!nextLine.equals(""))
                    fileWriter.write(enDeCryptV2.encrypt(profilePW,enDeCryptV2.decrypt(profilePWOld,nextLine)));
                }
                fileWriter.flush();
                nextLine=fileReader.readLine();
                if(nextLine!=null){
                    fileWriter.write("\n");
                    fileWriter.flush();
                }
            }
            fileWriter.write("\n");
            fileWriter.flush();

            fileReader.close();
            fileWriter.close();
            String s = file.getAbsolutePath();
            try {
                Files.delete(Paths.get(file.getAbsolutePath()));
            } catch (Exception exy) {
                exy.printStackTrace();
            }
            file.delete();
            tmpFile.renameTo(new File(tmpFile.getAbsolutePath().substring(0,tmpFile.getAbsolutePath().length()-settingFile.reservedName.length())+".pwp"));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void updateToV2 (File file, String s) {
        try{
            backupFile(file);
            File newFile = new File(file.getParentFile()+"/"+settingFile.reservedName);
            newFile.createNewFile();
            BufferedReader oldFile =new BufferedReader(new FileReader(file.getAbsolutePath()));
            FileWriter fileWriter = new FileWriter(newFile.getAbsolutePath());
            fileWriter.write("V2.0"+"\n");//Version
            fileWriter.write(enDeCryptV2.newPwHash(s)+"\n");//Hash
            oldFile.readLine();//old pw
            fileWriter.write(oldFile.readLine()+"\n");//profilePic
            fileWriter.write(oldFile.readLine()+"\n");//emptyLine

            String nextLine = oldFile.readLine();
            while(nextLine!=null && !nextLine.isEmpty()){
                fileWriter.write(enDeCryptV2.encrypt(s, enDeCrypt.decrypt(s, nextLine))+"\n"); //username
                fileWriter.write(enDeCryptV2.encrypt(s, enDeCrypt.decrypt(s, oldFile.readLine()))+"\n"); //password
                fileWriter.write(enDeCryptV2.encrypt(s, enDeCrypt.decrypt(s, oldFile.readLine()))+"\n"); //description
                fileWriter.write(enDeCryptV2.encrypt(s, enDeCrypt.decrypt(s, oldFile.readLine()))+"\n"); //pic
                fileWriter.write(oldFile.readLine()+"\n"); //emptyLine

                nextLine = oldFile.readLine();
            }

            fileWriter.flush();

            oldFile.close();
            fileWriter.close();
            file.delete();
            newFile.renameTo(file);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
