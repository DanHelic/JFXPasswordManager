package com.pwmanager.pwmanager;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.*;

import static com.pwmanager.pwmanager.Main.*;

public class LoggedInPane extends GridPane { //Pane after LogIn
    String profilePW;
    File profileFile;
    ClipboardStatusPane clipboardStatusPane;
    boolean hadAlreadyOneStatusPane;
    boolean firstEventFilter;

    public LoggedInPane(String pW, File pro){
        profilePW =pW;
        profileFile =pro;
        mainClass.loggedInPane=this;
        hadAlreadyOneStatusPane=false;
        firstEventFilter=true;
        paintIt();
    }
    LoggedInPaneButtons loggedInPaneButtons;
    HBox buttonsAndClipStatus;
    PasswordPane pwPane;

    void paintIt(){
        getChildren().clear();
        setPadding(new Insets(10,10,10,10));
        setHgap(10);
        setVgap(10);
        setStyle("-fx-background-color: "+settingFile.lIColor2);

        pwPane = new PasswordPane(profilePW, profileFile);

        loggedInPaneButtons = new LoggedInPaneButtons(this, pwPane, profileFile);

        clipboardStatusPane = new ClipboardStatusPane();
        clipboardStatusPane.setMaxWidth(250);
        hadAlreadyOneStatusPane=true;


        buttonsAndClipStatus = new HBox();
        buttonsAndClipStatus.spacingProperty().bind(loggedInPaneButtons.widthProperty().add(250).multiply(-1).add(mainClass.mainStage.widthProperty()).subtract(40));
        buttonsAndClipStatus.getChildren().addAll(loggedInPaneButtons, clipboardStatusPane);
        buttonsAndClipStatus.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, new CornerRadii(0),new BorderWidths(2))));
        buttonsAndClipStatus.setStyle("-fx-background-color: "+settingFile.lIColor1);


        add(buttonsAndClipStatus,0,0);
        add(pwPane,0,1);
        pwPane.setStyle("-fx-background-color: "+settingFile.lIColor1);
        mainClass.mainStage.setWidth(settingFile.xWidth); //fix Scrollpane bug

        if(firstEventFilter){
            addEventFilter(KeyEvent.KEY_PRESSED, e->{
                pwPane.requestPasswordMove(e.getCode());
                pwPane.setScroll();
            });
            firstEventFilter=false;
        }
    }

}
