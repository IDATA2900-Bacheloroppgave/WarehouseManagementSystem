package wms.rest.wms.api.model;

/**
 * Represents the response body containing a JSON web token (JWT) for successful customer login.
 *
 * @author Mikkel Stavelie.
 */
public class LoginResponse {

    /** The JSON web token generated upon successful login */
    private String jwt;

    /**
     * Return the JSON web token.
     *
     * @return the JSON web token String.
     */
    public String getJwt() {
        return jwt;
    }

    /**
     * Sets the JSON web token.
     *
     * @param jwt the JSON web token String to set.
     */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
