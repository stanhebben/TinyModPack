package stanhebben.tinymodpack;

/**
 *
 * @author Stan Hebben
 */
public interface DownloadListener {
	public void onStarted();
	
	public void onProgress(int downloaded, int total);
	
	public void onFailed(String reason);
	
	public void onSuccess();
}
