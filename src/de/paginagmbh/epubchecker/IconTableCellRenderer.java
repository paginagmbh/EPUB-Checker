package de.paginagmbh.epubchecker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import com.adobe.epubcheck.messages.Severity;



class IconTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -4957096937727032710L;

	private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		// set the Font, Color, etc.
		renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setBorder(new CompoundBorder(
        		new MatteBorder(0,1,1,1,Color.WHITE),
        		new EmptyBorder(new Insets(5,5,5,5))
    		));
		setVerticalAlignment(NORTH);
		setForeground(renderer.getForeground());
		setBackground(renderer.getBackground());
		setFont(renderer.getFont());

		// set the icon and text content
		if (value instanceof Severity) {
			setText(paginaEPUBChecker.l10n.getString( ((Severity)value).toString() ));
			setIcon(iconForLogLevel((Severity)value));
		}

		return this;
	}

	private ImageIcon iconForLogLevel(Severity severity) {

		if (severity == Severity.ERROR) {
			return new ImageIcon(paginaEPUBChecker.class.getResource("/resources/icons/icon_error.png"));
		}
		if (severity == Severity.WARNING) {
			return new ImageIcon(paginaEPUBChecker.class.getResource("/resources/icons/icon_warning.png"));
		}
		if (severity == Severity.INFO) {
			return new ImageIcon(paginaEPUBChecker.class.getResource("/resources/icons/icon_info.png"));
		}
		if (severity == Severity.FATAL) {
			return new ImageIcon(paginaEPUBChecker.class.getResource("/resources/icons/icon_debug.png"));
		}
		if (severity == Severity.SUPPRESSED) {
			return new ImageIcon(paginaEPUBChecker.class.getResource("/resources/icons/icon_config.png"));
		}

		return null;
	}
}
