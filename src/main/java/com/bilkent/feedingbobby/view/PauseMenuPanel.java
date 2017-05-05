package com.bilkent.feedingbobby.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.bilkent.feedingbobby.controller.GameManager;

public class PauseMenuPanel extends MenuPanel implements ComponentListener {

    private JLabel gamePausedLabel;
    private JButton resumeButton;
    private JButton exitGameButton;

    private GameManager gameManager;
    private GamePanel gamePanel;

    public PauseMenuPanel(CardLayout cardLayout, JPanel cardPanel, GameManager gameManager, GamePanel gamePanel) {
        super(cardLayout, cardPanel);
        this.gameManager = gameManager;
        this.gamePanel = gamePanel;

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 150, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 65, 65, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        gamePausedLabel = new JLabel("GAME IS PAUSED!");
        gamePausedLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        gamePausedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gamePausedLabel.setForeground(Color.ORANGE);
        GridBagConstraints gbc_gamePausedLabel = new GridBagConstraints();
        gbc_gamePausedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_gamePausedLabel.gridwidth = 3;
        gbc_gamePausedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_gamePausedLabel.gridx = 0;
        gbc_gamePausedLabel.gridy = 0;
        add(gamePausedLabel, gbc_gamePausedLabel);

        resumeButton = new JButton("Resume Game");
        GridBagConstraints gbc_resumeButton = new GridBagConstraints();
        gbc_resumeButton.fill = GridBagConstraints.BOTH;
        gbc_resumeButton.insets = new Insets(0, 0, 15, 5);
        gbc_resumeButton.gridx = 1;
        gbc_resumeButton.gridy = 1;
        add(resumeButton, gbc_resumeButton);

        exitGameButton = new JButton("Exit Game");
        GridBagConstraints gbc_exitGameButton = new GridBagConstraints();
        gbc_exitGameButton.fill = GridBagConstraints.BOTH;
        gbc_exitGameButton.insets = new Insets(0, 0, 15, 5);
        gbc_exitGameButton.gridx = 1;
        gbc_exitGameButton.gridy = 2;
        add(exitGameButton, gbc_exitGameButton);

        addActionListeners();
        addComponentListener(this);
    }

    private void addActionListeners() {
        resumeButton.addActionListener(ae -> {
            
            if(resumeButton.getText().equals("Resume Game")) {
                cardLayout.show(parentPanel, GamePanel.class.getName());
                gameManager.resumeGame();
            } else if(resumeButton.getText().equals("Restart Game")) {
                cardLayout.show(parentPanel, GamePanel.class.getName());
                gameManager.startGame();
            }
        });
        exitGameButton.addActionListener(ae -> {
            gameManager.stopGame();
            cardLayout.show(parentPanel, MainMenuPanel.class.getName());
            parentPanel.revalidate();
        });
    }

    @Override
    public void componentResized( ComponentEvent e) {
    }

    @Override
    public void componentMoved( ComponentEvent e) {
    }

    @Override
    public void componentShown( ComponentEvent e) {
        if (gameManager.isGameOver()) {
            gamePausedLabel.setText("GAME OVER");
            gamePausedLabel.setForeground(Color.RED);
            resumeButton.setText("Restart Game");
        } else {
            gamePausedLabel.setText("GAME IS PAUSED!");
            gamePausedLabel.setForeground(Color.ORANGE);
            resumeButton.setText("Resume Game");
        }
    }

    @Override
    public void componentHidden( ComponentEvent e) {
    }

}
