package org.pih.loganalyzer;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    "%h %l %u %t "%r" %>s %b "%{Referer}i" "%{User-agent}i""

    %h – Remote host (client IP address)
    %l – User identity, or dash, if none (often not used)
    %u – Username, via HTTP authentication, or dash if not used
    %t – Timestamp of when Apache received the HTTP request
    ”%r – The actual request itself from the client
    %>s – The status code Apache returns in response to the request
    %b – The size of the request in bytes.
    ”%{Referer}i” – Referrer header, or dash if not used  (In other words, did they click a URL on another site to come to your site)
    ”%{User-agent}i – User agent (contains information about the requester’s browser/OS/etc)
 */
public class SSLAccessLogLine {

    public static DateFormat dateFormat = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ssZ]");

    public String remoteHost;
    public String userIdentity;
    public String username;
    public Timestamp timestamp;
    public String request;
    public Integer statusCode;
    public Integer sizeBytes;
    public String referrer;
    public String userAgent;

    public SSLAccessLogLine(String line) {
        int index = 0;
        String[] parts = line.split(" ");

        remoteHost = parts[index++];
        userIdentity = parts[index++];
        username = parts[index++];

        String timestampString = parts[index++];
        while (!timestampString.endsWith("]")) {
            timestampString = timestampString + parts[index++];
        }
        timestamp = parse(timestampString);

        request = parts[index++].substring(1);
        while (!request.endsWith("\"")) {
            request = request + " " + parts[index++];
        }
        request = request.substring(0, request.length() - 1);

        statusCode = Integer.parseInt(parts[index++]);
        sizeBytes = Integer.parseInt(parts[index++]);
        referrer = parts[index++].substring(1);
        while (!referrer.endsWith("\"")) {
            referrer = referrer + " " +  parts[index++];
        }
        referrer = referrer.substring(0, referrer.length() - 1);

        userAgent = parts[index++].substring(1);
        while (!userAgent.endsWith("\"")) {
            userAgent = userAgent + " " + parts[index++];
        }
        userAgent = userAgent.substring(0, userAgent.length() - 1);
    }

    private Timestamp parse(String timestamp) {
        try {
            Date d = dateFormat.parse(timestamp);
            return new Timestamp(d.getTime());
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to parse " + timestamp + " into a timestamp", e);
        }
    }
}
