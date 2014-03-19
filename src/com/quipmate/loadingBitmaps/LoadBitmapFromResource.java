package com.quipmate.loadingBitmaps;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * The Class LoadBitmapFromResource.
 */
public class LoadBitmapFromResource {

	/** The context. */
	private Context context;
	private Bitmap mPlaceHolderBitmap;

	/**
	 * Instantiates a new load bitmap from resource.
	 * 
	 * @param context
	 *            the context
	 */
	public LoadBitmapFromResource(Context context) {
		super();
		this.context = context;
	}

	public static boolean cancelPotentialWork(int data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final int bitmapData = bitmapWorkerTask.data;
			// If bitmapData is not yet set or it differs from the new data
			if (bitmapData == 0 || bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public LoadBitmapFromResource(Context context, Bitmap mPlaceHolderBitmap) {
		super();
		this.context = context;
		this.mPlaceHolderBitmap = mPlaceHolderBitmap;
	}

	public void loadBitmap(int resId, ImageView imageView) {
		if (cancelPotentialWork(resId, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					context.getResources(), mPlaceHolderBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(resId);
		}
	}

	/**
	 * The Class AsyncDrawable. dedicated class to store reference to bitmap
	 * worker task
	 */
	static class AsyncDrawable extends BitmapDrawable {

		/** The bitmap worker task reference. */
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		/**
		 * Instantiates a new async drawable.
		 * 
		 * @param res
		 *            the res
		 * @param bitmap
		 *            the bitmap
		 * @param bitmapWorkerTask
		 *            the bitmap worker task
		 */
		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		/**
		 * Gets the bitmap worker task.
		 * 
		 * @return the bitmap worker task
		 */
		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	/**
	 * The Class BitmapWorkerTask.
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

		/** The image view reference. */
		private final WeakReference<ImageView> imageViewReference;

		/** The data. */
		private int data = 0;

		/**
		 * Instantiates a new bitmap worker task.
		 * 
		 * @param imageView
		 *            the image view
		 */
		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			return BitmapHelper.decodeSampledBitmapFromResource(
					context.getResources(), data, 100, 100);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
				if (this == bitmapWorkerTask && imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}
}
