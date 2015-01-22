//*****************************************************************************
// Name:    Andrew Hamilton and Kyle Kurzhal
//
// Project: CS 315 Final Project, Spring 2014
//
// File:	CardPanel.java
//*****************************************************************************

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CardPanel extends JPanel {
	
	/* Create a reference for the player whose cards are to be displayed. */
	Player player;
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   The players whose cards are to be displayed.
	//
	// Output:  None
	//
	// Purpose: The CardPanel constructor sets the player reference to the
	//			given player.
	//********************************************************************
	CardPanel(Player p) {
		
		player = p;
		
		setBackground(new Color(0,153,0));
		setLayout(new GridLayout(1,4));		
	}
	
	//*******************************************************************
	// Author:  Andrew Hamilton and Kyle Kurzhal
	//
	// Input:   Graphics object (handled by java).
	//
	// Output:  None
	//
	// Purpose: The paintComponent is used to display the player's hand
	//			of cards.
	//********************************************************************
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
	
		try {
			
			/* Reads in the card image. */
			BufferedImage cardImg = ImageIO.read(new File("Cards.png"));
			BufferedImage subImg;
			
			/* Loop for each card in the player's hand. X is incremented to layer the cards. */
			for(int i = 0, x = 10; i < player.getHandSize(); i++, x += 10) {
				
				/* If the player folded, display face down cards. */
				if (player.getBust() == false && player.getInGame() == false)
					/* subImg will be set to the face down card. */
					subImg = cardImg.getSubimage(0, 408, player.getCard(i).getWidth(), player.getCard(i).getHeight());
				
				else
					/* The coordinates for each card face is contained in the Card class. This is used to 
					 * create a sub image of the entire Cards.png image which just contains the needed card. 
					 */
					subImg = cardImg.getSubimage(player.getCard(i).getX(), player.getCard(i).getY(), player.getCard(i).getWidth(), player.getCard(i).getHeight());
					
				g.drawImage(subImg, x, 0, null);
	
				
			}
			
		} catch (IOException e) {} 
	}
}
