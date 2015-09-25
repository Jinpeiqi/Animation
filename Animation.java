import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Animation extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image imgs[][];
	private Timer timer;
	static private int FPS = 150;
	private int row = 0, col = 0;
	boolean isInit;

	private enum State {
		init, active, walkLeft, walkRight, die, attack
	}

	private State curState;

	public Animation() {
		// TODO Auto-generated constructor stub

		try {
			
			curState = State.init;
			isInit = false;
			imgs = new Image[5][10];
			Image whole = ImageIO.read(new File("./1.png"));
			int x = 0, y = 0;
			for (int i = 0; i < 5; i++) {
				x = 0;
				for (int j = 0; j < 10; j++) {
					imgs[i][j] = ((BufferedImage) whole).getSubimage(x, y, 256,
							256);
					x += 256;
				}
				y += 256;
			}
			setPreferredSize(new Dimension(256, 256));
			timer = new Timer(FPS, new MyActionListener());
			this.addKeyListener(new MyKeyListener());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		 super.paintComponent(g);
		if (curState == State.walkRight) {
			Graphics2D a = (Graphics2D) g;
			a.drawImage(imgs[row][col], new AffineTransform(-1, 0, 0, 1,
					imgs[row][col].getWidth(null), 0), null);
		} else
			g.drawImage(imgs[row][col], 0, 0, 256, 256, null);
	}

	private class MyKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (curState == State.init) {
					row = 0;
					col = 0;
					timer.start();
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN && isInit) {
				curState = State.die;
				isInit = false;
				col = 0;
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT && isInit) {
				curState = State.walkLeft;
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT && isInit) {
				curState = State.walkRight;
			}

			if (e.getKeyCode() == KeyEvent.VK_SPACE && isInit) {
				curState = State.attack;
				col = 0;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_LEFT && isInit) {
				curState = State.active;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_RIGHT && isInit) {
				curState = State.active;
			}
		}

	}
	JComponent a;

	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (curState == State.init) {
			
				repaint();
				if (col >= 9) {
					curState = State.active;
					isInit = true;
					return;
				}
				col++;
			}
			if (curState == State.active) {
				row = 1;
				if (col >= 5) {
					col = 0;
				}
				col++;
				repaint();
			}
			if (curState == State.walkLeft || curState == State.walkRight) {
				row = 2;
				if (col >= 6) {
					col = 0;

				}
				col++;
				repaint();

			}

			if (curState == State.die) {
				row = 3;
				repaint();
				if (col >= 7) {
					timer.stop();
					curState = State.init;
					return;
				}
				col++;
			}

			if (curState == State.attack) {
				row = 4;
				repaint();
				if (col >= 7) {
					curState = State.active;
					return;
				}
				col++;
			}
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Animation panel = new Animation();	
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setFocusable(false);
		if(!panel.isFocusOwner())
		panel.requestFocus();

	}

}
