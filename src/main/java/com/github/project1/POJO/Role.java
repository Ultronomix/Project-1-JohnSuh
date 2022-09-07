package com.github.project1.POJO;

import java.util.Objects;

public class Role {

    private String roleId;
    private String role;

    public Role(String roleId, String role) {
        this.roleId = roleId;
        this.role = role;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role cRole = (Role) o;
        return Objects.equals(roleId, cRole.roleId) && Objects.equals(role, cRole.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, role);
    }

    @Override
    public String toString() {
        return "Role [role=" + role + ", roleId=" + roleId + "]";
    }
    
}
