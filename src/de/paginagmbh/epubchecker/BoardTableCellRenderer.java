package de.paginagmbh.epubchecker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;



class BoardTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3292904223607348915L;

	private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		// set the Font, Color, etc.
		renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		setBorder(new CompoundBorder(
        		new MatteBorder(0,0,1,1,Color.WHITE),
        		new EmptyBorder(new Insets(5,5,5,5))
    		));
		setVerticalAlignment(NORTH);
		setForeground(renderer.getForeground());
		setBackground(renderer.getBackground());
		setFont(renderer.getFont());

		// set the text content
		setText(value.toString());

		return this;
	}
}
