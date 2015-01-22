//*******************************************************************************
// Name:    Andrew Hamilton and Kyle Kurzhal
//
// Project: CS 315 Final Project, Spring 2014
//
// File:    Board.java
//******************************************************************************

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board extends JFrame implements ActionListener{
	
	
	PlayerPanel [] playerPanels;
	Player [] players;
	CardPanel [] cardPanels;
	BlackJack game;
	Player dealer;
	
	JButton hit = new JButton("Hit ($25)");
	JButton stay = new JButton("Stay ($25)");
	JButton fold = new JButton("Fold");		
	JButton next = new JButton("Next Hand");
	
	JPanel centerPanel = new JPanel();
	JPanel buttonsPanel = new JPanel();
	
	JTextField playerTurn = new JTextField("Player's Turn");
	JTextField dealerField = new JTextField("Dealer");
	JTextField pot = new JTextField("Pot: $100");
	JTextField dealerTotal = new JTextField("Total");
	
	JPanel potPanel = new JPanel();
	JPanel turnPanel = new JPanel();
	JPanel cardsPanel = new JPanel();
	JPanel dealerPanel = new JPanel();
	
	CardPanel dealerCards;
	
//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: n - string containing the title of the JFrame
//        g - reference to an instance of the BlackJack class
//
// Output: Sets up the entire JFrame for displaying the game.  This
//         includes JPanels that are stored according to the layout.
//         All cards and information are displayed on the screen.
//
// Purpose: Sets up the entire user interface for the BlackJack game.
//**********************************************************************
	Board(String n, BlackJack g) {
		
		super(n);
	
		game = g;
		dealer = game.getDealer();
		initializePlayers();
		initializePlayerPanels();
		initializeButtonsPanel();
		initializeCardPanels();
		
		dealerCards = new CardPanel(dealer);
		cardsPanel.setLayout(new GridLayout(1,4));
		
		/* Holds the four players info. */
		JPanel playersBoard = new JPanel();
		playersBoard.setLayout(new GridLayout(1,4));
		
		/* Add playerPanels to the player board. */
		for (int i = 0; i < game.getNumPlayers(); i++) 
			playersBoard.add(playerPanels[i]);
		
		/* Add cardPanels to the cardsPanel.*/
		for (int i = 0; i < game.getNumPlayers(); i++)
			cardsPanel.add(cardPanels[i]);
		
		
		initializeDealer();
		initializePot();
		initializePlayerTurn();
		
		
		/* Layout for the whole board, consists of south, center, and north panels. */
		setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.NORTH);
		add(playersBoard, BorderLayout.SOUTH);
		
		setBounds(60, 60, 800, 600);
		setVisible(true);
		getContentPane().setBackground(new Color(0,153,0));
		
		resetBoard();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	}

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: e - an instance of ActionEvent that is passed to the function
//            when the event listener detects that a button has been
//            clicked.
//
// Output: Changes information text as each button is clicked.  Also
//         updates the cards displayed when each round passes.
//
// Purpose: Allows some action to be taken depending on the button that
//          is clicked.
//**********************************************************************
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		if (source == hit) {		//make the player be hit with a card at the end of the round
			
			game.getPlayer().setAddCard(true);		
			
			game.getPlayer(game.getTurn()).makeBet(25);
			game.getBet(25);
			pot.setText("Pot: $" + game.getPot());
			playerPanels[game.getTurn()].setWalletField("Wallet: $"  + Integer.toString(game.getPlayer().getWallet()));
			playerPanels[game.getTurn()].setBetField("Bet: $" + Integer.toString(game.getPlayer().getBet()));

			game.nextTurn();
			
			
			if (game.checkEnd())
				handleEnd();
			else
				playerTurn.setText("Player " + Integer.toString((game.getTurn()) + 1) + "'s Turn");			
			
			
		}
		
		else if (source == stay) {			//make the player stay
			game.getPlayer().setAddCard(false);
			game.getPlayer(game.getTurn()).makeBet(25);
			game.getBet(25);
			pot.setText("Pot: $" + game.getPot());
			playerPanels[game.getTurn()].setWalletField("Wallet: $"  + Integer.toString(game.getPlayer().getWallet()));
			playerPanels[game.getTurn()].setBetField("Bet: $" + Integer.toString(game.getPlayer().getBet()));

			game.nextTurn();
			
			if (game.checkEnd())
				handleEnd();
			
			else
				playerTurn.setText("Player " + Integer.toString((game.getTurn()) + 1) + "'s Turn");
						
		}
		
		else if(source == fold){	//make the player fold
			
			game.getPlayer().setInGame(false);			//decide to take player out of game
			playerPanels[game.getTurn()].setTotalField("Fold");
			game.nextTurn();
			if (game.checkEnd())
				handleEnd();
			else
				playerTurn.setText("Player " + Integer.toString((game.getTurn()) + 1) + "'s Turn");	
		}
		
		else if (source == next) {	//start the next hand		
			
			resetBoard();
		}

		for(int i = 0; i < game.getNumPlayers(); ++i) {		//update the board
			
			cardPanels[i].repaint();
			
			if (playerPanels[i].getTotalField().equals("Fold")) {}
			
			else if (game.getPlayer(i).getBust())
				playerPanels[i].setTotalField("Bust");
				
			else playerPanels[i].setTotalField("Total: " + Integer.toString(game.getPlayer(i).getTotal()));
		}

		dealerPanel.repaint();
	}
	
