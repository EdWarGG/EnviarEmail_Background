package dad.enviaremail_background;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class Controller implements Initializable{
	
	//model
	StringProperty servidor = new SimpleStringProperty();
	StringProperty puerto = new SimpleStringProperty();
	StringProperty remitente = new SimpleStringProperty();
	StringProperty contraseña = new SimpleStringProperty();
	StringProperty destinatario = new SimpleStringProperty();
	StringProperty asunto = new SimpleStringProperty();
	StringProperty mensaje = new SimpleStringProperty();
	BooleanProperty ssl = new SimpleBooleanProperty();
	private Task<Void> tarea;

	
	//view
    @FXML
    private AnchorPane view;
    @FXML
    private TextField servidorText, puertoText, remitenteText, destinatarioText, asuntoText;
    @FXML
    private PasswordField contraseñaText;
    @FXML
    private CheckBox sslCheck;
    @FXML
    private TextArea mensajeText;
    @FXML
    private Button enviarButton, vaciarButton, cerrarButton;


    public Controller() throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
    	loader.setController(this);
    	loader.load();
    }
    
    
	public void initialize(URL location, ResourceBundle resources) {
		//bindings
		servidor.bindBidirectional(servidorText.textProperty());
		puerto.bindBidirectional(puertoText.textProperty());
		remitente.bindBidirectional(remitenteText.textProperty());
		contraseña.bindBidirectional(contraseñaText.textProperty());
		destinatario.bindBidirectional(destinatarioText.textProperty());
		asunto.bindBidirectional(asuntoText.textProperty());
		mensaje.bindBidirectional(mensajeText.textProperty());
		ssl.bindBidirectional(sslCheck.selectedProperty());
	}

	
	public AnchorPane getView() {
		return view;
	}

	
    @FXML
    void onCerrarAction(ActionEvent event) {
    	App.primaryStage.close();
    }

    
    @FXML
    void onEnviarAction(ActionEvent event) {	
	   	tarea = new Task<Void>() {
	    	@Override
	    	protected Void call() throws Exception {
		    	Email email = new SimpleEmail();
		    	email.setHostName(servidor.getValue());
		    	email.setSmtpPort(Integer.parseInt(puerto.getValue()));
		    	email.setAuthenticator(new DefaultAuthenticator(remitente.getValue(),contraseña.getValue() ));
		   		email.setSSLOnConnect(ssl.getValue());
		   		email.setFrom(remitente.getValue());
		   		email.setSubject(asunto.getValue());
		   		email.setMsg(mensaje.getValue());
		   		email.addTo(destinatario.getValue());
		    	email.send();
	    		Thread.sleep(10L);
	    		return null;
	    	}
		};
		
		tarea.setOnSucceeded(e -> {
			Alert alertAcierto = new Alert(AlertType.INFORMATION);
    		alertAcierto.setTitle("Mensaje enviado");
    		alertAcierto.setHeaderText("Mensaje enviado con éxito a '" + destinatario.getValue() + "'.");
    		alertAcierto.showAndWait();
		});

		tarea.setOnFailed(e -> {
			Alert alertError = new Alert(AlertType.ERROR);
			alertError.setTitle("Error");
			alertError.setHeaderText("No se pudo enviar el email.");
			alertError.setContentText("Invalid message supplied");
			alertError.showAndWait();
		});

		new Thread(tarea).start();
	}
    

    @FXML
    void onVaciarAction(ActionEvent event) {
    	servidor.setValue("");
    	puerto.setValue("");
    	remitente.setValue("");
    	contraseña.setValue("");
        destinatario.setValue("");
    	asunto.setValue("");
    	mensaje.setValue("");
    	ssl.setValue(false);
    }

}
