package com.alexsoft.ipchecker;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class CheckerUI {

	private JFrame frmIpRangeValidator;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckerUI window = new CheckerUI();
					window.frmIpRangeValidator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CheckerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIpRangeValidator = new JFrame();
		frmIpRangeValidator.setTitle("IP range validator");
		frmIpRangeValidator.setBounds(100, 100, 450, 300);
		frmIpRangeValidator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmIpRangeValidator.getContentPane().setLayout(springLayout);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 10, SpringLayout.NORTH, frmIpRangeValidator.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, frmIpRangeValidator.getContentPane());
		frmIpRangeValidator.getContentPane().add(textField);
		textField.setColumns(10);
	}
}
