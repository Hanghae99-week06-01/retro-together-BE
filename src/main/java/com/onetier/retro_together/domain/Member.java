package com.onetier.retro_together.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

/**
 * Member entity
 */
@Builder
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Entity
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String emailId;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Override
    public boolean equals(Object object) {
        if(this == object) {
            return true;

        }
        if(object == null || Hibernate.getClass(this) != Hibernate.getClass(object)) {
            return false;
        }
        Member member = (Member) object;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * 비밀번호 유효성검사
     * @param passwordEncoder
     * @param password
     * @return
     * @author doosan
     */
    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }
}
