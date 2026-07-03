package com.pwmanager.pwmanager;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static com.pwmanager.pwmanager.Main.*;

public class AutoClicker extends TabPane {

    public AutoClicker(){

        paintIt();
    }
    /*
what i do in däre

simple:
buttontype (left/right)
duration of click
time between clicks
clicktype (single/double)
how often to repeat (0=infinite)
x and y coordinates (check if they are in bounds) or current location
set x and y by pressing control key
chose start and stop button (click on button to get the button by pressing on keyboard)


expert:



     */

    public void paintIt(){
        Tab simpleTab = new Tab();
        simpleTab.setText(lang.getText(53));
        simpleTab.setClosable(false);
        Tab expertTab = new Tab();
        expertTab.setText(lang.getText(54));
        expertTab.setClosable(false);
        VBox watermarkBox = new VBox();
        Label watermarkL1 = new Label("By Me");
        Label watermarkL2 = new Label("The Daniel P.");
        Label watermarkL3 = new Label("Created in 2022");
        Label watermarkL4 = new Label("Because i had nothing to do");
        watermarkL1.setTextFill(Color.web(settingFile.textColor1));
        watermarkL2.setTextFill(Color.web(settingFile.textColor1));
        watermarkL3.setTextFill(Color.web(settingFile.textColor1));
        watermarkL4.setTextFill(Color.web(settingFile.textColor1));
        watermarkBox.getChildren().addAll(watermarkL1,watermarkL2,watermarkL3,watermarkL4);
        watermarkBox.setStyle("-fx-background-color:"+settingFile.mMColor1 +";");
        expertTab.setContent(watermarkBox);
        getTabs().addAll(simpleTab,expertTab);
    }
}
