
import java.awt.Color;
import java.util.Arrays;

public class MusicInfo {
	Modality[] modalities;
	double[] tempos;

	public MusicInfo(ImageInfo imginf) {
		modalities = modality(imginf);
		tempos = tempos(imginf);
	}

	public String toString() {
		return modalities[0] + " " + tempos[0];
	}

	private static Modality[] modality(ImageInfo imginf) {
		float[][] hsbs = imginf.hsb;
		float[] centers = new float[(ImageInfo.ROWS - 1) * (ImageInfo.COLS - 1)],
				edges = new float[ImageInfo.ROWS * 2 + (ImageInfo.COLS - 2) * 2];
		for (int i = 0; i < ImageInfo.COLS; i++)
			for (int j = 0; j < ImageInfo.ROWS; j++) {
				float[] point = hsbs[i + j * ImageInfo.COLS];
				if (i == 0 || j == 0 || i == ImageInfo.ROWS - 1 || j == ImageInfo.COLS - 1) {
					edges[0] += point[0];
					edges[1] += point[1];
					edges[2] += point[2];
				} else {
					centers[0] += point[0];
					centers[1] += point[1];
					centers[2] += point[2];
				}
			}
		for (int i = 0; i < 3; i++) {
			edges[i] /= (2 * ImageInfo.ROWS + (ImageInfo.COLS - 2) * 2);
			centers[i] /= (ImageInfo.ROWS - 2) * (ImageInfo.COLS - 2);
		}
		// less than hue, greater than value and saturation
		return new Modality[] { modalFromHSB(hsbs[0]), modalFromHSB(hsbs[1]) };
	}

	private static final double[] thresholds = new double[] { .056, .181, .417, .667, 1 };
	private static final Modality[] hueOrder = new Modality[] { Modality.AEOLIAN, Modality.LYDIAN_SCALE,
			Modality.DORIAN, Modality.MIXOLYDIAN, Modality.MINOR };

	private static Modality modalFromHSB(float[] hsb) {
		// less than hue, greater than value and saturation
		if (hsb[2] <= .5)
			return Modality.MELODIC_MINOR;
		if (hsb[1] < .4 && hsb[2] > .3)
			return (int) (Math.random() + 1) == 0 ? Modality.MIXOLYDIAN : Modality.LYDIAN_SCALE;
		for (int i = 0; i < thresholds.length; i++)
			if (hsb[0] < thresholds[i])
				return hueOrder[i];
		return hueOrder[thresholds.length - 1];
		/*
		 * double[] hsb_thresholds = new double[] { .367, .83, .74 }; int rgb =
		 * Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]), r = rgb & 0xff0000, g = rgb &
		 * 0xff00, b = rgb & 0xff, avg = (r + g + b) / 3; boolean[] thresholded
		 * = new boolean[] { hsb[0] < hsb_thresholds[0], hsb[1] >
		 * hsb_thresholds[1], hsb[2] > hsb_thresholds[2] }; int threshold =
		 * (thresholded[0] ? 4 : 0) | (thresholded[1] ? 2 : 0) | (thresholded[2]
		 * ? 1 : 0); System.out.println(threshold); for (byte options = 0;
		 * options < 8; options++) if (options == threshold) return
		 * Modality.values()[options]; return null;
		 */
	}

	/**
	 * returns multiplicative factor of tempo
	 * 
	 * @param imginf
	 * @return
	 */
	private static final double[] tempoOrder = new double[] { 240.0, 240.0, 120.0,60.0, 60.0 };

	double[] tempos(ImageInfo imginf) {
		float[][] hsbs = imginf.hsb;
		float[] centers = new float[(ImageInfo.ROWS - 1) * (ImageInfo.COLS - 1)],
				edges = new float[ImageInfo.ROWS * 2 + (ImageInfo.COLS - 2) * 2];
		for (int i = 0; i < ImageInfo.COLS; i++)
			for (int j = 0; j < ImageInfo.ROWS; j++) {
				float[] point = hsbs[i + j * ImageInfo.COLS];
				if (i == 0 || j == 0 || i == ImageInfo.ROWS - 1 || j == ImageInfo.COLS - 1) {
					edges[0] += point[0];
					edges[1] += point[1];
					edges[2] += point[2];
				} else {
					centers[0] += point[0];
					centers[1] += point[1];
					centers[2] += point[2];
				}
			}
		for (int i = 0; i < 3; i++) {
			edges[i] /= (2 * ImageInfo.ROWS + (ImageInfo.COLS - 2) * 2);
			centers[i] /= (ImageInfo.ROWS - 2) * (ImageInfo.COLS - 2);
		}

		return new double[] { toMusic.MAIN_TEMPO / tempoFromHSB(hsbs[0]), toMusic.MAIN_TEMPO / tempoFromHSB(hsbs[1]) };
	}

	private static double tempoFromHSB(float[] hsb) {
		if (hsb[2] <= .3)
			return 30.0;
		if (hsb[1] < .4 && hsb[2] > .3)
			return 240.0;
		for (int i = 0; i < thresholds.length; i++)
			if (hsb[0] < thresholds[i])
				return tempoOrder[i];
		return tempoOrder[tempoOrder.length - 1];
	}
}
