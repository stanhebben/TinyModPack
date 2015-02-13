package stanhebben.tinymodpack;

/**
 *
 * @author Stan Hebben
 */
public interface EncodingProgress {
	public void setStatusEncodingFile(String name);
	
	public void setStatusEncodingData(long total, long progress);
	
	public void setStatusEmbedding(String name);
}
