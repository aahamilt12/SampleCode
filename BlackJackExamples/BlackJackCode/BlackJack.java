//*******************************************************************************
// Name:    Andrew Hamilton and Kyle Kurzhal
//
// Project: CS 315 Final Project, Spring 2014
//
// File:    BlackJack.java
//******************************************************************************
import java.util.ArrayList;
import java.util.Random;

public class BlackJack {

	/* Create a new deck and index, player array reference, dealer reference, 
	   and list of winners. */ 
	private Card [] deck = new Card[52];
	private int deckIndex;
	private Player [] players;
	private Player dealer;
	public ArrayList<Player> winners = new ArrayList<Player>();
	
	/* Number of times to shuffle the cards. */
	private final int NUM_SHUFFLE = 200;

	/* Number of players and wallet value. */
	private final int NUM_PLAYERS;	
	private final int WALLET_VAL;

	/* Data members for the pot and player turn. */
	private int turn;
	private int pot;
	
//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  Player array, dealer as a Player, number of players as an integer.
//
// Output: None
//
// Purpose: Initializes the deck, sets references, initializes the pot and turn.
//**********************************************************************
	BlackJack(Player [] p, Player d, int playerNum) {		

		/* Create Cards to go in the deck an initialize their values. */
		initializeDeck();
		
		NUM_PLAYERS = playerNum;		

		/* Set references. */
		dealer = d;
		players = p;
		
		/* Initialize turn, set pot to 100 (house's money), get initial wallet. */
		turn = 0;
		pot = 100;
		WALLET_VAL = players[0].getWallet();
	}
	
//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: None
//
// Purpose: Used to shuffle the deck of cards. NUM_SHUFFLE can be increased
//			for better shuffle.
//**********************************************************************
	public void shuffle() {
		
		int swap1; 
		int swap2;
		Card temp;
		
		for (int i = 0; i < NUM_SHUFFLE; i++) {
			
			/* Generate cards to be swapped. */
			swap1 = (int)(51 * Math.random());
			swap2 = (int)(51 * Math.random());
			
			/* Swap contents. */
			temp = deck[swap1];
			deck[swap1] = deck[swap2];
			deck[swap2] = temp;	
		}
	}

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: None
//
// Purpose: Deals 2 cards to the players and dealer.
//**********************************************************************	
	public void deal() {
		
		
		for(int j = 0; j < 2; ++j){				//deal two cards maximum to each player
			
			for (int i = 0; i < getNumPlayers(); i++) {	//deal one card to each player still in the game		
				
				if(players[i].getInGame()) {
					players[i].addToHand(deck[deckIndex]);
					deckIndex++;
				}
			}
	
			dealer.addToHand(deck[deckIndex]);	//deal a card to the dealer
			deckIndex++;
		}
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  The player to hit.
//
// Output: None
//
// Purpose: Used to hit a given player.
//**********************************************************************
	public void hit(Player p) {
		
		/* Add a card to the player's hand, increment the deck index
		   to keep track where we are in the deck. */
		p.addToHand(deck[deckIndex]);
		deckIndex++;
		
	}
	
//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Player.
//
// Purpose: Retrieve the Player whose turn it currently is.
//**********************************************************************
	public Player getPlayer() {
		
		return players[turn];
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  Index of the player as an integer.
//
// Output: None
//
// Purpose: Retrieve a given player from the players array.
//**********************************************************************
	public Player getPlayer(int index) {
		
		return players[index];
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  Amount to bet as an integer.
//
// Output: None
//
// Purpose: Gets the bet from a player and adds it to the pot.
//**********************************************************************
	public void getBet(int amount) {
		
		pot += amount;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: None
//
// Purpose: If a players chooses to stay, go to the next player's turn.
//**********************************************************************
	public void stay() {
		
		nextTurn();
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Dealer's total as an integer. (-1 if he busts)
//
// Purpose: Used for dealer to make decisions on whether to hit at the 
//			end of a round.
//**********************************************************************
	public int dealerLogic() {
		
		/* If all players bust, dealer does not need to hit. */
		if (allBust() == false) {
			
			/* If the dealer is not winning, hit. */
			if(!isWinning()){
				hit(dealer);
			}
		
		}
		
		/* If the dealer busts return -1. */
		if (dealer.getBust()) {
			return -1;
		}

		/* Otherwise return the dealer's total. */
		else 
			return dealer.getTotal();
		
	}

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  Player
//
// Output: Boolean value.
//
// Purpose: AI for a computer player, makes decisions based on how close
//			to 21 the total is. Not currently used.
//**********************************************************************
	private boolean AI (Player p) {
	
		Random rand = new Random();
		double chance;
		boolean hit = false;
		
		while (!hit) {
		
			chance = (1 - (p.getTotal() / 21));
			
			if (rand.nextDouble() - chance < 0) 
				hit(p);
			
		}
		
		if (p.getTotal() <= 21)
			return true;
		
		return false;
	} 

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Boolean value.
//
// Purpose: Used by the dealer to determine if he is winning.
//**********************************************************************
	private boolean isWinning() {
		
		for (int i = 0; i < getNumPlayers(); i++) {
			
			/* Check if the player has folded and if the player has a higher total than the dealer. */
			if (players[i].getInGame() && players[i].getTotal() > dealer.getTotal() && players[i].getBust() == false)
				/* Dealer is not winning if a player is not folded and has a higher total, return false. */
				return false;
		}
		
		return true;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Boolean value.
//
// Purpose: Used by the dealer to check if all the players have busted.
//**********************************************************************
	public boolean allBust() {
		
		for (int i = 0; i < NUM_PLAYERS; i++) {
			
			/* If a player did not bust return false. */
			if (getPlayer(i).getBust() == false)
				return false;
		}
		
		return true;
 		
	}


//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Boolean value.
// 
// Purpose: Resets the game information and calls reset for each player.
//			Returns true if there are still players in the game and false
//			if the game is over.
//**********************************************************************
	public boolean reset() {
		
		deckIndex = 0;
		turn = -1;
		pot = 100;
		shuffle();
		
		for (int i = 0; i < NUM_PLAYERS; i++) 
			players[i].reset();
	
		dealer.reset();
		
		if(nextTurn())
			return true;
		
		/* The game is over. */
		else return false;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Boolean value.
//
// Purpose: Starts a new hand in the current game. Retuns false if the game
//			is over (passed up from reset function).
//**********************************************************************
	public boolean startNewHand() {
		
		/* The game is over if reset returns false. */
		if (!reset())
			return false;
	
		/* Shuffle, deal, and clear winners. */
		shuffle();
		deal();
		winners.clear();
		
		return true;
	}


//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Boolean value.
//
// Purpose: Check if it is the end of the current round. Returns true
//			if so, false otherwise.
//**********************************************************************
	public boolean checkEnd(){		//added; this keeps the game logic separate from the front end		

		if(turn == getNumPlayers()){	//we return true if all players are done for this game
			boolean end = true;

			for(int i = 0; i < getNumPlayers(); ++i){	//adding a check for if all players are folded/staying
				
				if(players[i].getAddCard())
					end = false;
			}

			int dealerTotal = dealerLogic();
			if(dealerTotal == -1 || end) 		//if the players are done, or if the dealer makes his
				return true;			//move and busts, then we return true.
			
			else {
				
				for(int i = 0; i < getNumPlayers(); ++i){	//if the game isn't over,
					
					if(getPlayer(i).getAddCard())		   //hit each player that needs a card		
						hit(getPlayer(i));
				}

				turn = 0;			     //reset the round to the next player who has not folded
				if(!players[turn].getInGame())
					if(!nextTurn())		//this is here to handle if the last player busts after
						return true;	//receiving a card
				return false;
			}
		}
		else
			return false;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: The dealer's total as an integer.
//
// Purpose: Handles the end of the game, set the winners array, returns the dealers final 
//			card total.
//**********************************************************************
	public int endGame() {
		
		boolean bust = false;
		int dealerTotal;
		
		dealerTotal = dealerLogic();
		
		/* Dealer busted, all players who didn't bust win. */
		if (dealerTotal < 0) {
			
			for (int i = 0; i < getNumPlayers(); i++) {			
				if(getPlayer(i).getBust() == false && getPlayer(i).getInGame())	//added check for players who folded
					winners.add(getPlayer(i));
			}	
		}
		
		/* Dealer didn't bust, all players who didn't bust and are closer to 21 (total > dealer) win. */
		else {
			for (int i = 0; i < getNumPlayers(); i++) {
				if (getPlayer(i).getBust() == false && getPlayer(i).getTotal() > dealer.getTotal() && getPlayer(i).getInGame())
					winners.add(getPlayer(i));
			}
		}
		
		/* Distribute pot to the winners. */
		for (int i = 0; i < getNumPlayers(); i++) {
			
			if (isWinner(getPlayer(i)))
				getPlayer(i).addToWallet(getPot()/winners.size());
		}
		
		return dealerTotal;
	}


//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: None
//
// Purpose: Starts a new game.
//**********************************************************************
	public void restartGame(){
		turn = 0;
		
		for(int i = 0; i < getNumPlayers(); ++i)
			players[i].setWallet(WALLET_VAL);

		startNewHand();
	}
	
//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  Player
//
// Output: Boolean
//
// Purpose: Determines if a given player is in the winners array.
//**********************************************************************
	private boolean isWinner(Player p) {
		
		for (int i = 0; i < winners.size(); i++) {
			
			if(p == winners.get(i))
				return true;
		}
		
		return false;
	}


//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Integer
//
// Purpose: Returns the number of players.
//**********************************************************************	
	public int getNumPlayers(){
		
		return NUM_PLAYERS;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Integer
//
// Purpose: Returns the current turn.
//**********************************************************************
	public int getTurn() {
		return turn;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: None
//
// Purpose: Resets the current turn to 0.
//**********************************************************************
	public void resetTurn(){
		
		turn = 0;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: Boolean value.
//
// Purpose: Finds the next player still in the game. Returns false if there
//			are no more players in the game.
//**********************************************************************
	public boolean nextTurn() {
		
		turn++;
		while(turn < getNumPlayers()){		//loop to find next available player
			
			if(players[turn].getInGame())
				return true;
			
			else
				++turn;
		}
		
		return false;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Integer
//
// Purpose: Returns the current pot value.
//**********************************************************************
	public int getPot() {
		
		return pot;
	}


//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: Reference to the dealer (Player).
//
// Purpose: Returns the reference to the dealer.
//**********************************************************************
	public Player getDealer() {	
		
		return dealer;
	}
	

//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input: None
//
// Output: The ArrayList of winner.
//
// Purpose: Used to get the list of winner.
//**********************************************************************
	public ArrayList<Player> getWinners() {		
		
		return winners;
	}
	
	
//**********************************************************************
// Author: Andrew Hamilton and Kyle Kurzhal
//
// Input:  None
//
// Output: None
//
// Purpose: Initializes the deck by creating 52 cards to be put into 
//			the deck array. Each card takes in a suit, face, value, x coordinate,
//			and y coordinate.
//**********************************************************************
	public void initializeDeck() {
	 
		deck[0] = new Card("Spades", "Ace", 1, 0, 204);
		deck[1] = new Card("Spades", "Two", 2, 73, 204);
		deck[2] = new Card("Spades", "Three", 3, 146, 204);
		deck[3] = new Card("Spades", "Four", 4, 219, 204);
		deck[4] = new Card("Spades", "Five", 5, 292, 204);
		deck[5] = new Card("Spades", "Six", 6, 365, 204);
		deck[6] = new Card("Spades", "Seven", 7, 438, 204);
		deck[7] = new Card("Spades", "Eight", 8, 511, 204);
		deck[8] = new Card("Spades", "Nine", 9, 584, 204);
		deck[9] = new Card("Spades", "Ten", 10, 657, 204);
		deck[10] = new Card("Spades", "Jack", 10, 730, 204);
		deck[11] = new Card("Spades", "Queen", 10, 803, 204);
		deck[12] = new Card("Spades", "King", 10, 876, 204);
		
		deck[13] = new Card("Clubs", "Ace", 1, 0, 0);
		deck[14] = new Card("Clubs", "Two", 2, 73, 0);
		deck[15] = new Card("Clubs", "Three", 3, 146, 0);
		deck[16] = new Card("Clubs", "Four", 4, 219, 0);
		deck[17] = new Card("Clubs", "Five", 5, 292, 0);
		deck[18] = new Card("Clubs", "Six", 6, 365, 0);
		deck[19] = new Card("Clubs", "Seven", 7, 438, 0);
		deck[20] = new Card("Clubs", "Eight", 8, 511, 0);
		deck[21] = new Card("Clubs", "Nine", 9, 584, 0);
		deck[22] = new Card("Clubs", "Ten", 10, 657, 0);
		deck[23] = new Card("Clubs", "Jack", 10, 730, 0);
		deck[24] = new Card("Clubs", "Queen", 10, 803, 0);
		deck[25] = new Card("Clubs", "King", 10, 876, 0);

		deck[26] = new Card("Hearts", "Ace", 1, 0, 102);
		deck[27] = new Card("Hearts", "Two", 2, 73,102);
		deck[28] = new Card("Hearts", "Three", 3, 146, 102);
		deck[29] = new Card("Hearts", "Four", 4, 219, 102);
		deck[30] = new Card("Hearts", "Five", 5, 292, 102);
		deck[31] = new Card("Hearts", "Six", 6, 365, 102);
		deck[32] = new Card("Hearts", "Seven", 7, 438, 102);
		deck[33] = new Card("Hearts", "Eight", 8, 511, 102);
		deck[34] = new Card("Hearts", "Nine", 9, 584, 102);
		deck[35] = new Card("Hearts", "Ten", 10, 657, 102);
		deck[36] = new Card("Hearts", "Jack", 10, 730, 102);
		deck[37] = new Card("Hearts", "Queen", 10, 803, 102);
		deck[38] = new Card("Hearts", "King", 10, 876, 102);		

		deck[39] = new Card("Diamonds", "Ace", 1, 0, 306);
		deck[40] = new Card("Diamonds", "Two", 2, 73, 306);
		deck[41] = new Card("Diamonds", "Three", 3, 146, 306);
		deck[42] = new Card("Diamonds", "Four", 4, 219, 306);
		deck[43] = new Card("Diamonds", "Five", 5, 292, 306);
		deck[44] = new Card("Diamonds", "Six", 6, 365, 306);
		deck[45] = new Card("Diamonds", "Seven", 7, 438, 306);
		deck[46] = new Card("Diamonds", "Eight", 8, 511, 306);
		deck[47] = new Card("Diamonds", "Nine", 9, 584, 306);
		deck[48] = new Card("Diamonds", "Ten", 10, 657, 306);
		deck[49] = new Card("Diamonds", "Jack", 10, 730, 306);
		deck[50] = new Card("Diamonds", "Queen", 10, 803, 306);
		deck[51] = new Card("Diamonds", "King", 10, 876, 306);	
 	}


}
