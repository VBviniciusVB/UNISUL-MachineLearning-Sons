package application.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.jlibrosa.audio.exception.FileFormatNotSupportedException;
import com.jlibrosa.audio.wavFile.WavFileException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import application.controller.dados.CaracteristicasAudio;

public class AudioController {
    
	 @FXML
	 private TextField AudioNome;
	 
	 @FXML
	 private TextField AudioTempo;

	 @FXML
	 private TextField AudioVelocidade;
	 
	 @FXML
	 private TextField AudioAnimal;
	 
	 @FXML
	 private TextField AudioChance;
	 
	 @FXML
	 private TextField CaracteristicaMagnitude;

	 @FXML
	 private TextField CaracteristicaMel;

	 @FXML
	 private TextField CaracteristicaSTFT;
	 
	 @FXML
	 private Button ReproduzirAudio;
	
	 File f;
	 
	 @FXML
	 void ReproduzirAudio(MouseEvent event) {
		 
		 Media media = new Media (Paths.get(f.getPath()).toUri().toString());	
		 MediaPlayer audio = new MediaPlayer(media);
		 audio.play();	
		 
	 }
	 
    @FXML
    void GerarARFF(MouseEvent event) throws UnsupportedAudioFileException, IOException, WavFileException, FileFormatNotSupportedException {
    	System.out.println("Gerando ARFF");
    	
    	CaracteristicasAudio.extrair();
    }
	
	@FXML
	void PegarAudio(MouseEvent event) throws UnsupportedAudioFileException, IOException, WavFileException, FileFormatNotSupportedException {
		System.out.println("Pegando Audio");
		
		f = buscaAudio();
		if(f != null) {
			
			AudioNome.setText(f.getName());
			ReproduzirAudio.setDisable(false);

			double[] caracteristicas = CaracteristicasAudio.extraiCaracteristicas(f);
			
			//Exibi Caracteristicas
			
			CaracteristicaMagnitude.setText(""+caracteristicas[0]);
			CaracteristicaMel.setText(""+caracteristicas[1]);
			CaracteristicaSTFT.setText(""+caracteristicas[2]);
			
			
			
			String animal;
			if (caracteristicas[3] == 0) {
				animal = "Gato";
			}else {
				animal = "Cachorro";
			}
			
			caracteristicas = multilayerPerceptron(caracteristicas);
			AudioAnimal.setText(animal);
			String porcentagem;
			DecimalFormat df = new DecimalFormat("###.##");
			
			if (animal.contentEquals("Gato")) {
				porcentagem = df.format(caracteristicas[0] * 100) +"%";
			}else {
				porcentagem = df.format(caracteristicas[1] * 100) +"%";
			}
			
			AudioChance.setText(porcentagem);
			System.out.println("Chance de ser Gato: "+caracteristicas[0]);
			System.out.println("Chance de ser Cachorro: "+caracteristicas[1]);	
			
		}
	}
	
	private File buscaAudio() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new 
				   FileChooser.ExtensionFilter(
						   "Audios", "*.wav", "*.WAV")); 	
		 fileChooser.setInitialDirectory(new File("src/application/audios/animais"));
		 File audioSelec = fileChooser.showOpenDialog(null);
		 try {
			 if (audioSelec != null) {
			    return audioSelec;
			 }
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return null;
	}
	
	double[] multilayerPerceptron(double[]caracteristicas) {
		double[] retorno = {0,0};
		try {
		
			//*******carregar arquivo de características
			DataSource ds = new DataSource("CaracteristicasAudio.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes()-1);
			
			MultilayerPerceptron arvore = new MultilayerPerceptron();
			arvore.setTrainingTime(Integer.parseInt(AudioTempo.getText()));
			arvore.setLearningRate(Double.parseDouble(AudioVelocidade.getText()));
			arvore.setMomentum(0.25);
			arvore.setNumDecimalPlaces(2);
			arvore.setHiddenLayers("4");
			arvore.buildClassifier(instancias);
			
			Instance novo = new DenseInstance(instancias.numAttributes());
			novo.setDataset(instancias);
			novo.setValue(0, caracteristicas[0]);
			novo.setValue(1, caracteristicas[1]);
			novo.setValue(2, caracteristicas[2]);
			novo.setValue(3, caracteristicas[3]);
			
			retorno = arvore.distributionForInstance(novo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	
}
