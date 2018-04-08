package io.andromeda.pippo.shariff.providers;

import com.headissue.sharecount.proxy.ShariffBackendConfiguration;
import io.andromeda.pippo.shariff.providers.Pinterest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PinterestTest {

  @Test
  public void testParseCount() throws Exception {
    String json = "receiveCount({\"url\":\"http://www.yegor256.com/2014/07/31/travis-and-rultor.html\",\"count\":3})";
    int count = new Pinterest(new ShariffBackendConfiguration()).parseCount(json);
    assertEquals(3, count);
  }
}