package de.paginagmbh.common.internet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


/**
  * collection of different internet tests
  */
public class NetTest {
	
	
	/**
	  * tests wheather an internet connection is available or not
	  * 
	  * idea: http://stackoverflow.com/questions/1139547/detect-internet-connection-using-java
	  * 
	  * @author		Tobias Fischer
	  * @copyright	pagina GmbH, Tuebingen
	  * @version	1.0
	  * @date 		2012-02-07
	  * @lastEdit	Tobias Fischer
	  */
	public boolean InternetTester(String website) {
		try {
			//make a URL to a known source
			URL url = new URL(website);

			//open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

			//trying to retrieve data from the source. If there
			//is no connection, this line will fail
			@SuppressWarnings("unused")
			Object objData = urlConnect.getContent();

		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
		
		return true;
	}


	
	
	/**
	  * tests whether a proxy server is active or not
	  * 
	  * @author		Tobias Fischer
	  * @copyright	pagina GmbH, Tuebingen
	  * @version	1.0
	  * @date 		2012-02-07
	  * @lastEdit	Tobias Fischer
	  */
	public void ProxyTester(Logger logger, Boolean debug) throws Exception {

		System.setProperty("java.net.useSystemProxies", "true");

		List<?> l = ProxySelector.getDefault().select(new URI("http://www.pagina-online.de/"));

		for (Iterator<?> iter = l.iterator(); iter.hasNext();) {
			
			Proxy proxy = (Proxy) iter.next();
			if(debug) { logger.info("Debug: " + "Proxy hostname : " + proxy.type()); }
			InetSocketAddress addr = (InetSocketAddress) proxy.address();

			if (addr == null) {
				if(debug) { logger.info("Debug: " + "No Proxy"); }
			} else {
				if(debug) { logger.info("Debug: " + "Proxy hostname : " + addr.getHostName()); }
				if(debug) { logger.info("Debug: " + "Proxy port : " + addr.getPort()); }
			}
		}
	}
}
