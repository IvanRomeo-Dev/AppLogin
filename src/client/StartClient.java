package client;

import client.controller.LoginController;
import client.model.ClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

public class StartClient extends Application {


    @Override
    public void start(Stage primaryStage){
        try{
            final ClientUtil clientUtil=new ClientUtil();
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/client/gui/Login.fxml"));
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aclass){
                    return new LoginController(clientUtil);
                }
            });
            Parent root= loader.load();
            Scene scene=new Scene(root);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Login by Ivan Romeo");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
