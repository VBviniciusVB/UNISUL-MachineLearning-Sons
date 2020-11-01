package application.controller;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane PainelPrincipal;

    //FXML Controller
    FXMLLoader ImagemFX = new FXMLLoader();
    FXMLLoader AudioFX = new FXMLLoader();
    
    //Janelas
    BorderPane Imagem;
    BorderPane Audio;
    
    // Comandos
    
    public void initialize() throws IOException {
    	
    	//Inicializar as janelas
    	
    	ImagemFX.setLocation(Main.class.getResource("controller/view/Imagem.fxml"));
    	Imagem = ImagemFX.load();
    	
    	AudioFX.setLocation(Main.class.getResource("controller/view/Audio.fxml"));
    	Audio = AudioFX.load();
    	
    	System.out.println("Inicialização do APP completa");
    	
    }
    
    // ---
    
    @FXML
    void Imagem(MouseEvent event){
    	PainelPrincipal.setCenter(Imagem);
    }
    
    
    @FXML
    void Audio(MouseEvent event){
    	PainelPrincipal.setCenter(Audio);
    }
    

}