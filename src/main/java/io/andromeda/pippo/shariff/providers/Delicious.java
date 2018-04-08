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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author globalworming
 * @author Alexander Brandt
 */
public class Delicious extends ShareCountProvider {

    private static final String QUERY_URL = "https://api.del.icio.us/v2/json/urlinfo/data?url=";
    private static final String NAME = "delicious";

    public Delicious(ShariffBackendConfiguration configuration) {
        super(configuration, QUERY_URL, NAME);
    }

    @Override
    public int parseCount(String json) {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() > 0) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            return jsonObject.getInt("total_posts");
        } else return 0;
    }
}
