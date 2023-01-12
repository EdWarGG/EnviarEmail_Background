package dad.enviaremail_background;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
	
	public static  Stage primaryStage;
	
	private Controller controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		App.primaryStage = primaryStage;
		
		controller = new Controller();
		
		Scene scene = new Scene(controller.getView());
		
		primaryStage.setTitle("Enviar email");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("/images/email-send-icon-32x32.png"));
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}



}
