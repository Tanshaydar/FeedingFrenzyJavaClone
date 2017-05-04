package com.bilkent.feedingbobby.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.bilkent.feedingbobby.controller.InputManager;
import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.PlayerFish;

public class GamePanel extends JPanel implements ComponentListener {
    public static final Dimension RESOLUTION = new Dimension(800, 600);

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private List<GameObject> gameObjects;
    private Image backgroundImage;

    public GamePanel(JPanel cardPanel, CardLayout cardLayout) {
        setDoubleBuffered(true);
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        setDoubleBuffered(true);
        setMinimumSize(RESOLUTION);
        setMaximumSize(RESOLUTION);
        setPreferredSize(RESOLUTION);

        setBackground(Color.CYAN);

        addComponentListener(this);
        addMouseMotionListener(InputManager.getInstance());

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped( KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setFocusable(false);
                    cardLayout.show(cardPanel, MainMenuPanel.class.getName());
                }
            }

            @Override
            public void keyReleased( KeyEvent e) {
                setFocusable(false);
                cardLayout.show(cardPanel, MainMenuPanel.class.getName());
            }

            @Override
            public void keyPressed( KeyEvent e) {
                setFocusable(false);
                cardLayout.show(cardPanel, MainMenuPanel.class.getName());
            }
        });
    }

    public void draw( List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
        repaint();
    }

    @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (gameObjects == null || gameObjects.isEmpty()) {
			return;
		}

		Graphics2D graphics2d = (Graphics2D) g;

		graphics2d.drawImage(backgroundImage, 0, 0, RESOLUTION.width, RESOLUTION.height, null);

		for (GameObject gameObject : gameObjects) {
		    if(gameObject instanceof PlayerFish && ((PlayerFish)gameObject).isEnabled())
		    graphics2d.drawImage(gameObject.getImage(), gameObject.getX(),
                            gameObject.getY(), gameObject.getWidth(), gameObject.getHeight(), null);
                }
		
		graphics2d.dispose();
	}

    @Override
    public void componentResized( ComponentEvent e) {
    }

    @Override
    public void componentMoved( ComponentEvent e) {
    }

    @Override
    public void componentShown( ComponentEvent e) {
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void componentHidden( ComponentEvent e) {
    }

    public void setLevel( int level) {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/backgrounds/level_background_" + level + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
