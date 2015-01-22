//*****************************************************************************
// Name:    Andrew Hamilton and Kyle Kurzhal
//
// Project: CS 315 Final Project, Spring 2014
//
// File:	Player.java
//****************************************************************************

import java.util.*;


public class Player {
	
	/* The player's hand of cards. */
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	private int total;
	private int wallet;
	private int handSize;
	private int handIndex;
	private int bet;
	private int playerNumber;
	
	private boolean bust;
	private boolean inHand;
	private boolean inGame;
	private boolean addCard;
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   The player's number and wallet value as integers.
	//
	// Output:  None
	//
	// Purpose: The Player constructor initializes the player's values to
    //			the appropriate amount for the beginning of the game.
	//********************************************************************
	Player(int p, int walletVal) {
		
		playerNumber = p;
		bust = false;
		inHand = true;
		inGame = true;
		addCard = false;
		total = 0;
		wallet = walletVal;
		handSize = 0;
		handIndex = 0;
		bet = 0;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  The index of a card.
	//
	// Output: The card at the given index.
	//
	// Purpose: Get card is used to retrieve a card a given location in
	//			the deck.
	//********************************************************************
	public Card getCard (int index) {
		
		return hand.get(index);
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: None
	//
	// Purpose: Set the player out for the hand if they choose to stay.
	//********************************************************************
	public void stay() {
		
		inHand = false;
	}
	
		
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  Card
	//
	// Output: None
	//
	// Purpose: Adds a card to the player's hand. Accounts for when the 
	//			player is dealt an ace and if the player busts on retrieval
	//			of the card.
	//********************************************************************
	public void addToHand(Card c) {
		
		/* Add the card to the players hand and increment the players hand size and index. */
		hand.add(handIndex, c);	
		handSize++;
		handIndex++;
		
		/* Special case for being dealt ace an ace on the initial deal. */
		if (handSize == 2) {
			
			/* Ace and 10. */
			if ((hand.get(0).getValue() + hand.get(1).getValue()) == 11 && (hand.get(0).getValue() == 1 || hand.get(0).getValue() == 1))
				total = 21;
			
			/* Dealt 2 aces */
			else if ((hand.get(0).getValue() + hand.get(1).getValue()) == 2)
				total = 12;
			
			/* Dealt ace as the first card. */
			else if (hand.get(0).getValue() == 1)
				total = (hand.get(1).getValue() + 11);
				
			/* Dealt ace as the second card. */
			else if (hand.get(1).getValue() == 1)
				total = (hand.get(0).getValue() + 11);
			
			else 
				total += c.getValue();
		}
		
		/* Ace counts as 11 if the players total is less than or equal to 10. */
		else if (c.getValue() == 1 && getTotal() <= 10)
			total += 11;
		
		else 
			total += c.getValue();
		
		/* Check for bust. */
		if (total > 21) {
			
			bust = true;
			inHand = false;
			setInGame(false);	//added; taking the player out of the game
		}
		
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  Amount to bet
	//
	// Output: None
	// 
	// Purpose: Allows the player to make a bet. Adds to bet and takes
	//			away from wallet.
	//********************************************************************
	public void makeBet(int amount) {
		
		wallet -= amount;
		bet += amount;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: The player's total bet amount.
	//
	// Purpose: Returns the player's bet amount.
	//********************************************************************
	public int getBet() {
		
		return bet;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  Amount to be added to the wallet.
	//
	// Output: None
	//
	// Purpose: Add's to the player's wallet if they have winnings
	//			from a round.
	//********************************************************************
	public void addToWallet(int amount) {
		
		wallet += amount;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: None
	//
	// Purpose: Resets the player's values, check if the player has money
	//			let in their wallet to play.
	//********************************************************************
	public void reset() {
		
		handIndex = 0;
		handSize = 0;
		total = 0;
		bet = 0;
		bust = false;
		
		/* If they have money they are still in the game. */
		if (wallet > 0)
			setInGame(true);
		/* Otherwise they are removed from the game. */
		else 
			setInGame(false);
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: Boolean value.
	//
	// Purpose: Returns true if the player has busted (total > 21).
	//********************************************************************
	public boolean getBust() {
		
		return bust;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: The player's card total.
	//
	// Purpose: Returns the players card total of the given hand so far.
	//********************************************************************
	public int getTotal() {
		
		return total;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: The player's current wallet as an integer.
	//
	// Purpose: Returns the players wallet, used to check if they are in 
	//			in game still as well as update the board.
	//********************************************************************
	public int getWallet() {
		
		return wallet;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  New amount for the wallet as an integer.
	//
	// Output: None
	//
	// Purpose: Sets the wallet of the player to a given amount.
	//********************************************************************
	public void setWallet(int money){
	
		wallet = money;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: The player's hand size as an integer.
	//
	// Purpose: Used to retrieve the player's hand size.
	//********************************************************************
	public int getHandSize() {
		
		return handSize;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: The current position in the player's hand as an integer.
	//
	// Purpose: Used to see where the player is in their hand.
	//********************************************************************
	public int getHandIndex() {
		
		return handIndex;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: Boolean value.
	//
	// Purpose: Used to check if the player is still in the game.
	//********************************************************************
	public boolean getInGame() {
		
		return inGame;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  Boolean value.
	//
	// Output: None
	//
	// Purpose: Takes in a boolean as a parameter to check if the player
	//			is still in the game. If not, set add card to false so 
	//			the player does not recieve a card.
	//********************************************************************
	public void setInGame(boolean in){
		
		inGame = in;
		
		if(!inGame)			
			addCard = false;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: Boolean value.
	//
	// Purpose: Returns whether or not the player is in the current hand.
	//********************************************************************
	public boolean getInHand() {
		
		return inHand;
	}

	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  Boolean value.
	//
	// Output: None
	//
	// Purpose: Sets the addCard data member based on the given input.
	//********************************************************************
	public void setAddCard(boolean add){	
		
		addCard = add;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: Boolean value.
	//
	// Purpose: Used to check if the player will recieve a card on the 
	//			current round.
	//********************************************************************
	public boolean getAddCard(){	 
		
		return addCard;
	}
	
	//*******************************************************************
	// Author: Andrew Hamilton and Kyle Kurzhal
	//
	// Input:  None
	//
	// Output: The player's number as an integer.
	//
	// Purpose: Used to retrieve the player's number.
	//********************************************************************
	public int getPlayerNumber() {
		
		return playerNumber;
	}
}
