package com.bilkent.feedingbobby.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class HighScoreTableModel extends AbstractTableModel {

    private static String[] columnNames = { "Name", "Score", "Date" };
    private List<HighScore> scores;

    public HighScoreTableModel() {
        scores = new ArrayList<>();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return scores.size();
    }

    @Override
    public Object getValueAt( int row, int column) {

        Object value = null;

        switch (column) {
        case 0:
            value = scores.get(row).getName();
            break;
        case 1:
            value = scores.get(row).getScore();
            break;
        case 2:
            value = scores.get(row).getDate();
            break;
        default:
            break;
        }

        return value;
    }

    @Override
    public String getColumnName( int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable( int rowIndex, int columnIndex) {
        return false;
    }

    public void addScores( List<HighScore> highScores) {
        scores.clear();
        scores.addAll(highScores);
        Collections.sort(scores);
        fireTableDataChanged();
    }

}
