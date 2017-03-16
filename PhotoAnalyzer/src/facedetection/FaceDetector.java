/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facedetection;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Nomi
 */
public class FaceDetector {
    private Mat img;
    private List<Rect> faceList;
    
    public FaceDetector(String fileName) {
        img = Imgcodecs.imread(fileName);    
        faceList = new ArrayList();
    }
    
    public BufferedImage getImg() {
        return mat2BufferedImage(img); 
    }
    
    public void findFaces() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        CascadeClassifier faceDetector = new CascadeClassifier("D:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(img, faceDetections);
 
        System.out.println(faceDetections);
 
        for (Rect rect : faceDetections.toArray()) {
            faceList.add(rect);
            Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }
    }
    
    public List<Rect> getFaceList() {
        return faceList;
    }
    
    public BufferedImage mat2BufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return image;

    }
    
}
