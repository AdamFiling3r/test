package imgProcess;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
            int[] labels = {iter, iter, iter, iter};
            label = new Mat(4, 1, CvType.CV_32S);
            label.put(0, 0, labels);


            for (File f2 :
                    subFolder.listFiles()) {
                    pics.add(Imgcodecs.imread(f2.getAbsolutePath()));

            }

            for(int i = 0; i < pics.size(); i++){
                Mat pic = pics.get(i);
                Imgproc.cvtColor(pic, pic, Imgproc.COLOR_RGB2GRAY);
                pic.convertTo(pic, CvType.CV_32F);
                Imgproc.cvtColor(label, label, Imgproc.COLOR_RGB2GRAY);
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
