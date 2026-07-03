package com.pwmanager.pwmanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.pwmanager.pwmanager.Main.*;
import static com.pwmanager.pwmanager.Main.lang;

public class EditProfilePane extends BorderPane {

    Stage thisStage;
    String picPath;
    List<File> profileListFiles, pictureListFiles;
    String ProfileNameS, pWFieldS1, pWFieldS2,cpWFOld;
    File chosenProfileFile;
    ArrayList<Profile> profilePanes;

    public EditProfilePane(List<File> profiles,List<File> pictures,Stage stage, ArrayList<Profile> profileObj){
        chosenProfileFile =profiles.get(profileObj.get(0).getNumber());
        profilePanes = (ArrayList<Profile>) profileObj.clone();
        for(Profile p:profileObj){
            if(p.getChosen()){
                chosenProfileFile =profiles.get(p.getNumber());
            }
        }
        thisStage=stage;
        profileListFiles = profiles;
        pictureListFiles = pictures;
        ProfileNameS = chosenProfileFile.getName().substring(0, chosenProfileFile.getName().length()-4);
        pWFieldS1 ="";
        pWFieldS2 ="";
        boolean DefaultPicFound=false;
        for(File f:pictures){
            if(f.getName().equals("NoProfilePic.png")){
                picPath=f.getAbsolutePath();
                DefaultPicFound=true;
            }
        }
        if(!DefaultPicFound)picPath="Default";


        String picNameS="";
        try{
            BufferedReader file =new BufferedReader(new FileReader(chosenProfileFile.getAbsolutePath()));
            picNameS =file.readLine();
            picNameS =file.readLine();
            file.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        for(File f: pictureListFiles){
            if(f.getName().equals(picNameS)){
                picPath=f.getAbsolutePath();
            }
        }
        paintIt();
    }

    void paintIt(){
        setStyle("-fx-background-color: "+settingFile.mMColor2);
        Insets insets = new Insets(5,5,5,5);
        this.setPadding(insets);

        //Later inserted into borderpane
        GridPane fieldGrid = new GridPane(); //this is at the left
        fieldGrid.setVgap(10);
        TextField username = new TextField(ProfileNameS);
        PasswordField pwOld = new PasswordField();
        pwOld.setText(cpWFOld);
        PasswordField pw1 = new PasswordField();
        pw1.setText(pWFieldS1);
        PasswordField pw2 = new PasswordField();
        pw2.setText(pWFieldS2);

        GridPane labelGrid = new GridPane(); //this is at the right
        labelGrid.setVgap(10);
        Font lFont = new Font(16);
        Label userN = new Label(lang.getText(12));
        userN.setTextFill(Color.web(settingFile.textColor1));
        userN.setContentDisplay(ContentDisplay.RIGHT);
        Label oldPwL = new Label(lang.getText(39));
        oldPwL.setTextFill(Color.web(settingFile.textColor1));
        oldPwL.setContentDisplay(ContentDisplay.RIGHT);
        Label pwl1 = new Label(lang.getText(37));
        pwl1.setTextFill(Color.web(settingFile.textColor1));
        pwl1.setContentDisplay(ContentDisplay.RIGHT);
        Label pwl2 = new Label(lang.getText(14));
        pwl2.setTextFill(Color.web(settingFile.textColor1));
        pwl2.setContentDisplay(ContentDisplay.RIGHT);
        userN.setFont(lFont);
        oldPwL.setFont(lFont);
        pwl1.setFont(lFont);
        pwl2.setFont(lFont);

        fieldGrid.add(username,1,0);
        fieldGrid.add(pwOld,1,1);
        fieldGrid.add(pw1,1,2);
        fieldGrid.add(pw2,1,3);
        labelGrid.add(userN,0,0);
        labelGrid.add(oldPwL,0,1);
        labelGrid.add(pwl1,0,2);
        labelGrid.add(pwl2,0,3);

        Button changeProfileButton = new Button(lang.getText(36));
        Button addPictureButton = new Button(lang.getText(38));
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(changeProfileButton,addPictureButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        ImageView pictureIV=ioS.tryGetImage(picPath);
        pictureIV.setFitWidth(50);
        pictureIV.setFitHeight(50);
        buttonBox.getChildren().add(pictureIV);

        //Error Labels
        Label notSameEL = new Label(lang.getText(15));
        notSameEL.setFont(new Font(16));
        notSameEL.setTextFill(Color.RED);
        notSameEL.setVisible(false);

        Label notFilledEL = new Label(lang.getText(40));
        notFilledEL.setFont(new Font(16));
        notFilledEL.setTextFill(Color.RED);
        notFilledEL.setVisible(false);

        Label profileAlreadyThereEL = new Label(lang.getText(17));
        profileAlreadyThereEL.setFont(new Font(16));
        profileAlreadyThereEL.setTextFill(Color.RED);
        profileAlreadyThereEL.setVisible(false);

        Label oldPwWrongEL = new Label(lang.getText(41));
        oldPwWrongEL.setFont(new Font(16));
        oldPwWrongEL.setTextFill(Color.RED);
        oldPwWrongEL.setVisible(false);

        VBox errorBox = new VBox();
        errorBox.getChildren().addAll(notSameEL,notFilledEL,profileAlreadyThereEL,oldPwWrongEL);

        changeProfileButton.setOnAction(e->{
            boolean anyError=false; //setting all errors not visible
            notSameEL.setVisible(false);
            notFilledEL.setVisible(false);
            profileAlreadyThereEL.setVisible(false);
            oldPwWrongEL.setVisible(false);

            //checking on errors
            if(!pw1.getText().equals(pw2.getText())){ //Pw dont match error
                notSameEL.setVisible(true);
                anyError=true;
            }
            if(username.getText().isEmpty()||username.getText().equals(settingFile.reservedName)){ //empty Username field error
                notFilledEL.setVisible(true);
                anyError=true;
            }
            for(File f: profileListFiles){ //Name taken error
                if(f.getName().substring(0,f.getName().length()-4).equals(username.getText())){
                    if(!chosenProfileFile.getName().substring(0, chosenProfileFile.getName().length()-4).equals(username.getText())){
                        profileAlreadyThereEL.setVisible(true);
                        anyError=true;
                    }
                }
            }
            for(Profile p:profilePanes) { //Wrong old Pw error
                if (p.getChosen() == true) {
                    if(pwOld.getText()==null){
                        anyError=true;
                        oldPwWrongEL.setVisible(true);
                    }
                    else{
                        if(p.checkPassword(chosenProfileFile,pwOld.getText())){
                        }
                        else{
                            anyError=true;
                            oldPwWrongEL.setVisible(true);
                        }
                    }
                }
            }

            if(!anyError){ //if no error, call changeProfile method
                try{
                    mainClass.ioS.backupFile(chosenProfileFile);
                    String newPw=pwOld.getText();//old pw or new entered pw if there is one
                    if(!pw1.getText().equals("")){
                        newPw=pw1.getText();
                    }
                    picPath=ioS.checkIfNewPic(new File(picPath));
                    mainClass.ioS.changeProfile(chosenProfileFile,username.getText(),newPw,new File(picPath),pwOld.getText());
                    mainClass.logInPane.checkForProfiles(); //refresh everything in Pane
                    thisStage.close();
                }
                catch(Exception x){
                    x.printStackTrace();
                }
            }
        });

        addPictureButton.setOnAction(e->{
            FileChooser fc = new FileChooser();
            fc.setTitle(lang.getText(19));
            fc.setInitialDirectory(pictureListFiles.get(0).getParentFile());
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File chosenOne = fc.showOpenDialog(new Stage());
            picPath=chosenOne.getAbsolutePath();
            ProfileNameS =username.getText();
            pWFieldS1 =pw1.getText();
            pWFieldS2 =pw2.getText();
            cpWFOld=pwOld.getText();
            paintIt();
        });

        BorderPane usernamePane = new BorderPane();
        usernamePane.setLeft(labelGrid);
        usernamePane.setRight(fieldGrid);
        this.setCenter(buttonBox);
        this.setTop(usernamePane);
        this.setBottom(errorBox);
        BorderPane.setMargin(buttonBox,insets);
        BorderPane.setMargin(usernamePane,insets);
        BorderPane.setMargin(errorBox,insets);

    }






}
