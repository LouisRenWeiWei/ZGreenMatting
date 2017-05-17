package com.seu.magicfilter.helper;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;

import com.seu.magicfilter.utils.MagicParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePictureTask extends AsyncTask<Bitmap, Integer, String>{
	
	private OnPictureSaveListener onPictureSaveListener;
	private File file;

	public SavePictureTask(File file, OnPictureSaveListener listener){
		this.onPictureSaveListener = listener;
		this.file = file;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(final String result) {
		if(result != null)
			MediaScannerConnection.scanFile(MagicParams.context,
	                new String[] {result}, null,
	                new MediaScannerConnection.OnScanCompletedListener() {
	                    @Override
	                    public void onScanCompleted(final String path, final Uri uri) {
	                        if (onPictureSaveListener != null)
                                onPictureSaveListener.onSaved(result);
	                    }
            	});
	}

	@Override
	protected String doInBackground(Bitmap... params) {
		if(file == null)
			return null;
		return saveBitmap(params[0]);
	}
	
	private String saveBitmap(Bitmap bitmap) {
		if (file.exists()) {
			file.delete();
		}
		try {
			bitmap = rotaingImageView(180,bitmap);
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			bitmap.recycle();
			return file.toString();
		} catch (FileNotFoundException e) {
		   e.printStackTrace();
		} catch (IOException e) {
		   e.printStackTrace();
		}
		return null;
	}

	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);// 图像垂直翻转
		matrix.postScale(-1, 1); // 镜像水平翻转
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public interface OnPictureSaveListener{
		void onSaved(String result);
	}
}
