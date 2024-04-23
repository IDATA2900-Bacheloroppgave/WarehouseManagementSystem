package wms.rest.wms.api.model;

import lombok.Getter;
import lombok.Setter;


public class LoginResponse {

    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
