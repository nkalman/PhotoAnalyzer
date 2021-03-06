/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import objectdetection.ObjectDetector;
import org.opencv.core.Core;

/**
 *
 * @author Nomi
 */
public class Application {
    
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filename = "D:\\1Downloads\\Firefox downloads\\eggs.jpg";
        ObjectDetector objDet = new ObjectDetector(filename);
        objDet.findObjects();
        
        showImage(objDet.getImg());
        showImage(objDet.getImgMeanShifted());
        showImage(objDet.getImgGrayscale());
        showImage(objDet.getImg());
        showImage(objDet.getMRgba());

//        FaceDetector faceDet = new FaceDetector(filename);
//        faceDet.findFaces();
//        showImage(faceDet.getImg());
       
    }
    
    
    
    public static void showImage(BufferedImage img) {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());        
        frame.setSize(img.getWidth(null)+50, img.getHeight(null)+50);     
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
