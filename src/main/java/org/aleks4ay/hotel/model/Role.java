package org.aleks4ay.hotel.model;

public enum Role {
    ROLE_GUEST,
    ROLE_USER,
    ROLE_MANAGER,
    ROLE_ADMIN;

    public String getTitle() {
        return this.toString().replace("ROLE_", "");
    }
}