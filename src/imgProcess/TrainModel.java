package imgProcess;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TrainModel {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        List<Mat> pics = new ArrayList<>();
        List<Mat> labels = new ArrayList<>();
        int[] labels2 = {0, 1, 2, 3, 4};

        File trainFolder = new File("img\\train");
        File labelsFolder = new File("img\\lalbels");

        for (File f :
                trainFolder.listFiles()) {

            for (File img:
                  f.listFiles()) {


                Mat tmp = Imgcodecs.imread(img.getAbsolutePath());
                Imgproc.resize(tmp, tmp, new Size(28, 28));
                pics.add(tmp);

            }

        }

        for (File f :
                labelsFolder.listFiles()) {

            labels.add(Imgcodecs.imread(f.getAbsolutePath()));

        }

        int imgNum = pics.size();
        int outNum = 5;

        INDArray input = Nd4j.create(imgNum, 784);
        INDArray knownOut = Nd4j.create(imgNum, outNum);

        for(int i = 0; i < imgNum; i++){

            Mat tmp = pics.get(i);
            INDArray in = Nd4j.create(tmp.rows(), tmp.cols());
            for (int x = 0; i < tmp.rows(); i++) {
                for (int j = 0; j < tmp.cols(); j++) {
                    double[] pixel = tmp.get(i, j);
                    in.putScalar(x, j, pixel[0] / 255.0);
                }
            }
            INDArray arr = in;

            input.putRow(i, arr.reshape(1, 784));

            int label = labels2[i];
            knownOut.putScalar(i, label, 1.0);
        }

        DataSet trainData = new DataSet(input, knownOut);











    }

}
