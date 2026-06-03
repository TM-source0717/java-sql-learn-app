package org.example.sqllearnapp.Model;

import java.time.LocalDate;

public class UserFactory {
    public static User createUser(int id, String name, String dept, String role, LocalDate birth, LocalDate join) {
        if ("管理者".equals(role)) {
            return new AdminUser(id, name, dept, birth, join);
        } else {
            return new GeneralUser(id, name, dept, birth, join);
        }
    }
}