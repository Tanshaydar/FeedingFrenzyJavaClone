package com.bilkent.feedingbobby;

import javax.swing.SwingUtilities;

import com.bilkent.feedingbobby.view.MainFrame;

public class FeedingBobbyApp {

    public static void main( String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
            }
        });
    }

}
