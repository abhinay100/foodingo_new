package com.foodingo.activities.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by admin on 20-01-2016.
 */
@ParseClassName("emailForInvitationCode")
public class EmailInvitationCode extends ParseObject {
    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public Boolean getsentFlag() {
        return getBoolean("sentFlag");
    }

    public void setsentFlag(Boolean sentFlag) {
        put("sentFlag", sentFlag);
    }
}
