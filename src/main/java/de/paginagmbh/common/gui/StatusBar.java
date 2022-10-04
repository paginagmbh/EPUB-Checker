package de.paginagmbh.common.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.paginagmbh.epubchecker.GuiManager;

/**
 * generates a status bar at the bottom of a border
 * 
 * idea: http://www.java2s.com/Code/Java/Swing-Components/StatusBarDemo.htm
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @version		1.2
 * @date			2015-03-21
 */
public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JLabel lbl_text;




	/* ********************************************************************************************************** */

	public StatusBar(Icon icon, String text, Boolean draggableCorner) {

		// set new layout
		setLayout(new BorderLayout());
		// set dimensions - only the height ("22") is important
		setPreferredSize(new Dimension(10, 22));
		
		// set focusable so that it can be accessed via screen readers
		setFocusable(true);
		// add name for screen readers
		getAccessibleContext().setAccessibleName(__("Status bar"));

		// create text label
		lbl_text = new JLabel(text);
		// if the label is empty, make it explicit in the parent widget for screen readers
		if(text == null || text.trim().equals("")) {
			getAccessibleContext().setAccessibleDescription(__("empty"));
		}
		
		lbl_text.setFont(lbl_text.getFont().deriveFont(lbl_text.getFont().getSize() - 2f));
		// border as padding
		lbl_text.setBorder(BorderFactory.createEmptyBorder(3,10,0,10));
		// set font color
		lbl_text.setForeground(Color.gray);
		// set icon
		lbl_text.setIcon(icon);
		// add to component
		add(lbl_text, BorderLayout.WEST);

		// set draggable corner icon if desired
		if(draggableCorner) {
			JPanel rightPanel = new JPanel(new BorderLayout());
			rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
			rightPanel.setOpaque(false);
			add(rightPanel, BorderLayout.EAST);
		}

	}




	/* ********************************************************************************************************** */

	public void update(Icon icon, String text) {
		lbl_text.setIcon(icon);
		lbl_text.setText(text);
		
		// update accessible description for screen readers
		if(text == null || text.trim().equals("")) {
			getAccessibleContext().setAccessibleDescription(__("empty"));
		}else {
			getAccessibleContext().setAccessibleDescription(text);
		}
	}




	/* ********************************************************************************************************** */

	public void reset() {
		lbl_text.setIcon(null);
		lbl_text.setText(null);
		// update accessible description for screen readers
		getAccessibleContext().setAccessibleDescription(__("empty"));
	}




	/* ********************************************************************************************************** */

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = 0;
		g.setColor(new Color(196, 194, 183));
		g.drawLine(0, y, getWidth(), y);
		y++;
		g.setColor(new Color(218, 215, 201));
		g.drawLine(0, y, getWidth(), y);
		y++;
		g.setColor(new Color(233, 231, 217));
		g.drawLine(0, y, getWidth(), y);

		y = getHeight() - 3;
		g.setColor(new Color(233, 232, 218));
		g.drawLine(0, y, getWidth(), y);
		y++;
		g.setColor(new Color(233, 231, 216));
		g.drawLine(0, y, getWidth(), y);
		y = getHeight() - 1;
		g.setColor(new Color(221, 221, 220));
		g.drawLine(0, y, getWidth(), y);

	}

	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}
}




/* ********************************************************************************************************** */

class AngledLinesWindowsCornerIcon implements Icon {

	private static final Color WHITE_LINE_COLOR = new Color(255, 255, 255);
	private static final Color GRAY_LINE_COLOR = new Color(172, 168, 153);
	private static final int WIDTH = 13;
	private static final int HEIGHT = 13;


	public int getIconHeight() {
		return WIDTH;
	}

	public int getIconWidth() {
		return HEIGHT;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {

		g.setColor(WHITE_LINE_COLOR);
		g.drawLine(0, 12, 12, 0);
		g.drawLine(4, 12, 12, 4);
		g.drawLine(8, 12, 12, 8);

		g.setColor(GRAY_LINE_COLOR);
		g.drawLine(1, 12, 12, 1);
		g.drawLine(2, 12, 12, 2);

		g.drawLine(5, 12, 12, 5);
		g.drawLine(6, 12, 12, 6);

		g.drawLine(9, 12, 12, 9);
		g.drawLine(10, 12, 12, 10);

	}
	
}