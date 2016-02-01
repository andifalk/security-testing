package info.novatec.test.ui.security;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.zaproxy.clientapi.core.Alert;

import java.util.ArrayList;
import java.util.List;

/**
 * Special hamcrest matchers for ZAP alerts.
 */
public final class AlertMatchers {


    public static class HighRiskAlertMatcher extends TypeSafeMatcher<Alert> {

        @Override
        protected boolean matchesSafely ( Alert alert ) {
            return Alert.Risk.High == alert.getRisk ();
        }

        @Override
        public void describeTo ( Description description ) {
            description.appendText("an alert with a high risk");
        }
    }

    public static class ContainsNoHighRiskAlertMatcher extends TypeSafeMatcher<List<Alert>> {

        @Override
        protected boolean matchesSafely ( List<Alert> alerts ) {
            for ( Alert alert : alerts ) {
                if ( Alert.Risk.High == alert.getRisk () ) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo ( Description description ) {
            description.appendText("Contains no alert(s) with a high risk");
        }

        @Override
        protected void describeMismatchSafely(List<Alert> alerts, Description mismatchDescription) {
            List<String> highRiskAlerts = new ArrayList<> ();

            for ( Alert alert : alerts ) {
                if ( Alert.Risk.High == alert.getRisk () ) {
                    highRiskAlerts.add ( String.format ( "%s (%s): %s", alert.getAlert (), alert.getRisk (), alert.getDescription () ) );
                }
            }

            mismatchDescription.appendText("has ").appendValue ( String.join ( ",", highRiskAlerts ) );

        }

    }

    public static class ContainsOnlyMinorRiskAlertMatcher extends TypeSafeMatcher<List<Alert>> {

        @Override
        protected boolean matchesSafely ( List<Alert> alerts ) {
            for ( Alert alert : alerts ) {
                if ( Alert.Risk.High == alert.getRisk () || Alert.Risk.Medium == alert.getRisk () ) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo ( Description description ) {
            description.appendText("Contains no alert(s) with high or medium risk");
        }

        @Override
        protected void describeMismatchSafely(List<Alert> alerts, Description mismatchDescription) {
            List<String> highRiskAlerts = new ArrayList<> ();

            for ( Alert alert : alerts ) {
                if ( Alert.Risk.High == alert.getRisk () || Alert.Risk.Medium == alert.getRisk () ) {
                    highRiskAlerts.add ( String.format ( "%s (%s): %s", alert.getAlert (), alert.getRisk (), alert.getDescription () ) );
                }
            }

            mismatchDescription.appendText("has ").appendValue(highRiskAlerts);

        }

    }


    @Factory
    public static Matcher<Alert> highRiskAlert() {
        return new HighRiskAlertMatcher ();
    }

    @Factory
    public static Matcher<List<Alert>> containsNoHighRiskAlerts() {
        return new ContainsNoHighRiskAlertMatcher ();
    }

    @Factory
    public static Matcher<List<Alert>> containsOnlyMinorRiskAlerts() {
        return new ContainsOnlyMinorRiskAlertMatcher ();
    }
}
