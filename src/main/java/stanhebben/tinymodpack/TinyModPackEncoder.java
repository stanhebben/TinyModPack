package stanhebben.tinymodpack;

import SevenZip.Compression.LZMA.Encoder;
import SevenZip.ICodeProgress;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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

/**
 *
 * @author Stan Hebben
 */
public class TinyModPackEncoder {
	private static final String[] EMBED = {
		"SevenZip/CRC.class",
		"SevenZip/ICodeProgress.class",
		"SevenZip/Compression/LZ/BinTree.class",
		"SevenZip/Compression/LZ/InWindow.class",
		"SevenZip/Compression/LZ/OutWindow.class",
		"SevenZip/Compression/LZMA/Base.class",
		"SevenZip/Compression/LZMA/Decoder$LenDecoder.class",
		"SevenZip/Compression/LZMA/Decoder$LiteralDecoder$Decoder2.class",
		"SevenZip/Compression/LZMA/Decoder$LiteralDecoder.class",
		"SevenZip/Compression/LZMA/Decoder.class",
		"SevenZip/Compression/RangeCoder/BitTreeDecoder.class",
		"SevenZip/Compression/RangeCoder/Decoder.class",
		"com/google/gson/annotations/Expose.class",
		"com/google/gson/annotations/SerializedName.class",
		"com/google/gson/annotations/Since.class",
		"com/google/gson/annotations/Until.class",
		"com/google/gson/internal/bind/ArrayTypeAdapter$1.class",
		"com/google/gson/internal/bind/ArrayTypeAdapter.class",
		"com/google/gson/internal/bind/CollectionTypeAdapterFactory$Adapter.class",
		"com/google/gson/internal/bind/CollectionTypeAdapterFactory.class",
		"com/google/gson/internal/bind/DateTypeAdapter$1.class",
		"com/google/gson/internal/bind/DateTypeAdapter.class",
		"com/google/gson/internal/bind/JsonTreeReader$1.class",
		"com/google/gson/internal/bind/JsonTreeReader.class",
		"com/google/gson/internal/bind/JsonTreeWriter$1.class",
		"com/google/gson/internal/bind/JsonTreeWriter.class",
		"com/google/gson/internal/bind/MapTypeAdapterFactory$Adapter.class",
		"com/google/gson/internal/bind/MapTypeAdapterFactory.class",
		"com/google/gson/internal/bind/ObjectTypeAdapter$1.class",
		"com/google/gson/internal/bind/ObjectTypeAdapter$2.class",
		"com/google/gson/internal/bind/ObjectTypeAdapter.class",
		"com/google/gson/internal/bind/ReflectiveTypeAdapterFactory$1.class",
		"com/google/gson/internal/bind/ReflectiveTypeAdapterFactory$Adapter.class",
		"com/google/gson/internal/bind/ReflectiveTypeAdapterFactory$BoundField.class",
		"com/google/gson/internal/bind/ReflectiveTypeAdapterFactory.class",
		"com/google/gson/internal/bind/SqlDateTypeAdapter$1.class",
		"com/google/gson/internal/bind/SqlDateTypeAdapter.class",
		"com/google/gson/internal/bind/TimeTypeAdapter$1.class",
		"com/google/gson/internal/bind/TimeTypeAdapter.class",
		"com/google/gson/internal/bind/TypeAdapterRuntimeTypeWrapper.class",
		"com/google/gson/internal/bind/TypeAdapters$1.class",
		"com/google/gson/internal/bind/TypeAdapters$10.class",
		"com/google/gson/internal/bind/TypeAdapters$11.class",
		"com/google/gson/internal/bind/TypeAdapters$12.class",
		"com/google/gson/internal/bind/TypeAdapters$13.class",
		"com/google/gson/internal/bind/TypeAdapters$14.class",
		"com/google/gson/internal/bind/TypeAdapters$15.class",
		"com/google/gson/internal/bind/TypeAdapters$16.class",
		"com/google/gson/internal/bind/TypeAdapters$17.class",
		"com/google/gson/internal/bind/TypeAdapters$18.class",
		"com/google/gson/internal/bind/TypeAdapters$19.class",
		"com/google/gson/internal/bind/TypeAdapters$2.class",
		"com/google/gson/internal/bind/TypeAdapters$20.class",
		"com/google/gson/internal/bind/TypeAdapters$21.class",
		"com/google/gson/internal/bind/TypeAdapters$22.class",
		"com/google/gson/internal/bind/TypeAdapters$22$1.class",
		"com/google/gson/internal/bind/TypeAdapters$23.class",
		"com/google/gson/internal/bind/TypeAdapters$24.class",
		"com/google/gson/internal/bind/TypeAdapters$25.class",
		"com/google/gson/internal/bind/TypeAdapters$26.class",
		"com/google/gson/internal/bind/TypeAdapters$27.class",
		"com/google/gson/internal/bind/TypeAdapters$28.class",
		"com/google/gson/internal/bind/TypeAdapters$29.class",
		"com/google/gson/internal/bind/TypeAdapters$3.class",
		"com/google/gson/internal/bind/TypeAdapters$30.class",
		"com/google/gson/internal/bind/TypeAdapters$31.class",
		"com/google/gson/internal/bind/TypeAdapters$32.class",
		"com/google/gson/internal/bind/TypeAdapters$4.class",
		"com/google/gson/internal/bind/TypeAdapters$5.class",
		"com/google/gson/internal/bind/TypeAdapters$6.class",
		"com/google/gson/internal/bind/TypeAdapters$7.class",
		"com/google/gson/internal/bind/TypeAdapters$8.class",
		"com/google/gson/internal/bind/TypeAdapters$9.class",
		"com/google/gson/internal/bind/TypeAdapters$EnumTypeAdapter.class",
		"com/google/gson/internal/bind/TypeAdapters.class",
		"com/google/gson/internal/$Gson$Preconditions.class",
		"com/google/gson/internal/$Gson$Types$GenericArrayTypeImpl.class",
		"com/google/gson/internal/$Gson$Types$ParameterizedTypeImpl.class",
		"com/google/gson/internal/$Gson$Types$WildcardTypeImpl.class",
		"com/google/gson/internal/$Gson$Types.class",
		"com/google/gson/internal/ConstructorConstructor$1.class",
		"com/google/gson/internal/ConstructorConstructor$10.class",
		"com/google/gson/internal/ConstructorConstructor$11.class",
		"com/google/gson/internal/ConstructorConstructor$12.class",
		"com/google/gson/internal/ConstructorConstructor$2.class",
		"com/google/gson/internal/ConstructorConstructor$3.class",
		"com/google/gson/internal/ConstructorConstructor$4.class",
		"com/google/gson/internal/ConstructorConstructor$5.class",
		"com/google/gson/internal/ConstructorConstructor$6.class",
		"com/google/gson/internal/ConstructorConstructor$7.class",
		"com/google/gson/internal/ConstructorConstructor$8.class",
		"com/google/gson/internal/ConstructorConstructor$9.class",
		"com/google/gson/internal/ConstructorConstructor.class",
		"com/google/gson/internal/Excluder$1.class",
		"com/google/gson/internal/Excluder.class",
		"com/google/gson/internal/JsonReaderInternalAccess.class",
		"com/google/gson/internal/LazilyParsedNumber.class",
		"com/google/gson/internal/LinkedTreeMap$1.class",
		"com/google/gson/internal/LinkedTreeMap$EntrySet$1.class",
		"com/google/gson/internal/LinkedTreeMap$EntrySet.class",
		"com/google/gson/internal/LinkedTreeMap$KeySet$1.class",
		"com/google/gson/internal/LinkedTreeMap$KeySet.class",
		"com/google/gson/internal/LinkedTreeMap$LinkedTreeMapIterator.class",
		"com/google/gson/internal/LinkedTreeMap$Node.class",
		"com/google/gson/internal/LinkedTreeMap.class",
		"com/google/gson/internal/ObjectConstructor.class",
		"com/google/gson/internal/Primitives.class",
		"com/google/gson/internal/Streams$1.class",
		"com/google/gson/internal/Streams$AppendableWriter$CurrentWrite.class",
		"com/google/gson/internal/Streams$AppendableWriter.class",
		"com/google/gson/internal/Streams.class",
		"com/google/gson/internal/UnsafeAllocator$1.class",
		"com/google/gson/internal/UnsafeAllocator$2.class",
		"com/google/gson/internal/UnsafeAllocator$3.class",
		"com/google/gson/internal/UnsafeAllocator$4.class",
		"com/google/gson/internal/UnsafeAllocator.class",
		"com/google/gson/reflect/TypeToken.class",
		"com/google/gson/stream/JsonReader$1.class",
		"com/google/gson/stream/JsonReader.class",
		"com/google/gson/stream/JsonScope.class",
		"com/google/gson/stream/JsonToken.class",
		"com/google/gson/stream/JsonWriter.class",
		"com/google/gson/stream/MalformedJsonException.class",
		"com/google/gson/DefaultDateTypeAdapter.class",
		"com/google/gson/ExclusionStrategy.class",
		"com/google/gson/FieldAttributes.class",
		"com/google/gson/FieldNamingPolicy$1.class",
		"com/google/gson/FieldNamingPolicy$2.class",
		"com/google/gson/FieldNamingPolicy$3.class",
		"com/google/gson/FieldNamingPolicy$4.class",
		"com/google/gson/FieldNamingPolicy$5.class",
		"com/google/gson/FieldNamingPolicy.class",
		"com/google/gson/FieldNamingStrategy.class",
		"com/google/gson/Gson$1.class",
		"com/google/gson/Gson$2.class",
		"com/google/gson/Gson$3.class",
		"com/google/gson/Gson$4.class",
		"com/google/gson/Gson$5.class",
		"com/google/gson/Gson$FutureTypeAdapter.class",
		"com/google/gson/Gson.class",
		"com/google/gson/GsonBuilder.class",
		"com/google/gson/InstanceCreator.class",
		"com/google/gson/JsonArray.class",
		"com/google/gson/JsonDeserializationContext.class",
		"com/google/gson/JsonDeserializer.class",
		"com/google/gson/JsonElement.class",
		"com/google/gson/JsonIOException.class",
		"com/google/gson/JsonNull.class",
		"com/google/gson/JsonObject.class",
		"com/google/gson/JsonParseException.class",
		"com/google/gson/JsonParser.class",
		"com/google/gson/JsonPrimitive.class",
		"com/google/gson/JsonSerializationContext.class",
		"com/google/gson/JsonSerializer.class",
		"com/google/gson/JsonStreamParser.class",
		"com/google/gson/JsonSyntaxException.class",
		"com/google/gson/LongSerializationPolicy$1.class",
		"com/google/gson/LongSerializationPolicy$2.class",
		"com/google/gson/LongSerializationPolicy.class",
		"com/google/gson/TreeTypeAdapter$1.class",
		"com/google/gson/TreeTypeAdapter$SingleTypeFactory.class",
		"com/google/gson/TreeTypeAdapter.class",
		"com/google/gson/TypeAdapter$1.class",
		"com/google/gson/TypeAdapter.class",
		"com/google/gson/TypeAdapterFactory.class",
		"stanhebben/tinymodpack/DecodingProgress.class",
		"stanhebben/tinymodpack/DownloadListener.class",
		"stanhebben/tinymodpack/ScheduledDownload$1.class",
		"stanhebben/tinymodpack/ScheduledDownload.class",
		"stanhebben/tinymodpack/ScheduledDownloadListener.class",
		"stanhebben/tinymodpack/TinyModPackExtractor$1.class",
		"stanhebben/tinymodpack/TinyModPackExtractor$2.class",
		"stanhebben/tinymodpack/TinyModPackExtractor$3.class",
		"stanhebben/tinymodpack/TinyModPackExtractor$4$1$1.class",
		"stanhebben/tinymodpack/TinyModPackExtractor$4$1.class",
		"stanhebben/tinymodpack/TinyModPackExtractor$4.class",
		"stanhebben/tinymodpack/TinyModPackExtractor.class",
		"stanhebben/tinymodpack/Utils$DownloadThread.class",
		"stanhebben/tinymodpack/Utils.class"
	};
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Arguments args2 = new Arguments(args);
		
