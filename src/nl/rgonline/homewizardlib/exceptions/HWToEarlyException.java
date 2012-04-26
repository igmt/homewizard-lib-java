package nl.rgonline.homewizardlib.exceptions;

public class HWToEarlyException extends HWException {

	public HWToEarlyException() {
		super("You're a bit to early, the timeout isn't passed yet!");
	}

}
