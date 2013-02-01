package de.paginagmbh.common.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;

import javax.swing.border.AbstractBorder;



/**
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @version	1.0
  * @date 		2012-02-26
  * @lastEdit	Tobias Fischer
  * 
  * draws a dashed border with insets
  * 
  */
public class DashedLineBorder extends AbstractBorder {
	
	private static final long serialVersionUID = 1L;
	protected Color lineColor;
	protected int borderInsets;
	
	
	
	public DashedLineBorder(Color color, int insets) {
		lineColor = color;
		borderInsets = insets;
	}
	
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Stroke drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(lineColor);
		g2d.setStroke(drawingStroke);
		g2d.drawRect(x, y, width-1, height-1);
	}
	
	
	public Insets getBorderInsets(Component c) {
		return new Insets(borderInsets,borderInsets,borderInsets,borderInsets);
	}
	
	
	public boolean isBorderOpaque() { 
		return true; 
	}
}