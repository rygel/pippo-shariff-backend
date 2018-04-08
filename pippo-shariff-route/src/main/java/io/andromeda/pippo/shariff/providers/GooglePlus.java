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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author globalworming
 * @author Alexander Brandt
 */
public class GooglePlus extends ShareCountProvider {

    private static final String QUERY_URL ="https://clients6.google.com/rpc?key=AIzaSyCKSbrvQasunBoV16zDH9R33D88CeLr9gQ";
    private static final String NAME = "googleplus";

    public GooglePlus(ShariffBackendConfiguration configuration) {
        super(configuration, QUERY_URL, NAME);
    }

    @Override
    protected String getJsonResponse(String forUrl) throws MalformedURLException {
        String body = "[{\n" +
                "    \"method\":\"pos.plusones.get\",\n" +
                "    \"id\":\"p\",\n" +
                "    \"params\":{\n" +
                "        \"nolog\":true,\n" +
                "        \"id\":\"http://stylehatch.co/\",\n" +
                "        \"source\":\"widget\",\n" +
                "        \"userId\":\"@viewer\",\n" +
                "        \"groupId\":\"@self\"\n" +
                "        },\n" +
                "    \"jsonrpc\":\"2.0\",\n" +
                "    \"key\":\"p\",\n" +
                "    \"apiVersion\":\"v1\"\n" +
                "}]s";

        URL url = new URL(QUERY_URL);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type",
                        "application/json");
                connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            OutputStreamWriter writer = null;
            try {
                writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                for (String line; (line = reader.readLine()) != null; ) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    protected Integer getCountInternal(String forUrl) throws MalformedURLException {
        return parseCount(getJsonResponse(forUrl));
    }

    @Override
    protected int parseCount(String json) {
        try {
            JSONArray a = new JSONArray(json);
            if (a.length() > 0) {
                return ((JSONObject) a.get(0)).getJSONObject("result").getJSONObject("metadata").getJSONObject("globalCounts")
                        .getInt
                                ("count");
            }
        } catch (JSONException e) {
            System.err.print(json);
            e.printStackTrace();
        }
        return 0;
    }
}
