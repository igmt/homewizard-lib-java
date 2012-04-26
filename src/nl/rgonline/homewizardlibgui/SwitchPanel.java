package nl.rgonline.homewizardlibgui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import nl.rgonline.homewizardlib.HWSwitch;

public class SwitchPanel extends JPanel {
	private HWSwitch theSwitch;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel statusLabel;
	private JButton toggleButton;
	
	public SwitchPanel(HWSwitch theNewSwitch) {
		this.theSwitch = theNewSwitch;
		this.setLayout(new BorderLayout());
		
		idLabel = new JLabel(theSwitch.getId() + "     ");
		this.add(idLabel, BorderLayout.WEST);
		
		JPanel nameStatus = new JPanel();
		nameStatus.setLayout(new GridLayout(1,2));
		nameLabel = new JLabel("" + theSwitch.getName());
		nameStatus.add(nameLabel, BorderLayout.CENTER);
		
		statusLabel = new JLabel("Unknown");
		nameStatus.add(statusLabel, BorderLayout.EAST);
		
		this.add(nameStatus);
		
		toggleButton = new JButton("Toggle");
		toggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				theSwitch.doToggle();
			}
		});
		
		this.add(toggleButton, BorderLayout.EAST);
	}
	
	public void updateSwitchStatus() {
		boolean isOn = theSwitch.isOn();
		if (isOn) {
			statusLabel.setText("on");
		} else {
			statusLabel.setText("off");
		}
	}
}
