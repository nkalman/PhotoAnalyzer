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
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
/**
 *
 * @author Nomi
 */
public class DiagonalDominanceAnalyzer {
    private Mat img; 
    private List<Line> diagonalLineList;
    
    private List<Line> frameLines;
    
    private int frameX;
    private int frameY;
    private int frameWidth;
    private int frameHeight;
    
    public DiagonalDominanceAnalyzer(Mat image, List dList) {
        img = image;
        diagonalLineList = dList;
        
        frameLines =  new ArrayList();

        //System.out.println("DIAGONAL DOMINANCE: " + calcEDiagonalDominance());
    }
    
    private double minDistToThirdLines(Line line) {
        int width = img.width() - 1;
        int height = img.height() - 1;
        Point p1 = new Point(line.getX1(), line.getY1());
        Point p2 = new Point(line.getX2(), line.getY2());
        
        Line diag1 = new Line(0, 0, width, height);
        Line diag2 = new Line(width, 0, 0, height);
        
        //Imgproc.line(img, new Point(0, 0),new Point(width, height), new Scalar(0,255,0), 1);
        
        double dist1 = 0;
        Point closestPoint = getClosestPointOnSegment(diag1, p1);
        dist1 += distanceBtwPoints(closestPoint, p1);

        //Imgproc.line(img, closestPoint,p1, new Scalar(255,255,255), 1);
        
        closestPoint = getClosestPointOnSegment(diag1, p2);
        dist1 += distanceBtwPoints(closestPoint, p2);
        dist1 =  dist1 / 2;
        
        
        //Imgproc.line(img, closestPoint,p2, new Scalar(255,255,255), 1);
        
         //Imgproc.line(img, new Point(width, 0),new Point(0, height), new Scalar(0,255,0), 1);
        
        double dist2 = 0;
        closestPoint = getClosestPointOnSegment(diag2, p1);
        dist2 += distanceBtwPoints(closestPoint, p1);
        
        //Imgproc.line(img, closestPoint,p1, new Scalar(255,255,255), 1);
        closestPoint = getClosestPointOnSegment(diag2, p2);
        dist2 += distanceBtwPoints(closestPoint, p2);
        dist2 =  dist2 / 2;
        
        //Imgproc.line(img, closestPoint,p2, new Scalar(255,255,255), 1);
        
        
       //showImage(mat2BufferedImage(img));
        
        if (dist1 < dist2) {
            return dist1;
        }
        else {
            return dist2;
        
        }
    }
    
    public void setFrame(int x, int y, int width, int height) {
        this.frameX = x;
        this.frameY = y;
        this.frameWidth = width;
        this.frameHeight = height;
    }
    
    private double calcSumOfLines() {
        double sumOfLines = 0;
        for (Line line : diagonalLineList) {
            sumOfLines += line.getLength();
        }
        return sumOfLines;
    }
    
    public double calcEDiagonalDominance() {
        calculateFrameLines();
        actualizeLineList();
        if (diagonalLineList.size() > 0) {
            double eLine = calcSumOfLines();
            double sum = 0;
            for (Line line : diagonalLineList) {
                sum = sum +  (line.getLength() * Math.exp((-1 * Math.pow(minDistToThirdLines(line), 2)) / (2 * 0.17)));
            }
            System.out.println("ediagline: " + 1/eLine * sum);
            return (1/eLine * sum);
        }
        else {
            return 0;
        }
    }
    
    private double distanceBtwPoints(Point a, Point b) {
        double xDiff = Math.abs(a.x - b.x);
        double yDiff = Math.abs(a.y - b.y);
        return xDiff / img.width() + yDiff / img.height();
    }
    
