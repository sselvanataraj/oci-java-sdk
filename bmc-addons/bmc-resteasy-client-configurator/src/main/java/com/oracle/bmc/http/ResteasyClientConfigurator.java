/**
 * Copyright (c) 2016, 2020, Oracle and/or its affiliates.  All rights reserved.
 * This software is dual-licensed to you under the Universal Permissive License (UPL) 1.0 as shown at https://oss.oracle.com/licenses/upl or Apache License 2.0 as shown at http://www.apache.org/licenses/LICENSE-2.0. You may choose either license.
 */
package com.oracle.bmc.http;

import com.oracle.bmc.http.internal.ContentLengthFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * A client configurator object allows to use resteasy with JavaSDK.
 */
public class ResteasyClientConfigurator implements ClientConfigurator {
    /**
     * Customize the ClientBuilder instance.
     *
     * @param builder
     *            The client builder.
     */
    public void customizeBuilder(ClientBuilder builder) {}

    /**
     * Customize the Client generated by the builder.
     *
     * @param client
     *            The client.
     */
    public void customizeClient(Client client) {
        // Java SDK needs to write Json string to http body directly, StringTextStar is the class that resteasy writes
        // Json string with application/json media type.
        // Add more providers to support stream content type. Add DefaultTextPlain, DefaultNumberWriter and ByteArrayProvider
        // for future user scenarios
        client.register(org.jboss.resteasy.plugins.providers.StringTextStar.class);
        client.register(org.jboss.resteasy.plugins.providers.StreamingOutputProvider.class);
        client.register(org.jboss.resteasy.plugins.providers.InputStreamProvider.class);
        client.register(org.jboss.resteasy.plugins.providers.DefaultTextPlain.class);
        client.register(org.jboss.resteasy.plugins.providers.DefaultNumberWriter.class);
        client.register(org.jboss.resteasy.plugins.providers.ByteArrayProvider.class);

        // Required for calls to only allow ResteasyClient to set the content-length header.
        // Otherwise, apache will throw an exception if it already exists. For example, such a case includes
        // ObjectStorage where the content-length is explicitly set in the header based on the value in the request.
        client.register(new ContentLengthFilter());
    }
}
