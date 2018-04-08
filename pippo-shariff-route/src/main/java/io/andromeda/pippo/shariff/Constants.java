package io.andromeda.pippo.shariff;

import ro.pippo.core.RuntimeMode;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class holding all application wide constants.
 * @author Alexander Brandt
 */
public class Constants {
    public static final String APPLICATION_NAME = "Shariff Backend";
    public static final String APPLICATION_DOMAIN_NAME = "";
    public static final String APPLICATION_VERSION = "0.0.6";
    public static final String APPLICATION_TITLE = "Shariff Backend";
    public static final String APPLICATION_EMAIL = "";
    public static final String LAST_UPDATE = "01.03.2018";

    private Constants() {

    }

    public static Map<String, Object> getDefaultContext() {
        final Map<String, Object> context = new TreeMap<>();
        context.put("apptitle", APPLICATION_TITLE);
        context.put("domain_name", APPLICATION_DOMAIN_NAME);
        context.put("appver", APPLICATION_VERSION);
        context.put("last_update", LAST_UPDATE);
        context.put("runtime_mode", RuntimeMode.getCurrent().toString());
        return context;
    }

}
