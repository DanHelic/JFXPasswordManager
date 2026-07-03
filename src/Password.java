package com.pwmanager.pwmanager;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.File;

import static com.pwmanager.pwmanager.Main.*;

public class Password extends HBox { //One picture with label and doubleclick to copy pw
    int id;
    String description,username,picUrl, password;
    boolean ready,chosen;

    public Password(int i, String des, String userN,String Url, String pw){
        id=i;
        description = des;
        username=userN;
        picUrl= settingFile.picFolder+Url;
        ready=true;
        password=pw;
        chosen=false;
        paintIt();
    }

    public boolean isChosen(){
        return chosen;
    }

    public void setChosen(boolean b){
        chosen=b;
    }

    public boolean getReady(){
        return ready;
    }

    public void setReady(boolean b){
        ready=b;
    }


    ScrollPane scrollLabels;
    VBox labelBox; //for resize
    File picFile;
    ImageView picView;
    Label userNameL,descriptionL;

    public void paintIt(){
        picFile = new File(picUrl);
        picView = ioS.tryGetImage(picFile.getAbsolutePath());
        picView.setFitHeight(70);
        picView.setFitWidth(70);

        userNameL = new Label(username);
        userNameL.setTextFill(Color.web(settingFile.textColor1));
        userNameL.setWrapText(true);
        descriptionL = new Label(description);
        descriptionL.setTextFill(Color.web(settingFile.textColor1));
        descriptionL.setWrapText(true);
        labelBox = new VBox();
        labelBox.setPrefWidth(settingFile.xWidth/3-26);
        labelBox.setMinHeight(68);
        if(settingFile.showUsernames) labelBox.getChildren().add(userNameL);
        labelBox.getChildren().add(descriptionL);

        scrollLabels = new ScrollPane(labelBox);
        scrollLabels.setFitToWidth(true);
        scrollLabels.focusedProperty().addListener((obs,oldVal,focused)->{
            if(focused)requestFocus();
        });

        setPrefWidth(settingFile.xWidth/3-26);
        setPrefHeight(70);
        getChildren().clear();
        getChildren().addAll(picView,scrollLabels);


        if(id%2==0){
            setStyle("-fx-background-color: "+settingFile.pWPanelColor1);
            labelBox.setStyle("-fx-background-color: "+settingFile.pWPanelColor1);
            scrollLabels.setStyle("-fx-focus-color: "+settingFile.pWPanelColor1+";-fx-background-color: "+settingFile.pWPanelColor1);
        }
        else{
            setStyle("-fx-background-color: "+settingFile.pWPanelColor2);
            labelBox.setStyle("-fx-background-color: "+settingFile.pWPanelColor2);
            scrollLabels.setStyle("-fx-focus-color: "+settingFile.pWPanelColor2+";-fx-background-color: "+settingFile.pWPanelColor2);
        }

        setBorder(new Border(new BorderStroke(Color.web(settingFile.lIColor2),
                BorderStrokeStyle.SOLID, new CornerRadii(0),new BorderWidths(4))));
    }


}
