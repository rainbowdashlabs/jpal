package de.chojo.jpal.entities.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuth2Response {
    String scope;
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("token_type")
    String tokenType;
    @JsonProperty("expires_in")
    int expiresIn;
    String nonce;

    @Override
    public String toString() {
        return "OAuth2Response{scope='%s', accessToken='%s', tokenType='%s', expiresIn=%d, nonce='%s'}"
                .formatted(scope, accessToken, tokenType, expiresIn, nonce);
    }
}
