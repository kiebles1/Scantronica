package com.jensenkiebles.scantronica;

import java.io.FileOutputStream;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.util.Log;

public class PicView extends JavaCameraView implements PictureCallback {

	private String mPictureFileName;
	
	public PicView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void takePicture(final String fileName) {
		this.mPictureFileName = fileName;
		mCamera.takePicture(null, null, this);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.i("PicSave", "Saving a bitmap to file" + mPictureFileName);
		// The camera preview was automatically stopped. Start it again.
		mCamera.startPreview();
		mCamera.setPreviewCallback(this);

//		// Write the image in a file (in jpeg format)
//		try {
//			FileOutputStream fos = new FileOutputStream(this.mPictureFileName);
//
//			fos.write(data);
//			fos.close();
//
//		} catch (java.io.IOException e) {
//			Log.e("PictureDemo", "Exception in photoCallback", e);
//		}
	}

}
