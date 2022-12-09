package imgProcess;

import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class Processing {


    public Image toGrayScale(File image){


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String path = "C:/Users/Thinkpad/Pictures/mikey.jpg";
        Mat source = Imgcodecs.imread(path);

        Mat destination = new Mat();

        // Converting the image to gray scale and
        // saving it in the dst matrix
        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);

        // convert the matrix to 8-bit unsigned type
        destination.convertTo(destination, CvType.CV_8U);

        // save the matrix data to a file in JPEG format
        Imgcodecs.imwrite("temp.jpg", destination);

        Image comp = new Image("tempImg/temp.jpg");
        new File("tempImg/temp.jpg").delete();

        return comp;

    }


}
