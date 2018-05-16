package test;

import layer.FCLayer;
import network.SequentialNN;
import optimizer.*;
import utils.Activation;
import utils.Loss;
import utils.MNISTUtils;
import utils.Tensor;
import utils.Utils;

public class TrainMNIST{
	public static void main(String[] args){
		SequentialNN nn = new SequentialNN(784);
		nn.add(new FCLayer(300, Activation.sigmoid));
		nn.add(new FCLayer(10, Activation.softmax));
		
		Tensor[] x = MNISTUtils.loadDataSetImages("train-images-idx3-ubyte", Integer.MAX_VALUE);
		Tensor[] y = MNISTUtils.loadDataSetLabels("train-labels-idx1-ubyte", Integer.MAX_VALUE);
		
		long start = System.currentTimeMillis();
		
		nn.fit(Utils.flattenAll(x), y, 100, 32, Loss.softmaxCrossEntropy, new AdamOptimizer(0.1), null, true, false, false);
		
		System.out.println("Training time: " + Utils.formatElapsedTime(System.currentTimeMillis() - start));
		
		nn.saveToFile("mnist_weights.nn");
		
		Tensor[] testX = MNISTUtils.loadDataSetImages("t10k-images-idx3-ubyte", Integer.MAX_VALUE);
		Tensor[] testY = MNISTUtils.loadDataSetLabels("t10k-labels-idx1-ubyte", Integer.MAX_VALUE);
		Tensor[] testResult = nn.predict(Utils.flattenAll(testX));
		
		System.out.println("Classification accuracy: " + Utils.format(Utils.classificationAccuracy(testResult, testY)));
		
		TestMNIST1.main(args);
	}
}