    public Point getClosestPointOnSegment(Line line, Point p) {
        double xDelta = line.getX2() - line.getX1();
        double yDelta = line.getY2() - line.getY1();

        double u = ((p.x - line.getX1()) * xDelta + (p.y- line.getY1()) * yDelta) 
                / (xDelta * xDelta + yDelta * yDelta);

        Point closestPoint;
        if (u < 0) {
          closestPoint = new Point(line.getX1(), line.getY1());
        }
        else if (u > 1) {
          closestPoint = new Point(line.getX2(), line.getY2());
        }
        else {
          closestPoint = new Point((int) Math.round(line.getX1() + u * xDelta), 
                  (int) Math.round(line.getY1()+ u * yDelta));
        }

        return closestPoint;
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
    
    
    private Point intersPointOfTwoLines(Line l1, Line l2) {
        double x1 = l1.getX1();
        double x2 = l1.getX2();
        double y1 = l1.getY1();
        double y2 = l1.getY2();
        
        double x3 = l2.getX1();
        double x4 = l2.getX2();
        double y3 = l2.getY1();
        double y4 = l2.getY2();
        
        double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
        if (d == 0) {
            return null;
        }

        double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
        double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
        
        if (!isPointInFrame(new Point(xi,yi))) {
            return null;
        }
        return new Point(xi,yi);
    }
    
    private void calculateFrameLines() {
        frameLines = new ArrayList();
        Line line = new Line(frameX, frameY, frameX+frameWidth-1, frameY);
        frameLines.add(line);
        line = new Line(frameX, frameY, frameX, frameY+frameHeight-1);
        frameLines.add(line);
        line = new Line(frameX+frameWidth-1, frameY, frameX+frameWidth-1, frameY+frameHeight-1);
        frameLines.add(line);
        line = new Line(frameX, frameY+frameHeight-1, frameX+frameWidth-1, frameY+frameHeight-1);
        frameLines.add(line);
    }
    
    private boolean isPointInFrame(Point p) {
        if (p.x >= frameX-1 && p.x <= frameWidth+1 &&
                p.y >= frameY-1 && p.y <= frameHeight+1) {
            return true;
        }
        return false;
    }
    
    private Line getLineSegmentInFrame(Line originalLine) {
        ArrayList<Point> points = new ArrayList();
        Point intersect;
        int nullNr = 0;
        for (Line frameLine : frameLines) {
            intersect = intersPointOfTwoLines(originalLine, frameLine);
            
            if (intersect != null) {
                if (isPointOnLine(intersect, originalLine)) {
                    if (intersect.x == 0 && intersect.y == 0 && nullNr == 0) {
                        //System.out.println(intersect.x + " " + intersect.y);
                        points.add(intersect);
                        nullNr++;
                    }
                    else if (intersect.x == 0 && intersect.y == 0) {
                        //System.out.println("Meg 1 nulla de nem adom hozza");
                    }
                    else {
                        //System.out.println(intersect.x + " " + intersect.y);
                        points.add(intersect);
                    }
                }
            }
        }
        //System.out.println(points.size());
        
        if (points.size() == 2) {
            if (isPointOnLine(new Point(points.get(0).x, points.get(0).y), originalLine) &&
                    isPointOnLine(new Point(points.get(1).x, points.get(1).y), originalLine)) {
                return new Line(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);
            }
        }
        else if (points.size() == 1) {
            Point secondPoint = new Point(originalLine.getX1(), originalLine.getY1());
            if (!isPointInFrame(secondPoint)) {
                secondPoint = new Point(originalLine.getX2(), originalLine.getY2());
            }
            return new Line(points.get(0).x, points.get(0).y, secondPoint.x, secondPoint.y);
        }
        Point startPoint = new Point(originalLine.getX1(), originalLine.getY1());
        Point endPoint = new Point(originalLine.getX2(), originalLine.getY2());
        if (isPointInFrame(startPoint) && isPointInFrame(endPoint)) {
            return originalLine;
        }
        System.out.println("nil eset");
        return null;
    }
    
    private boolean isPointOnLine(Point point, Line line) {
        Point p1 = new Point(line.getX1(), line.getY1());
        Point p2 = new Point(line.getX2(), line.getY2());
        return normalDistBtwPoints(p1,point) + normalDistBtwPoints(p2, point) == normalDistBtwPoints(p1, p2);
    }
    
    private double normalDistBtwPoints(Point p1, Point p2) {
        return (Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }
    
    private void actualizeLineList() {
        List<Line> actualLines = new ArrayList();
        for (Line line : diagonalLineList) {
            Line lineInFrame = getLineSegmentInFrame(line);
            if (line != null) {
                actualLines.add(lineInFrame);
            }
        }
        diagonalLineList = actualLines;
    }
}
