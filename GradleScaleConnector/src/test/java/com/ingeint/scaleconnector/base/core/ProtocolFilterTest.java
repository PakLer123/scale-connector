package com.ingeint.scaleconnector.base.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class ProtocolFilterTest {

    private static Object roundTrip(Object object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(object);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            in.setObjectInputFilter(ProtocolFilter.FILTER);
            return in.readObject();
        }
    }

    @Test
    void allowsRequests() throws Exception {
        Request request = new Request();
        request.setType(RequestType.READ_PORT);
        request.addParameter("serialport", "/dev/ttyUSB0");
        request.setDate(new Date());

        Request result = (Request) roundTrip(request);

        assertEquals(RequestType.READ_PORT, result.getType());
        assertEquals("/dev/ttyUSB0", result.getParameter("serialport"));
    }

    @Test
    void allowsResponses() throws Exception {
        Request request = new Request();
        request.setType(RequestType.TEST);
        Response response = new Response();
        response.setStatus(ResponseStatus.SUCCESS);
        response.addValue("value", "12.5");
        response.setRequest(request);
        response.setDate(new Date());

        Response result = (Response) roundTrip(response);

        assertEquals(ResponseStatus.SUCCESS, result.getStatus());
        assertEquals("12.5", result.getValue("value"));
    }

    @Test
    void rejectsOtherClasses() {
        assertThrows(InvalidClassException.class, () -> roundTrip(new ArrayList<>(java.util.List.of("x"))));
    }
}
