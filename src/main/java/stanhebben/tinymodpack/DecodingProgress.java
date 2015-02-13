package stanhebben.tinymodpack;

/**
 *
 * @author Stan Hebben
 */
public interface DecodingProgress {
	public void setDecodingStart(long total);
	
	public void setDecodingData(long total, long status);
	
	public void setWritingFile(String name);
	
	public void setDownloadingFile(String file);
	
	public void setDownloadProgress(int done, int total);
	
	public void finished();
}
