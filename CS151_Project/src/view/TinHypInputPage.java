package view;

/**
 * page 4
 *
 * Part 3 of Registering a patient. Has a field for entering the Etiology Description.
 * 
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;
public class TinHypInputPage {
    JFrame frame; 
	JButton next;
	JButton back;
	private JTextArea description; 
	/**
	 * Reuses frame from last Page and redraws on it
	 * Creates UI
	 * @param frame
	 */
	public TinHypInputPage(JFrame frame)
	{
		this.frame = frame; 
		frame.getContentPane().removeAll(); 
		frame.setTitle("Tinnitus and Hyperacusus Status"); 
		
		//panel for the top blue part 
		JPanel top = new JPanel(); 
		top.setPreferredSize(new Dimension(440, 100));
		top.setOpaque(true);
		top.setForeground(Color.white);
		top.setBackground(new Color(41,142,208));
		top.setLayout(new BorderLayout());
		
		
		
		JLabel titleOfTop = new JLabel("Optional Registration Information"); 
		titleOfTop.setFont(new Font("Arial", Font.BOLD, 40));
		titleOfTop.setForeground(Color.white);
		top.add(titleOfTop, BorderLayout.WEST); 
		
		JPanel topDecoration = new JPanel();
		topDecoration.setPreferredSize(new Dimension(440, 15));
		top.add(topDecoration, BorderLayout.NORTH);
		
		
		//IDNumber till SSN. Labels and text fields 
		JPanel center = new JPanel(new BorderLayout()); 
		
		JPanel etiologyPanel = new JPanel(new BorderLayout()); 
		JLabel etiology = new JLabel("Etiology Description"); 
		etiology.setFont(new Font("Arial", Font.PLAIN, 20));
		etiologyPanel.add(etiology, BorderLayout.WEST); 
		center.add(etiologyPanel, BorderLayout.NORTH);
		
		description = new JTextArea(); 
		center.add(description, BorderLayout.CENTER);
	
		
		
		//Panel to organize back and next buttons 
		JPanel backAndNext = new JPanel(); 
		backAndNext.setLayout(new BorderLayout());
		
		back = new JButton("Back"); 
		back.setPreferredSize(new Dimension(125,50));
		back.setOpaque(true);
		back.setForeground(Color.white);
		back.setBackground(new Color(41,142,208));
		back.setBorderPainted(false);
		backAndNext.add(back, BorderLayout.WEST);
		
		
		next = new JButton("Next");
		next.setPreferredSize(new Dimension(125,50));
		next.setOpaque(true);
		next.setForeground(Color.white);
		next.setBackground(new Color(41,142,208));
		next.setBorderPainted(false);
		backAndNext.add(next, BorderLayout.EAST); 
		
		
		// For space between buttons and bottom 
		JPanel bottomDecorationWhite = new JPanel();
		
		backAndNext.add(bottomDecorationWhite, BorderLayout.SOUTH); 
		
		
		//add the panels into the frame 
		
		frame.add(center, BorderLayout.CENTER);
		frame.add(top, BorderLayout.NORTH); 
		frame.add(backAndNext, BorderLayout.SOUTH); 
		
		
	
		frame.repaint();
		frame.revalidate();
	}
	
	/**
	 * actionlisteners work for this next button
	 * @param a
	 */
	public void addNextListener(ActionListener a)
	{
		next.addActionListener(a);
	}
	
	/**
	 * actionlisteners work for this back button
	 * @param b
	 */
	public void addBackListener(ActionListener b)
	{
		back.addActionListener(b);
	}
	/**
	 * gives value of status in string from field
	 * @return
	 */
	public String getTinHypStatus()
	{
		return description.getText(); 
	}
	
}
