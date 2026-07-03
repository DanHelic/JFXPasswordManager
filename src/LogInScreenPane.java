package com.pwmanager.pwmanager;

import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

import static com.pwmanager.pwmanager.Main.*;


public class LogInScreenPane extends HBox {
    //public static LogInScreenPane logInPane;
    List <File> profileFiles,files;
    List <File> pictures;
    //double stageX,stageY;

    public LogInScreenPane(){
        setPadding(new Insets(5,5,5,5));
        setSpacing(10);
        mainClass.logInPane=this;
        checkForProfiles();
        mainClass.mainStage.setWidth(settingFile.xWidth);
    }

    //void widthChange(double x){stageX=x;paintIt();}
    //void heightChange(double y){stageY=y;paintIt();}

    public void checkForProfiles(){
        File profileDir = new File(settingFile.profileFolder);
        File picDir = new File(settingFile.picFolder);
        try{
            if(!profileDir.exists())profileDir.mkdir();
            if(!picDir.exists())picDir.mkdir();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        files = new ArrayList<File>();
        files = Arrays.asList(profileDir.listFiles());
        profileFiles = new ArrayList<File>();
        for(File f:files){if(f.getName().contains(".pwp"))profileFiles.add(f);}
        pictures = new ArrayList<File>();
        pictures = Arrays.asList(picDir.listFiles());

        profileFiles.sort(new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                int sSize;
                if(file1.getName().length()<file2.getName().length())sSize=(int)file1.getName().length();
                else sSize=(int)file2.getName().length();
                String fileName1=file1.getName();
                String fileName2=file2.getName();
                fileName1=fileName1.toUpperCase();
                fileName2=fileName2.toUpperCase();
                for(int i=0;i<sSize;i++){
                    if((int)fileName1.charAt(i)>(int)fileName2.charAt(0)){return 1;}
                    if((int)fileName1.charAt(i)<(int)fileName2.charAt(0)){return -1;}
                }
                return 1;
            }
        });

        /*
        //same as above, just different code (lambda)
        profileFiles.sort((file1,file2)-> {
            int sSize;
            if(file1.getName().length()<file2.getName().length())sSize=(int)file1.getName().length();
            else sSize=(int)file2.getName().length();
            String fileName1=file1.getName();
            String fileName2=file2.getName();
            fileName1=fileName1.toUpperCase();
            fileName2=fileName2.toUpperCase();
            for(int i=0;i<sSize;i++){
                if((int)fileName1.charAt(i)>(int)fileName2.charAt(0)){return 1;}
                if((int)fileName1.charAt(i)<(int)fileName2.charAt(0)){return -1;}
            }
            return 1;
        });
        */

