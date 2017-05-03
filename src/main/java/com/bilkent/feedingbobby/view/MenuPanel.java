package com.bilkent.feedingbobby.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public abstract class MenuPanel extends JPanel {

    private static final Dimension RESOLUTION = new Dimension(800, 600);
    protected Image backGroundImage;
    protected CardLayout cardLayout;
    protected JPanel parentPanel;

    public MenuPanel(CardLayout cardLayout, JPanel parentPanel) {
//        setDoubleBuffered(true);
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        try {
            backGroundImage = ImageIO.read(getClass().getResource("/backgrounds/main_menu_background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setMinimumSize(RESOLUTION);
        setMaximumSize(RESOLUTION);
        setPreferredSize(RESOLUTION);
        
        addComponentListener(new ComponentListener() {
            
            @Override
            public void componentShown( ComponentEvent e) {
                repaint();
            }
            
            @Override
            public void componentResized( ComponentEvent e) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void componentMoved( ComponentEvent e) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void componentHidden( ComponentEvent e) {
                // TODO Auto-generated method stub
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent( Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.drawImage(backGroundImage, 0, 0, null);
//        graphics2d.dispose();
    }

}
