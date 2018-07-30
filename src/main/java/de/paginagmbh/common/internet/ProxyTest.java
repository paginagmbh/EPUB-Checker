package de.paginagmbh.common.internet;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;



/**
 * tests whether a proxy server is active or not
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @version		1.1
 * @date 		2018-07-30
 */
public class ProxyTest {

	public static void test(Logger logger, Boolean debug) throws URISyntaxException {

		System.setProperty("java.net.useSystemProxies", "true");

		List<?> l = ProxySelector.getDefault().select(new URI("https://www.pagina.gmbh/"));

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
