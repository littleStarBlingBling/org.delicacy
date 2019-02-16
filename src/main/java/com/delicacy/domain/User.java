package com.delicacy.domain;

import com.delicacy.util.UsernameTransferUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: MyDear
 */
@Entity
@Table(name = "delicacy_user")
@Data
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = -2307286286718443641L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String pinyinUsername;
    private String email;
    private String password;
    private String avatar;
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorityList;

    public static User of(String username, String password, String email) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        user.setEmail(email);
        user.setPinyinUsername(UsernameTransferUtil.transferToPinyin(username));
        return user;
    }

    public void setEncodePassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return authorityList.stream().map(authority -> {
          return  new SimpleGrantedAuthority(authority.getAuthority());
        }).collect(Collectors.toList());
    }

    public String getPinyinUsername(){
        if(StringUtils.isEmpty(this.pinyinUsername)){
            return UsernameTransferUtil.transferToPinyin(this.username);
        }
        return this.getPinyinUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
