/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2018 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.paginagmbh.epubchecker;
/*
 * Taken in parts from https://github.com/SubOptimal/keystore-explorer/blob/master/kse/src/org/kse/gui/MacOsIntegration.java
 * under the GNU General Public License v3.0
 */

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class MacOsIntegration implements InvocationHandler {

	private final MainGUI mainGui;
	private Double javaVersion = 0.0;


	public MacOsIntegration() {
		this.mainGui = GuiManager.getInstance().getCurrentGUI();

		// Check Java Version
		String v = System.getProperty("java.version");
		if (v.startsWith("1.")) {
			// parse Java versions up to 1.8
			javaVersion = Double.parseDouble(v.substring(0, 3));
		} else {
			// parse Java versions 9 and higher (9, 9-ea, 9+, 9.1, 10, 10.0, 11, 11*, 11.13, etc.)
			javaVersion = Double.parseDouble(v.replaceAll("^(9|\\d\\d)(\\.\\d\\d?)?.*$", "$1$2"));
		}
	}


	public void addEventHandlers() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {

		// using reflection to avoid Mac specific classes being required for compiling
		// KSE on other platforms
		Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
		Class<?> aboutHandlerClass = null;
		Class<?> openFilesHandlerClass = null;

		if (javaVersion <= 1.8) {
			aboutHandlerClass = Class.forName("com.apple.eawt.AboutHandler");
			openFilesHandlerClass = Class.forName("com.apple.eawt.OpenFilesHandler");

		} else if (javaVersion >= 9.0) {
			aboutHandlerClass = Class.forName("java.awt.desktop.AboutHandler");
			openFilesHandlerClass = Class.forName("java.awt.desktop.OpenFilesHandler");
		}

		Object application = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
		Object proxy = Proxy.newProxyInstance(MacOsIntegration.class.getClassLoader(),
				new Class<?>[] { aboutHandlerClass, openFilesHandlerClass }, this);

		applicationClass.getDeclaredMethod("setAboutHandler", aboutHandlerClass).invoke(application, proxy);
		applicationClass.getDeclaredMethod("setOpenFileHandler", openFilesHandlerClass).invoke(application, proxy);
	}


	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ("openFiles".equals(method.getName())) {
			if (args[0] != null) {
				Object files = args[0].getClass().getMethod("getFiles").invoke(args[0]);
				if (files instanceof List) {
					// validate EPUB file(s)
					new EpubValidator().validate((List<File>) files);
				}
			}
		} else if ("handleAbout".equals(method.getName())) {
			SubGUI s = new SubGUI(mainGui);
			s.displayAboutBox();
		}
		return null;
	}


	public void setDockIconBadge(String string) {
		// https://docs.oracle.com/javase/9/migrate/toc.htm#JSMIG-GUID-97C1D0BB-D5D3-4CAD-B17D-03A87A0AAF3B
		// https://bugs.openjdk.java.net/browse/JDK-8048731
		// https://docs.oracle.com/javase/9/docs/api/java/awt/Taskbar.html

		try {
			// Java 6, 7, 8
			if (javaVersion <= 1.8) {
				Class<?> c = Class.forName("com.apple.eawt.Application");
				Method methodSetIconBadge = c.getDeclaredMethod("setDockIconBadge", String.class);
				Object applicationInstance = c.getConstructor((Class[]) null).newInstance((Object[]) null);
				methodSetIconBadge.invoke(applicationInstance, string);

			// Java 9, 10, 11, ..., 20, ...
			} else if (javaVersion >= 9.0) {
				Class<?> c = Class.forName("java.awt.Taskbar");
				Method methodGetTaskbar = c.getDeclaredMethod("getTaskbar");
				Object taskbarInstance = methodGetTaskbar.invoke(null);
				Method methodSetIconBadge = c.getDeclaredMethod("setIconBadge", String.class);
				methodSetIconBadge.invoke(taskbarInstance, string);
			}
		} catch (Exception e) {
			System.out.println("ERROR: Failed to load Mac OS integration: Dock Icon Badge feature not working as expected! Please report to the developer!");
			e.printStackTrace();
		}
	}
}