//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: Displays information relating to a new hand.  Displays the
//         cards in the new hand.
//
// Purpose: Used to call the backend to reset the board, and then
//          resets the information and card images on the board.
//**********************************************************************
	public void resetBoard() {
		
		if(!game.startNewHand()) {	//start the new hand
			
			playerTurn.setText("Game Over");
			next.setEnabled(false);
			
			int reply = JOptionPane.showConfirmDialog(this,  "Would you like to start a new game?", "Game Over", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION)
					game.restartGame();
				
				else System.exit(0);
		}
		
		for (int i = 0; i < game.getNumPlayers(); i++) {
			
			cardPanels[i].repaint();
		}
		
		dealerCards.repaint();
		
		next.setEnabled(false);
		hit.setEnabled(true);		
		fold.setEnabled(true);		
		stay.setEnabled(true);
		
		pot.setText("Pot: $100");
		
		for (int i = 0; i < game.getNumPlayers(); i++) {
			
			playerPanels[i].setBetField("Bet: $" + Integer.toString(players[i].getBet()));
			playerPanels[i].setTotalField("Total: " + Integer.toString(game.getPlayer(i).getTotal()));
			
			if (game.getPlayer(i).getWallet() > 0)
				playerPanels[i].setWalletField("Wallet: $" + Integer.toString(game.getPlayer(i).getWallet()));
			else {
				playerPanels[i].setWalletField("Out");
				playerPanels[i].setTotalField("");
			}
		}
		
		
		playerTurn.setText("Player " + (game.getTurn()+1) + "'s Turn");
		dealerTotal.setText("Total: " + Integer.toString(game.getDealer().getTotal()));
		
//		}
		
	}


