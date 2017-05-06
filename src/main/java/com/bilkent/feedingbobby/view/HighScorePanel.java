package com.bilkent.feedingbobby.view;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import com.bilkent.feedingbobby.controller.HighScoreManager;
import com.bilkent.feedingbobby.model.HighScoreTableModel;

public class HighScorePanel extends MenuPanel implements ComponentListener {

    private JButton backButton;
    private JScrollPane scrollPane;
    private JTable table;
    private HighScoreTableModel highScoreTableModel;

    public HighScorePanel(CardLayout cardLayout, JPanel cardPanel) {
        super(cardLayout, cardPanel);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 150, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 65, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        highScoreTableModel = new HighScoreTableModel();
        scrollPane = new JScrollPane();
        table = new JTable(highScoreTableModel);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<HighScoreTableModel> rowSorter = new TableRowSorter<HighScoreTableModel>(highScoreTableModel);
        table.setRowSorter(rowSorter);
        scrollPane.setViewportView(table);
        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.gridwidth = 2;
        gbc_textArea.insets = new Insets(15, 15, 15, 15);
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 0;
        add(scrollPane, gbc_textArea);

        backButton = new JButton("Back");
        GridBagConstraints gbc_backButton = new GridBagConstraints();
        gbc_backButton.insets = new Insets(0, 15, 15, 15);
        gbc_backButton.fill = GridBagConstraints.BOTH;
        gbc_backButton.gridx = 0;
        gbc_backButton.gridy = 1;
        add(backButton, gbc_backButton);

        addActionListeners();
        addComponentListener(this);
        fillTable();
    }

    private void addActionListeners() {
        backButton.addActionListener(ae -> {
            cardLayout.show(parentPanel, MainMenuPanel.class.getName());
        });
    }

    private void fillTable() {
        highScoreTableModel.addScores(HighScoreManager.getInstance().getHighScores());
    }

    @Override
    public void componentHidden( ComponentEvent e) {}

    @Override
    public void componentMoved( ComponentEvent e) {}

    @Override
    public void componentResized( ComponentEvent e) {}

    @Override
    public void componentShown( ComponentEvent e) {
        fillTable();
    }

}
