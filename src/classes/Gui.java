package classes;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.Font;
import javax.swing.JLabel;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Dimension buttonSize = new Dimension(300, 100);
	private MazeCell[][] maze = new MazeCell[10][10];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450*2, 300*2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setBounds(490, 110, 300, 553);
		contentPane.add(btnPanel);
		btnPanel.setLayout(null);
		
		JButton playMaze = playMazeBtn();
		btnPanel.add(playMaze);
		
		JButton solveMaze = solveMazeBtn();
		btnPanel.add(solveMaze);
		
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, 886, 72);
		contentPane.add(titlePanel);
		
		JLabel lblTitle = new JLabel("Maze");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 50));
		lblTitle.setBounds(54, 130, 48, 14);
		titlePanel.add(lblTitle);
		
		JPanel mazePanel = createMazePanel(10);
		contentPane.add(mazePanel);
	}	
	
			
	public void clickCell(MazeCell cell) {
		if(cell.isFilled()) {
			cell.setFilled(false);
		}else {
			cell.setFilled(true);
		}
	}
	
	public void playMaze(){
		
	}
	
	public void solveMaze() {
		
	}

	public JPanel createMazePanel(int size) {
		//TODO implement other sizes. Add buttons to interface for other sizes
		
		JPanel mazePanel = new JPanel();
		mazePanel.setBounds(27, 99, 436, 430);
		Dimension dimension;
		
		switch(size) {
		case 10: dimension = new Dimension(38, 38); break;
		default: dimension = new Dimension(38, 38); break;
		}
		
		char type;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(i == 0 && j == 1) {
					type = 's';
				}else if(i == size-1 && j == size-2) {
					type = 'e';
				}else {
					type = 'r';
				}
				maze[i][j] = new MazeCell(i, j, (i == 0 || j == 0 || i == size-1 || j == size-1), dimension, type);
				mazePanel.add(maze[i][j]);
			}
		}
		
		return mazePanel;
	}
	
	public JButton playMazeBtn() {
		JButton playMaze = new JButton("Play Maze");
		playMaze.setBounds(0, 178, 300, 100);
		
		//TODO add attributes of the button
		playMaze.setPreferredSize(buttonSize);
		playMaze.setMaximumSize(buttonSize);
		playMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		playMaze.setFont(new Font("Tahoma", Font.PLAIN, 21));

		
		playMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[][] array = new int[10][10];
				for(int i = 0; i < 10; i++) {
					for(int j = 0; j < 10; j++) {
						array[i][j] = (maze[i][j].isFilled() ? 1 : 0);
					}
					
					array[0][1] = 2;
					array[10][9] = 3;
				}
		        setVisible(false);
		        new Thread(() -> {
		            Raycasting.updateMap(array);
		        }).start();
		    }
		});
		
		return playMaze;
	}
	
	public JButton solveMazeBtn() {
		JButton solveMaze = new JButton("Solve Maze");
		solveMaze.setBounds(0, 314, 300, 100);
		
		solveMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		
		//TODO add attributes of the button
		solveMaze.setPreferredSize(buttonSize);
		solveMaze.setMaximumSize(buttonSize);
		solveMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		solveMaze.setFont(new Font("Tahoma", Font.PLAIN, 21));
		return solveMaze;
	}
}
