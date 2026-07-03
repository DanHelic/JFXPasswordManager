package com.pwmanager.pwmanager;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;

import static com.pwmanager.pwmanager.Main.*;

public class LoggedInPaneButtons extends VBox {
    boolean askDelete;
    PasswordPane pwPane;
    LoggedInPane loggedInPane;
    File profileFile;

    public LoggedInPaneButtons(LoggedInPane loggedInP, PasswordPane passwordP, File profileF){
        askDelete=false;
        loggedInPane=loggedInP;
        pwPane = passwordP;
        profileFile =profileF;
        paintIt();
    }

    public void paintIt(){
        Button newPasswordB = new Button(lang.getText(23));
        Button editPasswordB = new Button(lang.getText(25));
        Button logoutB = new Button (lang.getText(26));
        if(askDelete){
            HBox deleteBox = new HBox();
            Button yesB = new Button(lang.getText(20));
            Button noB = new Button(lang.getText(21));
            Label askDelL = new Label(lang.getText(22),yesB);
            askDelL.setTextFill(Color.web(settingFile.textColor1));
            deleteBox.getChildren().addAll(askDelL,yesB,noB);
            getChildren().clear();
            getChildren().addAll(newPasswordB,deleteBox,editPasswordB, logoutB);
            yesB.setOnAction(e->{
                for(Password p:pwPane.passwordObjectList){
                    if(p.isChosen()){
                        ioS.delPassword(p.id, profileFile);
                    }
                }
                askDelete=false;
                paintIt();
                loggedInPane.pwPane.paintIt();
            });
            noB.setOnAction(e->{
                askDelete=false;
                paintIt();
            });
        }
        else{
            Button deletePasswordB = new Button(lang.getText(24));
            deletePasswordB.setOnAction(e->{
                askDelete=true;
                paintIt();
            });
            getChildren().clear();
            getChildren().addAll(newPasswordB,deletePasswordB,editPasswordB, logoutB);
        }
        setPadding(new Insets(5,5,5,5));
        setSpacing(10);


        newPasswordB.setOnAction(e->{
            try{
                Stage newStage=new Stage();
                Scene newScene = new Scene(new AddPasswordPane(newStage, profileFile,loggedInPane.profilePW),320,382);
                newStage.setX(mainClass.mainStage.getX()+settingFile.xWidth/2-5-320/2);
                newStage.setY(mainClass.mainStage.getY()+settingFile.yHeight/2-5-382/2);
                newStage.setScene(newScene);
                mainClass.newStage(newStage);
                newStage.show();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        });

        editPasswordB.setOnAction(e->{
            try{
                int passwordID=-1;
                for(Password password: pwPane.passwordObjectList){
                    if(password.chosen){
                        passwordID=password.id;
                    }
                }
                if(passwordID!=-1){
                    Stage newStage=new Stage();
                    Scene newScene = new Scene(new EditPasswordPane(newStage, profileFile,loggedInPane.profilePW,passwordID),320,382);
                    newStage.setX(mainClass.mainStage.getX()+settingFile.xWidth/2-5-320/2);
                    newStage.setY(mainClass.mainStage.getY()+settingFile.yHeight/2-5-382/2);
                    newStage.setScene(newScene);
                    mainClass.newStage(newStage);
                    newStage.show();
                }
            }
            catch (Exception x){
                x.printStackTrace();
            }
        });

        logoutB.setOnAction(e->{
            mainClass.logOut();
        });
    }

}
