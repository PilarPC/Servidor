package com.example.sevidor;

import com.example.paquete.Paquete;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HelloController implements Runnable {
    public String IP = "127.0.0.1";
    @FXML
    private VBox vbMensajes;

    public void initialize(){
        Thread hilo1 = new Thread(this);
        hilo1.start();
    }

    public void run(){
        try{
            ServerSocket servidor = new ServerSocket(8000);
            while(true){
                Socket misocket = servidor.accept(); //aceptamos todas las conecciones del exterior, abrimos mi socket
                ObjectInputStream paqueteEntreda = new ObjectInputStream(misocket.getInputStream());
                Paquete paqueteRecibido = (Paquete) paqueteEntreda.readObject();
                //DataInputStream flujo_entrada = new DataInputStream(misocket.getInputStream()); //llegan mi flujo de datos en binario
                //String mensaje = flujo_entrada.readUTF();//convertir a string
                //this.txt_recibido.setText(mensaje+ "");
                System.out.println(paqueteRecibido.getMensaje());
                Platform.runLater(() -> {
                    //txt_recibido.setText(mensaje);
                    vbMensajes.getChildren().add(new Label(paqueteRecibido.getEmisor()+ ": " + paqueteRecibido.getMensaje() + "  " + paqueteRecibido.getTiempo()+"\n"));
                });
                Socket Socketcliente = new Socket(IP, paqueteRecibido.getpPuertoR());
                ObjectOutputStream paqueteSalida =  new ObjectOutputStream(Socketcliente.getOutputStream());
                paqueteSalida.writeObject(paqueteRecibido);
                Socketcliente.close();


                misocket.close();
            }

        }
        catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}


