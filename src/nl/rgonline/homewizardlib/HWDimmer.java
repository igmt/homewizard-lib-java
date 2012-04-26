package nl.rgonline.homewizardlib;

/**
 * Represents a dimmer in the homewizard system.
 * @author Ruud Greven
 *
 */
public class HWDimmer extends HWSwitch {
	private int dimlevel;
	
	protected HWDimmer(HWSystem system, int id, String name, boolean isOn, int dimlevel) {
		super(system, id, name, isOn);
		this.dimlevel = dimlevel;
	}
	
	public int getDimLevel() {
		return dimlevel;
	}
}
