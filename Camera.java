package com.jensenkiebles.scantronica;

import java.io.FileOutputStream;
import java.util.Arrays;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.CvType;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.Toast;

public class Camera extends Activity implements CvCameraViewListener2, OnTouchListener {

	private static final String  TAG = "Sample::Puzzle15::Activity";

	private PicView mOpenCvCameraView;

	private boolean wasTouched;

	private boolean isForKey;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i(TAG, "OpenCV loaded successfully");

				/* Now enable camera view to start receiving frames */
				mOpenCvCameraView.setOnTouchListener(Camera.this);
				//                    mOpenCvCameraView.setMaxFrameSize(640, 480);
				mOpenCvCameraView.enableView();
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isForKey = getIntent().getBooleanExtra("keyBool", true);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Log.d(TAG, "Creating and seting view");
		setContentView(R.layout.activity_camera);
		mOpenCvCameraView = (PicView) findViewById(R.id.tutorial1_activity_java_surface_view);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE); 
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);
	}

	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		//        String fileName = Environment.getExternalStorageDirectory().getPath() +
		//                "/sample_picture" + ".jpg";
		//        mOpenCvCameraView.takePicture(fileName);
		String fileName = Environment.getExternalStorageDirectory().getPath() +
				"/sample_picture.jpg";
		mOpenCvCameraView.takePicture(fileName);
		wasTouched = true;
		Log.e("Tag", "onTouchWorking");
		return false;
	}



	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Point newPoint = new Point(100, 100);
		mOpenCvCameraView.setOnTouchListener(Camera.this);
		mOpenCvCameraView.setMaxFrameSize(1920, 1080);
		if(wasTouched) {
			if(isForKey)
			{
				String fileName = Environment.getExternalStorageDirectory().getPath() +
						"/keyImage.png";
				Mat outFrame = processImage(inputFrame);
				if (outFrame != null) {
					Highgui.imwrite(fileName, outFrame);
				}
			}
			else
			{
				String fileName = Environment.getExternalStorageDirectory().getPath() +
						"/testImage.png";
				Mat outFrame = processImage(inputFrame);
				if (outFrame != null) {
					Highgui.imwrite(fileName, outFrame);
				}
			}
			wasTouched = false;
		}
		return inputFrame.rgba();

		//		Scalar red = new Scalar(0, 0, 0, 255);
		////		Mat test = inputFrame.rgba();
		////		Core.circle(test, newPoint, 50, red, 3);
		//		mOpenCvCameraView.setMaxFrameSize(1080, 1920);
		//		Mat houghCircles = new Mat();
		//		Mat processFrame = inputFrame.rgba();
		//		Mat outFrame = inputFrame.rgba();
		//		Imgproc.cvtColor(processFrame, processFrame, Imgproc.COLOR_RGBA2GRAY);
		//		Imgproc.GaussianBlur(processFrame, processFrame, new Size(5, 5), 0);
		//		Imgproc.HoughCircles(processFrame, houghCircles, Imgproc.CV_HOUGH_GRADIENT, 1, 20, 100, 19, 1, 20);
		//		Mat outFrameLast = inputFrame.rgba();
		//		for(int i=0; i<houghCircles.cols();i++) {
		//			Core.circle(outFrameLast , new Point(houghCircles.get(0, i)[0], houghCircles.get(0, i)[1]), (int)houghCircles.get(0, i)[2], red);
		//		}
		//		return outFrameLast;
	}

	public Mat processImage(CvCameraViewFrame inputFrame) {
		Scalar red = new Scalar(0, 0, 255);
		Scalar green = new Scalar(0, 255, 0);
		Mat houghCircles = new Mat();
		Mat processFrame = inputFrame.rgba();
		Mat outFrame = inputFrame.rgba();
		Imgproc.cvtColor(processFrame, processFrame, Imgproc.COLOR_RGBA2GRAY);
		Imgproc.GaussianBlur(processFrame, processFrame, new Size(5, 5), 0);
		Imgproc.HoughCircles(processFrame, houghCircles, Imgproc.CV_HOUGH_GRADIENT, 1, 20, 100, 19, 10, 25);
		Mat outFrameLast = inputFrame.rgba();
		Log.v("Tag", Integer.toString(houghCircles.cols()));
		//		for(int i=0; i<houghCircles.cols();i++) {
		//			Core.circle(outFrameLast , new Point(houghCircles.get(0, i)[0], houghCircles.get(0, i)[1]), (int)houghCircles.get(0, i)[2], red, -1);
		//		}
		Mat outFrameConverted = new Mat();
		Imgproc.cvtColor(outFrameLast, outFrameConverted, Imgproc.COLOR_RGBA2BGR);
		double currentX;
		double currentY;
		if (houghCircles.cols() > 0) {
			currentX = houghCircles.get(0, 0)[0];
			currentY = houghCircles.get(0, 0)[1];
		}
		else {
			return null;
		}
		Point TL = new Point(0, 0);
		Point TR = new Point((double)outFrameConverted.width(), 0);
		Point BL = new Point(0, (double)outFrameConverted.height());
		Point BR = new Point((double)outFrameConverted.width(), (double)outFrameConverted.height());
		double minTL = distance(TL, currentX, currentY);
		double minTR = distance(TR, currentX, currentY);
		double minBL = distance(BL, currentX, currentY);
		double minBR = distance(BR, currentX, currentY);
		Point minTLPoint = new Point();
		Point minTRPoint = new Point();
		Point minBLPoint = new Point();
		Point minBRPoint = new Point();
		for(int i=1; i<houghCircles.cols(); i++) {
			currentX = houghCircles.get(0, i)[0];
			currentY = houghCircles.get(0, i)[1];
			double currentTL = distance(TL, currentX, currentY);
			double currentTR = distance(TR, currentX, currentY);
			double currentBL = distance(BL, currentX, currentY);
			double currentBR = distance(BR, currentX, currentY);
			if(currentTL < minTL) {
				minTLPoint.x = currentX;
				minTLPoint.y = currentY;
				minTL = currentTL;
			}
			if(currentTR < minTR) {
				minTRPoint.x = currentX;
				minTRPoint.y = currentY;
				minTR = currentTR;
			}
			if(currentBL < minBL) {
				minBLPoint.x = currentX;
				minBLPoint.y = currentY;
				minBL = currentBL;
			}
			if(currentBR < minBR) {
				minBRPoint.x = currentX;
				minBRPoint.y = currentY;
				minBR = currentBR;
			}
		}
		//		Core.circle(outFrameConverted, minTLPoint, 5, green, -1);
		//		Core.circle(outFrameConverted, minTRPoint, 5, green, -1);
		//		Core.circle(outFrameConverted, minBLPoint, 5, green, -1);
		//		Core.circle(outFrameConverted, minBRPoint, 5, green, -1);
		Mat outSize = new Mat(3, 3, CvType.CV_32FC1);
		Point[] srcPoints = new Point[4];
		Point[] destPoints = new Point[4];
		srcPoints[0] = minTLPoint;
		srcPoints[1] = minTRPoint;
		srcPoints[2] = minBLPoint;
		srcPoints[3] = minBRPoint;
		destPoints[1] = new Point(216, 190);
		destPoints[3] = new Point(1548, 190);
		destPoints[0] = new Point(230, 2622);
		destPoints[2] = new Point(1544, 2622);
		Mat sourceList = Converters.vector_Point2f_to_Mat(Arrays.asList(srcPoints));
		Mat destList = Converters.vector_Point2f_to_Mat(Arrays.asList(destPoints));
		Mat transform = Imgproc.getPerspectiveTransform(sourceList, destList);
		Imgproc.warpPerspective(outFrameConverted, outFrameConverted, transform, new Size(1742, 2698));
		return outFrameConverted;
	}

	private double distance(Point origin, double x, double y) {
		double dist = Math.sqrt(Math.pow(x - origin.x, 2) + Math.pow(y-origin.y, 2));
		return dist;
	}

}