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
        String filename = "D:\\1Downloads\\Firefox downloads\\man22.jpg";

        Analyzer analyzer = new Analyzer(filename);
        System.out.println("\n\nTHE RESULT: " + analyzer.calcCombinedAestheticScore());
    }
    
}
