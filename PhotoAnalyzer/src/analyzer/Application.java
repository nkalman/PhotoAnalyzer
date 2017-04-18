/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;



import cropGA.Individual;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Nomi
 */
public class Application {
    
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filename = "D:\\1Downloads\\Firefox downloads\\tree.jpg";
        Mat img;
        img = Imgcodecs.imread(filename);
        int width = img.width();
        int height = img.height();
        System.out.println("w= " + width + "  h= " + height);
        
        Individual ind = new Individual();
        ind.generateIndividual(width, height);
        
        
        
        
        
//INNENTOL
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        String filename = "D:\\1Downloads\\Firefox downloads\\tree.jpg";
//
//        Analyzer analyzer = new Analyzer(filename);
//        System.out.println("\n\nTHE RESULT: " + analyzer.calcCombinedAestheticScore());

//IDAIG KELL MAJD!!!!!
        
        
        
//        String filePath = "D:\\1Downloads\\Firefox downloads\\man22.jpg";
//        System.out.println("Sending file: " + filePath);
//         
//        try {
//            File file = new File(filePath);
//            FileInputStream fis = new FileInputStream(file);
//            BufferedInputStream inputStream = new BufferedInputStream(fis);
//            byte[] fileBytes = new byte[(int) file.length()];
//            inputStream.read(fileBytes);
//            inputStream.close();
//             
//            System.out.println(fileBytes);
//            
//            String filePath2 = "D:\\1Downloads\\Firefox downloads\\man222.jpg";
//            FileOutputStream fos = new FileOutputStream(filePath2);
//            BufferedOutputStream outputStream = new BufferedOutputStream(fos);
//            outputStream.write(fileBytes);
//            outputStream.close();
//             
//            System.out.println("Received file: " + filePath2);
//        } catch (IOException ex) {
//            System.err.println(ex);
//            throw new WebServiceException(ex);
//        } 
        
        
    }
    
}
