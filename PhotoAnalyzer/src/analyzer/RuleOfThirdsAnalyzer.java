/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import linedetection.Line;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Nomi
 */
public class RuleOfThirdsAnalyzer {
    
    private Mat img; 
    private List<Rect> objectList;
    private List<Rect> faceList;
    private List<Line> lineList;
    
    //metric variables
    private double sumOfMass;
    
    private List<Point> powerPoints;
    
    public RuleOfThirdsAnalyzer(Mat image, List<Rect> oList, List<Rect> fList, List<Line> lList) {
        img = image;
        objectList = oList;
        faceList = fList;
        lineList = lList;
        
        sumOfMass = 0 ;
        powerPoints = new ArrayList();
        calculatePowerPoints();
        System.out.println(calcEPoint());
    }
    
    public double calcSumOfMass() {
        double imgArea = img.width() * img.height();
        for (Rect rect : objectList) {
            sumOfMass += rect.area();
        }
        for (Rect rect : faceList) {
            sumOfMass += rect.area();
        }    
        return sumOfMass;
    }
     
    private void calculatePowerPoints() {
        int width = img.width();
        int height = img.height();
        powerPoints.add(new Point((width-1)/3, (height-1)/3));
        powerPoints.add(new Point((width-1)/3 * 2, (height-1)/3));
        powerPoints.add(new Point((width-1)/3, (height-1)/3 * 2));
        powerPoints.add(new Point((width-1)/3 * 2, (height-1)/3 * 2));
    }
    
    private double minDistToPowerPoints(Rect rect) {
        Point center = new Point(rect.x + (rect.width - 1) / 2, rect.y + (rect.height - 1) / 2);
        Imgproc.drawMarker(img, center, new Scalar(255,0,0));
        Imgproc.rectangle(img, rect.tl(), rect.br(), new Scalar(255, 0, 0));
        showImage(mat2BufferedImage(img));
        double min = 100000000;
        double actualDist = 0;
        for (Point p : powerPoints) {
            actualDist = distanceBtwPoints(center, p);
            if (actualDist < min) {
                min = actualDist;
            }
        }
        System.out.println(min);
        return min;
    }
    
    private double distanceBtwPoints(Point a, Point b) {
        double xDiff = Math.abs(a.x - b.x);
        double yDiff = Math.abs(a.y - b.y);
        return xDiff / img.width() + yDiff / img.height();
    }
    
    private double calcEPoint() {
        double ePoint = calcSumOfMass();
        double sum = 0;
        for (Rect rect : objectList) {
            sum = sum +  (rect.area() * Math.exp((-1 * Math.pow(minDistToPowerPoints(rect), 2)) / (2 * 0.17)));
        }
        for (Rect rect : faceList) {
            sum = sum +  (rect.area()* 3/100 * Math.exp((-1 * Math.pow(minDistToPowerPoints(rect), 2)) / (2 * 0.17)));
        }
        return (1/ePoint * sum);
    }
    
    private BufferedImage mat2BufferedImage(Mat m){
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
