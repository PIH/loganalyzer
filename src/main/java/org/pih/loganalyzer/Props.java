package org.pih.loganalyzer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class Props {

    public static Properties readResource(String resourceName) {
        try {
            Properties props = new Properties();
            props.load(Props.class.getResourceAsStream(resourceName));
            return props;
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to load Properties from " + resourceName, e);
        }
    }

    public static Properties readFile(File file) {
        try (InputStream is = FileUtils.openInputStream(file)) {
            Properties props = new Properties();
            props.load(is);
            return props;
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to load Properties from " + file, e);
        }
    }
}
