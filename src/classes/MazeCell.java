package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class MazeCell extends JButton {
    private int cellType; 
    private boolean isBorder;
    
    public MazeCell(int x, int y, boolean filled, Dimension size, char type) {
        this.setContentAreaFilled(true);
        this.setOpaque(true); 
        this.setBorderPainted(false);
        this.setPreferredSize(size);
        
        if(filled) { this.isBorder = true; this.setCellType(1); }
        else { this.isBorder = false; this.setCellType(0); }
        
        switch(type) {
            case 's': this.setBackground(Color.GREEN); this.isBorder = true; this.cellType = 2; break;
            case 'e': this.setBackground(Color.RED); this.isBorder = true; this.cellType = 3; break;
        }
        
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!isBorder) {
                    if (cellType == 0) setCellType(1);
                    else if (cellType == 1) setCellType(4);
                    else if (cellType == 4) setCellType(5);
                    else if (cellType == 5) setCellType(6);
                    else setCellType(0);
                }
            }
        });
    }

    public int getCellType() { return cellType; }
    
    public void setCellType(int type) {
        this.cellType = type;
        switch(type) {
            case 0: this.setBackground(Color.WHITE); break;
            case 1: this.setBackground(Color.BLACK); break;
            case 4: this.setBackground(Color.BLUE); break;
            case 5: this.setBackground(Color.DARK_GRAY); break;
            case 6: this.setBackground(Color.ORANGE); break;
        }
        this.repaint();
    }

    public void setFilled(boolean fill) { setCellType(fill ? 1 : 0); }
    public boolean isFilled() { return cellType != 0; }
}