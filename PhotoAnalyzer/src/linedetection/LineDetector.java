/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linedetection;

import static analyzer.Constants.COLOR_WINDOW_RADIUS;
import static analyzer.Constants.COUNT_VALUE;
import static analyzer.Constants.EPS_VALUE;
import static analyzer.Constants.MAX_LEVEL;
import static analyzer.Constants.SPATIAL_WINDOW_RADIUS;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
import static org.opencv.core.TermCriteria.COUNT;
import static org.opencv.core.TermCriteria.EPS;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2BGR;

/**
 *
 * @author Nomi
 */
public class LineDetector {
    private Mat img;
    private Mat dst;
    private List<Line> lineList;
    
    public LineDetector(String fileName) {
        img = Imgcodecs.imread(fileName);
        dst = new Mat();
        lineList = new ArrayList();
    }
    
    public BufferedImage getImg() {
        return mat2BufferedImage(img);
    }
    
    public BufferedImage getDst() {
        return mat2BufferedImage(dst);
    }
    
    public void detectLines() {
//        TermCriteria termCriteria = new TermCriteria(COUNT + EPS, COUNT_VALUE, EPS_VALUE);
//        Imgproc.pyrMeanShiftFiltering(img, img, SPATIAL_WINDOW_RADIUS,
//                COLOR_WINDOW_RADIUS, MAX_LEVEL, termCriteria);
        Imgproc.Canny(img, dst, 100, 200, 3, true); 
        Mat lines = new Mat();
        //Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 4);


        int threshold = 50;
        int minLineSize = 20;
        int lineGap = 20;
    
        //Imgproc.HoughLinesP(img, lines, EPS, EPS, threshold, minLineSize, lineGap);
        int width = img.width();
        int height = img.height();
        double diagonal = Math.sqrt(width*width + height*height);
        
        int minOfWidthHeight = (width<height)?width:height;
        System.out.println(minOfWidthHeight);
        
        Imgproc.HoughLinesP(dst, lines, 1, Math.PI/180, minOfWidthHeight*10/100, diagonal*25/100, diagonal*4/100);

        int l = (lines.rows() < 3)?lines.rows():3;
        
        
          for (int x = 0; x < lines.rows(); x++) {
                  double[] vec = lines.get(x,0 );
                  double x1 = vec[0], 
                         y1 = vec[1],
                         x2 = vec[2],
                         y2 = vec[3];
                  Point startPoint = new Point(x1, y1);
                  Point endPoint = new Point(x2, y2);
                  
                  lineList.add(new Line(x1,y1,x2,y2));

                  Imgproc.line(img, startPoint, endPoint, new Scalar(255,0,0), 1);

          }
          
          System.out.println(lineList);
          
           Collections.sort(lineList, new Comparator<Line>() {
                @Override public int compare(Line l1, Line l2) {
                    return (int)(l2.getLength() - l1.getLength());
                }

           });
           
           System.out.println(lineList);
           
           
                for (int i=0; i<l+1; i++) {
                    if (lineList.size() >= l+1) {
                       double x1 = lineList.get(i).getX1(), 
                              y1 = lineList.get(i).getY1(),
                              x2 = lineList.get(i).getX2(),
                              y2 = lineList.get(i).getY2();
                       Point startPoint = new Point(x1, y1);
                       Point endPoint = new Point(x2, y2);

                       Imgproc.line(img, startPoint, endPoint, new Scalar(0,0,255), 1);
                    }
                }
        
        
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
