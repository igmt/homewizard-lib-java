package nl.rgonline.homewizardlib;

import java.util.ArrayList;

import nl.rgonline.homewizardlib.exceptions.HWException;
import nl.rgonline.homewizardlib.exceptions.HWToEarlyException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * HWSystem is a, reversed enginered, interface to the HomeWizard system ( http://www.homewizard.nl).
 * @version 1, Tested with sensors on HomeWizard version 1.3104
 * @author Ruud Greven
 *
 */
public final class HWSystem {
	private HWConnection connection;
	private double hwversion;
	private double libraryversion = 1.0;
	
	private ArrayList<HWSwitch> switches;
	
	public HWSystem(String ipadres, String password) {
		connection = new HWConnection(ipadres,password);
	}
	
	public double getHWVersion() {
		return hwversion;
	}
	
	public double getLibraryVersion() {
		return libraryversion;
	}
	
	/**
	 * This method initializes the connection with the HomeWizard system. It gets the list of switches and more
	 */
	public void init() throws HWException {
		//Get response from server
		String sensorsJSON = connection.getSensors();
		JSONObject response = getResponse(sensorsJSON);
		
		//Read switches
		initSwitches(response);
		
		//Read uvmeters
		//TODO
		//Read windmeters
		//TODO
		//Read rainmeters
		//TODO
		//Read thermometers
		//TODO
		//Read energymeters
		//TODO
		//Read scenes
		//TODO
	}
	
	private void initSwitches(JSONObject response) throws HWException {
		try {
			JSONArray switchesJSON = response.getJSONArray("switches");
			switches = new ArrayList<HWSwitch>();
			
			for (int i = 0; i < switchesJSON.length(); i++) {
				JSONObject switchJSON = switchesJSON.getJSONObject(i);
				
				int id = switchJSON.getInt("id");
				String name = switchJSON.getString("name");
				boolean isOn = switchJSON.getString("status").equals("on");
				
				if (switchJSON.getString("dimmer").equals("no")) {
					HWSwitch theSwitch = new HWSwitch(this, id, name, isOn);
					switches.add(theSwitch);
				} else {
					int dimlevel = switchJSON.getInt("dimlevel");
					HWDimmer dimmer = new HWDimmer(this, id, name, isOn, dimlevel);
					switches.add(dimmer);
				}
				
			}
		} catch (JSONException e) {
			throw new HWException("Error initializing switches", e);
		}
	}
	
	/**
	 * Returns the list with all the switches known in the HomeWizard system
	 * @return an ArrayList with switches.
	 * @throws HWException if not initialized yet.
	 */
	public ArrayList<HWSwitch> getSwitches()  throws HWException {
		if (switches == null) {
			throw new HWException("Not initialized, you should call init() first!");
		}
		return switches;
	}
	
	/**
	 * Find the switch with the given id.
	 * @param The id to search for
	 * @return The right HWSwitch if the switch is found, else NULL
	 * @throws HWException, if this HWSystem is not initialized yet
	 */
	public HWSwitch getSwitchById(int id) throws HWException {
		if (switches == null) {
			throw new HWException("Not initialized, you should call init() first!");
		}
		for (HWSwitch theSwitch: switches) {
			if (theSwitch.getId() == id) {
				return theSwitch;
			}
		}
		return null;
	}
	
	/**
	 * Find the switch with the given name.
	 * @param The name to search for
	 * @return The right HWSwitch if the switch is found, else NULL
	 * @throws HWException, if this HWSystem is not initialized yet
	 */
	public HWSwitch getSwitchByName(String name) throws HWException {
		if (switches == null) {
			throw new HWException("Not initialized, you should call init() first!");
		}
		for (HWSwitch theSwitch: switches) {
			if (theSwitch.getName().equals(name)) {
				return theSwitch;
			}
		}
		return null;
	}
	
	
	// ******************************************************************************************************************************
	// Methods that only should be used from inside this package
	// ****************************************************************************************************************************** 
	
	protected void updateStatus() throws HWException {
		String statusJSON;
		try {
			statusJSON = connection.getStatus();
			JSONObject response = getResponse(statusJSON);
			
			//When we are the early the code throws an toEarlyException. This code is only executed when we are not to early
			JSONArray switchesJSON = response.getJSONArray("switches");
			for (int i = 0; i < switchesJSON.length(); i++) {
				JSONObject switchJSON = switchesJSON.getJSONObject(i);
				
				//Get the switch and set it's status and updatetime
				HWSwitch theSwitch = getSwitchById(switchJSON.getInt("id"));
				theSwitch.setIsOn(switchJSON.getString("status").equals("on"));
				theSwitch.setUpdateTime(connection.getLastStatusUpdate());
			}
		} catch (HWToEarlyException e) {
			//Intenially do nothing, just don't update.
		} catch (Exception e) {
			throw new HWException("An error occured while getting statusses", e);
		}
	}
	
	protected boolean doToggleSwitch(int id, boolean onOrOff) throws HWException {
		String statusJSON;
		statusJSON = connection.doToggleSwitch(id, onOrOff);
		
		try {
			JSONObject hwmessage = new JSONObject(statusJSON);
			if (hwmessage.getString("status").equals("ok")) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			throw new HWException("Error parsing JSON", e);
		}
	}
	
	
	
	
	// ******************************************************************************************************************************
	// Convenience methods
	// ****************************************************************************************************************************** 
	
	/**
	 * Reads the server response and returns the "response" part as a JSON Object
	 * @param The jsonString from the server
	 * @return a JSONObject representing the response.
	 * @throws HWException
	 */
	private JSONObject getResponse(String jsonString) throws HWException {
		JSONObject retVal = null;
		try {
			JSONObject hwmessage = new JSONObject(jsonString);
			hwversion = hwmessage.getDouble("version");
			//Check status
			if (hwmessage.getString("status").equals("ok")) {
				retVal = hwmessage.getJSONObject("response");
			} else {
				throw new HWException("HomeWizard returns not an OK status: '" + jsonString + "'");
			}
		} catch (JSONException e) {
			throw new HWException("Error parsing JSON", e);
		}
		return retVal;
	}
}
