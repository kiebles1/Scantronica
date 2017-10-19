package com.jensenkiebles.scantronica;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.jensenkiebles.scantronica.ScoreFragment;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements ScoreFragment.OnOkPressedListener {

	   private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

	        @Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                    Log.i("TAG", "OpenCV loaded successfully");

	                    /* Now enable camera view to start receiving frames */
//	                    mOpenCvCameraView.setOnTouchListener(Camera.this);
//	                    mOpenCvCameraView.setMaxFrameSize(640, 480);
//	                    mOpenCvCameraView.enableView();
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	        }
	    };
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);

		ImageButton camera = (ImageButton) findViewById(R.id.imageButton1);

		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraScreen = new Intent(getApplicationContext(), Camera.class);
				cameraScreen.putExtra("keyBool", false);
				startActivity(cameraScreen);
			}
		});
		
		Button grade = (Button) findViewById(R.id.button1);
		
		grade.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String key = Environment.getExternalStorageDirectory().getPath() + "/keyImage.png";
				String test = Environment.getExternalStorageDirectory().getPath() + "/testImage.png";
				int[] answers = gradeSheet(key, test);
				double score = answers[0]/(answers[0]+answers[1]);
				ScoreFragment frag = new ScoreFragment();
				frag.score = score;
				frag.correct = answers[0];
				frag.show(getFragmentManager(), null);
				Log.v("Score Calulated", String.valueOf(score*100));

						
//				String fileName = Environment.getExternalStorageDirectory().getPath() +
//		                "/sample_picture.png";
//				Scalar red = new Scalar(0, 0, 255);
//				Scalar green = new Scalar(0, 255, 0);
//				Mat houghCircles = new Mat();
//				Mat original = Highgui.imread(fileName, Highgui.CV_LOAD_IMAGE_COLOR);
//				Mat image = new Mat();
//				Imgproc.cvtColor(original, image, Imgproc.COLOR_BGR2GRAY);
//				Imgproc.GaussianBlur(image, image, new Size(5, 5), 0);
//				Imgproc.HoughCircles(image, houghCircles, Imgproc.CV_HOUGH_GRADIENT, 1, 20, 100, 19, 15, 25);
//				Log.v("Circle Count", Integer.toString(houghCircles.cols()));
////				for(int i=0; i<houghCircles.cols();i++) {
////					Core.circle(original , new Point(houghCircles.get(0, i)[0], houghCircles.get(0, i)[1]), (int)houghCircles.get(0, i)[2], red, -1);
////				}
//				Mat outFrameConverted = new Mat();
////				double[] val = new double[original.channels()];
//				
////				val = original.get((int)houghCircles.get(0,  0)[1], (int)houghCircles.get(0, 0)[0]);
//				for(int i=0; i<houghCircles.cols();i++) {
//					Log.v("Intensity Test", Double.toString(original.get((int)houghCircles.get(0,  i)[1], (int)houghCircles.get(0, i)[0])[2]));
//					if((original.get((int)houghCircles.get(0,  i)[1], (int)houghCircles.get(0, i)[0]))[2] <110) {
//						Core.circle(original, new Point(houghCircles.get(0, i)[0], houghCircles.get(0, i)[1]), (int)houghCircles.get(0, i)[2], green, 10);
//					}
//					else {
//						Core.circle(original, new Point(houghCircles.get(0, i)[0], houghCircles.get(0, i)[1]), (int)houghCircles.get(0, i)[2], red, 10);
//					}
//				}
////				Imgproc.cvtColor(original, outFrameConverted, Imgproc.COLOR_GRAY2RGBA);
//				String fileNameOut = Environment.getExternalStorageDirectory().getPath() +
//						"/picture_out.png";
//				Highgui.imwrite(fileNameOut, original);
			}
		});
		
		ImageButton key = (ImageButton) findViewById(R.id.imageButton2);
		
		key.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent cameraScreen = new Intent(getApplicationContext(), Camera.class);
				cameraScreen.putExtra("keyBool", true);
				startActivity(cameraScreen);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public int[] gradeSheet(String keyFileName, String testFileName) {
		Mat key = Highgui.imread(keyFileName, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Mat test = Highgui.imread(testFileName, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Imgproc.GaussianBlur(key, key, new Size(5, 5), 0);
		Mat houghCircles = new Mat();
		Imgproc.HoughCircles(key, houghCircles, Imgproc.CV_HOUGH_GRADIENT, 1, 20, 100, 19, 15, 25);
		Log.v("Circle Count", Integer.toString(houghCircles.cols()));
		int answers[] = {0, 0};
		for(int i=0; i<houghCircles.cols();i++) {
//			Log.v("Intensity Test", Double.toString(original.get((int)houghCircles.get(0,  i)[1], (int)houghCircles.get(0, i)[0])[2]));
			if((key.get((int)houghCircles.get(0,  i)[1], (int)houghCircles.get(0, i)[0]))[0] < 100) {
//				Core.circle(key, new Point(houghCircles.get(0, i)[0], houghCircles.get(0, i)[1]), (int)houghCircles.get(0, i)[2], green, 10);
				if((test.get((int)houghCircles.get(0,  i)[1], (int)houghCircles.get(0, i)[0]))[0] < 100) {
					answers[0] += 1;
				}
				else {
//					Core.circle(key, new Point(houghCircles.get(0, i)[0], houghCircles.get(0, i)[1]), (int)houghCircles.get(0, i)[2], red, 10);
					answers[1] += 1;
				}
			}
			Log.v("Correct", String.valueOf(answers[0]));
			Log.v("Wrong", String.valueOf(answers[1]));
		}
		return answers;
	}
	
}
