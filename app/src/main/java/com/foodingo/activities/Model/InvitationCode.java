package com.foodingo.activities.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by admin on 20-01-2016.
 */
@ParseClassName("invitationCode")
public class InvitationCode extends ParseObject {
    String invitationCode;

    public InvitationCode(String invitationCode) {
        super();
        this.invitationCode = invitationCode;
    }

    public InvitationCode() {

    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

}
