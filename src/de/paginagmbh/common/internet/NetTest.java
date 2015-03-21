package de.paginagmbh.common.internet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Tests whether an URL is available by reading it's header information with a HEAD request
 * 
 * idea: http://stackoverflow.com/questions/1139547/detect-internet-connection-using-java
 * better: http://stackoverflow.com/questions/4596447/java-check-if-file-exists-on-remote-server-using-its-url
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tuebingen
 * @version		2.1
 * @date 		2015-03-21
 */
public class NetTest {

	public static final int HTTP_ANY = 0;
	public static final int HTTP_INFO = 1;
	public static final int HTTP_OK = 2;
	public static final int HTTP_REDIRECT = 3;
	public static final int HTTP_CLIENT_ERROR = 4;
	public static final int HTTP_SERVER_ERROR = 5;

	private URL test_url = null;



	/*
	 * Constructor - used to set the URL to be checked
	 */
	public NetTest(String website) throws MalformedURLException {
		test_url = new URL(website);
	}



	/**
	 * Tests whether an internet connection is established
	 * 
	 * @return (true) if internet connection seems to be established, (false) if not (bad HTTP response code)
	 */
	public boolean testInternetConnection() {
		return URLTester(HTTP_ANY);
	}



	/**
	 * Tests whether a website/server is responding with expected HTTP Response Code (Range)
	 * 
	 * @param expectedResponseCode (int)
	 * @return (true) if website/server sends expected HTTP Response Code, (false) if not
	 */
	public boolean testWebsiteConnection(int expectedResponseCode) {
		return URLTester(expectedResponseCode);
	}



	/**
	 * Tests whether an URL is available by reading it's header information with a HEAD request
	 * 
	 * @param expectedResponseCode	(int) set to 0 to accept any HTTP ResponseCode (internet connection check) or to a specific HTTP ResponseCode to be matched (3 digit) or to a StatusCode Range (1,2,3...) to be matched
	 * @return (true) in case we hit the URL and got a valid HTTP response, otherwise (false)
	 */
	private boolean URLTester(int expectedResponseCode) {
		try {

			// open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection) test_url.openConnection();
			urlConnect.setReadTimeout(10000);

			// set RequestMethod to HEAD, not GET
			urlConnect.setRequestMethod("HEAD");

			// get HTTP response
			int responseCode = urlConnect.getResponseCode();

			// if HTTP response isn't -1 but a 3 digit length integer we got an internet connection
			// because the server sent a response. This doesn't mean that the website is fully functional,
			// it only says that we got a connection to the server somehow...
			if(responseCode != -1 && String.valueOf(responseCode).length() == 3) {

				// accept any HTTP responseCode
				if(expectedResponseCode == HTTP_ANY) {
					return true;

				// return true only if requiredResponseCode is matched exactly by all three digits
				} else if(String.valueOf(expectedResponseCode).length() == 3 && responseCode == expectedResponseCode) {
					return true;

				// return true only if requiredResponseCode is matching the status code group
				} else if(String.valueOf(expectedResponseCode).length() == 1 && String.valueOf(responseCode).startsWith(String.valueOf(expectedResponseCode))) {
					return true;

				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (IOException e) {
			return false;
		}
	}

}
