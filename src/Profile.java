package com.pwmanager.pwmanager;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.io.*;
import org.mindrot.jbcrypt.BCrypt;

import static com.pwmanager.pwmanager.Main.*;


public class Profile extends HBox {
    boolean chosen; //if this one is highlighted
    int profileNumber; //the number of the profile (to get the file from profiles)
    public void setChosen(boolean chose){ //set color and chosen
        chosen=chose;
        if(chose){
            setStyle("-fx-background-color: "+settingFile.mMColor2+";");
        }
        else{
            setStyle("-fx-background-color:"+settingFile.mMColor1+";");
        }
    }

    public int getNumber(){return profileNumber;} //get number for file in profiles

    public boolean getChosen(){return chosen;} //if this profile is chosen

    public Profile(String nom,String picUrl,int num){ //profileName, ProfilePicture, ProfileNumber
        profileNumber =num;
        chosen=false;
        setStyle("-fx-background-color:"+settingFile.mMColor1+";");
        ImageView profilePic=ioS.tryGetImage(picUrl);
        setPadding(new Insets(4,4,4,4));
        profilePic.setPreserveRatio(true);
        profilePic.setFitHeight(50);


        Label profileNameL = new Label(nom,profilePic);
        profileNameL.setTextFill(Color.web(settingFile.textColor1));
        profileNameL.setContentDisplay(ContentDisplay.LEFT);
        profileNameL.setFont(new Font(16));
        profileNameL.setWrapText(true);

        getChildren().addAll(profilePic,profileNameL);
        setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, new CornerRadii(2),BorderWidths.DEFAULT)));

    }

    boolean checkPassword(File profile, String s){ //check if Password s is correct
        try{
            BufferedReader file =new BufferedReader(new FileReader(profile.getAbsolutePath()));
            String firstLine = file.readLine();
            String secondLine = file.readLine();
            if(firstLine.length()>25) {
                System.out.println("Conversion to V2 triggered");
                file.close();
                if(firstLine.equals(enDeCrypt.doSha256(s))) return convertOldHash(profile, s);
                return false;
            }
            if(BCrypt.checkpw(s, secondLine)){ //Read hash in first line
                file.close();
                return true;
            }
            else{
                //System.out.println("Password wrong!");
            }
            file.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    boolean convertOldHash(File profile, String s){
        try{
            ioS.updateToV2(profile, s);
            return checkPassword(profile, s);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return checkPassword(profile, s);
    }

}
