package info.novatec.test.ui.config;

import info.novatec.testit.webtester.api.browser.ProxyConfiguration;
import org.openqa.selenium.Proxy;

/**
 * Proxy configuration for security testing.
 */
public class ProxyConfig implements ProxyConfiguration {
    @Override
    public void configureProxy ( Proxy proxy ) {
        proxy.setProxyType ( Proxy.ProxyType.MANUAL );
        proxy.setHttpProxy ( "localhost:8085" );
    }
}
