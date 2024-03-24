package com.formssafe.domain.reward.entity;


import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Builder
    private RewardRecipient(Long id, User user, Form form) {
        this.id = id;
        this.user = user;
        this.form = form;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Form getForm() {
        return form;
    }
}
