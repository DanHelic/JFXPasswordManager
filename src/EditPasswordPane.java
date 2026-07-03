package com.pwmanager.pwmanager;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

import static com.pwmanager.pwmanager.Main.*;

public class EditPasswordPane extends BorderPane {

    boolean showPasswordBoolean, pwNotSameShow;
    TextField pw1TF, pw2TF, usernameTF, countPwTF;
    TextArea descriptionTA;
    String pw1String, pw2String, usernameString, descriptionString; //Text of X to paintIt again
    Stage stage;
    File picFile;
    String genLength;
    File profileFile;
    String profilePw;
    int id;

    public EditPasswordPane(Stage s, File prof, String pPw, int i){
        showPasswordBoolean =false;
        Password thisPw = ioS.getPasswords(prof,pPw).get(i-1);
        pw1String =enDeCryptV2.decrypt(pPw,thisPw.password);
        pw2String =enDeCryptV2.decrypt(pPw,thisPw.password);
        usernameString =thisPw.username;
        descriptionString =thisPw.description;
        stage=s;
        picFile = new File(thisPw.picUrl);
        genLength="12";
        profileFile = prof;
        profilePw=pPw;
        id=i;
        pwNotSameShow=false;
        paintIt();
    }



    void showPw(boolean b){
        showPasswordBoolean =b;
        pw1String = pw1TF.getText();
        pw2String = pw2TF.getText();
        usernameString = usernameTF.getText();
        descriptionString = descriptionTA.getText();
        genLength= countPwTF.getText();
        paintIt();
    }

    void paintIt(){
        setStyle("-fx-background-color: "+settingFile.lIColor2);
        GridPane paneOfFields = new GridPane();
        usernameTF = new TextField();
        descriptionTA = new TextArea();
        if(showPasswordBoolean){
            pw1TF = new TextField(pw1String);
            pw2TF = new TextField(pw2String);
        }
        else{
            pw1TF = new PasswordField();
            pw1TF.setText(pw1String);
            pw2TF = new PasswordField();
            pw2TF.setText(pw2String);
        }
        CheckBox showPwCB = new CheckBox();
        Button genPwB = new Button(lang.getText(32));
        countPwTF = new TextField(genLength);
        Button choosePicB = new Button(lang.getText(33)); //rechts daneben das Bild?
        Button changePwB = new Button(lang.getText(49));
        ImageView picView = ioS.tryGetImage(picFile.getAbsolutePath());
        countPwTF.setPrefColumnCount(3);

        paneOfFields.add(usernameTF,1,0);
        paneOfFields.add(pw1TF,1,1);
        paneOfFields.add(pw2TF,1,2);
        paneOfFields.add(descriptionTA,1,3);
        paneOfFields.add(genPwB,0,4);
        paneOfFields.add(showPwCB,0,5);
        paneOfFields.add(countPwTF,1,4);
        paneOfFields.add(choosePicB,0,6);
        paneOfFields.add(picView,1,6);

        Label userNL = new Label(lang.getText(27));
        userNL.setTextFill(Color.web(settingFile.textColor1));
        Label pw1L = new Label(lang.getText(28));
        pw1L.setTextFill(Color.web(settingFile.textColor1));
        Label pw2L = new Label(lang.getText(29));
        pw2L.setTextFill(Color.web(settingFile.textColor1));
        Label descriptionL = new Label(lang.getText(30));
        descriptionL.setTextFill(Color.web(settingFile.textColor1));
        Label countPwL = new Label(lang.getText(42), countPwTF);
        countPwL.setTextFill(Color.web(settingFile.textColor1));
        countPwL.setContentDisplay(ContentDisplay.RIGHT);
        Label showPwL = new Label(lang.getText(31), showPwCB);
        showPwL.setTextFill(Color.web(settingFile.textColor1));
        showPwL.setContentDisplay(ContentDisplay.RIGHT);

        Label pwNotSameErrorL = new Label(lang.getText(15));
        pwNotSameErrorL.setTextFill(Paint.valueOf("#ff0000"));
        pwNotSameErrorL.setFont(new Font(15));
        pwNotSameErrorL.setWrapText(true);
        pwNotSameErrorL.setContentDisplay(ContentDisplay.LEFT);
        pwNotSameErrorL.setMaxWidth(150);
        pwNotSameErrorL.setVisible(pwNotSameShow);

        paneOfFields.add(userNL,0,0);
        paneOfFields.add(pw1L,0,1);
        paneOfFields.add(pw2L,0,2);
        paneOfFields.add(descriptionL,0,3);
        paneOfFields.add(countPwL,1,4);
        paneOfFields.add(showPwL,0,5);
        paneOfFields.add(pwNotSameErrorL,1,5);


        descriptionTA.setPrefColumnCount(12);
        descriptionTA.setPrefRowCount(3);
        picView.setFitHeight(45);
        picView.setFitWidth(45);
        showPwCB.setSelected(showPasswordBoolean);
        paneOfFields.setHgap(12);
        paneOfFields.setVgap(12);

        //set Text
        usernameTF.setText(usernameString);
        descriptionTA.setText(descriptionString);

        setTop(paneOfFields);
        setBottom(changePwB);
        setPadding(new Insets(8,8,8,8));


        showPwCB.setOnAction(e->{
            CheckBox x = (CheckBox) e.getSource();
            if(x.isSelected()){
                showPw(true);
            }
            else{
                showPw(false);
            }
        });

        choosePicB.setOnAction(e->{
            FileChooser fc = new FileChooser();
            fc.setTitle(lang.getText(19));
            fc.setInitialDirectory(new File(settingFile.picFolder));
            File x = fc.showOpenDialog(new Stage());
            if(x!=null){
                picFile = x;
            }
            showPw(showPasswordBoolean);
        });

        genPwB.setOnAction(e->{
            int pwCount=0;
            if(!countPwTF.getText().equals(""))
                pwCount = Integer.parseInt(countPwTF.getText());
            if(pwCount<1||pwCount>99){
                pwCount=12;
            }
            String password = new PasswordGen().genPassword(pwCount);
            pw1TF.setText(password);
            pw2TF.setText(password);
            showPw(showPasswordBoolean);
        });

        changePwB.setOnAction(e->{ //no errors yet
            if(pw1TF.getText().equals(pw2TF.getText())){
                picFile = new File (settingFile.picFolder+ioS.checkIfNewPic(picFile));
                ioS.changePassword(usernameTF.getText(), pw1TF.getText(), descriptionTA.getText(), picFile.getName(), profileFile,profilePw, id);
                mainClass.loggedInPane.paintIt();
                stage.close();
            }
            else{
                pwNotSameShow=true;
                showPw(showPasswordBoolean);
            }
        });

        showPwL.setOnMouseClicked(e->{
            if(showPwCB.isSelected()){
                showPwCB.setSelected(false);
                showPw(false);
            }
            else{
                showPwCB.setSelected(true);
                showPw(true);
            }
        });

    }

}
