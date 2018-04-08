package io.andromeda.pippo.shariff.providers;

import com.headissue.sharecount.proxy.ShariffBackendConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class FacebookTest {

  @Test
  public void testParseCount() throws Exception {
    String json = "{\n" +
            "  \"engagement\": {\n" +
            "    \"reaction_count\": 0,\n" +
            "    \"comment_count\": 0,\n" +
            "    \"share_count\": 0,\n" +
            "    \"comment_plugin_count\": 0\n" +
            "  },\n" +
            "  \"id\": \"http:\\/\\/www.yegor256.com\\/2014\\/\\/07\\/31\\/travis-and-rultor.html\"\n" +
            "}";

    int count = new Facebook(new ShariffBackendConfiguration(), "").parseCount(json);
    assertEquals(0, count);

  }
}