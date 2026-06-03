package org.example.sqllearnapp.Model;

import java.util.List;

public class SQLLearnIF {

    private final SQLController m_sql_controller;

    /**
     * コンストラクタ
     */
    public SQLLearnIF(SQLController sql_controller) {
        this.m_sql_controller = sql_controller;
    }

    /**
     * データ取得
     */
    public List<String> GetAllUserList() {
        return m_sql_controller.getAllUsers();
    }

    /**
     * データ追加
     */
    public void insertUser(String name, String department_name, String role) {
        this.m_sql_controller.insertUser(name, department_name, role);
        return;
    }

    /**
     * データ削除
     */
    public void deleteUser(String name) {
        this.m_sql_controller.deleteUser(name);
    }

    /**
     * 検索機能
     */
    public List<UserRecord> searchItem(String name, String department_name) {
        return this.m_sql_controller.searchItem(name, department_name);
    }

    /**
     * データ取得
     */
    public List<UserRecord> getUsersByDepartment(String department_name) {
        return this.m_sql_controller.getUsersByDepartment(department_name);
    }

    /**
     * 部署名取得
     */
    public List<String> getAllDepartments() {
        return this.m_sql_controller.getAllDepartments();
    }

    public List<UserRecord> getAllUsersWithDetails() {
        return this.m_sql_controller.getAllUsersWithDetails();
    }
}
