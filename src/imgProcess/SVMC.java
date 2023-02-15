package imgProcess;

import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SVMC {

    static SVM classifier = SVM.create();

    public static void main(String[] args) {
        List<Mat> pics = new ArrayList<>();
        Mat label = new Mat();

        File folder = new File("C:\\Users\\Thinkpad\\Documents\\ImageProcess\\traindata\\train");
        for (File f :
                folder.listFiles()) {
            File subFolder = new File(f.getAbsolutePath());
            for (File f2 :
                    subFolder.listFiles()) {

            }
        }

    }

    public static void training(List<Mat> trainingPics, Mat trainingLabel){


    for(int i = 0; i < trainingPics.size(); i++){
        classifier.train(trainingPics.get(i), Ml.ROW_SAMPLE, trainingLabel);
    }


    }

    public static float prediction(Mat pic){

        return classifier.predict(pic);

    }





}
