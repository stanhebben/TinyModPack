package stanhebben.tinymodpack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *
 * @author Stan Hebben
 */
public class Utils {
	private Utils() {}
	
	public static final int TYPE_EOF = 0;
	public static final int TYPE_BINARY = 1;
	public static final int TYPE_ZIP = 2;

	/**
     * Splits a string in parts, given a specified delimiter.
     * 
     * @param value string to be split
     * @param delimiter delimiter
     * @return 
     */
    public static String[] split(String value, char delimiter) {
        ArrayList<String> result = new ArrayList<String>();
        int start = 0;
        outer: for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == delimiter) {
                result.add(value.substring(start, i));
                start = i + 1;
            }
        }
        result.add(value.substring(start));
        return result.toArray(new String[result.size()]);
    }

	public static File findLauncherDir() {
		// default on windows
		File attempt1 = new File(System.getProperty("user.home") + "/Application Data/.minecraft");
		if (isLauncherDir(attempt1)) return attempt1;
		// default on linux (untested)
		File attempt2 = new File(System.getProperty("user.home") + "/.minecraft");
		if (isLauncherDir(attempt2)) return attempt2;
		// default on mac (untested)
		File attempt3 = new File(System.getProperty("user.home") + "/Library/Application Support/minecraft");
		if (isLauncherDir(attempt3)) return attempt3;
		
		// none found
		return null;
	}
	
	public static boolean isLauncherDir(File target) {
		return target.exists()
				&& target.isDirectory()
				&& new File(target, "launcher_profiles.json").exists();
	}
	
	private static final byte[] BUFFER = new byte[65536];
	
	public static int transfer(InputStream input, OutputStream output) throws IOException {
		int read = input.read(BUFFER);
		int total = 0;
		while (read >= 0) {
			output.write(BUFFER, 0, read);
			total += read;
			read = input.read(BUFFER);
		}
		return total;
	}
	
	public static void transfer(InputStream input, OutputStream output, int size) throws IOException {
		int read = 0;

		while (read < size) {
			int readBytes = input.read(BUFFER, 0, Math.min(size - read, BUFFER.length));
			output.write(BUFFER, 0, readBytes);
			read += readBytes;
		}
	}
	
	public static void download(URL url, File target, DownloadListener listener) {
		target.getParentFile().mkdirs();
		
		DownloadThread thread = new DownloadThread(listener, url, target);
		thread.start();
	}
	
	private static class DownloadThread extends Thread {
		private final DownloadListener listener;
		private final URL source;
		private final File target;
		
		public DownloadThread(DownloadListener listener, URL source, File target) {
			this.listener = listener;
			this.source = source;
			this.target = target;
		}
		
		@Override
		public void run() {
			try {
				URLConnection connection = source.openConnection();
				int length = connection.getContentLength();
				
				FileOutputStream output = new FileOutputStream(target);
				listener.onStarted();
				
				byte[] buffer = new byte[128*1024];
				int finished = 0;
				InputStream input = connection.getInputStream();
				while (finished < length) {
					int read = input.read(buffer);
					output.write(buffer, 0, read);
					finished += read;
					listener.onProgress(finished, length);
				}
				
				listener.onSuccess();
			} catch (IOException ex) {
				listener.onFailed("IO Exception: " + ex.getMessage());
			}
		}
	}
}
