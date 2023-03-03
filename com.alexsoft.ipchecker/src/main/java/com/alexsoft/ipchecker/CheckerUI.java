package com.alexsoft.ipchecker;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CheckerUI {

	private JFrame frmIpRangeValidator;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				CheckerUI window = new CheckerUI();
				window.frmIpRangeValidator.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
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
		frmIpRangeValidator.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmIpRangeValidator.getContentPane().setLayout(springLayout);

		JTextField textFieldFirst = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textFieldFirst, 10, SpringLayout.NORTH, frmIpRangeValidator.getContentPane());
		frmIpRangeValidator.getContentPane().add(textFieldFirst);
		textFieldFirst.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		springLayout.putConstraint(SpringLayout.WEST, textFieldFirst, 6, SpringLayout.EAST, lblIpAddress);
		springLayout.putConstraint(SpringLayout.EAST, textFieldFirst, 176, SpringLayout.EAST, lblIpAddress);
		springLayout.putConstraint(SpringLayout.NORTH, lblIpAddress, 13, SpringLayout.NORTH, frmIpRangeValidator.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblIpAddress, 10, SpringLayout.WEST, frmIpRangeValidator.getContentPane());
		frmIpRangeValidator.getContentPane().add(lblIpAddress);

		JTextField textFieldSecond = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, textFieldSecond, 0, SpringLayout.EAST, textFieldFirst);
		textFieldSecond.setColumns(10);
		frmIpRangeValidator.getContentPane().add(textFieldSecond);

		JLabel label = new JLabel("IP Address");
		springLayout.putConstraint(SpringLayout.NORTH, textFieldSecond, -3, SpringLayout.NORTH, label);
		springLayout.putConstraint(SpringLayout.WEST, textFieldSecond, 6, SpringLayout.EAST, label);
		springLayout.putConstraint(SpringLayout.NORTH, label, 21, SpringLayout.SOUTH, textFieldFirst);
		springLayout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, lblIpAddress);
		frmIpRangeValidator.getContentPane().add(label);
		
		JButton btnValidate = new JButton("Validate");
		btnValidate.addActionListener((ActionEvent e) -> {});
		springLayout.putConstraint(SpringLayout.NORTH, btnValidate, 28, SpringLayout.NORTH, frmIpRangeValidator.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnValidate, 47, SpringLayout.EAST, textFieldFirst);
		frmIpRangeValidator.getContentPane().add(btnValidate);
	}
}
