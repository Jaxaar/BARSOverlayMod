package Jaxaar.BARSOverlay;

import Jaxaar.BARSOverlay.HttpRequests.ApacheHttpClient;
import Jaxaar.BARSOverlay.HttpRequests.JaxAPI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class APIRequestTranslatorTest {

    private JaxAPI jaxAPI;

    @Before
    public void setUp() throws Exception {
        jaxAPI = new JaxAPI(new ApacheHttpClient());
    }

    @Test
    public void testFetchMojangPlayer() throws InterruptedException {
        jaxAPI.getPlayerByName("Jaxaar").whenComplete((mojangPlayerReply, throwable) -> {
            System.out.println(mojangPlayerReply);
            System.out.println("UUID:" + mojangPlayerReply.getUUID() + "\n  == 2589ea18-4df3-4ff0-9961-3d3a8616168f");
            Assert.assertEquals(mojangPlayerReply.getUUID().toString(), "2589ea18-4df3-4ff0-9961-3d3a8616168f");
        });
        Thread.sleep(4000);
        Assert.fail();
    }

    @Test
    public void testFetchMojangPlayerBadInput() throws InterruptedException {
        jaxAPI.getPlayerByName("Jaxaaaaaar").whenComplete((mojangPlayerReply, throwable) -> {
//            System.out.println(throwable);
        });
        Thread.sleep(4000);
        Assert.fail();
    }
}