        paintIt();
    }


    int tmp;
    public void paintIt(){
        Font optionsFont = new Font(16);
        setStyle("-fx-background-color:"+settingFile.mMColor2 +";");

        //Boxes
        VBox profileAndButtons = new VBox(); //for Profiles and Buttons under it
        VBox profileBox = new VBox(); //for the profiles (is in profileAndButtons)
        VBox settingBox = new VBox(); //For the settings
        GridPane settingsGrid = new GridPane(); //for the settings (is in settings)

        profileBox.setStyle("-fx-background-color:"+settingFile.mMColor1 +";");
        profileBox.setPadding(new Insets(10,10,10,10));
        profileBox.setPrefSize(settingFile.xWidth*0.325-10,settingFile.yHeight-161);//setPrefSize needed to get Scrollpane to work
        profileBox.setSpacing(5);
        ScrollPane profileBoxScrollPane = new ScrollPane(profileBox);
        profileBoxScrollPane.setFitToWidth(true);
        profileBoxScrollPane.setStyle("-fx-background-color:"+settingFile.mMColor1+";");
        if(settingFile.darkMode) {
            profileBoxScrollPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
        }
        else {
            profileBoxScrollPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
        }

        //Insert Profiles into the box and set which Profile is selected in chosenProfile;
        //Doing stuff here because stuff is happening when choosing a profile
        PasswordField pwField = new PasswordField();
        pwField.setEditable(false);
        pwField.setStyle("-fx-control-inner-background: LIGHTGREY");

        ArrayList<Profile> profilePanes = ioS.getProfiles(profileFiles);
        //boolean chosenProfile [] = new boolean[profileFiles.size()]; //useless? seems like

        for(Profile p:profilePanes){ //chosen stuff (marking and setting in setting-File on mouse click)
            profileBox.getChildren().add(p);
            p.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) { //mark Profile/set chosen
                    //for(boolean b:chosenProfile){b=false;}
                    for(Profile p:profilePanes){
                        p.setChosen(false);
                    }
                    Profile x = (Profile)mouseEvent.getSource();
                    x.setChosen(true);
                    //chosenProfile[x.getNumber()]=true;
                    settingFile.lastChosen=profileFiles.get(x.getNumber()).getName();
                    pwField.setEditable(true);
                    pwField.setStyle(null);
                }
            });
        }

        for(int i=0;i<profileFiles.size();i++){ //Setting chosen according to settingsfile
            if(profileFiles.get(i).getName().equals(settingFile.lastChosen)){
                profilePanes.get(i).setChosen(true);
                //chosenProfile[i]=true;
                pwField.setEditable(true);
                pwField.setStyle(null);
            }
        }

        //VBox for add and delete button and edit Button
        VBox addDEButtonBox = new VBox();
        addDEButtonBox.setPadding(new Insets(10,10,0,10));
        addDEButtonBox.setSpacing(8);
        Button addProfile= new Button(lang.getText(2));
        Button delProfile= new Button(lang.getText(3));
        Button editProfile = new Button(lang.getText(36));
        addProfile.setMaxWidth(130);
        addProfile.setTextAlignment(TextAlignment.CENTER);
        delProfile.setMaxWidth(130);
        delProfile.setTextAlignment(TextAlignment.CENTER);
        editProfile.setMaxWidth(130);
        editProfile.setTextAlignment(TextAlignment.CENTER);
        addProfile.setStyle("-fx-focus-color: transparent;");
        delProfile.setStyle("-fx-focus-color: transparent;");
        editProfile.setStyle("-fx-focus-color: transparent;");
        addDEButtonBox.getChildren().addAll(addProfile,delProfile,editProfile);
        addDEButtonBox.setAlignment(Pos.BOTTOM_CENTER);
        profileBox.getChildren().add(addDEButtonBox);
        profileAndButtons.getChildren().addAll(profileBoxScrollPane,addDEButtonBox);



        //set up Box for Password and Settings
        VBox passwordSettingBox = new VBox();
        passwordSettingBox.setStyle("-fx-background-color:"+settingFile.mMColor1+";");
        if(settingFile.darkMode) {
            passwordSettingBox.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
        }
        else {
            passwordSettingBox.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
        }
        passwordSettingBox.setPadding(new Insets(10,10,10,10));
        passwordSettingBox.setPrefSize(settingFile.xWidth*0.675-30,settingFile.yHeight *0.9-10);
        passwordSettingBox.setSpacing(10);

        //Set up Password entry
        HBox passwordBox = new HBox();
        passwordBox.setPadding(new Insets(10));
        passwordBox.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, new CornerRadii(2),BorderWidths.DEFAULT)));
        passwordBox.setSpacing(10);
        if(settingFile.xWidth>585){
            passwordBox.setSpacing((settingFile.xWidth-530)*0.175);
        }
        passwordBox.setAlignment(Pos.CENTER);
        passwordSettingBox.getChildren().addAll(passwordBox);

        //initiated above for deactivation
        //PW Field Stuff
        pwField.setPrefWidth(((settingFile.xWidth-550)*0.15)+150);
        Label pwFieldL= new Label(lang.getText(0),pwField);
        pwFieldL.setFont(new Font(16));
        pwFieldL.setTextFill(Color.web(settingFile.textColor1));
        pwFieldL.setContentDisplay(ContentDisplay.RIGHT);
        Button loginButton = new Button(lang.getText(1));
        loginButton.setFont(new Font(13));
        //Action at the Bottom
        passwordBox.getChildren().addAll(pwField,pwFieldL,loginButton);


        //Set up Settings
        settingBox.setSpacing(2);
        settingBox.setPadding(new Insets(10,0,10,0));

        //Settings Label
        HBox settingLabelBox = new HBox();
        settingLabelBox.setAlignment(Pos.TOP_CENTER);
        Label settingLabel = new Label (lang.getText(7));
        settingLabel.setTextFill(Color.web(settingFile.textColor1));
        settingLabel.setFont(new Font(20));
        settingLabelBox.getChildren().add(settingLabel);

        //Language
        HBox languageBox = new HBox();
        languageBox.setSpacing(10);
        Label languageLabel = new Label(lang.getText(8));
        languageLabel.setTextFill(Color.web(settingFile.textColor1));
        languageLabel.setFont(optionsFont);
        ComboBox <String>languageComboBox = new ComboBox<String>();
        languageComboBox.getItems().addAll(lang.getText(4),lang.getText(5),lang.getText(6));
        languageLabel.setContentDisplay(ContentDisplay.RIGHT);
        languageComboBox.setValue(lang.getLangString());
        languageBox.getChildren().addAll(languageLabel,languageComboBox);


        //time to delete Username/Password from clipboard get by tfu and tfp
        VBox copyTimingsBox = new VBox();
        HBox timeUntilCantBeCopiedBox = new HBox();
        timeUntilCantBeCopiedBox.setAlignment(Pos.CENTER);
        timeUntilCantBeCopiedBox.setSpacing(5);
        HBox timeUntilPWGetsDeletedBox = new HBox();
        timeUntilPWGetsDeletedBox.setSpacing(5);
        timeUntilPWGetsDeletedBox.setAlignment(Pos.CENTER);
        //TextFields
        TextField pwNoCopyTimeTF = new TextField();
        pwNoCopyTimeTF.setText(settingFile.pwNoCopyTime+"");
        pwNoCopyTimeTF.setPrefColumnCount(3);
        TextField pwDeleteClipboardTimeTF = new TextField();
        pwDeleteClipboardTimeTF.setText(settingFile.pwDeleteClipboardTime+"");
        pwDeleteClipboardTimeTF.setPrefColumnCount(3);
        //Labels
        Label pwNoCopyTimeL = new Label(lang.getText(9));
        pwNoCopyTimeL.setTextFill(Color.web(settingFile.textColor1));
        pwNoCopyTimeL.setWrapText(true);
        pwNoCopyTimeL.setFont(optionsFont);
        Label pwDeleteClipboardTimeL = new Label(lang.getText(10));
        pwDeleteClipboardTimeL.setTextFill(Color.web(settingFile.textColor1));
        pwDeleteClipboardTimeL.setWrapText(true);
        pwDeleteClipboardTimeL.setFont(optionsFont);
        Label secondL1 = new Label(lang.getText(11),pwNoCopyTimeTF);
        secondL1.setTextFill(Color.web(settingFile.textColor1));
        Label secondL2 = new Label(lang.getText(11),pwDeleteClipboardTimeTF);
        secondL2.setTextFill(Color.web(settingFile.textColor1));
        pwNoCopyTimeL.setContentDisplay(ContentDisplay.RIGHT);
        pwDeleteClipboardTimeL.setContentDisplay(ContentDisplay.RIGHT);
        secondL1.setContentDisplay(ContentDisplay.LEFT);
        secondL2.setContentDisplay(ContentDisplay.LEFT);
        //adding to boxes
        timeUntilCantBeCopiedBox.getChildren().addAll(pwNoCopyTimeTF, secondL1);
        timeUntilPWGetsDeletedBox.getChildren().addAll(pwDeleteClipboardTimeTF, secondL2);
        copyTimingsBox.getChildren().addAll(timeUntilCantBeCopiedBox, timeUntilPWGetsDeletedBox);
        copyTimingsBox.setSpacing(10);

        //time to auto-logout
        TextField timeToAutoLogoutTF = new TextField();
        timeToAutoLogoutTF.setText(settingFile.autoLogoutTime +"");
        timeToAutoLogoutTF.setPrefColumnCount(3);
        Label secondL3 = new Label(lang.getText(11),timeToAutoLogoutTF);
        secondL3.setTextFill(Color.web(settingFile.textColor1));
        Label timeToAutoLogoutL = new Label(lang.getText(50));
        timeToAutoLogoutL.setTextFill(Color.web(settingFile.textColor1));
        timeToAutoLogoutL.setWrapText(true);
        timeToAutoLogoutL.setFont(optionsFont);
        HBox timeToAutoLogoutBox = new HBox();
        timeToAutoLogoutBox.setAlignment(Pos.CENTER);
        timeToAutoLogoutBox.setSpacing(5);
        timeToAutoLogoutBox.getChildren().addAll(timeToAutoLogoutTF, secondL3);

        //restore clipboard
        HBox restoreClipboardBox = new HBox();
        CheckBox restoreClipboardCheckBox = new CheckBox();
        Label restoreClipboardL = new Label(lang.getText(35));
        restoreClipboardL.setTextFill(Color.web(settingFile.textColor1));
        restoreClipboardL.setContentDisplay(ContentDisplay.RIGHT);
        restoreClipboardL.setFont(optionsFont);
        restoreClipboardL.setWrapText(true);
        restoreClipboardCheckBox.setSelected(settingFile.saveClipBoard);
        restoreClipboardBox.getChildren().addAll(restoreClipboardCheckBox,restoreClipboardL);

        //show Usernames
        HBox showUsernamesBox = new HBox();
        CheckBox showUserNamesCheckBox = new CheckBox();
        Label showUserNamesL = new Label(lang.getText(43));
        showUserNamesL.setTextFill(Color.web(settingFile.textColor1));
        showUserNamesL.setContentDisplay(ContentDisplay.RIGHT);
        showUserNamesL.setFont(optionsFont);
        showUserNamesL.setWrapText(true);
        showUserNamesCheckBox.setSelected(settingFile.showUsernames);
        showUsernamesBox.getChildren().addAll(showUserNamesCheckBox,showUserNamesL);

        //dark Mode
        HBox darkModeBox = new HBox();
        CheckBox darkModeCheckBox = new CheckBox();
        Label darkModeL = new Label(lang.getText(55));
        darkModeL.setTextFill(Color.web(settingFile.textColor1));
        darkModeL.setContentDisplay(ContentDisplay.RIGHT);
        darkModeL.setFont(optionsFont);
        darkModeL.setWrapText(true);
        darkModeCheckBox.setSelected(settingFile.darkMode);
        darkModeBox.getChildren().addAll(darkModeCheckBox,darkModeL);

        //create Backup
        HBox createBackupBox = new HBox();
        CheckBox createBackupCheckBox = new CheckBox();
        Label createBackupL = new Label(lang.getText(56));
        createBackupL.setTextFill(Color.web(settingFile.textColor1));
        createBackupL.setContentDisplay(ContentDisplay.RIGHT);
        createBackupL.setFont(optionsFont);
        createBackupL.setWrapText(true);
        createBackupCheckBox.setSelected(settingFile.createBackup);
        createBackupBox.getChildren().addAll(createBackupCheckBox,createBackupL);


        //add every box/Label to its box in the Grid
        settingsGrid.add(languageLabel,0,0);
        settingsGrid.add(languageComboBox,1,0);
        settingsGrid.add(pwNoCopyTimeL,0,1);
        settingsGrid.add(timeUntilCantBeCopiedBox,1,1);
        settingsGrid.add(pwDeleteClipboardTimeL,0,2);
        settingsGrid.add(timeUntilPWGetsDeletedBox,1,2);
        settingsGrid.add(timeToAutoLogoutL,0,3);
        settingsGrid.add(timeToAutoLogoutBox,1,3);
        settingsGrid.add(restoreClipboardL,0,4);
        settingsGrid.add(restoreClipboardCheckBox,1,4);
        settingsGrid.add(showUserNamesL,0,5);
        settingsGrid.add(showUserNamesCheckBox,1,5);
        settingsGrid.add(darkModeL,0,6);
        settingsGrid.add(darkModeCheckBox,1,6);
        settingsGrid.add(createBackupL,0,7);
        settingsGrid.add(createBackupCheckBox,1,7);

        //set width and other stuff of the 2 columns
        ColumnConstraints c1 = new ColumnConstraints(passwordSettingBox.getPrefWidth()*0.81-29);
        c1.setHalignment(HPos.CENTER);
        ColumnConstraints c2 = new ColumnConstraints(passwordSettingBox.getPrefWidth()*0.19-8);
        c2.setHalignment(HPos.CENTER);
        settingsGrid.setHgap(0);
        settingsGrid.setVgap(8);
        settingsGrid.getColumnConstraints().addAll(c1,c2);
        settingsGrid.setPadding(new Insets(5,3,5,3));
        settingsGrid.setBorder(new Border(new BorderStroke(Color.web("000000"),
                BorderStrokeStyle.SOLID, new CornerRadii(0),new BorderWidths(1))));


        //add boxes to the settingBox and add it to the passwordSettingBox
        settingBox.getChildren().addAll(settingLabelBox,settingsGrid);
        passwordSettingBox.getChildren().add(settingBox);
        getChildren().clear();
        getChildren().addAll(profileAndButtons,passwordSettingBox);



        //Action
        addProfile.setOnAction(e->{ //Line 160
            Stage newStage = new Stage();
            newStage.setTitle(lang.getText(2));
            AddProfilePane addProfilePane = new AddProfilePane(profileFiles,pictures,newStage);
            Scene scene = new Scene(addProfilePane,335,250);
            newStage.setX(mainClass.mainStage.getX()+settingFile.xWidth/2-5-335/2);
            newStage.setY(mainClass.mainStage.getY()+settingFile.yHeight/2-5-250/2);
            newStage.setScene(scene);
            mainClass.newStage(newStage);
            newStage.show();
        });

        delProfile.setOnAction(e->{
            addDEButtonBox.getChildren().clear();
            Button yesButton = new Button(lang.getText(20));
            Button noButton = new Button(lang.getText(21));
            PasswordField pwDeletePF = new PasswordField();
            pwDeletePF.setPrefColumnCount(11);
            Label pwDeleteL = new Label(lang.getText(13),pwDeletePF);
            pwDeleteL.setTextFill(Color.web(settingFile.textColor1));
            pwDeleteL.setFont(new Font(15));
            pwDeleteL.setContentDisplay(ContentDisplay.RIGHT);
            HBox pwDeleteBox = new HBox();
            pwDeleteBox.setAlignment(Pos.CENTER);
            pwDeleteBox.getChildren().addAll(pwDeletePF,pwDeleteL);
            Label deleteQuestionL= new Label(lang.getText(22));
            deleteQuestionL.setTextFill(Color.web(settingFile.textColor1));
            deleteQuestionL.setFont(new Font(16));
            HBox yNButtonsBox = new HBox();
            yNButtonsBox.setSpacing(5);
            yNButtonsBox.setAlignment(Pos.CENTER);
            yNButtonsBox.getChildren().addAll(yesButton,noButton);
            addDEButtonBox.getChildren().addAll(yNButtonsBox,pwDeleteBox,deleteQuestionL);

            yesButton.setOnAction(e2->{
                boolean deleted=false;
                for(Profile p:profilePanes){
                    if(p.getChosen()&&p.checkPassword(profileFiles.get(p.getNumber()),pwDeletePF.getText())){
                        File f = profileFiles.get(p.getNumber());
                        f.delete();
                        deleted=true;
                    }
                    else{
                        pwDeletePF.setStyle("-fx-background-color:#FF7F7F;-fx-border-color:#6E6E6E;");
                    }
                }
                if(deleted)checkForProfiles();
            });
            noButton.setOnAction(e3->{
                checkForProfiles();
            });
        });

        editProfile.setOnAction(e->{
            if(profileFiles.size()!=0){
                Stage newStage = new Stage();
                newStage.setTitle(lang.getText(36));
                EditProfilePane editProfilePane = new EditProfilePane(profileFiles,pictures,newStage,profilePanes);
                Scene scene = new Scene(editProfilePane,335,325);
                newStage.setX(mainClass.mainStage.getX()+settingFile.xWidth/2-5-335/2);
                newStage.setY(mainClass.mainStage.getY()+settingFile.yHeight/2-5-325/2);
                newStage.setScene(scene);
                mainClass.newStage(newStage);
                newStage.show();
            }
        });



        loginButton.setOnAction(e->{ //Login with press on button
            for(Profile p:profilePanes){
                if(p.getChosen()){
                    if(p.checkPassword(profileFiles.get(p.getNumber()),pwField.getText())){
                        mainClass.logIn(pwField.getText(),profileFiles.get(p.getNumber()));
                    }
                    else{
                        //error
                        pwField.setStyle("-fx-background-color:#FF7F7F;-fx-border-color:#6E6E6E;");
                    }
                }
            }
        });
        pwField.setOnKeyPressed(e->{ //login with pressing enter
            if(e.getCode()==KeyCode.ENTER){
                for(Profile p:profilePanes){
                    if(p.getChosen()){
                        if(p.checkPassword(profileFiles.get(p.getNumber()),pwField.getText()))
                        mainClass.logIn(pwField.getText(),profileFiles.get(p.getNumber()));
                        else{
                            //error
                            pwField.setStyle("-fx-background-color:#FF8A8A;-fx-border-color:#6E6E6E;");
                        }
                    }
                }
            }
        });


        languageComboBox.setOnAction(e->{ //set Language
            ComboBox <String>comboBox = (ComboBox) e.getSource();
            switch(comboBox.getValue()){
                case "Bier":
                case "German":
                case "Deutsch":
                    lang.setLang(0,this);
                    settingFile.langInt =0;
                    break;
                case "Tea":
                case "English":
                case "Englisch":
                    lang.setLang(1,this);
                    settingFile.langInt =1;
                    break;
                case "GIBBYYY":
                case "Other":
                case "Andere":
                    lang.setLang(2,this);
                    settingFile.langInt =2;
                    break;
            }
        });


        //set values in settings on change
        pwNoCopyTimeTF.textProperty().addListener((observable, oldValue, newValue)->{
            if(!pwNoCopyTimeTF.getText().equals("")&&pwNoCopyTimeTF.getText().matches("[0-9]+"))
                settingFile.pwNoCopyTime = Integer.parseInt(pwNoCopyTimeTF.getText());
            else
                settingFile.pwNoCopyTime = 10;
        });

        pwDeleteClipboardTimeTF.textProperty().addListener((observable, oldValue, newValue)->{
            if(!pwDeleteClipboardTimeTF.getText().equals("")&&pwDeleteClipboardTimeTF.getText().matches("[0-9]+"))
                settingFile.pwDeleteClipboardTime = Integer.parseInt(pwDeleteClipboardTimeTF.getText());
            else
                settingFile.pwDeleteClipboardTime = 10;
        });

        timeToAutoLogoutTF.textProperty().addListener((observable, oldValue, newValue)->{
            if(!timeToAutoLogoutTF.getText().equals("")&&timeToAutoLogoutTF.getText().matches("[0-9]+"))
                settingFile.autoLogoutTime = Integer.parseInt(timeToAutoLogoutTF.getText());
            else
                settingFile.autoLogoutTime = 300;
        });

        restoreClipboardCheckBox.setOnAction(e->{
            if(restoreClipboardCheckBox.isSelected()){
                settingFile.saveClipBoard=true;
            }
            else{
                settingFile.saveClipBoard=false;
            }
        });

        restoreClipboardL.setOnMouseClicked(e->{ //change checkbox when clicking on Text
            if(restoreClipboardCheckBox.isSelected())restoreClipboardCheckBox.setSelected(false);
            else restoreClipboardCheckBox.setSelected(true);
            if(restoreClipboardCheckBox.isSelected()){
                settingFile.saveClipBoard=true;
            }
            else{
                settingFile.saveClipBoard=false;
            }
        });

        showUserNamesCheckBox.setOnAction(e->{
            if(showUserNamesCheckBox.isSelected()){
                settingFile.showUsernames=true;
            }
            else{
                settingFile.showUsernames=false;
            }
        });

        showUserNamesL.setOnMouseClicked(e->{ //change checkbox when clicking on Text
            if(showUserNamesCheckBox.isSelected())showUserNamesCheckBox.setSelected(false);
            else showUserNamesCheckBox.setSelected(true);
            if(showUserNamesCheckBox.isSelected()){
                settingFile.showUsernames=true;
            }
            else{
                settingFile.showUsernames=false;
            }
        });

        darkModeCheckBox.setOnAction(e->{
            if(darkModeCheckBox.isSelected()){
                settingFile.setDarkMode(true);
            }
            else{
                settingFile.setDarkMode(false);
            }
            paintIt();
        });

        darkModeL.setOnMouseClicked(e->{ //change checkbox when clicking on Text
            if(darkModeCheckBox.isSelected())darkModeCheckBox.setSelected(false);
            else darkModeCheckBox.setSelected(true);
            if(darkModeCheckBox.isSelected()){
                settingFile.setDarkMode(true);
            }
            else{
                settingFile.setDarkMode(false);
            }
            paintIt();
        });

        createBackupCheckBox.setOnAction(e->{
            if(createBackupCheckBox.isSelected()){
                settingFile.createBackup=true;
            }
            else{
                settingFile.createBackup=false;
            }
        });

        createBackupL.setOnMouseClicked(e->{ //change checkbox when clicking on Text
            if(createBackupCheckBox.isSelected())createBackupCheckBox.setSelected(false);
            else createBackupCheckBox.setSelected(true);
            if(createBackupCheckBox.isSelected()){
                settingFile.createBackup=true;
            }
            else{
                settingFile.createBackup=false;
            }
        });



    }


}
