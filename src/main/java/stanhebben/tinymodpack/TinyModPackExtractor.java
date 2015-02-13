package stanhebben.tinymodpack;

import SevenZip.Compression.LZMA.Decoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/**
 *
 * @author Stan Hebben
 */
public class TinyModPackExtractor {

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("--installServer")) {
			extract(new File("."), new DecodingProgress() {
				@Override
				public void setDecodingStart(long total) {
					System.out.println("Decoding data...");
				}

				@Override
				public void setDecodingData(long total, long status) {
				}

				@Override
				public void setWritingFile(String name) {
					System.out.println("Extracting " + name);
				}

				@Override
				public void setDownloadingFile(String file) {
				}

				@Override
				public void setDownloadProgress(int done, int total) {
				}

				@Override
				public void finished() {
					System.out.println("Finished");
				}
			});
		} else {
			extractWithProgressWindow(null);
		}
	}

	public static void extractWithProgressWindow(final File directory) {
		final JFrame frame = new JFrame("TinyMod extractor");
		JPanel content = new JPanel(new BorderLayout());

		JPanel mcdir = new JPanel();
		final JTextField target = new JTextField(50);
		JButton open = new JButton("Select");
		final JButton start = new JButton("Start");
		start.setEnabled(false);
		
		final JComboBox version = new JComboBox(new String[] {"- Select Forge Version -"});

		final File[] launcherDir = new File[1];
		launcherDir[0] = Utils.findLauncherDir();
		if (launcherDir != null) {
			target.setText(launcherDir[0].getAbsolutePath());
			loadVersions(version, launcherDir[0]);
		}
		
		version.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(target.getText().length() > 0 && version.getSelectedIndex() > 0);
			}
		});
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Minecraft Launcher Directory");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					if (Utils.isLauncherDir(chooser.getSelectedFile())) {
						launcherDir[0] = chooser.getSelectedFile();
						target.setText(chooser.getSelectedFile().getAbsolutePath());
						loadVersions(version, chooser.getSelectedFile());
						start.setEnabled(target.getText().length() > 0 && version.getSelectedIndex() > 0);
					}
				}
			}
		});

		mcdir.add(new JLabel("Minecraft Dir: "));
		mcdir.add(target);
		mcdir.add(open);
		mcdir.add(version);
		mcdir.add(start);
		content.add(mcdir, BorderLayout.NORTH);

		final JProgressBar progress = new JProgressBar();
		progress.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		final JLabel status = new JLabel("Select your minecraft directory and Forge version. Install Forge first!!");
		content.add(progress);
		content.add(status, BorderLayout.SOUTH);
		content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		frame.setContentPane(content);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Rectangle sb = frame.getGraphicsConfiguration().getBounds();
		frame.setLocation(
				(int) ((sb.getWidth() - frame.getWidth()) / 2),
				(int) ((sb.getHeight() - frame.getHeight()) / 2));
		frame.setVisible(true);

		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						extract(directory, launcherDir[0], 
								new DecodingProgress() {
									@Override
									public void setDecodingStart(long total) {
										status.setText("Decoding...");
										progress.setMaximum((int) total);
									}

									@Override
									public void setDecodingData(long total, long status) {
										progress.setValue((int) status);
									}

									@Override
									public void setWritingFile(String name) {
										status.setText("Writing " + name);
									}
									
									@Override
									public void setDownloadingFile(String file) {
										status.setText("Downloading " + file);
									}
									
									@Override
									public void setDownloadProgress(int done, int total) {
										progress.setMaximum(total);
										progress.setValue(done);
									}
									
									@Override
									public void finished() {
										System.out.println("Finished");
										frame.dispose();
										System.exit(0);
									}
								},
								(String) version.getSelectedItem()
						);
					}
				};
				thread.start();
			}
		});
	}

	public static void extract(File directory, DecodingProgress progress) {
		extract(directory, null, progress, null);
	}
	
	public static void extract(File directory, File launcherDir, final DecodingProgress progress, String version) {
		Properties properties = new Properties();

		try {
			properties.load(TinyModPackExtractor.class
					.getResourceAsStream("/modpack.properties"));
			if (directory == null)
				directory = new File(System.getProperty("user.home"), properties.getProperty("folder", "/Modpacks/" + properties.getProperty("name")));

			// what should we keep?
			HashSet<String> keep = new HashSet<String>();

			if (properties.containsKey("keep") && properties.getProperty("keep").length() > 0) {
				for (String s : Utils.split(properties.getProperty("keep"), ';')) {
					s = s.trim();
					if (s.length() == 0) continue;

					keep.add(s.toLowerCase());
				}
			}

			// delete old files, if necessary
			if (directory.exists()) {
				if (properties.getProperty("delete").length() > 0) {
					for (String s : Utils.split(properties.getProperty("delete"), ';')) {
						s = s.trim();
						if (s.length() == 0) continue;

						File f = new File(directory, s);
						deleteFiles(s, f, keep);
					}
				}
			} else {
				directory.mkdirs();
			}

			long dataSize = Long.parseLong(properties.getProperty("datasize"));

			progress.setDecodingStart(dataSize);
			File tmp = File.createTempFile("data", "dat");
			OutputStream tmpOutput = null;
			try {
				tmpOutput = new BufferedOutputStream(new FileOutputStream(tmp));
				Decoder decoder = new Decoder();

				InputStream dataInput = null;
				try {
					dataInput = new BufferedInputStream(TinyModPackExtractor.class.getResourceAsStream("/data"));
					byte[] lzmaProperties = new byte[5];
					dataInput.read(lzmaProperties);
					decoder.SetDecoderProperties(lzmaProperties);
					decoder.Code(dataInput, tmpOutput, dataSize);
				} finally {
					if (dataInput != null) dataInput.close();
				}
			} finally {
				if (tmpOutput != null) tmpOutput.close();
			}
			
			DataInputStream tmpInput = null;
			try {
				tmpInput = new DataInputStream(new BufferedInputStream(new FileInputStream(tmp)));
				decode(directory, tmpInput, progress);
			} finally {
				if (tmpInput != null) tmpInput.close();
			}
			
			// write modpack info file
			Writer propertiesWriter = new FileWriter(new File(directory, "modpack.properties"));
			propertiesWriter.append("name=").append(properties.getProperty("name")).append("\n");
			propertiesWriter.append("version=").append(properties.getProperty("version")).append("\n");
			propertiesWriter.close();
			
			if (launcherDir != null) {
				// patch json file
				JsonObject profile = new JsonObject();
				profile.addProperty("name", properties.getProperty("name"));
				profile.addProperty("gameDir", directory.getAbsolutePath());
				profile.addProperty("lastVersionId", version);
				profile.addProperty("javaArgs", properties.getProperty("javaArgs"));
				profile.addProperty("launcherVisibilityOnGameClose", properties.getProperty("launcherVisibility"));
				profile.addProperty("useHopperCrashService", false);

				File configs = new File(launcherDir, "launcher_profiles.json");
				JsonParser parser = new JsonParser();
				JsonObject object = parser.parse(new FileReader(configs)).getAsJsonObject();
				if (object.getAsJsonObject("profiles").has(properties.getProperty("name"))) {
					object.getAsJsonObject("profiles").remove(properties.getProperty("name"));
				}
				object.getAsJsonObject("profiles").add(properties.getProperty("name"), profile);

				Gson gson = new GsonBuilder().create();
				FileWriter writer = null;
				try {
					writer = new FileWriter(configs);
					gson.toJson(object, writer);
				} finally {
					if (writer != null) writer.close();
				}
			}
			
			progress.finished();
		} catch (IOException ex) {
			Logger.getLogger(TinyModPackExtractor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private static List<String> loadVersions(JComboBox cb, File dir) {
		List<String> versions = new ArrayList<String>();
		versions.add("- Select Forge Version -");
		File versionsDir = new File(dir, "versions");
		for (String s : versionsDir.list()) {
			if (s.contains("Forge")) versions.add(s);
		}
		
		cb.setModel(new DefaultComboBoxModel(versions.toArray(new String[versions.size()])));
		return versions;
	}
	
	private static boolean deleteFiles(String name, File file, Set<String> keep) {
		if (keep.contains(name.toLowerCase())) return false;
		
		if (file.isFile()) {
			file.delete();
			return true;
		} else if (file.isDirectory()) {
			boolean canDelete = true;
			for (File f : file.listFiles()) {
				canDelete &= deleteFiles(name + "/" + f.getName(), f, keep);
			}
			if (canDelete) file.delete();
			return canDelete;
		} else {
			return false;
		}
	}
	
	private static void decode(File directory, DataInputStream input, DecodingProgress progress) throws IOException {
		while (true) {
			int type = input.readByte();
			switch (type) {
				case Utils.TYPE_EOF:
					return;
				case Utils.TYPE_BINARY: {
					String filename = input.readUTF();
					progress.setWritingFile(filename);
					
					File file = new File(directory, filename);
					if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
					
					FileOutputStream output = null;
					try {
						output = new FileOutputStream(file);
						Utils.transfer(input, output, input.readInt());
					} finally {
						if (output != null) output.close();
					}
					break;
				}
				case Utils.TYPE_ZIP: {
					String filename = input.readUTF();
					progress.setWritingFile(filename);
					
					File file = new File(directory, filename);
					if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
					
					int numFiles = input.readInt();

					ZipOutputStream output = null;
					try {
						output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
						for (int i = 0; i < numFiles; i++) {
							String name = input.readUTF();
							int size = input.readInt();
							
							output.putNextEntry(new ZipEntry(name));
							Utils.transfer(input, output, size);
							output.closeEntry();
						}
					} finally {
						if (output != null) output.close();
					}
					break;
				}
				default:
					throw new RuntimeException("Invalid marker: " + type);
			}
		}
	}
}
