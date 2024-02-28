package co.com.turbos.entity;

import java.util.Date;

import javax.persistence.PrePersist;

public class UserEntityListener {

	@PrePersist
    public void setCreationDate(Users user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Date());
        }
    }

}
