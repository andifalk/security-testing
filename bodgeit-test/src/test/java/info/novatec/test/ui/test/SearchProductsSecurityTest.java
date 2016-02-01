
package info.novatec.test.ui.test;

import info.novatec.test.ui.security.ZapScanner;
import info.novatec.testit.webtester.junit.runner.WebTesterJUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ClientApiException;

import java.util.List;

import static info.novatec.test.ui.security.AlertMatchers.containsNoHighRiskAlerts;
import static org.junit.Assert.assertThat;

/**
 * Verifies searching products.
 */
@RunWith( WebTesterJUnitRunner.class )
public class SearchProductsSecurityTest extends SearchProductsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger ( SearchProductsSecurityTest.class );

	private ZapScanner zapScanner;

	@Before
	public void initZap () throws ClientApiException {
		LOGGER.info ( "Init scanner" );
		zapScanner = new ZapScanner ( "afa", "", "localhost", "8085" );
	}

    @After
    public void securityScan() {
        LOGGER.info ( "Performing security scan" );

        List<Alert> alerts = zapScanner.completeScan ( "http://localhost:8080", "" );
        assertThat ( "Tested workflow should have no high risk alerts", alerts, containsNoHighRiskAlerts () );
    }
}
