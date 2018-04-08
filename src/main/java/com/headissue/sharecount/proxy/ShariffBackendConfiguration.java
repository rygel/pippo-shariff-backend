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
package com.headissue.sharecount.proxy;

/**
 * @author globalworming
 * @author Alexander Brandt
 */
public class ShariffBackendConfiguration {
    String facebookApplicationID = "";
    String facebookApplicationSecret = "";

    String maintainer = "https://github.com/headissue/shariff-backend-java";
    String domainList = ".*";
    int cacheSize = 1000;
    long cacheExpiryMilliSeconds = 1000L * 60 * 5;

    public ShariffBackendConfiguration() {
        /* Default Constructor. */
    }

    public ShariffBackendConfiguration(String facebookApplicationID, String facebookApplicationSecret) {
        this.facebookApplicationID = facebookApplicationID;
        this.facebookApplicationSecret = facebookApplicationSecret;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public String getDomainList() {
        return domainList;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public long getCacheExpiryMilliSeconds() {
        return cacheExpiryMilliSeconds;
    }

    public String getFacebookApplicationID() {
        return facebookApplicationID;
    }

    public String getFacebookApplicationSecret() {
        return facebookApplicationSecret;
    }

}