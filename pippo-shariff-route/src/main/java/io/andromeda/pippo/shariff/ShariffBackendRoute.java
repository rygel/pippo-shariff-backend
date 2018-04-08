/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.andromeda.pippo.shariff;

import io.andromeda.pippo.shariff.providers.Delicious;
import io.andromeda.pippo.shariff.providers.Facebook;
import io.andromeda.pippo.shariff.providers.GooglePlus;
import io.andromeda.pippo.shariff.providers.LinkedIn;
import io.andromeda.pippo.shariff.providers.Pinterest;
import io.andromeda.pippo.shariff.providers.Reddit;
import io.andromeda.pippo.shariff.providers.ShareCountProvider;
import io.andromeda.pippo.shariff.providers.StumbleUpon;
import io.andromeda.pippo.shariff.providers.Twitter;
import com.headissue.sharecount.proxy.ShariffBackendConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteGroup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author globalworming
 * @author Alexander Brandt
 */
public class ShariffBackendRoute extends RouteGroup {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShariffBackendRoute.class);

    private ShariffBackendConfiguration shariffBackendConfiguration;

    private ShareCountProvider[] countProvider;

    public ShariffBackendRoute(ShariffBackendConfiguration shariffBackendConfiguration, Map<String, Object> defaultContext) {
        super("/shariff-backend");

        this.shariffBackendConfiguration = shariffBackendConfiguration;

        if (!shariffBackendConfiguration.getFacebookApplicationID().equals("")) {
            try {
                String facebookCredentials = String.format("%s|%s", shariffBackendConfiguration.getFacebookApplicationID(),
                        shariffBackendConfiguration.getFacebookApplicationID());
                String urlEncodedFacebookCredentials = URLEncoder.encode(facebookCredentials, "UTF-8");
                String facebookQueryString = String.format(Facebook.QUERY_URL, urlEncodedFacebookCredentials);

                countProvider  =
                        new ShareCountProvider[]{
                                new Reddit(shariffBackendConfiguration),
                                new Facebook(shariffBackendConfiguration, facebookQueryString),
                                new Twitter(shariffBackendConfiguration),
                                new LinkedIn(shariffBackendConfiguration),
                                new Delicious(shariffBackendConfiguration),
                                new StumbleUpon(shariffBackendConfiguration),
                                new Pinterest(shariffBackendConfiguration),
                                new GooglePlus(shariffBackendConfiguration)
                        };
            }  catch (UnsupportedEncodingException e) {
                LOGGER.error(e.toString());
            }
        }

        GET("/", routeContext -> {
            String forUrl = routeContext.getParameter("url").toString();

            if (forUrl == null) {
                // access to root will displays a welcome page
                routeContext.send("\"Hello!\\nshariff-backend-java is running.\\n\" +\n" +
                        "        \"Request the sharecounts like: /?url=http://example.com\\n\\n\" +\n" +
                        "        \"~~~\\nhttps://github.com/rygel/pippo-shariff-backend\"");
            } else {
                try {
                    validateUrl(forUrl);
                } catch (ValidationException e) {
                    routeContext.send("400 " + e.getMessage());
                    return;
                }
                String json = getCounts(forUrl);
                addCacheHeaders(routeContext);
                routeContext.setHeader("Content-Type", "application/json");
                routeContext.setHeader("Access-Control-Allow-Origin", "*");
                routeContext.setHeader("Access-Control-Expose-Headers", "Content-Type");
                routeContext.json().send(json);
            }
        });
    }

    /**
     * compares the url with a list of patterns from the shariffBackendConfiguration
     * throws an exception if the url is not whitelisted
     * @param forUrl
     * @throws ValidationException
     */
    protected void validateUrl(String forUrl) throws ValidationException {
        String domainWhiteListValue = shariffBackendConfiguration.getDomainList();
        String[] domains = new String[0];
        if (domainWhiteListValue != null) {
            domains = domainWhiteListValue.split(";");
        }
        for (int i = 0; i < domains.length; i++) {
            String domain = domains[i];
            Pattern p = Pattern.compile(domain);
            Matcher m = p.matcher(forUrl);
            if (m.matches()) {
                // this url is allowed to be requested sharecounts for
                return;
            }
        }
        // nothing matches
        throw new ValidationException("Disallowed query.");
    }

    protected String getCounts(String forUrl) {
        if (!forUrl.startsWith("http") || !forUrl.contains("://")) {
            forUrl = "http://" + forUrl;
        }
        try {
            forUrl = URLEncoder.encode(forUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.toString());
        }
        // signals the cache that we need an item soon
        for (ShareCountProvider p : countProvider) {
            p.prefetch(forUrl);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < countProvider.length; i++) {
            ShareCountProvider provider = countProvider[i];
            String name = provider.getName();
            int count = provider.getCount(forUrl);
            sb.append("\"").append(name).append("\"").append(":").append(count);
            if ((i + 1) < countProvider.length) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private void addCacheHeaders(RouteContext routeContext) {
        int fortySevenSeconds = 1000 * 47;
        routeContext.setHeader("Cache-Control", "max-age=" + fortySevenSeconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        routeContext.setHeader("Expires", dateFormat.format(new Date(new Date().getTime() + fortySevenSeconds)));
    }
}
