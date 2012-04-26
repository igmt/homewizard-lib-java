package nl.rgonline.homewizardlib.exceptions;

public class HWException extends Exception {
	
	public HWException(String message) {
		super(message);
	}
	
	public HWException(String message, Exception e) {
		super(message);
		this.initCause(e);
	}
}
