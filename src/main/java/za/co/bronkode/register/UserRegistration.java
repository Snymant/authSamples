/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bronkode.register;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;

/**
 *
 * @author theuns
 */
@Named
@RequestScoped
public class UserRegistration {
 
  @Inject
  private IdentityManager identityManager;
 
  private String loginName;
  private String firstName;
  private String lastName;
  private String password;
 
  public String register() {
    User newUser = new User(this.loginName);
 
    newUser.setFirstName(this.firstName);
    newUser.setLastName(this.lastName);
 
    this.identityManager.add(newUser);
 
    Password password = new Password(this.password);
 
    this.identityManager.updateCredential(newUser, password);
 
    return "/signin.xhtml";
  }
 
  // getters and setters
}
