package com.vmuguerza.presentacion;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppFX extends Application  {

    @Override
    public void start(Stage stage) throws Exception {
        Button boton = new Button("Probar JavaFX");
        boton.setOnAction(event-> boton.setText("Funciona!!!"));
        stage.setTitle("PageTurner");
        stage.setScene(new Scene(new StackPane(boton), 500, 300));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
    
}
