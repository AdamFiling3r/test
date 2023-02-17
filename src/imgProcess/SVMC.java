package imgProcess;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SVMC {


    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        SVM svm = SVM.create();
        svm.setType(SVM.C_SVC);
        svm.setKernel(SVM.LINEAR);
        svm.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 100, 1e-6));

        List<Mat> pics = new ArrayList<>();
        Mat label;

        File folder = new File("img\\train");
        int iter = 0;
        for (File f :
                folder.listFiles()) {
            File subFolder = new File(f.getAbsolutePath());

            File labelDir = new File("img\\labels");
            label = Imgcodecs.imread(labelDir.listFiles()[iter].getAbsolutePath());

            for (File f2 :
                    subFolder.listFiles()) {
                    pics.add(Imgcodecs.imread(f2.getAbsolutePath()));

            }

            for(int i = 0; i < pics.size(); i++){
                Mat pic = pics.get(i);
                pic.convertTo(pic, CvType.CV_32F);
                label.convertTo(label, CvType.CV_32SC1);
                svm.train(pics.get(i), Ml.ROW_SAMPLE, label);
            }
            iter++;
        }

        svm.save("SVM\\svm.xml");



    }



//    public static float prediction(Mat pic){
//
//        return classifier.predict(pic);
//
//    }





}
