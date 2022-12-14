package imgProcess;

import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Processing {

    public ArrayList<double[]> pixels = new ArrayList<>();

    private Mat toGrayScale(File image) {


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String path = image.getAbsolutePath();
        Mat source = Imgcodecs.imread(path);

        Mat destination = new Mat();

        // Converting the image to gray scale and
        // saving it in the dst matrix
        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);

        return destination;

    }

    public Image processImage(File image){

        Mat source = toGrayScale(image);

        //binarizing the image
        int adapt = Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
        int thresh = Imgproc.THRESH_BINARY_INV;
        Imgproc.adaptiveThreshold(source, source, 225, adapt, thresh, 11, 2);



        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".bmp", source, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));


    }

    /*private ArrayList<double[]> interateImage(Mat source){

        ArrayList<double[]> pixel = new ArrayList<>();
        // Go through the image and print the value of each pixel
        for (int i = 0; i < source.rows(); i++) {
            for (int j = 0; j < source.cols(); j++) {
                pixel.add(source.get(i, j));

            }
        }
        return pixel;
    }*/

    private void findLines(Mat source){
        //detecting lines
        Mat lines = new Mat();

        double rho = 1;
        double theta = Math.PI/180;
        int thresh = 150;

        Imgproc.HoughLines(source, lines, rho, theta, thresh);
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rh = lines.get(x, 0)[0], the = lines.get(x, 0)[1];
            double a = Math.cos(the), b = Math.sin(the);
            double x0 = a*rh, y0 = b*rh;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(source, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

    }




}
