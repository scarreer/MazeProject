package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MazeCell extends JButton {
	private static final long serialVersionUID = 8758030830332429424L;
	
	private int x;
	private int y;
	private boolean filled;
	private boolean isBorder;
	
	public MazeCell(int x, int y, boolean filled, Dimension size, char type) {
		this.x = x;
		this.y = y;
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
	}
}
