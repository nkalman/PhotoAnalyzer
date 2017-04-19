/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;



import cropGA.CropAlgorithm;
import cropGA.Individual;
import cropGA.Population;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Nomi
 */
public class Application {
    
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filename = "D:\\1Downloads\\Firefox downloads\\stock.jpg";
        Mat img;
        img = Imgcodecs.imread(filename);
        int width = img.width();
        int height = img.height();
        System.out.println("w= " + width + "  h= " + height);
        
        Analyzer analyzer = new Analyzer(filename);
        analyzer.setFrame(0, 0, width, height);
        
        Individual ind = new Individual(analyzer);
        ind.generateIndividual(width, height);
        System.out.println("\n\nTHE RESULT: " + analyzer.calcCombinedAestheticScore());
        
        Population myPop = new Population(100, true, width, height, analyzer);
        
        long startTime = System.currentTimeMillis();
        
        int generationCount = 0;
        while (generationCount < 500) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getAestheticScore());
            //myPop = CropAlgorithm.evolvePopulation(myPop);
            CropAlgorithm cropAlg = new CropAlgorithm(analyzer);
            myPop = cropAlg.evolvePopulation(myPop, width, height);
            System.out.println("-------------------------------------------------ACTUAL BEST:  " + myPop.getFittest().getAestheticScore());
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        Individual fittest =  myPop.getFittest();
        System.out.println(fittest);
        System.out.println(fittest.getAestheticScore());
        
        Rect roi = new Rect((int)fittest.getX(), (int)fittest.getY(),
                (int)fittest.getWidth(), (int)fittest.getHeight());
        Mat cropped = new Mat(img, roi);
        Imgcodecs.imwrite("D:\\1Downloads\\Firefox downloads\\cropped1.png",cropped);
        
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("IDOOO==== " + estimatedTime );
        
        
    }
    
}
