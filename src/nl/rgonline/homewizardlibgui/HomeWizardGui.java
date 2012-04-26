package nl.rgonline.homewizardlibgui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.rgonline.homewizardlib.HWSwitch;
import nl.rgonline.homewizardlib.HWSystem;
import nl.rgonline.homewizardlib.exceptions.HWException;

public class HomeWizardGui extends JFrame {
	private HWSystem hwsystem;
	private ArrayList<SwitchPanel> switchpanels;
	private Timer timer;
	
	public HomeWizardGui(String ipadres, String password) throws HWException {
		switchpanels = new ArrayList<SwitchPanel>();
		timer = new Timer();
		
		//Init HWSystem
		hwsystem = new HWSystem(ipadres, password);
		hwsystem.init();
		
		//Init frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100,100,600,400);
		this.setTitle("Homewizard GUI, Homewizard version: " + hwsystem.getHWVersion());
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		
		//Create switchpanels
		for (HWSwitch theSwitch: hwsystem.getSwitches()) {
			SwitchPanel panel = new SwitchPanel(theSwitch);
			switchpanels.add(panel);
			contentPane.add(panel);
		}
		
		this.setContentPane(contentPane);
		
		//Do regular updates
		timer.scheduleAtFixedRate(new UpdateTask(), 1000, 500);
	}
	
	private class UpdateTask extends TimerTask {
		public void run() {
			for (SwitchPanel switchpanel: switchpanels) {
				switchpanel.updateSwitchStatus();
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = null;
		try {
			frame = new HomeWizardGui("192.168.1.6", "secret");
		} catch (HWException e) {
			e.printStackTrace();
		}
		frame.setVisible(true);	
	}

}
