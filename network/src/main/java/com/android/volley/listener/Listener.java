package com.android.volley.listener;

import com.android.volley.VolleyError;

/**
 * Callback interface for delivering request status or response result.
 * Note : all method are calls over UI thread.
 * @param <T> Parsed type of this response
 */
public abstract class Listener<T> {
	/** Inform when start to handle this Request. */
	public void onPreExecute() {}

	/** Inform when {@link com.android.volley.Request} execute is finish,
	 * whatever success or error or cancel, this callback
	 * method always invoke if request is done. */
	public void onFinish() {}

	/** Called when response success. */
	public abstract void onSuccess(T response);

	/**
	 * Callback method that an error has been occurred with the
	 * provided error code and optional user-readable message.
	 */
	public void onError(VolleyError error) {}

	/** Inform when the {@link com.android.volley.Request} is truly cancelled. */
	public void onCancel() {}

	/**
	 * Inform When the {@link com.android.volley.Request} cache non-exist or expired,
	 * this callback method is opposite by the onUsedCache(),
	 * means the http retrieving will happen soon.
	 */
	public void onNetworking() {}

	/** Inform when the cache already use,
	 * it means http networking won't execute. */
	public void onUsedCache() {}

	/** Inform when {@link com.android.volley.Request} execute is going to retry. */
	public void onRetry() {}

	/**
	 * Inform when download progress change, this callback method only available
	 * when request was {@link com.android.volley.toolbox.FileDownloadRequest}.
	 */
	public void onProgressChange(long fileSize, long downloadedSize) {
	}
}
