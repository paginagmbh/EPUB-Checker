package com.adobe.epubcheck.ocf;

import java.io.InputStream;

public interface EncryptionFilter {

	public boolean canDecrypt();
	
	public InputStream decrypt(InputStream in);
}
