package org.vergien.vaadinator.webdriver.touchkit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.vaadin.addonhelpers.automated.AbstractBaseWebDriverCase;

/**
 * This abstract class can be used if one e.g. cannot afford TestBench license.
 *
 */
public class AbstractWebdriverTest extends AbstractBaseWebDriverCase {

	protected static final int TESTPORT = 5678;
	protected static final String BASEURL = "http://localhost:" + TESTPORT + "/";

	public AbstractWebdriverTest() {
		super();
	}

	@BeforeClass
	public static void startServer() {
		try {
			server = new Demo("target/WebDriverExampleTouchkit-0.20-SNAPSHOTstage").startServer(TESTPORT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void stopServer() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}