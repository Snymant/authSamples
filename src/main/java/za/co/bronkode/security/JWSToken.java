/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bronkode.security;

import org.picketlink.idm.credential.AbstractToken;
import org.picketlink.json.jose.JWS;
import org.picketlink.json.jose.JWSBuilder;

/**
 *
 * @author theuns
 */
public class JWSToken extends AbstractToken        
{
    private final JWS jws;

    public JWSToken(String encodedToken) {
        super(encodedToken);
         this.jws = new JWSBuilder().build(encodedToken);
    }

    @Override
    public String getSubject() {
        return this.jws.getSubject();      
    }
    
}
