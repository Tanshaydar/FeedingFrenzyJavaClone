package com.bilkent.feedingbobby;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import javax.swing.SwingUtilities;

import com.bilkent.feedingbobby.view.MainFrame;

public class FeedingBobbyApp {

    public static void main( String[] args) {
        try {
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("client.encoding.override", "UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

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
