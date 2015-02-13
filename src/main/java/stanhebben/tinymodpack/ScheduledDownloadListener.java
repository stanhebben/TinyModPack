package stanhebben.tinymodpack;

/**
 *
 * @author Stan Hebben
 */
public interface ScheduledDownloadListener {
	public void onStarted(ScheduledDownload download);
	
	public void onProgress(ScheduledDownload download, int total, int progress);
	
	public void onSuccess(ScheduledDownload download);
	
	public void onFailure(ScheduledDownload download, String reason);
	
	public void onFinished();
}
