package nl.rgonline.homewizardlib;

import nl.rgonline.homewizardlib.exceptions.HWException;

/**
 * Represents a switch in the homewizard system
 * @author Ruud Greven
 *
 */
public class HWSwitch {
	private HWSystem system;
	private long updateTime;
	
	private int id;
	private boolean isOn;
	private String name;
	
	protected HWSwitch(HWSystem system, int id, String name, boolean isOn) {
		this.system = system;
		this.id = id;
		this.name = name;
		this.isOn = isOn;
	}
	
	protected void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	protected void setIsOn(boolean isOn) {
		this.isOn = isOn;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOn() {
		try {
			system.updateStatus();
		} catch (HWException e) {
			e.printStackTrace();
		}
		return isOn;
	}
	
	public boolean isOff() {
		return !isOn;
	}
	
	public void doTurnOn() {
		try {
			if (system.doToggleSwitch(id, true)) {
				this.isOn = true;
			}
		} catch (HWException e) {
			e.printStackTrace();
		}
	}
	
	public void doTurnOff() {
		try {
			if (system.doToggleSwitch(id, false)) {
				this.isOn = false;
			}
		} catch (HWException e) {
			e.printStackTrace();
		}
	}
	
	public void doToggle() {
		if (isOn) {
			doTurnOff();
		} else {
			doTurnOn();
		}
	}
	
	public String toString() {
		return "HWSwitch: id=" + id + ", name=" + name + ", isOn=" + isOn + "(updated on " + updateTime + ");";
	}
}
