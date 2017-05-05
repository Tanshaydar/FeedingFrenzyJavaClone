package com.bilkent.feedingbobby.view;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.bilkent.feedingbobby.controller.InputManager;
import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.PlayerFish;

public class GamePanel extends JPanel implements ComponentListener {
    public static final Dimension RESOLUTION = new Dimension(800, 600);

    private List<GameObject> gameObjects;
    private PlayerFish playerFish;

    private Image backgroundImage;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public GamePanel(JPanel cardPanel, CardLayout cardLayout) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        setDoubleBuffered(true);
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
            }

            @Override
            public void keyReleased( KeyEvent e) {
            }

            @Override
            public void keyPressed( KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setFocusable(false);
                    cardLayout.show(cardPanel, PauseMenuPanel.class.getName());
                }
            }
        });
    }

    public void showGameOverScreen() {
        setFocusable(false);
        cardLayout.show(cardPanel, PauseMenuPanel.class.getName());
    }

    @Override
    protected void paintComponent( Graphics g) {
        super.paintComponent(g);

        if (gameObjects == null || playerFish == null || gameObjects.isEmpty()) {
            return;
        }

        // Hide cursor to let player be visible by itself
        // Create a new blank cursor.
        BufferedImage cursorImg = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(16, 16, Transparency.TRANSLUCENT);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        // Set the blank cursor to the JFrame.
        setCursor(blankCursor);

        // Get Graphics 2D from graphics object g
        Graphics2D graphics2d = (Graphics2D) g;
        // Set font for something visible
        graphics2d.setFont(graphics2d.getFont().deriveFont(Font.BOLD, 20));
        // Get font metrics
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        // Set quality
        applyGraphicQuality(graphics2d);
        // Draw Background
        graphics2d.drawImage(backgroundImage, 0, 0, RESOLUTION.width, RESOLUTION.height, null);

        // Draw Bars
        graphics2d.setColor(Color.DARK_GRAY);
        // Frenzy Bar Background
        graphics2d.fillRoundRect(10, 10, 210, 40, 5, 5);
        // Growth Bar Background
        graphics2d.fillRoundRect(RESOLUTION.width - 220, 10, 210, 40, 5, 5);
        graphics2d.setColor(Color.ORANGE);
        // Frenzy Bar Frame
        graphics2d.drawRect(15, 15, 200, 30);
        // Frenzy Bar
        graphics2d.fillRect(15, 15, playerFish.getFrenzy() * 2, 30);
        graphics2d.setColor(Color.WHITE);
        String frenzyText;
        if (playerFish.getFrenzy() == 100) {
            frenzyText = "IN FRENZY!!!";
        } else {
            frenzyText = "Frenzy: " + playerFish.getFrenzy();
        }
        graphics2d.drawString(frenzyText, (225 - fontMetrics.stringWidth(frenzyText)) / 2,
                50 - fontMetrics.getHeight() / 2);

        graphics2d.setColor(Color.BLUE);
        // Growth bar frame
        graphics2d.drawRect(RESOLUTION.width - 215, 15, 200, 30);
        // Growth bar
        graphics2d.fillRect(RESOLUTION.width - 215, 15, playerFish.getGrowth() * 2, 30);
        // Growth text
        graphics2d.setColor(Color.WHITE);
        String sizeText = "Size: " + playerFish.getSize();
        graphics2d.drawString(sizeText, RESOLUTION.width - (215 + fontMetrics.stringWidth(sizeText)) / 2,
                50 - fontMetrics.getHeight() / 2);

        // Draw Lives
        graphics2d.setColor(Color.GREEN);
        String livesText = "Lives: " + playerFish.getLives();
        graphics2d.drawString(livesText, (RESOLUTION.width - fontMetrics.stringWidth(livesText)) / 2,
                50 - fontMetrics.getHeight() / 2);

        // Draw Time
        // graphics2d.setColor(Color.BLACK);
        // graphics2d.setFont(graphics2d.getFont().deriveFont(Font.BOLD, 10));

        graphics2d.setColor(Color.WHITE);

        // Draw Player
        if (playerFish.isCurrentlyActive()) {
            if (playerFish.getFrenzy() >= 100) {
                BufferedImage frenzyMask = generateMask(playerFish.getImage(), Color.ORANGE, 0.6f);
                BufferedImage frenzyTint = tint(playerFish.getImage(), frenzyMask);
                graphics2d.drawImage(frenzyTint, playerFish.getX(), playerFish.getY(), playerFish.getWidth(),
                        playerFish.getHeight(), null);
            } else {
                graphics2d.drawImage(playerFish.getImage(), playerFish.getX(), playerFish.getY(), playerFish.getWidth(),
                        playerFish.getHeight(), null);
            }
        } else {
            if (playerFish.isDamaged()) {
                BufferedImage damageMask = generateMask(playerFish.getImage(), Color.RED, 0.5f);
                BufferedImage damageTint = tint(playerFish.getImage(), damageMask);
                graphics2d.drawImage(damageTint, playerFish.getX(), playerFish.getY(), playerFish.getWidth(),
                        playerFish.getHeight(), null);
            } else {
                graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                graphics2d.drawImage(playerFish.getImage(), playerFish.getX(), playerFish.getY(), playerFish.getWidth(),
                        playerFish.getHeight(), null);
                graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }

        // Draw game objects
        for (GameObject gameObject : gameObjects) {

            graphics2d.drawImage(gameObject.getImage(), gameObject.getX(), gameObject.getY(), gameObject.getWidth(),
                    gameObject.getHeight(), null);

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

    public void setLevel( PlayerFish playerFish, List<GameObject> gameObjects, int level) {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/backgrounds/level_background_" + level + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.gameObjects = gameObjects;
        this.playerFish = playerFish;
    }

    private BufferedImage generateMask( BufferedImage imgSource, Color color, float alpha) {
        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D graphics2d = imgMask.createGraphics();
        applyGraphicQuality(graphics2d);

        graphics2d.drawImage(imgSource, 0, 0, null);
        graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        graphics2d.setColor(color);

        graphics2d.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
        graphics2d.dispose();

        return imgMask;
    }

    private BufferedImage tint( BufferedImage master, BufferedImage tint) {
        int imgWidth = master.getWidth();
        int imgHeight = master.getHeight();

        BufferedImage tinted = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D graphics2d = tinted.createGraphics();
        applyGraphicQuality(graphics2d);
        graphics2d.drawImage(master, 0, 0, null);
        graphics2d.drawImage(tint, 0, 0, null);
        graphics2d.dispose();

        return tinted;
    }

    private void applyGraphicQuality( Graphics2D graphics2d) {
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

}
