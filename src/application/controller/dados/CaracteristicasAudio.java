package application.controller.dados;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.math3.complex.Complex;

import com.jlibrosa.audio.JLibrosa;
import com.jlibrosa.audio.exception.FileFormatNotSupportedException;
import com.jlibrosa.audio.wavFile.WavFile;
import com.jlibrosa.audio.wavFile.WavFileException;

public class CaracteristicasAudio {	
	
		public static double[] extraiCaracteristicas(File f) throws UnsupportedAudioFileException, IOException, WavFileException, FileFormatNotSupportedException {
			
			double[] caracteristicas = new double[4];

			//Caracteristica 1 = Magnitude
			//Caracteristica 2 = MelSpectrogram
			//Caracteristica 3 = STFT
			
			double caracteristica1 = 0;
			double caracteristica2 = 0;
			double caracteristica3 = 0;
			
			//Extração de Caracteristicas
			
						
			
			//---
			float[][] magnitude = new float[0][0];
			magnitude = extraiMagnitude(f);
			
			caracteristica1 = magnitude[0][0];
			//---
			

			//---
			float[] MagnitudeFeature = new float[0];
			MagnitudeFeature = extraiMagnitudeFeature(f,magnitude);
			//System.out.println("Magnitude Feature: "+MagnitudeFeature[0]);

			JLibrosa jLibrosa2 = new JLibrosa();
			WavFile wavFile = WavFile.openWavFile(f);;
			int mSampleRate = (int) wavFile.getSampleRate();
			wavFile.close();
			float [][] melSpectrogramGerado = jLibrosa2.generateMelSpectroGram(MagnitudeFeature, mSampleRate, 2048, 128, 256);
			System.out.println("Mel Spectrogram: "+melSpectrogramGerado[0][0]);
			
			caracteristica2 = melSpectrogramGerado[0][0];
			//---
			
			JLibrosa jLibrosa3 = new JLibrosa();
			WavFile wavFile3 = WavFile.openWavFile(f);;
			int mSampleRate3 = (int) wavFile3.getSampleRate();
			wavFile3.close();
			Complex[][] stftComplexValues = jLibrosa3.generateSTFTFeatures(MagnitudeFeature, mSampleRate3, 40);
			System.out.println("Tamanho do STFT Feature: " + stftComplexValues.length + " , " + stftComplexValues[0].length);
			
			caracteristica3 = stftComplexValues[0].length;
			
			//Fim da Extração
	    
	        caracteristicas[0] = caracteristica1;
	        caracteristicas[1] = caracteristica2;
	        caracteristicas[2] = caracteristica3;
	        //APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NOS AUDIOS DE TREINAMENTO
	        caracteristicas[3] = f.getName().charAt(0)=='c'?0:1;
						
			return caracteristicas;
		}

		public static float[][] extraiMagnitude (File f) throws IOException, WavFileException{
			
			// Caracteristica 1
			
			int BUFFER_SIZE = 4096;
			int sampleRate = -1;

			WavFile wavFile = null;
			wavFile = WavFile.openWavFile(f);
			int mNumFrames = (int) (wavFile.getNumFrames());
			int mSampleRate = (int) wavFile.getSampleRate();
			int mChannels = wavFile.getNumChannels();
						
			sampleRate = mSampleRate;
			if (sampleRate != -1) {
				mSampleRate = sampleRate;
			}

			// Read the magnitude values across both the channels and save them as part of
			// multi-dimensional array
			float[][] buffer = new float[mChannels][mNumFrames];
			int frameOffset = 0;
			int loopCounter = ((mNumFrames * mChannels) / BUFFER_SIZE) + 1;
			for (int i = 0; i < loopCounter; i++) {
				frameOffset = wavFile.readFrames(buffer, mNumFrames, frameOffset);
			}

			if(wavFile != null) {
				wavFile.close();
			}
						
			System.out.println("Magnitude: "+buffer[0][0]);
						
			return buffer;
		}
		
		public static float[] extraiMagnitudeFeature (File f,float[][] magnitude) throws IOException, WavFileException, FileFormatNotSupportedException {
			
			WavFile wavFile = WavFile.openWavFile(f);;
			int mNumFrames = (int) (wavFile.getNumFrames());
			int mChannels = wavFile.getNumChannels();
			wavFile.close();
			
			DecimalFormat df = new DecimalFormat("#,#####");
			df.setRoundingMode(RoundingMode.CEILING);
			
			// take the mean of amplitude values across all the channels and convert the
			// signal to mono mode
			
			float[] melSpectrogram = new float[mNumFrames];
					
			for (int q = 0; q < mNumFrames; q++) {
				double frameVal = 0;
				for (int p = 0; p < mChannels; p++) {
					frameVal = frameVal + magnitude[p][q];
				}
				melSpectrogram[q] = Float.parseFloat(df.format(frameVal / mChannels));
			}

			return melSpectrogram;
		}
		
		public static void extrair() throws UnsupportedAudioFileException, IOException, WavFileException, FileFormatNotSupportedException {
					
		    // Cabeçalho do arquivo Weka
			String exportacao = "@relation caracteristicas\n\n";
			exportacao += "@attribute Magnitude real\n";
			exportacao += "@attribute MelSpectrogram real\n";
			exportacao += "@attribute STFT real\n";
			exportacao += "@attribute classe {Gato, Cachorro}\n\n";
			exportacao += "@data\n";
				        
		    // Diretório onde estão armazenadas as imagens
		    File diretorio = new File("src\\application\\audios\\animais");
		    File[] arquivos = diretorio.listFiles();
		    
	        // Definição do vetor de características
	        double[][] caracteristicas = new double[280][4];
	        
	        // Percorre todas as imagens do diretório
	        int cont = -1;
	        for (File audio : arquivos) {
	        	cont++;
	        	
	        	caracteristicas[cont] = extraiCaracteristicas(audio);
	        	
	        	String classe = caracteristicas[cont][3] == 0 ?"Gato":"Cachorro";
	        	
	        	System.out.println("Magnitude: " + caracteristicas[cont][0]
	            		+ " - MelSpectrogram: " + caracteristicas[cont][1]
	            		+ " - STFT: " + caracteristicas[cont][2]
	            		+ " - Classe: " + classe);
	        	
	        	exportacao += caracteristicas[cont][0] + "," 
	                    + caracteristicas[cont][1] + "," 
	        		    + caracteristicas[cont][2] + ","
	                    + classe + "\n";
	        }
	        
	     // Grava o arquivo ARFF no disco
	        try {
	        	File arquivo = new File("caracteristicasAudio.arff");
	        	FileOutputStream f = new FileOutputStream(arquivo);
	        	f.write(exportacao.getBytes());
	        	f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

}