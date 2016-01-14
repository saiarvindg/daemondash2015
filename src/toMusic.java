

import java.util.ArrayList;
import java.util.Random;

import jm.JMC;
import jm.audio.AOException;
import jm.constants.Scales;
import jm.music.data.*;
import jm.util.*;

public class toMusic implements JMC {

	public static final double MAIN_TEMPO = 240;

	private static int keySigna = C4;
	private static final double[] PATTERN1 = { .5, .5, 1, 1, 1 };
	private static final double[] PATTERN2 = { .5, .5, .5, .5,  .5, .5, .5, .5};
	private static Random r = new Random();

	public toMusic(MusicInfo[] a) {
		constructScore(a);
	}

	public static Phrase generate(int startingNote, int[] scale, int numberOfMeasures, double restProbability, int inst,
			double tem) {
		Phrase phr = new Phrase(0.0);
		ArrayList<Note> sc = new ArrayList<Note>();
		for (int i : scale) {
			Note n = new Note(startingNote + i, QUARTER_NOTE);
			sc.add(n.copy());
			if (i < 7) {
				n = new Note(startingNote + i + 12, QUARTER_NOTE);
			}
			sc.add(n.copy());
		}
		Note res = new Note(REST, EIGHTH_NOTE * tem);
		res.setDuration(.9 * res.getRhythmValue());
		double loops = numberOfMeasures / tem;
		for (int i = 0; i < loops; i++) {
			double co = 0;
			// System.out.println("\nMEASURE\n");
			double o = 8;
			if (loops - i < 1) {
				o = 8 * loops;
			}
			while (Math.round(co * 2) <= o - 1) {
				if (restProbability > r.nextDouble()) {
					phr.add(res.copy());
					co += EIGHTH_NOTE;
					// System.out.println("REST");
					continue;
				}
				Note n = sc.get(r.nextInt(sc.size())).copy();
				if (r.nextInt(2) == 1 || (Math.round(co * 2) == o - 1)) {
					n.setRhythmValue(EIGHTH_NOTE * tem);
					n.setDuration(.9 * n.getRhythmValue());
					phr.add(n);
					co += EIGHTH_NOTE;
					// System.out.println("EIGHTH");
					continue;
				}
				co += QUARTER_NOTE;

				// System.out.println("QUARTER");
				n.setRhythmValue(QUARTER_NOTE * tem);
				n.setDuration(.9 * n.getRhythmValue());
				phr.add(n);
			}
		}
		// phr.setTempo(tem);
		phr.setInstrument(inst);
		return phr;
	}

	public static Phrase generateBeat(int startingNote, double[] pattern, int nomeasures, int inst, double tem) {
		Phrase phr = new Phrase(0.0);

		for (int i = 0; i < Math.round(nomeasures * pattern.length / tem); i++) {
			Note n = new Note(startingNote, pattern[i % pattern.length] * tem);
			n.setDuration(.9 * n.getRhythmValue());
			phr.add(n);
		}
		// phr.setTempo(tem);
		phr.setInstrument(inst);
		return phr;
	}

	public static void MusicalColoring(Modality[] modes, double[] tempos, Part[] p) {
		System.out.println(modes[0].name());
		p[0].appendPhrase(generate(keySigna, getScalees(modes[0].ordinal()), 1, .2, p[0].getInstrument(), tempos[0]/2.0));
		p[1].appendPhrase(generateBeat(keySigna-12 + MAJOR_SCALE[modes[0].ordinal()%7], PATTERN2, 1, p[1].getInstrument(), tempos[1]));
		p[2].appendPhrase(generateBeat(D2, PATTERN1, 1, p[2].getInstrument(), 1));
		p[3].appendPhrase(generate(keySigna, getScalees(modes[0].ordinal()), 1, .1, p[3].getInstrument(), tempos[0]/2.0));
	}
	public static int[] getScalees(int x){
		switch (x) {
		case 1:
			return MAJOR_SCALE;
		case 2:
			return DORIAN_SCALE;
		case 3:
			return AEOLIAN_SCALE;

		case 4:
			return LYDIAN_SCALE;

		case 5:
			return MIXOLYDIAN_SCALE;


		case 6:
			return MINOR_SCALE;


		default:
			return MAJOR_SCALE; 
		}

	}

	public static void constructScore(MusicInfo[] infos) {
		// toMusic a=new toMusic();
		// Part p1=new Part();
		// p1.setInstrument(GUITAR);
		// p1.appendPhrase(a.generate(C4, MAJOR_SCALE, 2, .20,GUITAR,2));
		// p1.appendPhrase(a.generate(C4, MAJOR_SCALE, 2, .20,GUITAR,1.0/2.0));
		// p1.appendPhrase(a.generate(C4, MAJOR_SCALE, 2, .20,GUITAR,2));
		// p1.appendPhrase(a.generate(C4, MAJOR_SCALE, 2, .20,GUITAR,1));
		// p1.setChannel(0);
		// Part p2=new Part();
		// p2.setInstrument(PIANO);
		// p2.appendPhrase(a.generateBeat(A2, PATTERN1, 2,PIANO,2));
		// p2.appendPhrase(a.generateBeat(E2, PATTERN1, 2, PIANO,1.0/2.0));
		// p2.appendPhrase(a.generateBeat(D2, PATTERN1, 2, PIANO,2));
		// p2.appendPhrase(a.generateBeat(G2, PATTERN1, 2, PIANO,1));
		// p2.setChannel(1);
		Part[] insts = new Part[4];
		insts[0] = new Part();
		insts[0].setInstrument(VIOLIN);
		insts[0].setChannel(1);
		insts[1] = new Part();
		insts[1].setInstrument(DOUBLE_BASS);
		insts[1].setChannel(2);
		insts[2] = new Part();
		insts[2].setInstrument(DRUM);
		insts[2].setChannel(3);
		insts[3] = new Part();
		insts[3].setInstrument(GUITAR);
		insts[3].setChannel(4);


		Score s = new Score();
		System.out.println(s);

		for (MusicInfo mi : infos)
			MusicalColoring(mi.modalities, mi.tempos, insts);

		s.add(insts[0]);
		s.add(insts[1]);
		s.add(insts[2]);
		s.add(insts[3]);

		s.setTempo(MAIN_TEMPO);

		/** OUTPUT **/
		Write.midi(s, "Synesthesia.mid");
		//Play.midi(s);
	}

}
