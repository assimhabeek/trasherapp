package com.stic.trasher.utils;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private String token;

    public JwtResponse(String jwttoken) {
        this.token = jwttoken;
    }

    public String getToken() {
        return token;
    }
}
