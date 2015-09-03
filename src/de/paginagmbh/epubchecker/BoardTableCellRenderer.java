package de.paginagmbh.epubchecker;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



class BoardTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3292904223607348915L;

	private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		// set the Font, Color, etc.
		renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		setVerticalAlignment(NORTH);
		setForeground(renderer.getForeground());
		setBackground(renderer.getBackground());
		setFont(renderer.getFont());

		// set the text content
		setText(value.toString());

		return this;
	}
}
