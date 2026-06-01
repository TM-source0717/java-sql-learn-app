package org.example.sqllearnapp.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.sqllearnapp.Model.SQLLearnIF;

import java.util.ArrayList;
import java.util.List;

public class SQLLearnViewModel {

    private final SQLLearnIF m_sql_learn_if;

    // Viewで監視しているユーザーリストとバインドするための変数
    private final ObservableList<String> userList = FXCollections.observableArrayList();

    private final ObservableList<String> m_dept_list = FXCollections.observableArrayList();

    /**
     * コンストラクタ
     */
    public SQLLearnViewModel(SQLLearnIF sql_learn_if) {
        this.m_sql_learn_if = sql_learn_if;

        m_dept_list.setAll(m_sql_learn_if.getAllDepartments());
    }

    /**
     * ユーザーリスト取得関数
     */
    public ObservableList<String> getUserList() {
        return this.userList;
    }

    /**
     * 部署名取得関数
     */
    public ObservableList<String> getDepartmentList() { return m_dept_list; }

    /**
     * ユーザーリスト取得（部署フィルター）
     */
    public void getUserBySearchDepartmentId(String name) {
        List<String> search_item = new ArrayList<>();
        if (name == null) {
            return;
        }

        search_item = this.m_sql_learn_if.getUserBySearchDepartmentId(name);

        this.userList.clear();
        this.userList.addAll(search_item);
    }

    /**
     * ユーザーリスト更新関数
     */
    public void refreshUserList(String department_name) {
        // 現在の画面連動リストを空っぽにする
        this.userList.clear();

        // Modelから現在のDB内のデータを取得
        List<String> db_users = this.m_sql_learn_if.getUserBySearchDepartmentId(department_name);
        //　ユーザーリストを更新
        this.userList.addAll(db_users);
    }

    /**
     * ユーザー登録処理
     */
    public void registerUser(String name,String department_name) {
        if (name == null || name.trim().isEmpty()) return;
        if (department_name == null || department_name.trim().isEmpty()) return;

        this.m_sql_learn_if.insertUser(name,department_name);
    }

    /**
     * ユーザー削除処理
     */
    public void deleteUser(String name) {
        if (name == null) return;

        this.m_sql_learn_if.deleteUser(name);
    }

    /**
     * 検索機能
     */
    public void searchItem(String name,String department_name) {
        List<String> search_item = new ArrayList<>();
        if (name == null) {
            return;
        }

        search_item = this.m_sql_learn_if.searchItem(name,department_name);

        this.userList.clear();
        this.userList.addAll(search_item);
    }
}