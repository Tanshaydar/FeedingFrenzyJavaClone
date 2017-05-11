package com.bilkent.feedingbobby.view;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import com.bilkent.feedingbobby.controller.SettingsManager;
import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.PlayerFish;

public class GamePanel extends MenuPanel implements ComponentListener {

    /** The Constant RESOLUTION. */
    public static final Dimension RESOLUTION = new Dimension(800, 600);

    /** The game objects. */
    private List<GameObject> gameObjects;

    /** The player fish. */
    private PlayerFish playerFish;

    /** The background image. */
    private Image backgroundImage;

    private boolean isInLevelTransition = false;
    private int seconds;
    private int minutes;

    public GamePanel(CardLayout cardLayout, JPanel cardPanel) {
        super(cardLayout, cardPanel);
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
        cardLayout.show(parentPanel, PauseMenuPanel.class.getName());
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
        graphics2d.drawString(frenzyText, (225 - graphics2d.getFontMetrics().stringWidth(frenzyText)) / 2,
                50 - graphics2d.getFontMetrics().getHeight() / 2);

        graphics2d.setColor(Color.BLUE);
        // Growth bar frame
        graphics2d.drawRect(RESOLUTION.width - 215, 15, 200, 30);
        // Growth bar
        graphics2d.fillRect(RESOLUTION.width - 215, 15, playerFish.getGrowth() * 2, 30);
        // Growth text
        graphics2d.setColor(Color.WHITE);
        String sizeText = "Size: " + playerFish.getSize();
        graphics2d.drawString(sizeText,
                RESOLUTION.width - (215 + graphics2d.getFontMetrics().stringWidth(sizeText)) / 2,
                50 - graphics2d.getFontMetrics().getHeight() / 2);

        // Draw Lives
        graphics2d.setColor(Color.GREEN);
        String livesText = "Lives: " + playerFish.getLives();
        graphics2d.drawString(livesText, (RESOLUTION.width - graphics2d.getFontMetrics().stringWidth(livesText)) / 2,
                50 - graphics2d.getFontMetrics().getHeight() / 2);

        // Draw Time
        graphics2d.setColor(Color.BLACK);
        graphics2d.setFont(graphics2d.getFont().deriveFont(Font.BOLD, 10));
        String timeText = String.format("%02d : %02d", minutes, seconds % 60);
        graphics2d.drawString(timeText, (RESOLUTION.width - graphics2d.getFontMetrics().stringWidth(timeText)) / 2, 50);

        // Draw Level State
        if (isInLevelTransition) {
            graphics2d.setFont(graphics2d.getFont().deriveFont(Font.BOLD, 30));
            String levelCompleteText = String.format("LEVEL COMPLETE!");
            graphics2d.drawString(levelCompleteText,
                    (RESOLUTION.width - graphics2d.getFontMetrics().stringWidth(levelCompleteText)) / 2,
                    (RESOLUTION.height - graphics2d.getFontMetrics().getHeight()) / 2);
        }

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

        switch (SettingsManager.getInstance().getSettings().getGraphicsQuality()) {

        case HIGH:
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            break;
        case LOW:
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
            graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            graphics2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            break;
        case NORMAL:
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
            graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
            graphics2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
            break;
        default:
            break;
        }
    }

    public void setMinutes( long minutes) {
        this.minutes = (int) minutes;
    }

    public void setSeconds( long seconds) {
        this.seconds = (int) seconds;
    }

    public void setInLevelTransition( boolean isInLevelTransition) {
        this.isInLevelTransition = isInLevelTransition;
    }

}
