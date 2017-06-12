package uk.gov.dvsa.mot.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the TestDataProvider interface. This implementation returns hard coded responses as a direct
 * replacement for the test data being directly coded in feature files.
 */
public class StaticTestDataProvider implements TestDataProvider {

    @Override
    public String getValidUserOfType(String userType) {
        return "JOEN5622";
    }
}
