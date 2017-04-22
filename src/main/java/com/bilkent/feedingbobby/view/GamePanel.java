package com.bilkent.feedingbobby.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private static final Dimension RESOLUTION = new Dimension(800, 600);

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Image fishImage;
    private Point mousePoint;

    public GamePanel(JPanel cardPanel, CardLayout cardLayout) {
        setDoubleBuffered(true);
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        mousePoint = new Point();
        setDoubleBuffered(true);
        setMinimumSize(RESOLUTION);
        setMaximumSize(RESOLUTION);
        setPreferredSize(RESOLUTION);

        setBackground(Color.CYAN);

        try {
            fishImage = ImageIO.read(getClass().getResource("/fish64x64.png"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved( MouseEvent e) {
                mousePoint = e.getPoint();
                repaint();
            }
        });
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

        addComponentListener(new ComponentListener() {

            @Override
            public void componentShown( ComponentEvent e) {
                setFocusable(true);
                requestFocus();
            }

            @Override
            public void componentResized( ComponentEvent e) {
            }

            @Override
            public void componentMoved( ComponentEvent e) {
            }

            @Override
            public void componentHidden( ComponentEvent e) {
            }
        });
    }

    @Override
    protected void paintComponent( Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D) g;

        graphics2d.drawImage(fishImage, mousePoint.x - fishImage.getWidth(null) / 2,
                mousePoint.y - fishImage.getHeight(null) / 2, null);

        graphics2d.dispose();
    }

}
