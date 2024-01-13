/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package regenrolmentsys;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author harley
 */
public class TableUtil {
    public static TableModel resultSetToTableModel(ResultSet rs) {
	try {
	    ResultSetMetaData metaData = rs.getMetaData();
	    int numberOfColumns = metaData.getColumnCount();
	    Vector<String> columnNames = new Vector<String>();

	    // Get the column names
	    for (int column = 0; column < numberOfColumns; column++) {
		columnNames.addElement(metaData.getColumnLabel(column + 1));
	    }

	    // Get all rows.
	    Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

	    while (rs.next()) {
		Vector<Object> newRow = new Vector<Object>();

		for (int i = 1; i <= numberOfColumns; i++) {
		    newRow.addElement(rs.getObject(i));
		}

		rows.addElement(newRow);
	    }

	    return new DefaultTableModel(rows, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
	} catch (Exception e) {
	    e.printStackTrace();

	    return null;
	}
    }
    public static void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    
    public static void styleTable(JTable table) {
        table.getTableHeader().setBackground(new Color(237, 178, 14));
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD,12));
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setForeground(new Color(255,255,255,255));
        table.setForeground(new Color (255, 255, 255));
    }
    
    public static void styleTable2 (JTable table) {
        table.getTableHeader().setBackground(new Color(237, 178, 14));
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD,12));
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setForeground(new Color(255,255,255,255));
    }
}
