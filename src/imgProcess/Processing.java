package imgProcess;

import javafx.scene.image.Image;
import jdk.jfr.internal.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Processing {


    public Image toGrayScale(File image) throws InterruptedException {


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String path = "C:/Users/Thinkpad/Pictures/mikey.jpg";
        Mat source = Imgcodecs.imread(path);

        Mat destination = new Mat();

        // Converting the image to gray scale and
        // saving it in the dst matrix
        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);

        destination.convertTo(destination, CvType.CV_8U);

        // save the matrix data to a file in JPEG format
        String tempPath = "src/imgProcess/tempImg/temp.jpg";
        if(Imgcodecs.imwrite(tempPath, destination)){
            System.out.println("success");


        }else{
            System.out.println("failed");
        }

        URL tempURL = getClass().getResource("tempImg/temp.jpg");
        System.out.println(tempURL.getPath());
        File tempFile = new File(tempURL.getPath());
        Image comp = new Image(tempFile.toURI().toString());
        tempFile.delete();




        return comp;

    }


}
