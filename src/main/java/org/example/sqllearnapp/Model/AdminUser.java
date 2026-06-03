package org.example.sqllearnapp.Model;

import java.time.LocalDate;

public class AdminUser extends User {
    private final String ROLE_NAME = "管理者";

    public AdminUser(int id, String name, String department_name, LocalDate birthDate, LocalDate joinDate) {
        super(id, name, department_name, birthDate, joinDate);
    }

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }
}
