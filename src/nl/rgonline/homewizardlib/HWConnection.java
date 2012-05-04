package nl.rgonline.homewizardlib;

import nl.rgonline.homewizardlib.exceptions.HWException;
import nl.rgonline.homewizardlib.exceptions.HWToEarlyException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This class will be used for setting up a connection to the homewizard and to retrieve data from it.
 * @author Ruud Greven
 *
 */
public final class HWConnection {
	private String connectionString;
	private int statusTimeout;
	
	private long lastStatusUpdate;
	
	protected HWConnection(String ipadres, String password, String port) {
		this.connectionString = "http://" + ipadres + ":" + port + "/" + password;
		this.statusTimeout = 2000;
	}
	
	protected void setStatusTimeout(int timeout) {
		this.statusTimeout = timeout;
	}
	
	protected long getLastStatusUpdate() {
		return lastStatusUpdate;
	}
	
	/**
	 * Gets a list of sensors, equivalent to http://<ip>/<password>/get-sensors.
	 * @return The JSON Answer from the HomeWizard
	 * @throws HWException
	 */
	protected String getSensors() throws HWException {
		String response = "";
		try {
			response = getData(connectionString + "/get-sensors").trim();
		} catch (Exception e) {
			throw new HWException("Error connection to HomeWizard",e);
		}
		return response;
	}
	
	/**
	 * Gets a list of statussen, equivalent to http://<ip>/<password>/get-status.
	 */
	protected String getStatus() throws HWException {
		long currentTime = System.currentTimeMillis();
		String response = "";
		if (currentTime - lastStatusUpdate > statusTimeout) {
			try {
				response = getData(connectionString + "/get-status").trim();
			} catch (Exception e) {
				throw new HWException("Error connection to HomeWizard",e);
			}
			
			//Set the lastStatusUpdate time
			lastStatusUpdate = System.currentTimeMillis();
		} else {
			throw new HWToEarlyException();
		}
		return response;
	}
	
	/**
	 * Toggles the switch with given id on. equivaltn to http://<ip>/<password>/sw/<id>/<OnOrOff>
	 * @param The id of the switch to toggle
	 */
	protected String doToggleSwitch(int id, boolean turnOn) throws HWException {
		String response = "";
		String onOrOff = "";
		if (turnOn) {
			onOrOff = "on";
		} else {
			onOrOff = "off";
		}
		try {
			response = getData(connectionString + "/sw/" + id + "/" + onOrOff).trim();
		} catch (Exception e) {
			throw new HWException("Error connection to HomeWizard",e);
		}
		return response;
	}
	
	/**
	 * Sets dimlevel of the dimmer with given id on. equivaltn to http://<ip>/<password>/sw/dim/<id>/<dimLevel>
	 * @param The id of the dimmer
	 */
	protected String setDimLevel(int id, int dimLevel) throws HWException {
		String response = "";
		try {
			response = getData(connectionString + "/sw/dim/" + id + "/" + dimLevel).trim();
		} catch (Exception e) {
			throw new HWException("Error connecting to HomeWizard",e);
		}
		return response;
	}
	
	/**
	 * Does a blocking call to the given URL
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private String getData(String url) throws Exception {
		//Build up connection
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		
		//Get data
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = client.execute(request, responseHandler);
		
		//Close connection
		client.getConnectionManager().shutdown(); 
		return response;
	}
}
