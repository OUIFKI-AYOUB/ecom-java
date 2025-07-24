package com.example.Ecom.entities;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(
            Permissions.write_product,
            Permissions.read_product,
            Permissions.update_product,
            Permissions.delete_product,
            Permissions.write_review,
            Permissions.read_review,
            Permissions.update_review,
            Permissions.delete_review,
            Permissions.write_order,
            Permissions.read_order,
            Permissions.update_status_order,
            Permissions.delete_order,
            Permissions.write_category,
            Permissions.read_category,
            Permissions.update_category,
            Permissions.delete_category
    )),
    USER(Set.of(
            Permissions.read_product,
            Permissions.write_review,
            Permissions.read_review,
            Permissions.update_review,
            Permissions.delete_review,
            Permissions.write_order,
            Permissions.read_order,
            Permissions.read_category
    ));
    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }
}
