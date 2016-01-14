

public enum Modality {
	MAJOR_SCALE(1), DORIAN(2), NATURAL_MINOR(3), LYDIAN_SCALE(4), MIXOLYDIAN(5), AEOLIAN(6), DIATONIC_MINOR(
			7), CHROMATIC(8), BLUES(9), INDIAN(10), HARMONIC_MINOR(11), MELODIC_MINOR(12), MINOR(
					13), PENTATONIC_SCALE(14), TURKISH_SCALE(15);

	private int mode;

	Modality(int offset) {
		mode = offset;
	}
}
