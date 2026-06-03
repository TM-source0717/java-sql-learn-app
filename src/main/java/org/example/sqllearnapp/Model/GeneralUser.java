package org.example.sqllearnapp.Model;

import java.time.LocalDate;

public class GeneralUser extends User {

    private final String ROLE_NAME = "一般";

    public GeneralUser(int id, String name, String department_name, LocalDate birthDate, LocalDate joinDate) {
        super(id, name, department_name, birthDate, joinDate);
    }

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }
}
