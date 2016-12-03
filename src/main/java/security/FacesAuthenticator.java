/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;
import org.picketlink.internal.DefaultIdentity;

/**
 *
 * @author theuns
 */
@PicketLink
public class FacesAuthenticator extends BaseAuthenticator {

    @Inject
    private DefaultLoginCredentials credentials;

    @Override
    public void authenticate() {
        if ("jsmith".equals(credentials.getUserId())
                && "abc123".equals(credentials.getPassword())) {
            setStatus(AuthenticationStatus.SUCCESS);
            User u = new User(credentials.getUserId());
            u.setAttribute(new Attribute<>("COSTGROUP", "1"));
            u.setAttribute(new Attribute<>("multi-valued", new String[] { "1", "2", "3" }));
            setAccount(u);
        } else {
            setStatus(AuthenticationStatus.FAILURE);
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    "Authentication Failure - The username or password you provided were invalid."));
        }
    }

}
