/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;



import org.opencv.core.Core;

/**
 *
 * @author Nomi
 */
public class Application {
    
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filename = "D:\\1Downloads\\Firefox downloads\\tree.jpg";

        Analyzer analyzer = new Analyzer(filename);
        System.out.println("\n\nTHE RESULT: " + analyzer.calcCombinedAestheticScore());
        
        
        
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
