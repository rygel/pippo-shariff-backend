package com.headissue.sharecount.proxy;

import io.andromeda.pippo.shariff.providers.SlowServer;
import org.junit.Test;

import java.net.SocketTimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProviderRequestTest {

  @Test
  public void testTimeout() throws Exception {
    SlowServer server = new SlowServer();
    try {
      server.setUp();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      new ProviderRequest(new ShariffBackendConfiguration(), server.address).execute();
      fail("Expected timeout did not occur");
    } catch (SocketTimeoutException e) {}
  }

  @Test
  public void testUserAgent() {
    ProviderRequest r = new ProviderRequest(new ShariffBackendConfiguration(), "");
    assertEquals("sharecountbot (https://github.com/rygel/pippo-shariff-backend)",r.getUserAgent());
  }
}