//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: Sets and displays total and wallet fields, and displays the
//         winners of the game.  Allows user to press "new hand" button.
//
// Purpose: Allows the user to see the outcome of the hand.  Allows
//          the user to start a new hand.
//**********************************************************************	
	public void handleEnd() {
		
		hit.setEnabled(false);
		stay.setEnabled(false);
		fold.setEnabled(false);		//adding for round robin
		
		int bust = game.endGame();
		
		dealerCards.repaint();
		for(int i = 0; i < game.getNumPlayers(); i++)
			cardPanels[i].repaint();
		
		if (bust < 0)
			dealerTotal.setText("Bust");
		else
			dealerTotal.setText(Integer.toString(game.getDealer().getTotal()));
			
		/* Update wallet field. */
		for (int i = 0; i < game.getNumPlayers(); i++) {
			
			playerPanels[i].setWalletField("Wallet: $" + Integer.toString(game.getPlayer(i).getWallet()));
		}
		
		
		/* No winners found in the winners list. */
		if (game.getWinners().size() == 0)
			playerTurn.setText("The house wins!");

		else if(game.getWinners().size() == game.getNumPlayers())
			playerTurn.setText("All players win!");
		
		/* Print message for each possible number of winners. */
		else{
			String winnersResult = "";
			for(int i = 0; i < game.getWinners().size(); ++i){
				if(i > 0)
					winnersResult += ", ";

				winnersResult += Integer.toString(game.getWinners().get(i).getPlayerNumber() + 1);
			}

			if(game.getWinners().size() == 1)
				playerTurn.setText("Player " + winnersResult + " wins!");
			else
				playerTurn.setText("Players " + winnersResult + " win!");
				
		}
			
		next.setEnabled(true);
	}

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: Sets up the panel to hold the title "Dealer" and the total
//         of the dealer's cards.
//
// Purpose: Allows the user to see the dealer's information.
//**********************************************************************	
	private void initializeDealer() {
		
		dealerPanel.setLayout(new BoxLayout(dealerPanel, BoxLayout.Y_AXIS));
		dealerTotal.setBackground(new Color(0,153,0));
		dealerTotal.setFont(new Font("AR Julian", Font.BOLD, 30));
		dealerTotal.setForeground(Color.YELLOW);
		dealerTotal.setBorder(null);
		dealerTotal.setBackground(new Color(0,153,0));
		dealerTotal.setHorizontalAlignment(JTextField.CENTER);
		dealerTotal.setEditable(false);
		
		dealerField.setBackground(new Color(0,153,0));
		dealerField.setFont(new Font("AR Julian", Font.BOLD, 30));
		dealerField.setForeground(Color.YELLOW);
		dealerField.setBorder(null);
		dealerField.setBackground(new Color(0,153,0));
		dealerField.setHorizontalAlignment(JTextField.CENTER);
		dealerField.setEditable(false);
		
		dealerPanel.add(dealerField);
		dealerPanel.add(dealerTotal);
		dealerPanel.setBorder(null);
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: Sets up the information and layout for the pot, dealer panel,
//         and the dealer cards.
//
// Purpose: This allows the user to see the information about the pot,
//          the dealer info panel, and the dealer's cards.
//**********************************************************************
	private void initializePot() {
		
		
		pot.setBackground(new Color(0,153,0));
		pot.setFont(new Font("AR Julian", Font.BOLD, 30));
		pot.setForeground(Color.YELLOW);
		pot.setBorder(null);
		pot.setBackground(new Color(0,153,0));
		pot.setHorizontalAlignment(JTextField.CENTER);
		pot.setEditable(false);
		
		potPanel.setLayout(new GridLayout(1,3));
		potPanel.add(pot);
		potPanel.add(dealerPanel);
		potPanel.add(dealerCards);
		potPanel.setBorder(null);
		potPanel.setBackground(new Color(0,153,0));
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: Sets up the  the pot panel, the player turn panel, and the
//         panel containing panels of cards (cards panel).
//
// Purpose: This allows the user to see who's turn it is, the pot, and
//          the cards displayed in an orderly fashion.
//**********************************************************************
	private void initializePlayerTurn() {
		
		playerTurn.setBackground(new Color(0,153,0));
		playerTurn.setFont(new Font("AR Julian", Font.BOLD, 30));
		playerTurn.setForeground(Color.YELLOW);
		playerTurn.setBorder(null);
		playerTurn.setHorizontalAlignment(JTextField.CENTER);
		
		turnPanel.add(playerTurn);
		turnPanel.setBackground(new Color(0,153,0));
		
		centerPanel.setLayout(new GridLayout(3,1));
		centerPanel.setBackground(new Color(0,153,0));
		centerPanel.add(potPanel);
		centerPanel.add(playerTurn);
		centerPanel.add(cardsPanel);
	}

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: Player planels containing player information are set and the
//         wallet fields are filled appropriately.
//
// Purpose: Allows the user to see player information organized with the
//          wallet amounts displayed.
//**********************************************************************	
	private void initializePlayerPanels() {
		
		playerPanels = new PlayerPanel[game.getNumPlayers()];
		
		for (int i = 0; i < game.getNumPlayers(); i++)
			playerPanels[i] = new PlayerPanel(players[i]);
		
		for (int i = 0; i < game.getNumPlayers(); i++) {
			
			playerPanels[i].setWalletField("Wallet: $" + Integer.toString(game.getPlayer(i).getWallet()));
		}
	
		playerTurn.setEditable(false);
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: None
//
// Purpose: Sets up each new player for the game.
//**********************************************************************
	private void initializePlayers() {
		
		players = new Player[game.getNumPlayers()];
		
		for (int i = 0; i < game.getNumPlayers(); i++)
			players[i] = game.getPlayer(i);
		
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: None
//
// Purpose: Sets up each new card panel (containing each set of cards)
//          for the game.
//**********************************************************************
	private void initializeCardPanels() {
		
		cardPanels = new CardPanel[game.getNumPlayers()];
		
		for (int i = 0; i < game.getNumPlayers(); i++) 
			cardPanels[i] = new CardPanel(game.getPlayer(i));
		
	}


//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: None
//
// Purpose: Adds action listeners to each button so that the buttons can
//          perform some action, and then adds the buttons to the panel.
//**********************************************************************
	private void initializeButtonsPanel() {
		
		buttonsPanel.setLayout(new GridLayout(1,4));
		next.addActionListener(this);
		hit.addActionListener(this);
		stay.addActionListener(this);
		fold.addActionListener(this);
		buttonsPanel.add(hit);
		buttonsPanel.add(stay);
		buttonsPanel.add(fold);
		buttonsPanel.add(next);
		next.setEnabled(false);
	}

}
