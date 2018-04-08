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
import org.json.JSONObject;

/**
 * @author globalworming
 * @author Alexander Brandt
 */
public class Twitter extends ShareCountProvider {

    private static final String QUERY_URL = "https://cdn.api.twitter.com/1/urls/count.json?url=";
    private static final String NAME = "twitter";

    public Twitter(ShariffBackendConfiguration configuration) {
        super(configuration, QUERY_URL, NAME);
    }

    @Override
    public int parseCount(String json) {
        return new JSONObject(json).getInt("count");
    }
}
