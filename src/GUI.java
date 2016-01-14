import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static BufferedImage play = null;
	private static BufferedImage stop = null;
	private static BufferedImage pause = null;
	private static BufferedImage exp = null;
	private static BufferedImage imp = null;
	private static double midiEnd = 0;
	private static double midiCur = 0;
	private static JProgressBar testPB = new JProgressBar();
	private static JProgressBar importPB = new JProgressBar();
	private static GUI gtw = null;
	private static Sequencer sq = null;
	private static boolean disPlay = true;
	private static File inputFile = null;
	private static File youtubeFile = null;
	private static JFileChooser fc = new JFileChooser();
	private static JTextField linkLabel = new JTextField(" YouTube Link:");
	private static JTextField linkAdd = new JTextField();
	private static JTextField curFileLabel = new JTextField(" Current File:");
	private static JTextField curFileInput = new JTextField();
	private static File midiFile = null;

	public GUI() {
		super("Synesthesia");

		setBackground(new Color(0, 0, 0));
		setSize(new Dimension(800, 400));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		JPanel panel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				if (g instanceof Graphics2D) {
					final int R = 50;
					final int G = 50;
					final int B = 50;

					Paint p = new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 200), 0.0f, getHeight(),
							new Color(R, G, B, 240), true);
					Graphics2D g2d = (Graphics2D) g;
					g2d.setPaint(p);
					g2d.fillRect(0, 0, getWidth(), getHeight());
				}
			}
		};
		setContentPane(panel);
		setLayout(null);
		Border mouseOverBorder = new LineBorder(new Color(200, 200, 200), 4);

		try {
			play = ImageIO.read(new File("playImg.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			pause = ImageIO.read(new File("pauseImg.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stop = ImageIO.read(new File("stopImg.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			exp = ImageIO.read(new File("expImg.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			imp = ImageIO.read(new File("impImg.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JPanel button1 = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (disPlay)
					g.drawImage(play, 0, 0, null);
				else
					g.drawImage(pause, 0, 0, null);
			}
		};
		button1.setSize(120, 120);
		button1.setBorder(null);
		button1.setBackground(new Color(160, 160, 160));
		button1.setLocation(480, 20);
		button1.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				button1.setBackground(new Color(160, 160, 160));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Not working :(
				button1.setBackground(new Color(200, 200, 200));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button1.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button1.setBorder(mouseOverBorder);
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				midiFile = new File("Synesthesia.mid");
				if (disPlay) {
					sq.start();
					disPlay = false;
				} else {
					sq.stop();
					disPlay = true;
				}

				if (midiCur >= midiEnd)
					testPB.setValue((int) (midiCur / (midiEnd) * 100));
				repaint();
			}
		});

		JPanel button2 = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(stop, 0, 0, null);
			}
		};

		button2.setSize(120, 120);
		button2.setBorder(null);
		button2.setBackground(new Color(160, 160, 160));
		button2.setLocation(620, 20);
		button2.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				button2.setBackground(new Color(160, 160, 160));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Not working :(
				button2.setBackground(new Color(200, 200, 200));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button2.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button2.setBorder(mouseOverBorder);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				sq.stop();
				midiFile = new File("Synesthesia.mid");
				disPlay = true;
				sq.setMicrosecondPosition(0);
				testPB.setValue((int) (midiCur / (midiEnd) * 100));
			}
		});

		JPanel button3 = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(imp, 0, 0, null);
			}
		};
		button3.setSize(120, 120);
		button3.setBorder(null);
		button3.setBackground(new Color(160, 160, 160));
		button3.setLocation(480, 160);
		button3.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				button3.setBackground(new Color(160, 160, 160));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Not working :(
				button3.setBackground(new Color(200, 200, 200));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button3.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button3.setBorder(mouseOverBorder);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Video Files", "flv", "mp4", "ogg", "swf", "mov",
						"wmv", "avi", "wav", "3gp"));
				fc.setAcceptAllFileFilterUsed(false);

				int returnVal = fc.showOpenDialog(GUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					inputFile = fc.getSelectedFile();
					// This is where a real application would open the file.
					System.out.println("Importing: " + inputFile.getName() + ".");
					curFileInput.setText(inputFile.getName());
				} else {
					System.out.println("Open command cancelled by user.");
				}
			}
		});

		JPanel button4 = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(exp, 0, 0, null);
			}
		};
		button4.setSize(120, 120);
		button4.setBorder(null);
		button4.setBackground(new Color(160, 160, 160));
		button4.setLocation(620, 160);
		button4.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				button4.setBackground(new Color(160, 160, 160));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Not working :(
				button4.setBackground(new Color(200, 200, 200));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button4.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button4.setBorder(mouseOverBorder);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Midi File", "mid"));
				fc.setAcceptAllFileFilterUsed(false);
				fc.showSaveDialog(null);
			}
		});

		testPB.setBackground(Color.black);
		testPB.setForeground(new Color(39, 117, 236));
		testPB.setBorder(null);
		testPB.setBorderPainted(false);
		testPB.setSize(260, 40);
		testPB.setLocation(480, 300);
		testPB.setValue(100);

		importPB.setBackground(Color.black);
		importPB.setForeground(new Color(39, 117, 236));
		importPB.setBorder(null);
		importPB.setBorderPainted(false);
		importPB.setSize(435, 40);
		importPB.setLocation(15, 300);
		importPB.setValue(0);

		Font labelFont = new Font("Tahoma Bold", Font.BOLD, 18);
		Font linkFont = new Font("Tahoma Bold", Font.PLAIN, 18);

		linkLabel.setFont(labelFont);
		linkLabel.setForeground(Color.BLACK);
		linkLabel.setBorder(null);
		linkLabel.setBackground(new Color(160, 160, 160));
		linkLabel.setEditable(false);
		linkLabel.setLocation(15, 20);
		linkLabel.setSize(140, 40);

		linkAdd.setFont(linkFont);
		linkAdd.setBorder(null);
		linkAdd.setSize(298, 40);
		linkAdd.setBackground(new Color(160, 160, 160));
		linkAdd.setForeground(Color.black);
		linkAdd.setLocation(153, 20);

		curFileLabel.setFont(labelFont);
		curFileLabel.setForeground(Color.BLACK);
		curFileLabel.setBorder(null);
		curFileLabel.setBackground(new Color(160, 160, 160));
		curFileLabel.setEditable(false);
		curFileLabel.setLocation(15, 100);
		curFileLabel.setSize(140, 40);

		curFileInput.setFont(linkFont);
		curFileInput.setBorder(null);
		curFileInput.setSize(298, 40);
		curFileInput.setEditable(false);
		curFileInput.setBackground(new Color(160, 160, 160));
		curFileInput.setForeground(Color.black);
		curFileInput.setLocation(153, 100);

		JPanel button5 = new JPanel();
		JLabel button5Text0 = new JLabel("                    ");
		JLabel button5Text1 = new JLabel("GENERATE FROM");
		JLabel button5Text2 = new JLabel("YOUTUBE");
		// button5.setLayout(new CardLayout());
		button5Text0.setFont(labelFont);
		button5Text1.setFont(labelFont);
		button5Text2.setFont(labelFont);
		button5.add(button5Text0);
		button5.add(button5Text1);
		button5.add(button5Text2);
		button5.setSize(207, 120);
		button5.setBorder(null);
		button5.setBackground(new Color(160, 160, 160));
		button5.setLocation(15, 160);
		button5.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				button5.setBackground(new Color(160, 160, 160));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Not working :(
				button5.setBackground(new Color(200, 200, 200));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button5.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button5.setBorder(mouseOverBorder);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				String youtubeVidUrl = linkAdd.getText();
				try {
					LoadVideo.getYouTubeVid(youtubeVidUrl);
					youtubeFile = new File(LoadVideo.file);
					midiFile = new File("Synesthesia.mid");
					 Desktop.getDesktop().open(youtubeFile);
					sq.start();
					disPlay = false;
					repaint();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		JPanel button6 = new JPanel();
		JLabel button6Text0 = new JLabel("                    ");
		JLabel button6Text1 = new JLabel("      GENERATE FROM      ");
		JLabel button6Text2 = new JLabel("FILE");
		// button6.setLayout(new BoxLayout());
		button6Text0.setFont(labelFont);
		button6Text1.setFont(labelFont);
		button6Text2.setFont(labelFont);
		button6.add(button6Text0);
		button6.add(button6Text1);
		button6.add(button6Text2);
		button6.setSize(207, 120);
		button6.setBorder(null);
		button6.setBackground(new Color(160, 160, 160));
		button6.setLocation(243, 160);
		button6.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				button6.setBackground(new Color(160, 160, 160));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Not working :(
				button6.setBackground(new Color(200, 200, 200));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button6.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button6.setBorder(mouseOverBorder);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				midiFile = new File("Synesthesia.mid");
				LoadVideo.file = inputFile.getAbsolutePath();
				LoadVideo.loadFile();
				midiFile = new File("Synesthesia.mid");
				try {
					Desktop.getDesktop().open(inputFile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				sq.start();

				disPlay = false;
				repaint();

				if (midiCur >= midiEnd)
					testPB.setValue((int) (midiCur / (midiEnd) * 100));
				repaint();
			}
		});

		add(testPB);
		add(importPB);
		add(button1);
		add(button2);
		add(button3);
		add(button4);
		add(linkLabel);
		add(linkAdd);
		add(curFileLabel);
		add(curFileInput);
		add(button5);
		add(button6);

	}

	public static void main(String[] args) {
		// Determine what the GraphicsDevice can support.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		boolean isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);

		// If translucent windows aren't supported, exit.
		if (!isPerPixelTranslucencySupported) {
			System.out.println("Per-pixel translucency is not supported");
			System.exit(0);
		}

		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create the GUI on the event-dispatching thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gtw = new GUI();

				// Display the window.
				gtw.setVisible(true);
			}
		});
		try {
			sq = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sq.open();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(new File("Synesthesia.mid")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sq.setSequence(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				is = new BufferedInputStream(new FileInputStream(midiFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sq.setSequence(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			midiCur = sq.getMicrosecondPosition();
			midiEnd = sq.getMicrosecondLength();
			while (midiCur < midiEnd) {
				midiCur = sq.getMicrosecondPosition();
				// System.out.println(midiCur/midiEnd); ==> debug purposes
				testPB.setValue((int) (midiCur / (midiEnd) * 100));
			}
			disPlay = true;
			sq.setMicrosecondPosition(0);
			testPB.setValue(0);
			gtw.repaint();
		}
	}
}