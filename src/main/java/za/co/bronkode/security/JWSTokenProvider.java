/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

https://github.com/jboss-developer/jboss-picketlink-quickstarts/blob/master/picketlink-angularjs-rest/src/main/java/org/jboss/as/quickstarts/picketlink/angularjs/security/authentication/JWSTokenProvider.java
 */
package za.co.bronkode.security;

import java.util.UUID;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.credential.storage.TokenCredentialStorage;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.json.jose.JWSBuilder;

/**
 *
 * @author theuns
 */
public class JWSTokenProvider implements Token.Provider<JWSToken> {

    @Inject
    private PartitionManager partitionManager;

    @Override
    public JWSToken issue(Account account) {
        TokenCredentialStorage tokenCredentialStorage = getIdentityManager().retrieveCurrentCredential(account, TokenCredentialStorage.class);
        JWSToken token;

        if (tokenCredentialStorage == null) {
            JWSBuilder builder = new JWSBuilder();

            byte[] privateKey = getPrivateKey();

            // here we construct a JWS signed with the private key provided by the partition.
            builder
                    .id(UUID.randomUUID().toString())
                    .rsa256(privateKey)
                    .issuer(account.getPartition().getName())
                    .issuedAt(getCurrentTime())
                    .subject(account.getId())
                    .expiration(getCurrentTime() + (5 * 60))
                    .notBefore(getCurrentTime())
                    .claim("branchno", 1)
                    .claim("costgroup", "active");

            token = new JWSToken(builder.build().encode());

            // now we update the account with the token previously issued by this provider.
            getIdentityManager().updateCredential(account, token);
        } else {
            token = new JWSToken(tokenCredentialStorage.getToken());

        }

        return token;
    }

    @Override
    public JWSToken renew(Account account, JWSToken renewToken) {
        return issue(account);
    }

    @Override
    public void invalidate(Account account) {
        getIdentityManager().removeCredential(account, TokenCredentialStorage.class);
    }

    @Override
    public Class<JWSToken> getTokenType() {
        return JWSToken.class;
    }

    private byte[] getPrivateKey() {
        String guid = "a15778ee-e8cf-4aec-8937-1ec0ea8cdb2d";
        return guid.getBytes();
        //return getPartition().<byte[]>getAttribute("PrivateKey").getValue();
    }

    private int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private Realm getPartition() {
        return partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);
    }

    private IdentityManager getIdentityManager() {
        return this.partitionManager.createIdentityManager(getPartition());
    }

}
