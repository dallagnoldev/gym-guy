package com.dallagnoldev.gymguy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "tb_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolesEntity implements GrantedAuthority {

    @Id
    private Long id;
    private String name;

    @Override
    public @Nullable String getAuthority() {
        return name;
    }
}
