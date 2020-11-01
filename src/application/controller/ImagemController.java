package application.controller;

import java.io.File;
import java.text.DecimalFormat;

import application.controller.dados.CaracteristicasImagem;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ImagemController {

    @FXML
    private TextField caracteristicasCabeloMel;

    @FXML
    private TextField caracteristicasCalcaMel;

    @FXML
    private TextField caracteristicasClasse;
    
    @FXML
    private TextField caracteristicasChance;

    @FXML
    private TextField caracteristicasRoupaWinggum;

    @FXML
    private TextField caracteristicasEstrelaWinggum;

    @FXML
    private ImageView VisualizaImagemAntes;

    @FXML
    private ImageView VisualizaImagemDepois;

	
    
    @FXML
    void GerarARFF(MouseEvent event) {
    	System.out.println("Gerando ARFF");
    	
    	CaracteristicasImagem.extrair();
    }
	
	@FXML
	void VerImagem(MouseEvent event) {
		System.out.println("Ver Imagem");
		File f = buscaImg();
		if(f != null) {
			Image img = new Image(f.toURI().toString());
			
			//VisualizaImagemAntes.setFitWidth(img.getWidth());
			//VisualizaImagemAntes.setFitHeight(img.getHeight());
			double[] caracteristicas = CaracteristicasImagem.extraiCaracteristicas(f);
			
			VisualizaImagemAntes.setImage(img);
			VisualizaImagemDepois.setImage(CaracteristicasImagem.imagemDepois);
			
			int texto = 0;
			int classe = 0;
			
			for (double d : caracteristicas) {
				//System.out.println(d);
				texto++;
				
				if (texto == 1) {
					caracteristicasCabeloMel.setText(""+d);
				}
				if (texto == 2) {
					caracteristicasCalcaMel.setText(""+d);
				}
				if (texto == 3) {
					caracteristicasRoupaWinggum.setText(""+d);
				}
				if (texto == 4) {
					caracteristicasEstrelaWinggum.setText(""+d);
				}
				if (texto == 5) {
					if (d == 0) {
						caracteristicasClasse.setText("Mel");
						classe = 1;
					}else {
						caracteristicasClasse.setText("Winggum");
						classe = 2;
					}
				}
			}
			caracteristicas = naiveBayes(caracteristicas);
			System.out.println("Chance de ser o Mel: "+caracteristicas[0]);
			System.out.println("Chance de ser o Winggum: "+caracteristicas[1]);
			
			//String porcentagem = ""+(caracteristicas[0] * 10)+"%";
			
			DecimalFormat df = new DecimalFormat("###.##");
			
			String porcentagem;
			if (classe == 1) {
				porcentagem = df.format(caracteristicas[0] * 100)+"%";
			}else {
				porcentagem = df.format(caracteristicas[1] * 100)+"%";
			}
			
			caracteristicasChance.setText(porcentagem);
			
		}
	}
		
	private File buscaImg() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new 
				   FileChooser.ExtensionFilter(
						   "Imagens", "*.jpg", "*.JPG", 
						   "*.png", "*.PNG", "*.gif", "*.GIF", 
						   "*.bmp", "*.BMP")); 	
		 fileChooser.setInitialDirectory(new File("src/application/images/Simpsons Misturado"));
		 File imgSelec = fileChooser.showOpenDialog(null);
		 try {
			 if (imgSelec != null) {
			    return imgSelec;
			 }
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return null;
	}
    
    
	
	
	public static double[] naiveBayes(double[]caracteristicas) {
		double[] retorno = {0,0};
		try {
		
			//*******carregar arquivo de características
			DataSource ds = new DataSource("caracteristicasImagem.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes()-1);
			
			//Classifica com base nas características da imagem selecionada
			//NaiveBayes nb = new NaiveBayes();
			//nb.buildClassifier(instancias);//aprendizagem (tabela de probabilidades)
			
			J48 arvore = new J48();
			arvore.buildClassifier(instancias);
			//arvore.setUnpruned(true);
			
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
