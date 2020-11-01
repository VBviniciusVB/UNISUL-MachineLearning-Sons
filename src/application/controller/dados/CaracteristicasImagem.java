package application.controller.dados;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileOutputStream;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class CaracteristicasImagem {	
	
		public static Image imagemDepois;
	
		public static double[] extraiCaracteristicas(File f) {
			
			double[] caracteristicas = new double[5];
			
			//Caracteristicas
			// caracteristica1 = Cabelo Mel
			// caracteristica2 = Calça Mel
			// caracteristica3 = Roupa Winggum
			// caracteristica4 = Estrela Winggum
			
			double caracteristica1 = 0;
			double caracteristica2 = 0;
			double caracteristica3 = 0;
			double caracteristica4 = 0;
			
			Image img = new Image(f.toURI().toString());
			PixelReader pr = img.getPixelReader();
					
			Mat imagemOriginal = Imgcodecs.imread(f.getPath());
	        Mat imagemProcessada = imagemOriginal.clone();

			int w = (int)img.getWidth();
			int h = (int)img.getHeight();
			
			for(int i=0; i<h; i++) {
				for(int j=0; j<w; j++) {
					
					Color cor = pr.getColor(j,i);
					
					double r = cor.getRed()*255; 
					double g = cor.getGreen()*255;
					double b = cor.getBlue()*255;
					
					if(Caracteristica1(r, g, b) && i<(h/2.3)) {
						caracteristica1++;
						imagemProcessada.put(i, j, new double[]{0, 255, 0});
					}
					if(Caracteristica2(r, g, b)) {
						caracteristica2++;
						imagemProcessada.put(i, j, new double[]{255, 128, 0});
					}
					
					if(Caracteristica3(r, g, b) ) {
						caracteristica3++;
						imagemProcessada.put(i, j, new double[]{0, 0, 255});
					}
					if (Caracteristica4(r, g, b)) {
						caracteristica4++;
						imagemProcessada.put(i, j, new double[]{255, 0, 255});
					}
					
				}
			}
			
			//Mostrar imagem depois de alterada
			imagemDepois = SwingFXUtils.toFXImage(toBufferedImage(imagemProcessada), null );
	        //imagemDepois = toBufferedImage(imagemProcessada);
			
			// Normaliza as características pelo número de pixels totais da imagem para %
			caracteristica1 = (caracteristica1 / (w * h)) * 100;
			caracteristica2 = (caracteristica2 / (w * h)) * 100;
			caracteristica3 = (caracteristica3 / (w * h)) * 100;
			caracteristica4 = (caracteristica4 / (w * h)) * 100;
	        
	        caracteristicas[0] = caracteristica1;
	        caracteristicas[1] = caracteristica2;
	        caracteristicas[2] = caracteristica3;
	        caracteristicas[3] = caracteristica4;
	        //APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
	        caracteristicas[4] = f.getName().charAt(8)==' '?0:1;
			
			//HighGui.imshow("Imagem original", imagemOriginal);
	        //HighGui.imshow("Imagem processada", imagemProcessada);
	        	        
	        //HighGui.waitKey(10);
			
			return caracteristicas;
		}
		
		//Cabelo Mel
		public static boolean Caracteristica1(double r, double g, double b) {
			 
			 //Desenho Novo
			 if (b >= 125 && b <= 205 &&  g >= 140 && g <= 200 &&  r >= 25 && r <= 110 && (b - 10) < g && ((b - 40) > r)) {                       
	         	return true;
	         }
			 
			 //Desenho Antigo
			 if (b >= 130 && b <= 170 &&  g >= 160 && g <= 190 &&  r >= 110 && r <= 135) {                       
		         	return true;
		     }
			 
			 return false;
		}
		
		//Calça Mel		
		public static boolean Caracteristica2(double r, double g, double b) {
			
			//Variação 1
			if (b >= 40 && b <= 65 &&  g >= 90 && g <= 140 &&  r >= 75 && r <= 100) {                       
				return true;
			}
			
			//Variação 2
			if (b >= 0 && b <= 25 &&  g >= 110 && g <= 145 &&  r >= 50 && r <= 85) {                       
				return true;
			}
			
			//Variação 3
			if (b >= 30 && b <= 45 &&  g >= 90 && g <= 110 &&  r >= 65 && r <= 85) {                       
				return true;
			}
			
			//Variação 4
			if (b >= 0 && b <= 15 &&  g >= 85 && g <= 95 &&  r >= 60 && r <= 85) {                       
				return true;
			}
			
			return false;
		}
		
		//Roupa Winggum
		public static boolean Caracteristica3(double r, double g, double b) {
			
			//Azul Escuro
			if (b >= 70 && b <= 130 &&  g >= 50 && g <= 75 &&  r >= 20 && r <= 40 && (r+g+b)/3 < 70 && (r+g+b)/3 > 50) {                       
				return true;
			}
			
			//Azul Super Escuro
			if (b >= 90 && b <= 110 &&  g >= 45 && g <= 65 &&  r >= 30 && r <= 50) {                       
				return true;
			}
			
			//Azul Meio Escuro
			if (b >= 115 && b <= 140 &&  g >= 85 && g <= 115 &&  r >= 10 && r <= 50 && (r+g+b)/3 < 100 && (r+g+b)/3 > 60) {                       
				return true;
			}
			
			//Azul Esverdiado Escuro
			if (b >= 90 && b <= 125 &&  g >= 75 && g <= 100 &&  r >= 40 && r <= 65) {                       
				return true;
			}
			
			//Azul Super Claro
			if (b >= 150 && b <= 255 &&  g >= 90 && g <= 155 &&  r >= 0 && r <= 30) {                       
				return true;
			}
			
			//Azul Claro
			if (b >= 135 && b <= 160 &&  g >= 95 && g <= 120 &&  r >= 50 && r <= 80) {                       
				return true;
			}
			
			//Azul Claro Escurecido
			if (b >= 150 && b <= 200 &&  g >= 95 && g <= 115 &&  r >= 85 && r <= 110) {                       
				return true;
			}
			
			//Azul Ultra Claro
			if (b >= 200 && b <= 255 &&  g >= 135 && g <= 160 &&  r >= 0 && r <= 100) {                       
				return true;
			}
			
			//Azul Esverdiado
			if (b >= 125 && b <= 145 &&  g >= 115 && g <= 135 &&  r >= 45 && r <= 75) {                       
				return true;
			}
			
			//Azul Avermelhado
			if (b >= 175 && b <= 205 &&  g >= 110 && g <= 125 &&  r >= 105 && r <= 120) {                       
				return true;
			}
			
			return false;
		}
		
		//Estrela Winggum
		public static boolean Caracteristica4(double r, double g, double b) {
			
			//Amarelo
			if (b >= 160 && b <= 185 &&  g >= 185 && g <= 215 &&  r >= 185 && r <= 225 && (b+25) < r && (b+25) < g ) {                       
				return true;
			}
			
			//Amarelo Escuro
			if (b >= 140 && b <= 170 &&  g >= 175 && g <= 195 &&  r >= 180 && r <= 195) {                       
				return true;
			}
			
			//Amarelo Super Claro
			if (b >= 180 && b <= 195 &&  g >= 240 && g <= 255 &&  r >= 240 && r <= 255) {                       
				return true;
			}
			
			
			
			return false;
		}

		public static void extrair() {
					
		    // Cabeçalho do arquivo Weka
			String exportacao = "@relation caracteristicas\n\n";
			exportacao += "@attribute cabelo_mel real\n";
			exportacao += "@attribute calca_mel real\n";
			exportacao += "@attribute roupa_winggum real\n";
			exportacao += "@attribute estrela_winggum real\n";
			exportacao += "@attribute classe {Mel, Winggum}\n\n";
			exportacao += "@data\n";
				        
		    // Diretório onde estão armazenadas as imagens
		    File diretorio = new File("src\\application\\images\\Simpsons Misturado");
		    File[] arquivos = diretorio.listFiles();
		    
	        // Definição do vetor de características
	        double[][] caracteristicas = new double[1026][5];
	        
	        // Percorre todas as imagens do diretório
	        int cont = -1;
	        for (File imagem : arquivos) {
	        	cont++;
	        	caracteristicas[cont] = extraiCaracteristicas(imagem);
	        	
	        	String classe = caracteristicas[cont][4] == 0 ?"Mel":"Winggum";
	        	
	        	System.out.println("Cabelo Mel: " + caracteristicas[cont][0] 
	            		+ " - Calça Mel: " + caracteristicas[cont][1]
	            		+ " - Roupa Winggum: " + caracteristicas[cont][2] 
	            		+ " - Estrela Winggum: " + caracteristicas[cont][3] 
	            		+ " - Classe: " + classe);
	        	
	        	exportacao += caracteristicas[cont][0] + "," 
	                    + caracteristicas[cont][1] + "," 
	        		    + caracteristicas[cont][2] + "," 
	                    + caracteristicas[cont][3] + "," 
	                    + classe + "\n";
	        }
	        
	     // Grava o arquivo ARFF no disco
	        try {
	        	File arquivo = new File("caracteristicasImagem.arff");
	        	FileOutputStream f = new FileOutputStream(arquivo);
	        	f.write(exportacao.getBytes());
	        	f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			
		}

		//Transforma MAT em BUFFEREDIMAGE
		private static BufferedImage toBufferedImage(Mat m) {
		    if (!m.empty()) {
		        int type = BufferedImage.TYPE_BYTE_GRAY;
		        if (m.channels() > 1) {
		            type = BufferedImage.TYPE_3BYTE_BGR;
		        }
		        int bufferSize = m.channels() * m.cols() * m.rows();
		        byte[] b = new byte[bufferSize];
		        m.get(0, 0, b); // get all the pixels
		        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		        System.arraycopy(b, 0, targetPixels, 0, b.length);
		        return image;
		    }
		    
		    return null;
		}
}