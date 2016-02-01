package info.novatec.test.ui.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Interface to the Zap Scanner API.
 */
public class ZapScanner {

    private static final Logger LOG = LoggerFactory.getLogger ( ZapScanner.class );

    private final String apiKey;
    private final String zapPath;
    private final String port;
    private ClientApi clientApi;

    public ZapScanner ( String apiKey, String zapPath, String host, String port ) throws ClientApiException {
        this.apiKey = apiKey;
        this.zapPath = zapPath;
        this.port = port;

        clientApi = new ClientApi ( host, Integer.parseInt ( port ) );
        clientApi.core.newSession ( apiKey, null, "true" );

    }

    /**
     * Perform passive and active scanning for given baseUrl and scanPolicyName.
     * @param baseUrl base url to scan
     * @param scanPolicyName policy name for scan (may be null for default)
     * @return list of alerts occurred
     */
    public List<Alert> completeScan( String baseUrl, String scanPolicyName ) {
        List<Alert> alerts = new ArrayList<> ();

        LOG.info ( "Started complete (active/passive) scan" );

        try {
            clientApi.ascan.enableAllScanners ( apiKey, scanPolicyName );
            clientApi.pscan.setEnabled ( apiKey, "true" );
            clientApi.pscan.enableAllScanners ( apiKey );

            alerts = securityScan ( baseUrl, false, scanPolicyName );

        } catch ( ClientApiException e ) {
            Assert.fail ( "Security scan failed: " + e.getMessage () );
        }

        LOG.info ( "Finished complete (active/passive) scan" );

        return alerts;
    }

    /**
     * Perform passive scanning for given baseUrl and scanPolicyName.
     * @param baseUrl base url to scan
     * @param scanPolicyName policy name for scan (may be null for default)
     * @return list of alerts occurred
     */
    public List<Alert> passiveScan( String baseUrl, String scanPolicyName ) {
        List<Alert> alerts = new ArrayList<> ();

        LOG.info ( "Started passive scan" );

        try {
            clientApi.pscan.setEnabled ( apiKey, "true" );
            clientApi.pscan.enableAllScanners ( apiKey );
            clientApi.ascan.disableAllScanners ( apiKey, scanPolicyName );

            alerts = securityScan ( baseUrl, false, scanPolicyName );

        } catch ( ClientApiException e ) {
            Assert.fail ( "Security scan failed: " + e.getMessage () );
        }

        LOG.info ( "Finished passive scan" );

        return alerts;
    }

    /**
     * Perform active scanning for given baseUrl and scanPolicyName.
     * @param baseUrl base url to scan
     * @param scanPolicyName policy name for scan (may be null for default)
     * @return list of alerts occurred
     */
    public List<Alert> activeScan( String baseUrl, String scanPolicyName ) {
        List<Alert> alerts = new ArrayList<> ();

        LOG.info ( "Started active scan" );

        try {
            clientApi.ascan.enableAllScanners ( apiKey, scanPolicyName );
            clientApi.pscan.setEnabled ( apiKey, "false" );

            alerts = securityScan ( baseUrl, false, scanPolicyName );

        } catch ( ClientApiException e ) {
            Assert.fail ( "Security scan failed: " + e.getMessage () );
        }

        LOG.info ( "Finished active scan" );

        return alerts;
    }


    private List<Alert> securityScan( String baseUrl, boolean inScopeOnly, String scanPolicyName ) throws ClientApiException {
        List<Alert> alerts;

        LOG.info ( "Started security scan" );

        clientApi.ascan.scan ( apiKey, baseUrl, "true", Boolean.toString ( inScopeOnly ), scanPolicyName, null, null );

        int scanProgress = 0;
        int prevScanProgress;
        do {
            prevScanProgress = scanProgress;
            try {
                Thread.sleep ( 100 );
            } catch ( InterruptedException e ) {
                LOG.error ( e.toString () );
            }
            scanProgress = scanProgress ();
            if ( LOG.isDebugEnabled () && prevScanProgress != scanProgress ) {
                LOG.debug ( "Scan progress : " + scanProgress + " %" );
            }
        } while ( scanProgress < 100 );

        LOG.info ( "Finished security scan" );

        try {
            Thread.sleep ( 1000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace ();
        }

        // Wait some time to get the alerts
        waitForAlerts ( baseUrl );

        alerts = getAlerts ( baseUrl );

        if ( LOG.isInfoEnabled () ) {
            LOG.info ( "Found {} alerts:", alerts.size () );

            Collections.sort ( alerts, ( o1, o2 ) -> o1.getRisk ().compareTo ( o2.getRisk () ) );

            for ( Alert alert : alerts ) {

                LOG.info ( String.format ( "Risk %s: %s, confidence=%s, %s , details: %s",
                        alert.getRisk (), alert.getAlert (),
                        alert.getConfidence ().name (), alert.getDescription (),
                        StringUtils.isNotBlank (
                                alert.getEvidence ()) ? alert.getEvidence () :
                                StringUtils.isNotBlank ( alert.getAttack () ) ? alert.getAttack () : alert.getParam () ) );

            }
        }

        return alerts;
    }

    private void waitForAlerts ( String baseUrl ) throws ClientApiException {
        int numChecks = 0;

        int previousNum = 0;
        int currentNum = getNumberOfAlerts ( baseUrl );

        while ( ( currentNum == 0 || ( currentNum > 0 && currentNum > previousNum ) ) && ++numChecks < 10 ) {
            previousNum = currentNum;
            try {
                Thread.sleep ( 1000 );
            } catch ( InterruptedException e ) {
                e.printStackTrace ();
            }
            currentNum = getNumberOfAlerts ( baseUrl );
        }
    }

    private List<Alert> getAlerts ( String baseUrl ) throws ClientApiException {
        return clientApi.getAlerts ( baseUrl, -1, -1 );
    }

    private int getNumberOfAlerts ( String baseUrl ) throws ClientApiException {
        ApiResponse apiResponse = clientApi.core.numberOfAlerts ( baseUrl );
        if ( apiResponse != null && apiResponse instanceof ApiResponseElement ) {
            ApiResponseElement element = (ApiResponseElement) apiResponse;
            return Integer.parseInt ( element.getValue () );
        } else {
            return 0;
        }
    }

    /**
     * Returns scanning progress as an integer
     *
     * @return progress value
     */
    private int scanProgress() throws ClientApiException {
        ApiResponse apiResponse = clientApi.ascan.status ( null );
        return Integer.parseInt(((ApiResponseElement)apiResponse).getValue());
    }

}
