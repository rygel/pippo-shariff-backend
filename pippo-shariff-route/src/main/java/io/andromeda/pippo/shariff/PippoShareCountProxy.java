package io.andromeda.pippo.shariff;

import com.headissue.sharecount.proxy.ConfigBuilder;
import com.headissue.sharecount.proxy.ShariffBackendConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;

import java.util.Map;

import static io.andromeda.pippo.shariff.Constants.getDefaultContext;

/**
 * A simple Pippo application.
 *
 * @see PippoLauncher#main(String[])
 */
public class PippoShareCountProxy extends Application {


    private static final Logger LOGGER = LoggerFactory.getLogger(PippoShareCountProxy.class);
    private static final String DEFAULT_PROTOCOL = "http://";
    private static final Map<String, Object> globalContext = getDefaultContext();


    @Override
    protected void onInit() {
        getRouter().ignorePaths("/favicon.png");

        String currentPath = System.getProperty("user.dir");
        ShariffBackendConfiguration configuration = new ShariffBackendConfiguration();
        addRouteGroup(new ShariffBackendRoute(configuration, null));


    }



}
