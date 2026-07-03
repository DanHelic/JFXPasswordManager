package com.pwmanager.pwmanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

import static com.pwmanager.pwmanager.Main.*;

public class AddProfilePane extends BorderPane {

    Stage thisStage;
    String picPath;
    List<File> profileFileList, pictureFileList;
    String profileNameS, pWField1S, pWField2S;

    public AddProfilePane(List<File> profiles,List<File> pictures,Stage stage){
        thisStage=stage;
        profileFileList = profiles;
        pictureFileList = pictures;
        profileNameS ="";
        pWField1S ="";
        pWField2S ="";
        boolean DefaultPicFound=false;
        for(File file:pictures){
            if(file.getName().equals("NoProfilePic.png")){
                picPath=file.getAbsolutePath();
                DefaultPicFound=true;
            }
        }
        if(!DefaultPicFound)picPath="Default";
        paintIt();
    }

    void paintIt(){
        setStyle("-fx-background-color: "+settingFile.mMColor2);
        Insets insets = new Insets(5,5,5,5);
        this.setPadding(insets);
        Font lFont = new Font(16);

        //Later inserted into borderpane
        GridPane labelGrid = new GridPane(); //this is at the left
        labelGrid.setVgap(10);
        Label userNameL = new Label(lang.getText(12));
        userNameL.setTextFill(Color.web(settingFile.textColor1));
        userNameL.setContentDisplay(ContentDisplay.RIGHT);
        Label pWFieldL1 = new Label(lang.getText(13));
        pWFieldL1.setTextFill(Color.web(settingFile.textColor1));
        pWFieldL1.setContentDisplay(ContentDisplay.RIGHT);
        Label pWFieldL2 = new Label(lang.getText(14));
        pWFieldL2.setTextFill(Color.web(settingFile.textColor1));
        pWFieldL2.setContentDisplay(ContentDisplay.RIGHT);
        userNameL.setFont(lFont);
        pWFieldL1.setFont(lFont);
        pWFieldL2.setFont(lFont);

        GridPane fieldGrid = new GridPane(); //this is at the right
        fieldGrid.setVgap(10);
        TextField userNameTF = new TextField(profileNameS);
        PasswordField pwField1 = new PasswordField();
        pwField1.setText(pWField1S);
        PasswordField pwField2 = new PasswordField();
        pwField2.setText(pWField2S);

        fieldGrid.add(userNameTF,1,0);
        fieldGrid.add(pwField1,1,1);
        fieldGrid.add(pwField2,1,2);
        labelGrid.add(userNameL,0,0);
        labelGrid.add(pWFieldL1,0,1);
        labelGrid.add(pWFieldL2,0,2);

        Button addProfileButton = new Button(lang.getText(2)); //the 2 buttons
        Button addPictureButton = new Button(lang.getText(18));
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(addProfileButton,addPictureButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        ImageView pictureIV=ioS.tryGetImage(picPath); //setting up picture display
        pictureIV.setFitWidth(50);
        pictureIV.setFitHeight(50);
        buttonBox.getChildren().add(pictureIV);

        //Error Labels
        Label notSameEL = new Label(lang.getText(15)); //error Label 1
        notSameEL.setFont(new Font(16));
        notSameEL.setTextFill(Color.RED);
        notSameEL.setVisible(false);

        Label notFilledEL = new Label(lang.getText(16)); //error Label 2
        notFilledEL.setFont(new Font(16));
        notFilledEL.setTextFill(Color.RED);
        notFilledEL.setVisible(false);

        Label profileAlreadyThereEL = new Label(lang.getText(17)); //error Label 3
        profileAlreadyThereEL.setFont(new Font(16));
        profileAlreadyThereEL.setTextFill(Color.RED);
        profileAlreadyThereEL.setVisible(false);

        VBox errorBox = new VBox(); //Box of errors
        errorBox.getChildren().addAll(notSameEL,notFilledEL,profileAlreadyThereEL);

        addProfileButton.setOnAction(e->{
            boolean anyError=false; //setting all errors not visible
            notSameEL.setVisible(false);
            notFilledEL.setVisible(false);
            profileAlreadyThereEL.setVisible(false);

            //checking on errors
            if(!pwField1.getText().equals(pwField2.getText())){ //if Passwords are not the same
                notSameEL.setVisible(true);
                anyError=true;
            }
            if(userNameTF.getText().isEmpty()||pwField1.getText().isEmpty()||pwField2.getText().isEmpty()){
                notFilledEL.setVisible(true); //if any Field is empty
                anyError=true;
            }
            for(File f: profileFileList){ //if a profile with the name already exists
                if(f.getName().substring(0,f.getName().length()-4).equals(userNameTF.getText())){
                    profileAlreadyThereEL.setVisible(true);
                    anyError=true;
                }
            }

            if(!anyError){ //if there is no error -> create new Profile
                File profile = new File(settingFile.profileFolder,userNameTF.getText()+".pwp");
                try{
                    profile.createNewFile(); //not case-sensitive
                    FileWriter fr = new FileWriter(profile.getAbsolutePath());
                    fr.write("V2.0"+"\n");
                    fr.write(enDeCryptV2.newPwHash(pwField1.getText())+"\n");
                    File f = new File(picPath);
                    f = new File(settingFile.picFolder+ioS.checkIfNewPic(f));
                    fr.write(f.getName()+"\n"+"\n"+"\n");
                    fr.flush();
                    fr.close();
                    thisStage.close();
                }
                catch(Exception x){
                    x.printStackTrace();
                }
                mainClass.logInPane.checkForProfiles();
            }
        });

        addPictureButton.setOnAction(e->{ //open new FileChoose with picFolder as startfolder
            FileChooser fc = new FileChooser();
            fc.setTitle(lang.getText(19));
            fc.setInitialDirectory(new File(settingFile.picFolder));
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File chosenOne = fc.showOpenDialog(new Stage());
            picPath=chosenOne.getAbsolutePath();
            profileNameS =userNameTF.getText();
            pWField1S =pwField1.getText();
            pWField2S =pwField2.getText();
            paintIt();
        });

        BorderPane labelAndFieldPane = new BorderPane(); //setting up box for labels and Fields
        labelAndFieldPane.setLeft(labelGrid);
        labelAndFieldPane.setRight(fieldGrid);
        this.setCenter(buttonBox); //adding everything to the main box
        this.setTop(labelAndFieldPane);
        this.setBottom(errorBox);
        BorderPane.setMargin(buttonBox,insets);
        BorderPane.setMargin(labelAndFieldPane,insets);
        BorderPane.setMargin(errorBox,insets);

    }

}
