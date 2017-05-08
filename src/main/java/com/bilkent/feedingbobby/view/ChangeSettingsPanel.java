package com.bilkent.feedingbobby.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.bilkent.feedingbobby.controller.SettingsManager;
import com.bilkent.feedingbobby.model.GraphicsQuality;
import com.bilkent.feedingbobby.model.Settings;

public class ChangeSettingsPanel extends MenuPanel {

    private JButton backButton;
    private JButton applySettingsButton;
    private JLabel qualitySettingLabel;
    private JComboBox<GraphicsQuality> qualitySettingComboBox;
    private JLabel audioSettingLabel;
    private JCheckBox audioSettingsCheckBox;
    private JLabel audioLevelLabel;
    private JSlider audioLevelSlider;

    public ChangeSettingsPanel(CardLayout cardLayout, JPanel cardPanel) {
        super(cardLayout, cardPanel);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 150, 150, 0, 150, 0 };
        gridBagLayout.rowHeights = new int[] { 65, 65, 65, 65, 0, 65, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        qualitySettingLabel = new JLabel("Quality:");
        qualitySettingLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_qualitySettingLabel = new GridBagConstraints();
        gbc_qualitySettingLabel.anchor = GridBagConstraints.EAST;
        gbc_qualitySettingLabel.insets = new Insets(0, 0, 5, 5);
        gbc_qualitySettingLabel.gridx = 0;
        gbc_qualitySettingLabel.gridy = 1;
        add(qualitySettingLabel, gbc_qualitySettingLabel);

        qualitySettingComboBox = new JComboBox<GraphicsQuality>();
        for (GraphicsQuality graphicsQuality : GraphicsQuality.values()) {
            qualitySettingComboBox.addItem(graphicsQuality);
        }
        GridBagConstraints gbc_qualitySettingComboBox = new GridBagConstraints();
        gbc_qualitySettingComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_qualitySettingComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_qualitySettingComboBox.gridx = 1;
        gbc_qualitySettingComboBox.gridy = 1;
        add(qualitySettingComboBox, gbc_qualitySettingComboBox);

        audioSettingLabel = new JLabel("Audio:");
        audioSettingLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_audioSettingLabel = new GridBagConstraints();
        gbc_audioSettingLabel.anchor = GridBagConstraints.EAST;
        gbc_audioSettingLabel.insets = new Insets(0, 0, 5, 5);
        gbc_audioSettingLabel.gridx = 0;
        gbc_audioSettingLabel.gridy = 2;
        add(audioSettingLabel, gbc_audioSettingLabel);

        audioSettingsCheckBox = new JCheckBox("Audio Enabled");
        GridBagConstraints gbc_audioSettingsCheckBox = new GridBagConstraints();
        gbc_audioSettingsCheckBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_audioSettingsCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_audioSettingsCheckBox.gridx = 1;
        gbc_audioSettingsCheckBox.gridy = 2;
        add(audioSettingsCheckBox, gbc_audioSettingsCheckBox);

        audioLevelLabel = new JLabel("Audio Level:");
        audioLevelLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_audioLevelLabel = new GridBagConstraints();
        gbc_audioLevelLabel.anchor = GridBagConstraints.EAST;
        gbc_audioLevelLabel.insets = new Insets(0, 0, 5, 5);
        gbc_audioLevelLabel.gridx = 0;
        gbc_audioLevelLabel.gridy = 3;
        add(audioLevelLabel, gbc_audioLevelLabel);

        audioLevelSlider = new JSlider(0, 100);
        GridBagConstraints gbc_slider = new GridBagConstraints();
        gbc_slider.fill = GridBagConstraints.HORIZONTAL;
        gbc_slider.insets = new Insets(0, 0, 5, 5);
        gbc_slider.gridx = 1;
        gbc_slider.gridy = 3;
        add(audioLevelSlider, gbc_slider);

        backButton = new JButton("Back");
        GridBagConstraints gbc_backButton = new GridBagConstraints();
        gbc_backButton.insets = new Insets(0, 15, 15, 15);
        gbc_backButton.fill = GridBagConstraints.BOTH;
        gbc_backButton.gridx = 0;
        gbc_backButton.gridy = 5;
        add(backButton, gbc_backButton);

        applySettingsButton = new JButton("Apply");
        GridBagConstraints gbc_applySettingsButton = new GridBagConstraints();
        gbc_applySettingsButton.insets = new Insets(0, 15, 15, 15);
        gbc_applySettingsButton.fill = GridBagConstraints.BOTH;
        gbc_applySettingsButton.gridx = 3;
        gbc_applySettingsButton.gridy = 5;
        add(applySettingsButton, gbc_applySettingsButton);

        addActionListenres();

        Settings settings = SettingsManager.getInstance().getSettings();
        if (settings == null) {
            Settings newSettings = new Settings(GraphicsQuality.HIGH, true, 50);
            SettingsManager.getInstance().writeSettingsToFile(newSettings);
        } else {
            qualitySettingComboBox.setSelectedItem(settings.getGraphicsQuality());
            audioSettingsCheckBox.setSelected(settings.isAudioEnabled());
            audioLevelSlider.setValue(settings.getAudioLevel());
        }
    }

    private void addActionListenres() {
        backButton.addActionListener(ae -> {
            cardLayout.show(parentPanel, MainMenuPanel.class.getName());
        });
        applySettingsButton.addActionListener(ae -> {
            Settings settings = new Settings((GraphicsQuality) qualitySettingComboBox.getSelectedItem(),
                    audioSettingsCheckBox.isSelected(), audioLevelSlider.getValue());
            SettingsManager.getInstance().writeSettingsToFile(settings);
        });
    }

}
