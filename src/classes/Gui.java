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
	private Dimension buttonSize = new Dimension(200, 70);
	private Font buttonFont = new Font("Tahoma", Font.PLAIN, 21);
	private int size = 11;
	private MazeCell[][] maze = new MazeCell[size][size];

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
		btnPanel.setBounds(490, 100, 300, 553);
		contentPane.add(btnPanel);
		btnPanel.setLayout(null);
		
		JButton playMaze = playMazeBtn();
		btnPanel.add(playMaze);
		
		JButton solveMaze = solveMazeBtn();
		btnPanel.add(solveMaze);
		
		JButton generateMaze = generateMazeBtn();
		btnPanel.add(generateMaze);
		
		JButton resetMaze = resetMazeBtn();
		btnPanel.add(resetMaze);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, 886, 72);
		contentPane.add(titlePanel);
		
		JLabel lblTitle = new JLabel("Maze");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 50));
		lblTitle.setBounds(54, 130, 48, 14);
		titlePanel.add(lblTitle);
		
		JPanel mazePanel = createMazePanel(size);
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
		mazePanel.setBounds(27, 100, 436, 430);
		Dimension dimension;
		
		switch(size) {
		default: dimension = new Dimension(33, 33); break;
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
		playMaze.setBounds(0, 0, 300, 100);
		
		//TODO add attributes of the button
		playMaze.setPreferredSize(buttonSize);
		playMaze.setMaximumSize(buttonSize);
		playMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		playMaze.setFont(buttonFont);

		
		playMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[][] array = new int[size][size];
				for(int i = 0; i < size; i++) {
					for(int j = 0; j < size; j++) {
						array[i][j] = (maze[i][j].isFilled() ? 1 : 0);
					}
					
					array[0][1] = 2;
					array[9][8] = 3;
				}
		        setVisible(false);
		        new Thread(() -> {
		            Raycasting.updateMap(array);
		            GuiMaze.main(null);
		        }).start();
		    }
		});
		
		return playMaze;
	}
	
	public JButton solveMazeBtn() {
		JButton solveMaze = new JButton("Solve Maze");
		solveMaze.setBounds(0, 115, 300, 100);
		
		solveMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Maze.solveMaze();
			}
		});
		
		solveMaze.setPreferredSize(buttonSize);
		solveMaze.setMaximumSize(buttonSize);
		solveMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		solveMaze.setFont(buttonFont);
		return solveMaze;
	}
	
	public JButton generateMazeBtn() {
		JButton generateMaze = new JButton("Generate Maze");
		generateMaze.setBounds(0, 230, 300, 100);
		
		generateMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[][] array = Maze.generateMaze();
				for(int i = 0; i < size; i++) {
					for(int j = 0; j < size; j++) {
						switch(array[i][j]) {
						case 0: maze[i][j].setFilled(false); break;
						case 1: maze[i][j].setFilled(true); break;
						}
					}
				}
			}
		});
		
		generateMaze.setPreferredSize(buttonSize);
		generateMaze.setMaximumSize(buttonSize);
		generateMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		generateMaze.setFont(buttonFont);
		return generateMaze;
	}
	
	public JButton resetMazeBtn() {
		JButton resetMaze = new JButton("Reset Maze");
		resetMaze.setBounds(0, 345, 300, 100);
		
		resetMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		
		resetMaze.setPreferredSize(buttonSize);
		resetMaze.setMaximumSize(buttonSize);
		resetMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		resetMaze.setFont(buttonFont);
		return resetMaze;
	}
}
