package stanhebben.tinymodpack;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Stan Hebben
 */
public class ScheduledDownload {
	private final URL url;
	private final File target;
	
	public ScheduledDownload(URL url, File target) {
		this.url = url;
		this.target = target;
	}
	
	public File getTarget() {
		return target;
	}
	
	public static void start(
			final List<ScheduledDownload> downloads,
			final ScheduledDownloadListener listener) {
		if (downloads.isEmpty()) {
			listener.onFinished();
			return;
		}
		
		ScheduledDownload first = downloads.get(0);
		DownloadListener downloadListener = new DownloadListener() {
			private int index = 0;
			private ScheduledDownload current = downloads.get(0);
			
			@Override
			public void onStarted() {
				listener.onStarted(current);
			}

			@Override
			public void onProgress(int downloaded, int total) {
				listener.onProgress(current, total, downloaded);
			}

			@Override
			public void onFailed(String reason) {
				listener.onFailure(current, reason);
				startNext();
			}

			@Override
			public void onSuccess() {
				listener.onSuccess(current);
				startNext();
			}
			
			private void startNext() {
				index++;
				if (index < downloads.size()) {
					current = downloads.get(index);
					Utils.download(current.url, current.target, this);
				} else {
					listener.onFinished();
				}
			}
		};
		Utils.download(first.url, first.target, downloadListener);
	}
}
