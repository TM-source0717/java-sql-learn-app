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
    public void insertUser(String name) {
        this.m_sql_controller.insertUser(name);
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
    public List<String> searchItem(String name) {
        return this.m_sql_controller.searchItem(name);
    }

}
