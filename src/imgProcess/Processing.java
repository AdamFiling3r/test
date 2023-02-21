package imgProcess;

import javafx.scene.image.Image;
import org.opencv.core.*;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import imgProcess.SVMC;


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

    public Image processImage(File image) {

        Mat source = toGrayScale(image);

        //binarizing the image
        int adapt = Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
        int thresh = Imgproc.THRESH_BINARY_INV;
        Imgproc.adaptiveThreshold(source, source, 225, adapt, thresh, 15, 2);

        //source = skeleton(source);


        source = getNeighbors(source);

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
    private Mat skeleton(Mat img) {

        boolean done = false;

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5
        ));
        Mat eroded = new Mat();
        Mat temp = new Mat();
        Mat skel = new Mat(img.rows(), img.cols(), CvType.CV_8UC1, new Scalar(0));

        int size = img.cols() * img.rows();
        int zeros = 0;

        while (!done) {
            Imgproc.erode(img, eroded, element);
            Imgproc.dilate(eroded, temp, element);
            Core.subtract(img, temp, temp);
            Core.bitwise_or(skel, temp, skel);
            eroded.copyTo(img);

            zeros = size - Core.countNonZero(img);
            if (zeros == size)
                done = true;
        }

        return skel;
    }


    private void findLines(Mat source) {
        //detecting lines
        Mat lines = new Mat();

        double rho = 1;
        double theta = Math.PI / 180;
        int thresh = 150;

        Imgproc.HoughLines(source, lines, rho, theta, thresh);
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rh = lines.get(x, 0)[0], the = lines.get(x, 0)[1];
            double a = Math.cos(the), b = Math.sin(the);
            double x0 = a * rh, y0 = b * rh;
            Point pt1 = new Point(Math.round(x0 + 1000 * (-b)), Math.round(y0 + 1000 * (a)));
            Point pt2 = new Point(Math.round(x0 - 1000 * (-b)), Math.round(y0 - 1000 * (a)));
            Imgproc.line(source, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

    }

    public static Mat getNeighbors(Mat image) {

        //loopin
        double white = 225.0;
        Mat finished = new Mat();
        image.copyTo(finished);
        double part_width = image.width()/5;
        double part_height = image.height()/5;
        System.out.println(image.cols());
        for (double i = 0; i < image.rows() - part_height; i += part_height) {
            for (double j = 0; j < image.cols() - part_width; j += part_width) {

                /*if (image.get(i, j)[0] == white) {

                    double left = image.get(i, j - 1)[0];
                    double right = image.get(i, j + 1)[0];
                    double up = image.get(i - 1, j)[0];
                    double down = image.get(i + 1, j)[0];
                    int neighbors = 0;

                    if (left == white) {
                        neighbors++;
                    }
                    if (right == white) {
                        neighbors++;
                    }
                    if (up == white) {
                        neighbors++;
                    }
                    if (down == white) {
                        neighbors++;
                    }

                    if (neighbors == 2) {
                        System.out.println(i + ", " + j + ": has less than 2 neighbors");
                        Imgproc.circle(finished, new Point(i, j), 10, new Scalar(225.0, 0, 0));
                    }
                }*/
                System.out.println("J: " + j);
                System.out.println("I: " + i);
                System.out.println("Widht: " + part_width);
                System.out.println("Height: " + part_height);
                Rect roi = new Rect((int) j, (int) i, (int)part_width, (int)part_height);

                Mat predImg = image.submat(roi).clone();
                Imgproc.line(finished, new Point(j, i), new Point(j, i + part_height), new Scalar(205.0, 310.0, 30.0));
                Imgproc.line(finished, new Point(j, i), new Point(j + part_width, i), new Scalar(205.0, 310.0, 30.0));
                Imgproc.line(finished, new Point(j, i + part_height), new Point(j + part_width, i + part_height), new Scalar(205.0, 310.0, 30.0));
                Imgproc.line(finished, new Point(j + part_width, i), new Point(j + part_width, i + part_height), new Scalar(205.0, 310.0, 30.0));

            }
        }

        return finished;
    }
}
