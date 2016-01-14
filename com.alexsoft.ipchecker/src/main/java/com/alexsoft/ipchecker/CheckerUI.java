package com.alexsoft.ipchecker;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CheckerUI {

	private JFrame frmIpRangeValidator;
	private JTextField textField_0;
	private JTextField textField_1;
	private JLabel label;

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
		
		textField_0 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_0, 10, SpringLayout.NORTH, frmIpRangeValidator.getContentPane());
		frmIpRangeValidator.getContentPane().add(textField_0);
		textField_0.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		springLayout.putConstraint(SpringLayout.WEST, textField_0, 6, SpringLayout.EAST, lblIpAddress);
		springLayout.putConstraint(SpringLayout.EAST, textField_0, 176, SpringLayout.EAST, lblIpAddress);
		springLayout.putConstraint(SpringLayout.NORTH, lblIpAddress, 13, SpringLayout.NORTH, frmIpRangeValidator.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblIpAddress, 10, SpringLayout.WEST, frmIpRangeValidator.getContentPane());
		frmIpRangeValidator.getContentPane().add(lblIpAddress);
		
		textField_1 = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, textField_1, 0, SpringLayout.EAST, textField_0);
		textField_1.setColumns(10);
		frmIpRangeValidator.getContentPane().add(textField_1);
		
		label = new JLabel("IP Address");
		springLayout.putConstraint(SpringLayout.NORTH, textField_1, -3, SpringLayout.NORTH, label);
		springLayout.putConstraint(SpringLayout.WEST, textField_1, 6, SpringLayout.EAST, label);
		springLayout.putConstraint(SpringLayout.NORTH, label, 21, SpringLayout.SOUTH, textField_0);
		springLayout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, lblIpAddress);
		frmIpRangeValidator.getContentPane().add(label);
		
		JButton btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    }
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnValidate, 28, SpringLayout.NORTH, frmIpRangeValidator.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnValidate, 47, SpringLayout.EAST, textField_0);
		frmIpRangeValidator.getContentPane().add(btnValidate);
	}
}
