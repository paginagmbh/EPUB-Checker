package de.paginagmbh.common.internet;

import java.net.URI;


/**
  * opens a given URL (as String) in the systems standard web browser
  * 
  * idea: http://johnbokma.com/mexit/2008/08/19/java-open-url-default-browser.html
  * 
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @date 		2012-02-26
  * @lastEdit	Tobias Fischer
  */
public class OpenURIinBrowser {
	
	public OpenURIinBrowser(String stringURI) {
		
		// if "java.awt.Desktop" is supported by the installed java version
		if( java.awt.Desktop.isDesktopSupported() ) {
			
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			
			//if "java.awt.Desktop.Action.BROWSE" is supported
			if( desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
				
				try {
					URI uri = new URI( stringURI );
					desktop.browse( uri );
				} catch ( Exception e ) {
					System.err.println( e.getMessage() );
				}
				
			} else {
//				System.err.println( "Desktop doesn't support the browse action (fatal)" );
//				System.exit( 1 );
			}
			
		} else {
//			System.err.println( "Desktop is not supported (fatal)" );
//			System.exit( 1 );
		}
	}
}