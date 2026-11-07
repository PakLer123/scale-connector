package com.ingeint.scaleconnector.base.core;

import java.io.ObjectInputFilter;

/**
 * Deserialization filter for the TCP protocol. Only the classes that make up
 * a {@link Request} or {@link Response} may be read from a socket, which
 * blocks deserialization attacks from untrusted clients.
 */
public final class ProtocolFilter {

	public static final ObjectInputFilter FILTER = ObjectInputFilter.Config.createFilter(
			"maxdepth=10;maxrefs=1000;maxbytes=1048576;"
					+ "com.ingeint.scaleconnector.base.core.*;"
					+ "java.util.HashMap;java.util.Map$Entry;java.util.Date;java.lang.String;java.lang.Enum;"
					+ "!*");

	private ProtocolFilter() {
	}
}
