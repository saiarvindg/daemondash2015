
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageInfo {

	static final int ROWS = 4;
	static int COLS = 4;
	static int CHUNKS = ROWS * COLS;

	final int CHUNK_HEIGHT;
	final int CHUNK_WIDTH;
	final int CHUNK_SIZE;

	float[][] hsb;

	public ImageInfo(BufferedImage buffImg) {
		CHUNK_HEIGHT = buffImg.getHeight() / ROWS;
		CHUNK_WIDTH = buffImg.getWidth() / COLS;
		CHUNK_SIZE = CHUNK_HEIGHT * CHUNK_WIDTH;
		hsb = new float[CHUNKS][3];
		getHSB(buffImg);
	}

	private void getHSB(BufferedImage img) {
		for (int chunk = 0; chunk < CHUNKS; chunk++) {
			long rgbs = 0;
			for (int i = 0; i < CHUNK_WIDTH; i++) {
				for (int j = 0; j < CHUNK_HEIGHT; j++) {
					rgbs += img.getRGB(i + chunk % COLS, j + chunk / ROWS);
				}
			}

			rgbs /= CHUNK_SIZE;
			Color.RGBtoHSB((int) (rgbs >> 16) & 0xff, (int) (rgbs >> 8) & 0xff, (int) (rgbs) & 0xff, hsb[chunk]);
			if (hsb[chunk][2]>1) {
				System.out.println("Error:");
				System.out.println((int) rgbs & 0xff0000);
				System.exit(1);
				
			}
		}

	}

}
