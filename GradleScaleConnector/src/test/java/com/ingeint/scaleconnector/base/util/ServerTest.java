package com.ingeint.scaleconnector.base.util;

import static org.junit.jupiter.api.Assertions.*;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingeint.scaleconnector.base.Client.Client;
import com.ingeint.scaleconnector.base.core.Request;
import com.ingeint.scaleconnector.base.core.RequestType;
import com.ingeint.scaleconnector.base.core.Response;
import com.ingeint.scaleconnector.base.core.ResponseStatus;

public class ServerTest {

    private Server server;
    private int port;

    @BeforeEach
    public void setUp() throws Exception {
        try (ServerSocket probe = new ServerSocket(0)) {
            port = probe.getLocalPort();
        }
        server = new Server(port);
        server.start();
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.stop();
    }

    private Response sendTestRequest() throws Exception {
        Request request = new Request();
        request.setType(RequestType.TEST);
        return new Client("localhost", port).sendRequest(request);
    }

    @Test
    void answersTestRequest() throws Exception {
        Response response = sendTestRequest();

        assertEquals(ResponseStatus.SUCCESS, response.getStatus());
        assertEquals("Successful test", response.getServerMessage());
    }

    @Test
    void handlesConcurrentClients() throws Exception {
        try (ExecutorService clients = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Response>> responses = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                responses.add(clients.submit(this::sendTestRequest));
            }
            for (Future<Response> response : responses) {
                assertEquals(ResponseStatus.SUCCESS, response.get().getStatus());
            }
        }
    }

    @Test
    void stopsCleanly() throws Exception {
        server.stop();

        assertFalse(server.isAlive());
    }
}
