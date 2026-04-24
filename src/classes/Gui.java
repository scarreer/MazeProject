package classes;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.Font;

import javax.swing.JLabel;



public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Dimension buttonSize = new Dimension(200, 70);
	private Font buttonFont = new Font("Tahoma", Font.PLAIN, 21);
	private static int size = 11;
	private static MazeCell[][] maze = new MazeCell[size][size];	
	private static boolean isPossible;

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

	private JPanel createMazePanel(int size) {
		
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
				}else if(i == 1 && j == 1 || i == size-2 && j == size-2){
					type = 'w';
				}else
					type = 'r';
				maze[i][j] = new MazeCell(i, j, (i == 0 || j == 0 || i == size-1 || j == size-1), dimension, type);
				mazePanel.add(maze[i][j]);
			}
		}
		
		return mazePanel;
	}
	
	private JButton playMazeBtn() {
		JButton playMaze = new JButton("Play Maze");
		playMaze.setBounds(0, 0, 300, 100);
		
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
					array[10][9] = 3;
				}
				
				Raycasting.updateMap(array);
				
		        setVisible(false);
	            Raycasting.AutoPlay(false);
		        new Thread(() -> {
		            Raycasting.updateMap(array);
		            GuiMaze.main(null);
		    

		        }).start();
		    }
		});
		
		return playMaze;
	}
	
	//Button to solve maze and play it.
	private JButton solveMazeBtn() {
		JButton solveMaze = new JButton("Solve Maze");
		solveMaze.setBounds(0, 115, 300, 100);
		
		solveMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[][] array = new int[size][size];
				for(int i = 0; i < size; i++) {
					for(int j = 0; j < size; j++) {
						array[i][j] = (maze[i][j].isFilled() ? 1 : 0);
					}
				}
				
				array[0][1] = 2;
				array[10][9] = 3;
				
				Raycasting.updateMap(Maze.solveMaze());
				
				if(isPossible) {
					setVisible(false);
			        new Thread(() -> {
			            Raycasting.AutoPlay(true);
			            GuiMaze.main(null);	
			        }).start();
				}
			}
		});
		
		solveMaze.setPreferredSize(buttonSize);
		solveMaze.setMaximumSize(buttonSize);
		solveMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		solveMaze.setFont(buttonFont);
		return solveMaze;
	}
	
	private JButton generateMazeBtn() {
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
	
	private JButton resetMazeBtn() {
		JButton resetMaze = new JButton("Reset Maze");
		resetMaze.setBounds(0, 345, 300, 100);
		
		resetMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[][] array = {
				        {1,2,1,1,1,1,1,1,1,1,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,0,0,0,0,0,0,0,0,0,1},
				        {1,1,1,1,1,1,1,1,1,3,1}
				    };
				
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
		
		resetMaze.setPreferredSize(buttonSize);
		resetMaze.setMaximumSize(buttonSize);
		resetMaze.setAlignmentX(Component.RIGHT_ALIGNMENT);
		resetMaze.setFont(buttonFont);
		return resetMaze;
	}
	
	
	public static MazeCell[][] getMaze(){
		return maze;
	}
	
	public static void displayErrorMessage() {
		JFrame errorWindow = new JFrame();
		errorWindow.setBounds(500, 220, 400, 200);
		errorWindow.setLayout(new BorderLayout());
		
		JLabel errorMess = new JLabel("   Warning: Path could not be found.");
		errorMess.setFont(new Font("Tacoma", Font.PLAIN, 18));
		errorMess.setForeground(Color.RED);
		errorWindow.add(errorMess, BorderLayout.CENTER);
		
		errorWindow.setVisible(true);
	}
	
	
	public static void isMazePossible(boolean possible) {
		isPossible = possible;
	}
}
