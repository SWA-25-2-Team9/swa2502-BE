package com.swa2502.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    private String role;
}
