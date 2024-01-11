package dev.example.xpenstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(name = "unique_phone", columnNames = "PHONE_NO"),
        @UniqueConstraint(name = "unique_email", columnNames = "EMAIL_ID")
})
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @NotBlank(message = "First name cannot be empty")
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @NotBlank(message = "Second name cannot be empty")
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @NotBlank(message = "Provide valid phone number")
    @Column(name = "PHONE_NO", nullable = false)
    private String phoneNo;

    @NotBlank(message = "Provide valid email address")
    @Column(name = "EMAIL_ID", nullable = false)
    @Email(message = "Invalid mail format")
    private String emailId;

    @JsonIgnore
    @NotBlank(message = "Provide your password")
    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
