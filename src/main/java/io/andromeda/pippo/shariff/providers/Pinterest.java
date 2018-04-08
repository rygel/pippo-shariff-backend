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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author globalworming
 * @author Alexander Brandt
 */
public class Pinterest extends ShareCountProvider {


    private static final String QUERY_URL = "https://widgets.pinterest.com/v1/urls/count.json?source=6&url=";
    private static final String NAME = "pinterest";

    public Pinterest(ShariffBackendConfiguration configuration) {
        super(configuration, QUERY_URL, NAME);
    }

    @Override
    public int parseCount(String json) {
        Pattern p = Pattern.compile("\\{.*\\}");
        Matcher m = p.matcher(json);
        if (m.find()) {
            JSONObject o = new JSONObject(m.group(0));
            return o.getInt("count");
        }
        return 0;
    }
}
