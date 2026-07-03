package com.pwmanager.pwmanager;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import static com.pwmanager.pwmanager.Main.*;
import static java.lang.Thread.sleep;

public class ClipboardStatusPane extends VBox {

    public ClipboardStatusPane(){
        statusInt=0; //0=nothing copied; 1= Username/E-mail copied; 2=pw Copied
        paintIt();
    }

    public void setStatus(int i){ //status: 0=standard, 1=username copied, 2=pw copied
        if(statusInt<3&&statusInt>=0) statusInt=i;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                paintIt();
            }
        });
    }

    int statusInt;
    public void paintIt(){
        getChildren().clear();
        setSpacing(8);
        Label whichStatusL = new Label();
        whichStatusL.setFont(new Font(16));
        whichStatusL.setWrapText(true);
        whichStatusL.setTextFill(Color.web(settingFile.textColor1));
        Label statusL = new Label(lang.getText(44));
        statusL.setFont(new Font(16));
        statusL.setWrapText(true);
        statusL.setPadding(new Insets(2,0,-10,0));
        statusL.setTextFill(Color.web(settingFile.textColor1));
        if(statusInt==0){
            whichStatusL.setText(lang.getText(45));
            whichStatusL.setStyle("-fx-background-color: #15AF00");
        }
        if(statusInt==1){
            whichStatusL.setText(lang.getText(46));
            whichStatusL.setStyle("-fx-background-color: #FFA562");
        }
        if(statusInt==2){
            whichStatusL.setText(lang.getText(47));
            whichStatusL.setStyle("-fx-background-color: #FF4747");
        }

        Button cancelCopyB = new Button(lang.getText(48));

        //time to autologou
        if (settingFile.autoLogoutTime != 0) { //time left bar here
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.BLACK);
            rectangle.widthProperty().setValue(75);
            rectangle.heightProperty().setValue(26);
            rectangle.setStroke(Color.WHITE);
            rectangle.setStrokeWidth(2);

            StackPane stackPane = new StackPane();
            stackPane.setMaxWidth(104);
            stackPane.setMinWidth(104);
            stackPane.setMaxHeight(30);
            stackPane.setMinHeight(30);
            stackPane.setStyle("-fx-background-color: #FFFFFF");
            stackPane.setAlignment(Pos.CENTER_LEFT);
            stackPane.getChildren().addAll(rectangle);

            Scene imgScene = new Scene(stackPane);
            Label timeLeftL = new Label("1",stackPane);
            timeLeftL.setTextFill(Color.web(settingFile.textColor1));
            getChildren().addAll(statusL, whichStatusL, cancelCopyB,stackPane,timeLeftL);

            Runnable run = () -> {
                while (!mainClass.logOutTimer.stop) {
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() { //Progress in percent in 0.1 = 10%
                                double progress = (System.currentTimeMillis() - mainClass.logOutTimer.startTime + 0.0) / (settingFile.autoLogoutTime * 1000);
                                rectangle.widthProperty().setValue(progress*100);
                                timeLeftL.setText(" "+(int)(settingFile.autoLogoutTime -progress*settingFile.autoLogoutTime));
                            }
                        });
                        sleep(40);
                    }
                catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t = new Thread(run);
            t.start();

            }
        else {
            getChildren().addAll(statusL, whichStatusL, cancelCopyB);
        }

        cancelCopyB.setOnAction(e -> {
            if(mainClass.activeClipboardTimers!=null)mainClass.activeClipboardTimers.stop = true;
        });
    }



}
