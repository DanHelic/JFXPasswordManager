package com.pwmanager.pwmanager;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.File;
import java.util.ArrayList;

import static com.pwmanager.pwmanager.Main.*;

public class PasswordPane extends Pane { //Pane with only passwords to copy
    String profilePassword;
    File profileFile;
    int markedPw;
    ScrollPane scrollPane;
    double scrollPaneHeight;
    public PasswordPane(String pW,File prof){
        scrollPaneHeight=1;
        profilePassword = pW;
        markedPw=1;
        profileFile = prof;
        paintIt();
    }
    ArrayList<Password> passwordObjectList;

    void paintIt(){
        getChildren().clear();
        VBox boxInSP = new VBox();
        boxInSP.setStyle("-fx-background-color: "+settingFile.lIColor1);
        boxInSP.setPadding(new Insets(5,7,5,5));
        boxInSP.setSpacing(4);
        boxInSP.setPrefHeight(settingFile.yHeight-220);

        scrollPane = new ScrollPane(boxInSP);
        scrollPane.setStyle("-fx-background-color: "+settingFile.lIColor1);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(0),new BorderWidths(2))));
        scrollPane.setPrefWidth(settingFile.xWidth-38);

        try{
            passwordObjectList = ioS.getPwList(profileFile, profilePassword);
            for(Password p: passwordObjectList){
                p.setPrefWidth((settingFile.xWidth/3)-26);
                p.labelBox.setPrefWidth((settingFile.xWidth/3)-20);
                if(p.id==markedPw){
                    p.setChosen(true);
                    p.setBorder(new Border(new BorderStroke(Color.BLACK,
                            BorderStrokeStyle.SOLID, new CornerRadii(0),new BorderWidths(4))));
                }
            }
            int id=0;
            ArrayList <HBox> hBoxArray = new ArrayList<HBox>();
            for(Password p: passwordObjectList){
                if(id%3==0){ //horizontal box stuff
                    if(id!=0){ //add latest finished hbox
                        boxInSP.getChildren().add(hBoxArray.get((id/3)-1));
                    }
                    hBoxArray.add(new HBox());
                    hBoxArray.get(id/3).setSpacing(4);
                }
                hBoxArray.get(id/3).getChildren().add(p);
                id++;
            }
            if(!hBoxArray.isEmpty()&&id%3!=0)boxInSP.getChildren().add(hBoxArray.get((id/3)));
            else{
                if(!hBoxArray.isEmpty())boxInSP.getChildren().add(hBoxArray.get((id/3)-1));
            }

            for(Password p: passwordObjectList){
                p.setOnMouseClicked(e->{
                    if(e.getButton().equals(MouseButton.PRIMARY)){
                        boolean nothingIsBeingCopied = true; //only one pw can be copied at a time

                        for(Password y: passwordObjectList){ //reset all borders and chosen status
                            y.setBorder(new Border(new BorderStroke(Color.web(settingFile.lIColor2),
                                    BorderStrokeStyle.SOLID, new CornerRadii(0),new BorderWidths(4))));
                            y.setChosen(false);
                            if(!y.getReady()){ //if any pw is not ready set this to false
                                nothingIsBeingCopied=false;
                            }
                        }

                        Password x = (Password) e.getSource();
                        x.setChosen(true);
                        x.setBorder(new Border(new BorderStroke(Color.BLACK,
                                BorderStrokeStyle.SOLID, new CornerRadii(0),new BorderWidths(4))));

                        if(e.getClickCount()==2&&x.getReady()&&nothingIsBeingCopied){
                            x.setReady(false);
                            pwCopyTimer timer = new pwCopyTimer(x, profilePassword);
                        }
                    }
                });
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        getChildren().add(scrollPane);
        Platform.runLater(new Runnable(){ public void run() { //copy Username
            try{
                Thread.sleep(20);
                scrollPaneHeight=scrollPane.getHeight();
            }
            catch (Exception e){e.printStackTrace();}
        }});
        setScroll();
    }

    public void setScroll(){
        if(markedPw>3){
            int markedLine = markedPw/3;
            if(markedPw%3!=0)markedLine++;
            int totalLineCount = passwordObjectList.size()/3;
            if(passwordObjectList.size()%3!=0)totalLineCount++;
            double spPixelHeight=totalLineCount*82+12;
            spPixelHeight = spPixelHeight-scrollPaneHeight;
            double setPixelHeight=(markedLine-2)*82;
            scrollPane.setVvalue(setPixelHeight/spPixelHeight);
        }
    }

    public void requestPasswordMove(KeyCode key){
        if(key == KeyCode.DOWN){
            for(Password p:passwordObjectList){
                if(p.isChosen()&&p.id+3<=passwordObjectList.size()){
                    ioS.movePw(p.id,p.id+3,profileFile);
                    markedPw=p.id+3;
                    paintIt();
                }
            }
        }

        if(key == KeyCode.UP){
            for(Password p:passwordObjectList){
                if(p.isChosen()&&p.id-3>=1){
                    ioS.movePw(p.id,p.id-3,profileFile);
                    markedPw=p.id-3;
                    paintIt();
                }
            }
        }

        if(key == KeyCode.RIGHT){
            for(Password p:passwordObjectList){
                if(p.isChosen()&&p.id+1<=passwordObjectList.size()){
                    ioS.movePw(p.id,p.id+1,profileFile);
                    markedPw=p.id+1;
                    paintIt();
                }
            }
        }

        if(key == KeyCode.LEFT){
            for(Password p:passwordObjectList){
                if(p.isChosen()&&p.id-1>=1){
                    ioS.movePw(p.id,p.id-1,profileFile);
                    markedPw=p.id-1;
                    paintIt();
                }
            }
        }
        setScroll();
    }

}
