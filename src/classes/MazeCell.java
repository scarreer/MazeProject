package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
* Each individual cell in the 11x11 maze in the GUI.
*
* @author Davis Martin
*/
public class MazeCell extends JButton {
	private static final long serialVersionUID = 8758030830332429424L;
	
	private boolean filled;
	private boolean isBorder;
	
	public MazeCell(int x, int y, boolean filled, Dimension size, char type) {
		
		this.setContentAreaFilled(true);
	    this.setOpaque(true); 
	    this.setBorderPainted(false);
	    
	    
		this.setFilled(filled);
		if(filled) {
			this.isBorder = true;
		}else {
			this.isBorder = false;
		}
		this.setPreferredSize(size);
		
		switch(type) {
		case 's': this.setBackground(Color.GREEN); this.isBorder = true; break;
		case 'e': this.setBackground(Color.RED); this.isBorder = true; break;
		case 'w': this.isBorder = true; break;
		}
		
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isBorder())
					setFilled(!isFilled());
			}
		});
	}
	
	public boolean isBorder() {
		return isBorder;
	}
	
	public boolean isFilled() {
		return filled;
	}
	
	public void setFilled(boolean fill) {
		filled = fill;
		if(fill) {
			this.setBackground(Color.BLACK);
		}else {
			this.setBackground(Color.WHITE);
		}
		
		this.repaint();
	    this.revalidate();
	}
}