		TinyModPackEncoder modpack = new TinyModPackEncoder(args2);
		try {
			modpack.createWithProgressWindow();
		} catch (IOException ex) {
			Logger.getLogger(TinyModPackEncoder.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private final Arguments args;
	
	private TinyModPackEncoder(Arguments args) {
		this.args = args;
	}
	
	public void createWithProgressWindow() throws IOException {
		final JFrame frame = new JFrame("TinyModPack Encoder");
		JPanel content = new JPanel(new BorderLayout());

		JButton open = new JButton("Select");
		
		final JButton btnStart = new JButton("Start");
		
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
						btnStart.setEnabled(true);
					}
				}
			}
		});
		
		content.add(new JLabel("Edit modpack.properties to configure the modpack"), BorderLayout.NORTH);
		content.add(btnStart, BorderLayout.EAST);
		
		final JProgressBar progress = new JProgressBar();
		progress.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		final JLabel status = new JLabel("Press start to begin building the modpack.");
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
		
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							create(new EncodingProgress() {
								@Override
								public void setStatusEncodingFile(String name) {
									status.setText("Reading file " + name);
								}

								@Override
								public void setStatusEncodingData(long total, long done) {
									status.setText("Encoding file...");
									progress.setMaximum((int) total);
									progress.setValue((int) done);
								}

								@Override
								public void setStatusEmbedding(String name) {
									status.setText("Embedding " + name);
								}
							});
						} catch (IOException ex) {
							Logger.getLogger(TinyModPackEncoder.class.getName()).log(Level.SEVERE, null, ex);
						}

						frame.dispose();
						System.exit(0);
					}
				};
				thread.start();
			}
		});
	}
	
	public void create(EncodingProgress progress) throws IOException {
		// create uncompressed temporary data file
		File tmp = File.createTempFile("data", "dat");
		try {
			DataOutputStream dataOutput = null;
			try {
				dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(tmp)));
				for (String s : args.files) {
					String fileName = s.trim();
					if (fileName.length() == 0) continue;
					
					encode(fileName, new File(args.dir, fileName), dataOutput, progress, args);
				}
				dataOutput.writeByte(Utils.TYPE_EOF);
			} finally {
				if (dataOutput != null) dataOutput.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			ZipOutputStream zipOutput = null;
			try {
				zipOutput = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(args.output)));
				
				// encode data file
				zipOutput.setLevel(ZipOutputStream.STORED);
				zipOutput.putNextEntry(new ZipEntry("data"));
				
				Encoder encoder = new Encoder();
				encoder.SetAlgorithm(2);
				encoder.SetDictionarySize(1 << 23);
				encoder.SetNumFastBytes(128);
				encoder.SetMatchFinder(1);
				encoder.SetLcLpPb(3, 0, 2);
				encoder.SetEndMarkerMode(false);
				encoder.WriteCoderProperties(zipOutput);
				
				BufferedOutputStream bufferedOutput = new BufferedOutputStream(zipOutput);
				{
					BufferedInputStream input = null;
					try {
						input = new BufferedInputStream(new FileInputStream(tmp));
						encoder.Code(input, bufferedOutput, 0, 0, progress == null ? null : new EncodingWrapper(progress, tmp.length()));
					} finally {
						if (input != null) input.close();
					}
				}
				bufferedOutput.flush();
				zipOutput.closeEntry();
				
				zipOutput.setLevel(ZipOutputStream.DEFLATED);
				
				// encode forge
				byte[] buffer = new byte[16384];
				
				// encode embedded files
				for (String s : EMBED) {
					if (progress != null) progress.setStatusEmbedding(s);
					zipOutput.putNextEntry(new ZipEntry(s));
					{
						InputStream input = null;
						try {
							input = TinyModPackEncoder.class.getResourceAsStream("/" + s);
							
							int read = input.read(buffer);
							while (read >= 0) {
								zipOutput.write(buffer, 0, read);
								read = input.read(buffer);
							}
						} finally {
							if (input != null) input.close();
						}
					}
					zipOutput.closeEntry();
				}
				
				// encode properties file
				zipOutput.putNextEntry(new ZipEntry("modpack.properties"));
				{
					OutputStreamWriter writer = new OutputStreamWriter(zipOutput);
					writer.append("name=" + args.name + "\n");
					writer.append("folder=" + args.directory + "\n");
					writer.append("datasize=" + tmp.length() + "\n");
					writer.append("version=" + args.version + "\n");
					writer.append("keep=" + args.keep + "\n");
					writer.append("delete=" + args.delete + "\n");
					//writer.append("forge=" + version + "\n");
					//writer.append("forgeFiles=" + forgeFileList.toString() + "\n");
					writer.append("javaArgs=" + args.javaArgs + "\n");
					writer.append("launcherVisibility=" + args.launcherVisibility + "\n");
					writer.flush(); // don't close the writer, it would close the underlying zip stream
				}
				
				// encode meta
				zipOutput.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
				OutputStreamWriter writer = new OutputStreamWriter(zipOutput);
				writer.append("Manifest-Version: 1.0\n");
				writer.append("Created-By: 1.7.0_45-b18 (Oracle Corporation)\n");
				writer.append("Class-Path: \n");
				writer.append("Main-Class: stanhebben.tinymodpack.TinyModPackExtractor\n");
				writer.flush();
			} finally {
				if (zipOutput != null) zipOutput.close();
			}
		} catch (IOException ex) {
			Logger.getLogger(TinyModPackEncoder.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void diff(ZipFile original, EncodingProgress progress) throws IOException {
		
	}
	
	// ##############################
	// ### Private static methods ###
	// ##############################
	
	private static void encode(String dir, File source, DataOutputStream output, EncodingProgress progress, Arguments arguments) throws IOException {
		if (source.isDirectory()) {
			for (File f : source.listFiles()) {
				String subdir = dir + "/" + f.getName();
				encode(subdir, f, output, progress, arguments);
			}
		} else if (source.isFile()) {
			if (progress != null) progress.setStatusEncodingFile(dir);
			if (source.getName().endsWith(".jar") && arguments.recompress) {
				encodeZip(dir, source, output);
			} else if (source.getName().endsWith(".zip") && arguments.recompress) {
				encodeZip(dir, source, output);
			} else if (source.getName().endsWith(".pak") && arguments.recompress) {
				encodeZip(dir, source, output);
			} else {
				encodeBinary(dir, source, output);
			}
		}
	}
	
	private static void encodeZip(String dir, File source, DataOutputStream output) throws IOException {
		output.writeByte(Utils.TYPE_ZIP);
		output.writeUTF(dir);
		
		ZipFile file = new ZipFile(source);
		output.writeInt(file.size());
		
		Enumeration<? extends ZipEntry> entries = file.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			output.writeUTF(entry.getName());
			output.writeInt((int) entry.getSize());
			
			InputStream input = null;
			try {
				input = file.getInputStream(entry);
				Utils.transfer(input, output, (int) entry.getSize());
			} finally {
				if (input != null) input.close();
			}
		}
	}
	
	private static void encodeBinary(String dir, File source, DataOutputStream output) throws IOException {
		output.writeByte(Utils.TYPE_BINARY);
		output.writeUTF(dir);
		output.writeInt((int) source.length());
		
		InputStream input = null;
		try {
			input = new FileInputStream(source);
			Utils.transfer(input, output);
		} finally {
			if (input != null) input.close();
		}
	}
	
	private static boolean isModpack(ZipFile file) {
		return file.getEntry("modpack.properties") != null;
	}
	
	private static class EncodingWrapper implements ICodeProgress {
		private final EncodingProgress progress;
		private final long total;
		
		public EncodingWrapper(EncodingProgress progress, long total) {
			this.progress = progress;
			this.total = total;
		}

		@Override
		public void SetProgress(long inSize, long outSize) {
			progress.setStatusEncodingData(total, inSize);
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
	
	private static class Arguments {
		private File dir;
		private String name;
		private List<String> files;
		private String output;
		private String directory;
		private String javaArgs;
		private String diffFrom;
		private String launcherVisibility;
		private String version;
		private boolean recompress;
		
		private String keep;
		private String delete;
		
		public Arguments(String[] args) {
			dir = new File(".");
			
			Properties properties = new Properties();
			File propertiesFile = new File("modpack.properties");
			if (propertiesFile.exists()) {
				try {
					properties.load(new FileReader(propertiesFile));
					name = properties.getProperty("name", "TinyModPack");
					if (properties.containsKey("name")) {
						files = new ArrayList<String>();
						files.addAll(Arrays.asList(Utils.split(properties.getProperty("files"), ';')));
					}
					output = properties.getProperty("output", name + ".jar");
					directory = properties.getProperty("directory", "Modpacks/" + name);
					javaArgs = properties.getProperty("javaArgs", "");
					launcherVisibility = properties.getProperty("launcherVisibilityOnGameClose", "keep the launcher open");
					diffFrom = properties.getProperty("diffFrom", null);
					version = properties.getProperty("version", "1.0");
					recompress = Boolean.parseBoolean(properties.getProperty("recompress", "false"));
					keep = properties.getProperty("keep", "");
					delete = properties.getProperty("delete", "");
				} catch (IOException ex) {
					Logger.getLogger(TinyModPackEncoder.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-dir")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for dir argument");
						System.exit(1);
					}
					dir = new File(args[i + 1]);
					i++;
				} else if (args[i].equals("-name")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for name argument");
						System.exit(1);
					}
					name = args[i + 1];
					i++;
				} else if (args[i].equals("-directory")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for directory argument");
						System.exit(1);
					}
					directory = args[i + 1];
					i++;
				} else if (args[i].equals("-files")) {
					files = new ArrayList<String>();
					i++;
					while (i < args.length && !args[i].startsWith("-")) {
						files.add(args[i]);
					}
				} else if (args[i].equals("-keep")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for keep argument");
						System.exit(1);
					}
					keep = args[i + 1];
					i++;
				} else if (args[i].equals("-delete")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for delete argument");
						System.exit(1);
					}
					delete = args[i + 1];
					i++;
				} else if (args[i].equals("-javaArgs")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for javaArgs argument");
						System.exit(1);
					}
					javaArgs = args[i + 1];
					i++;
				} else if (args[i].equals("-diff")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for diff argument");
						System.exit(1);
					}
					diffFrom = args[i + 1];
					i++;
				} else if (args[i].equals("-version")) {
					if (i + 1 == args.length) {
						System.out.println("Missing value for version argument");
						System.exit(1);
					}
					version = args[i + 1];
				} else if (args[i].equals("-launcherClose")) {
					launcherVisibility = "close launcher when game starts";
				} else if (args[i].equals("-launcherHide")) {
					launcherVisibility = "hide launcher and re-open when game closes";
				} else if (args[i].equals("-launcherKeepOpen")) {
					launcherVisibility = "keep the launcher open";
				} else {
					System.out.println("Unknown argument: " + args[i]);
					System.exit(1);
				}
			}
		}
	}
}
