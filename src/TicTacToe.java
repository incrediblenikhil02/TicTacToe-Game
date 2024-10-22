import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 750; // Increased height for extra space

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel scorePanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    boolean gameOver = false;
    int turns = 0;
    int scoreX = 0;
    int scoreO = 0;

    JLabel scoreLabel = new JLabel("Score: X = 0 | O = 0");

    // Adding buttons
    JButton restartButton = new JButton("Restart Game");
    JButton aboutButton = new JButton("About Game");

    TicTacToe() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top text panel
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        // Score panel with buttons
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scorePanel.setLayout(new GridLayout(1, 3));  // Use GridLayout to place 3 elements (score, 2 buttons)

        scorePanel.add(scoreLabel);
        scorePanel.add(restartButton);
        scorePanel.add(aboutButton);
        frame.add(scorePanel, BorderLayout.SOUTH);

        // Board panel
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;
                        JButton tile = (JButton) e.getSource();
                        if (tile.getText() == "") {
                            tile.setText(currentPlayer);
                            turns++;
                            playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\click.wav"); // Play sound for tile click
                            animateTile(tile); // Add animation effect on click
                            checkWinner();
                            if (!gameOver) {
                                currentPlayer = currentPlayer == playerX ? playerO : playerX;
                                textLabel.setText(currentPlayer + "'s turn.");
                            }
                        }
                    }
                });
            }
        }

        // Restart button functionality
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBoard();
                playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\restart.wav"); // Play reset sound
            }
        });

        // About button functionality
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Tic-Tac-Toe is a game for two players, X and O, who take turns marking spaces in a 3×3 grid. The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row wins the game.", "About Tic-Tac-Toe", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    void checkWinner() {
        // Horizontal check
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText() == "") continue;
            if (board[r][0].getText() == board[r][1].getText() &&
                board[r][1].getText() == board[r][2].getText()) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[r][i]);
                }
                updateScore();
                playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\win.wav"); // Play win sound
                gameOver = true;
                return;
            }
        }

        // Vertical check
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText() == "") continue;
            if (board[0][c].getText() == board[1][c].getText() &&
                board[1][c].getText() == board[2][c].getText()) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][c]);
                }
                updateScore();
                playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\win.wav"); // Play win sound
                gameOver = true;
                return;
            }
        }

        // Diagonal check
        if (board[0][0].getText() == board[1][1].getText() &&
            board[1][1].getText() == board[2][2].getText() &&
            board[0][0].getText() != "") {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][i]);
            }
            updateScore();
            playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\win.wav"); // Play win sound
            gameOver = true;
            return;
        }

        // Anti-diagonal check
        if (board[0][2].getText() == board[1][1].getText() &&
            board[1][1].getText() == board[2][0].getText() &&
            board[0][2].getText() != "") {
            setWinner(board[0][2]);
            setWinner(board[1][1]);
            setWinner(board[2][0]);
            updateScore();
            playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\win.wav"); // Play win sound
            gameOver = true;
            return;
        }

        // Check for tie
        if (turns == 9) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    setTie(board[r][c]);
                }
            }
            textLabel.setText("Tie!");
            playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\tie.wav"); // Play tie sound
            gameOver = true;
        }
    }

    void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " is the winner!");
    }

    void setTie(JButton tile) {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
    }

    void updateScore() {
        if (currentPlayer == playerX) {
            scoreX++;
        } else {
            scoreO++;
        }
        scoreLabel.setText("Score: X = " + scoreX + " | O = " + scoreO);
    }

    void resetBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(Color.darkGray);
                board[r][c].setForeground(Color.white);
            }
        }
        turns = 0;
        gameOver = false;
        currentPlayer = playerX;
        textLabel.setText("Tic-Tac-Toe");
    }

    // Method for tile animation on click
    void animateTile(JButton tile) {
        Timer timer = new Timer(100, new ActionListener() {
            Color originalColor = tile.getBackground();
            int count = 0;
            public void actionPerformed(ActionEvent e) {
                if (count % 2 == 0) {
                    tile.setBackground(Color.lightGray);
                } else {
                    tile.setBackground(originalColor);
                }
                count++;
                if (count > 5) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    // Method to play sound effects
    void playSound(String soundFile) {
        try {
            File soundPath = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }
    void setWinner(JButton tile) {
        // Set the winning tiles to have a green color
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        
        // Show a creative winner message
        showWinnerMessage(currentPlayer + " is the winner!");
        
        // Add a background or text animation
        animateWinnerText();
    }
    
    // Function to display a creative winner message
    void showWinnerMessage(String message) {
        // Create a dialog box to show the winner message
        JDialog winnerDialog = new JDialog(frame, "Congratulations!");
        winnerDialog.setSize(400, 200);
        winnerDialog.setLayout(new BorderLayout());
        winnerDialog.setLocationRelativeTo(frame);
        
        // Create a label with the winner message
        JLabel winnerLabel = new JLabel(message, JLabel.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        winnerLabel.setForeground(Color.red);  // You can change this to a creative color
        
        // Add the label to the dialog
        winnerDialog.add(winnerLabel, BorderLayout.CENTER);
        
        // Optionally, add an image or icon to make it more fun
        JLabel trophyIcon = new JLabel(new ImageIcon("trophy.png"), JLabel.CENTER);
        winnerDialog.add(trophyIcon, BorderLayout.SOUTH);  // Add a trophy image (make sure to have this image in the project)
        
        // Show the dialog with a "celebrate" button
        JButton celebrateButton = new JButton("Celebrate!");
        celebrateButton.setFont(new Font("Arial", Font.BOLD, 20));
        celebrateButton.setBackground(Color.orange);
        winnerDialog.add(celebrateButton, BorderLayout.NORTH);
        
        // Play win sound when the message is shown
        playSound("C:\\Users\\lenovo\\Desktop\\TicTacToe Game\\src\\win.wav");
        
        celebrateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                winnerDialog.dispose();  // Close the dialog after clicking the button
            }
        });
        
        winnerDialog.setVisible(true);  // Make the dialog visible
    }
    
    // Function to animate the winner text (fade-in and fade-out effect)
    void animateWinnerText() {
        Timer timer = new Timer(100, new ActionListener() {
            Color[] colors = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta};
            int index = 0;
            int fadeCounter = 0;
    
            public void actionPerformed(ActionEvent e) {
                // Cycle through colors for a rainbow effect
                textLabel.setForeground(colors[index]);
                index = (index + 1) % colors.length;
                
                fadeCounter++;
                if (fadeCounter > 20) {  // Stop animation after some time
                    ((Timer) e.getSource()).stop();
                    textLabel.setForeground(Color.white);  // Reset to the default color
                }
            }
        });
        timer.start();
    }
    

    
}
