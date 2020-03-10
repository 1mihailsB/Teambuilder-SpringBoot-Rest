package com.teamplanner.rest.controller.googlelogin;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleAuthResponseParser {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    GoogleAuthorizationCodeExchange exchange;

    //authorization code in request body in JSON object {'authCode':'the Code'}
    protected String exchange(Map<String, Object> authorizationCode) {
        ResponseEntity<Map> googleResponse = null;

        googleResponse = exchange.exchangeAuthCode(authorizationCode);

        JSONObject responseToFrontend = new JSONObject().put("empty", "value");

        if (googleResponse != null) {
            JSONObject json = new JSONObject(googleResponse);

            LOG.debug("gresponse" + googleResponse.getHeaders());
            LOG.debug("id_token++   " + googleResponse.getBody().get("id_token"));
            LOG.debug("gresponse++" + json.toString(4));
            LOG.debug("JSON--------- \n" + json.get("body"));


            String jwtToken = (String) googleResponse.getBody().get("id_token");
            LOG.debug("------------ Decode JWT ------------");
            String[] split_string = jwtToken.split("\\.");
//			String base64EncodedHeader = split_string[0];
            String base64EncodedBody = split_string[1];
//			String base64EncodedSignature = split_string[2];
//			
//			LOG.debug("~~~~~~~~~ JWT Header ~~~~~~~");
            Base64 base64Url = new Base64(true);
//			String header = new String(base64Url.decode(base64EncodedHeader));
//			LOG.debug("JWT Header : " + header);


            LOG.debug("~~~~~~~~~ JWT Body ~~~~~~~");
            String body = new String(base64Url.decode(base64EncodedBody));
            LOG.debug("JWT Body : " + body);
            String newBody = body.replaceAll("\"", "");
            String[] bodyEntries = newBody.split(",");
            Map<String, String> userDetails =
                    Arrays.stream(bodyEntries)
                            .map(elem -> elem.split(":"))
                            .filter(elem -> elem[0].equals("given_name"))
                            .collect(Collectors.toMap(e -> e[0], e -> e[1]));
            LOG.debug(userDetails.toString());

            responseToFrontend = new JSONObject(userDetails);
            LOG.debug("response:  " + responseToFrontend);
            return responseToFrontend.toString();


//			LOG.debug("~~~~~ JWT Signature ~~~~~~");
//			String signature = new String(base64Url.decode(base64EncodedSignature));
//			LOG.debug("JWT Signature : "+signature);
        }


        return responseToFrontend.toString();
    }

}
