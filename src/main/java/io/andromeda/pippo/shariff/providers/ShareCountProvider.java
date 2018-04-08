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
package io.andromeda.pippo.shariff.providers;

import com.headissue.sharecount.proxy.ShariffBackendConfiguration;
import com.headissue.sharecount.proxy.ProviderRequest;
import org.cache2k.Cache;
import org.cache2k.CacheBuilder;
import org.cache2k.CacheSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author globalworming
 * @author Alexander Brandt
 */
public abstract class ShareCountProvider {

    private String queryUrl;
    private String name;
    private final CacheSource<String, Integer> cacheSource;
    private final ShariffBackendConfiguration configuration;

    protected Cache<String, Integer> cache;

    protected ShareCountProvider(ShariffBackendConfiguration configuration, String queryUrl, String name) {
        this.configuration = configuration;
        //this.queryUrl = queryUrl;
        //this.name = name;
        this.cacheSource = new CacheSource<String, Integer>() {
            @Override
            public Integer get(String forUrl) {
                try {
                    return getCountInternal(forUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
        this.cache = CacheBuilder
                .newCache(String.class, Integer.class)
                .source(cacheSource)
                .name(this.getClass().getName())
                .expiryDuration(configuration.getCacheExpiryMilliSeconds(), TimeUnit.MILLISECONDS)
                .maxSize(configuration.getCacheSize())
                .build();
    }

    protected Integer getCountInternal(String forUrl) throws IOException {
        String json = getJsonResponse(forUrl);
        return parseCount(json);
    }

    protected String getJsonResponse(String forUrl) throws IOException {
        ProviderRequest providerRequest = new ProviderRequest(configuration, buildQueryUrl(queryUrl, forUrl));
        return providerRequest.execute();
    }

    public String getName() {
        return name;
    }

    protected int parseCount(String json) {
        throw new UnsupportedOperationException("Do not call, overwrite this method in the subclass instead!");
    }

    public void prefetch(String o) {
        cache.prefetch(o);
    }

    public int getCount(String o) {
        return cache.get(o);
    }

    protected String buildQueryUrl(String providerUrl, String query) {
        return providerUrl + query;
    }
}
