package org.rvsoft.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.rvsoft.entity.enums.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "schema1", name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long telegramUserId;

    @CreationTimestamp
    private LocalDateTime firstLoginDate;

    private String firstName;

    private String lastName;

    private String userName;

    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private UserState state;
}
