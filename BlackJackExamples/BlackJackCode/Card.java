//*****************************************************************************
// Name:    Andrew Hamilton and Kyle Kurzhal
//
// Project: CS 315 Final Project, Spring 2014
//
// File:	Card.java
//*****************************************************************************
public class Card {

	String suit;
	String face;
	
	int value;
	
	/* Position of the card in the Card.png vector image. */
	int x;
	int y;
	
	/* Width and heigh of the cards in the vector image. */
	private final int width = 74;
	private final int height = 103;
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   Strings for the suit and face, integers for the value, 
	//			x coordinate and y coordinate.
	//
	// Output:  None
	//
	// Purpose: The Card class constructor sets the Card to the given values.
	//********************************************************************
	Card(String s, String f, int v, int xVal, int yVal) {
		
		suit = s;
		face = f;
		value = v;
		x = xVal;
		y = yVal;
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   None
	//
	// Output:  String
	//
	// Purpose: The card's suit as a String.
	//********************************************************************
	public String getSuit(){
		
		return suit;
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   None
	//
	// Output:  String.
	//
	// Purpose: Returns the card's face as a string.
	//********************************************************************
	public String getFace() {
		
		return face;
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   None
	//
	// Output:  The cards value as an integer. (Ace = 1, Two = 2,... Ten = 10, Jack = 10,..
	//
	// Purpose: Returns the value of the card.
	//********************************************************************
	public int getValue(){
		
		return value;
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   None
	//
	// Output:  The card's x coordinate in the vector image.
	//
	// Purpose: Used in CardPanel the retrieve the card's image from the vector.
	//********************************************************************
	public int getX() {
		
		return x;
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   None
	//
	// Output:  The card's y coordinate in the vector image.
	//
	// Purpose: Used in CardPanel the retrieve the card's image from the vector.
	//********************************************************************
	public int getY() {
		
		return y;
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   None
	//
	// Output:  The width of a card in the vector image.
	//
	// Purpose: Used in CardPanel to calculate the width of the sub image.
	//********************************************************************
	public int getWidth() {
		
		return width;
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   None
	//
	// Output:  The height of a card in the vector image.
	//
	// Purpose: Used in CardPanel to calculate the height of the sub image.
	public int getHeight() {
		
		return height;
	}
}
