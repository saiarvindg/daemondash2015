
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public class LoadVideo {

	private static final String FFMPEG_PATH = "/Users/saiarvind/Downloads/SnowLeopard_Lion_Mountain_Lion_Mavericks_Yosemite_09.08.2015";
	private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2;
	private static final String youtubePyDlPath = "/Users/saiarvind/Downloads/youtube/youtube-dl.py";
	private static final String youtubeVidStoragePath = "/tmp/youtube/youtube.mp4";
	public static MusicInfo[] musicInfos;

	public static String file;

	public static void getYouTubeVid(String youtubeVidUrl) throws IOException {
		String[] commands = { "python", youtubePyDlPath, youtubeVidUrl, "-o", youtubeVidStoragePath };

		ProcessBuilder pyYoutube = new ProcessBuilder(commands);
		Process pyProcess = pyYoutube.start();

		try {
			pyProcess.waitFor();
			file = youtubeVidStoragePath;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		loadFile();
		 new File(youtubeVidStoragePath).delete();
	}

	/**
	 * extracts the parameter video file's frames then goes through each frame
	 * and process it's data
	 */
	public static void loadFile() {
		ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
		try {
			String ext = file.substring(file.lastIndexOf("."));
			String notext = file.substring(0, file.lastIndexOf("."));
			String[] commands = { FFMPEG_PATH + "/ffmpeg", "-i", file, "-c", "copy", "-an", notext + ".noaudio" + ext };
			Process stripAudio = new ProcessBuilder(commands).start();
			try {
				stripAudio.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			file = notext + ".noaudio" + ext;
			// System.out.println(notext + ".noaudio" + ext);
			Process ffmpeg = new ProcessBuilder(FFMPEG_PATH + "/ffmpeg", "-i", file, "-r", "1", "-s", "sqcif", "-f",
					"image2", "/tmp/ffmpeg/image-%3d.jpeg").start();
			try {
				ffmpeg.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			File[] files = new File("/tmp/ffmpeg").listFiles();
			int numFrames = files.length;
			System.out.println("Frames: " + numFrames);
			musicInfos = new MusicInfo[numFrames];
			for (int i = 0; i < numFrames; i++) {
				pool.execute(new ImageProcessor(i, ImageIO.read(files[i])));
				files[i].delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		pool.shutdown();

		while (!pool.isTerminated())
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		System.out.println("Executed image processing!");
		new toMusic(musicInfos);
		new File(file).deleteOnExit();
	}
}

class ImageProcessor implements Runnable {

	private final int pos;
	private final BufferedImage img;

	public ImageProcessor(int num, BufferedImage image) {
		pos = num;
		img = image;
	}

	public void run() {
		ImageInfo imginf = new ImageInfo(img);
		MusicInfo frame = new MusicInfo(imginf);
		LoadVideo.musicInfos[pos] = frame;
	}
}
