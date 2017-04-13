/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;

import facedetection.FaceDetector;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import linedetection.Line;
import linedetection.LineDetector;
import objectdetection.ObjectDetector;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 *
 * @author Nomi
 */
public class Analyzer {
    private Mat img; 
    private ObjectDetector objectDetector;
    private FaceDetector faceDetector;
    private LineDetector lineDetector;
    
    private List<Rect> objectList;
    private List<Rect> faceList;
    private List<Line> lineList;
    private List<Line> diagonalLineList;
    
    private RuleOfThirdsAnalyzer ruleOfThirdsAnalyzer;
    private DiagonalDominanceAnalyzer diagonalDominanceAnalyzer;
    private VisualBalanceAnalyzer visualBalanceAnalyzer;
    
    private AestheticScoreCalculator aestheticScoreCalculator;
    private RegionSizeAnalyzer regionSizeAnalyzer;
    
    private int x;
    private int y;
    private int width;
    private int height;
    
    public Analyzer(String fileName) {
        objectDetector = new ObjectDetector(fileName);
        faceDetector = new FaceDetector(fileName);
        lineDetector = new LineDetector(fileName);
        img = objectDetector.getImg();
        
        x = 0;
        y = 0;
        width = img.width();
        height = img.height();
        
        showImage(mat2BufferedImage(img));
        
        objectDetector.findObjects();
        objectList = objectDetector.getMainObjects();
        faceDetector.findFaces();
        faceList = faceDetector.getFaceList();
        lineDetector.findLines();
        lineList = lineDetector.getLineList();
        diagonalLineList = lineDetector.getDiagonalLineList();

        showEvaluationSteps();
        
        aestheticScoreCalculator = new AestheticScoreCalculator(img, objectList, faceList, lineList, diagonalLineList);
        regionSizeAnalyzer = new RegionSizeAnalyzer(img, objectList, faceList);
    }
    
    public void setFrame(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public double calcCombinedAestheticScore() {
        double wSZ = 0.08;
        
        long startTime = System.currentTimeMillis();
        double aestheticScore = 0;
        double regionsSizeScore = 0;
        //for (int i = 0; i < 20000; ++i) {   
            aestheticScoreCalculator.setFrame(x, y, width, height);
            aestheticScore = aestheticScoreCalculator.calcAestheticScore();
            regionSizeAnalyzer.setFrame(x, y, width, height);
            regionsSizeScore = regionSizeAnalyzer.calcRegionSize();
        //}
        
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("IDOOO==== " + estimatedTime );
        return ((1-wSZ) * aestheticScore + wSZ * regionsSizeScore);
        

    }
    
    public void showEvaluationSteps() {
        //about object detection
        showImage(mat2BufferedImage(objectDetector.getImgMeanShifted()));
        showImage(mat2BufferedImage(objectDetector.getImgCanny()));
        showImage(mat2BufferedImage(objectDetector.getImgOut()));
        
        //about face detection
        showImage(mat2BufferedImage(faceDetector.getImg()));
        
        //about line detection
        showImage(mat2BufferedImage(lineDetector.getEdgeDetectedImg()));
        showImage(mat2BufferedImage(lineDetector.getImg()));
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
    
    public void showImage(BufferedImage img) {
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
