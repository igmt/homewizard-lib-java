homewizard-lib-java
===================

An reversed engineered open-source library for the Homewizard ( http://www.homewizard.nl ).

Requires the following .jar files to run:
- commons-logging-1.1.1.jar
- httpclient-4.1.3.jar
- httpcore-4.1.4.jar
- json-org.jar

Download the first 3 from http://hc.apache.org/downloads.cgi (direct download http://apache.hippo.nl//httpcomponents/httpclient/binary/httpcomponents-client-4.1.3-bin.tar.gz )
The last one can be obtained from http://www.docjar.com/jar/json-org.jar


Use the code from the package nl.rgonline.homewizardlibgui to see how you should use this library. The most import calls are:

**Init HWSystem**

	HWSystem hwsystem = new HWSystem(ipadres, password);
	hwsystem.init();
		
**Get a list of switches**

	ArrayList<HWSwitch> switches = hwsystem.getSwitches();

**Use switches**

Now you can find a switch with a certain name or number and turn it on or off, or ask for the status