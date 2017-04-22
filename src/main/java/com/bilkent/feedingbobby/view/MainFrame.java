package com.bilkent.feedingbobby.view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private JPanel contentPane;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private MainMenuPanel mainMenuPanel;
    private HighScorePanel highScorePanel;
    private HelpPanel helpPanel;
    private ChangeSettingsPanel changeSettingsPanel;
    private AboutPanel aboutPanel;
    private CreditsPanel creditsPanel;

    public MainFrame() {

        setTitle("Feeding Bobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        contentPane = new JPanel(true);
        setContentPane(contentPane);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.setDoubleBuffered(true);
        cardLayout = (CardLayout) cardPanel.getLayout();
        contentPane.add(cardPanel);

        mainMenuPanel = new MainMenuPanel(cardLayout, cardPanel);
        cardPanel.add(mainMenuPanel, MainMenuPanel.class.getName());
        highScorePanel = new HighScorePanel(cardLayout, cardPanel);
        cardPanel.add(highScorePanel, HighScorePanel.class.getName());
        helpPanel = new HelpPanel(cardLayout, cardPanel);
        cardPanel.add(helpPanel, HelpPanel.class.getName());
        changeSettingsPanel = new ChangeSettingsPanel(cardLayout, cardPanel);
        cardPanel.add(changeSettingsPanel, ChangeSettingsPanel.class.getName());
        aboutPanel = new AboutPanel(cardLayout, cardPanel);
        cardPanel.add(aboutPanel, AboutPanel.class.getName());
        creditsPanel = new CreditsPanel(cardLayout, cardPanel);
        cardPanel.add(creditsPanel, CreditsPanel.class.getName());
        
        cardLayout.show(cardPanel, MainMenuPanel.class.getName());
    }

}
