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

    /**
     * コンストラクタ
     */
    public SQLLearnViewModel(SQLLearnIF sql_learn_if) {
        this.m_sql_learn_if = sql_learn_if;

        this.refreshUserList();
    }

    /**
     * ユーザーリスト取得関数
     */
    public ObservableList<String> getUserList() {
        return this.userList;
    }

    /**
     * ユーザーリスト更新関数
     */
    public void refreshUserList() {
        // 現在の画面連動リストを空っぽにする
        this.userList.clear();

        // Modelから現在のDB内のデータを取得
        List<String> db_users = this.m_sql_learn_if.GetAllUserList();
        //　ユーザーリストを更新
        this.userList.addAll(db_users);
    }

    /**
     * ユーザー登録処理
     */
    public void registerUser(String name) {
        if (name == null || name.trim().isEmpty()) return;

        this.m_sql_learn_if.insertUser(name);

        this.refreshUserList();
    }

    /**
     * ユーザー削除処理
     */
    public void deleteUser(String name) {
        if (name == null) return;

        this.m_sql_learn_if.deleteUser(name);

        this.refreshUserList();
    }

    /**
     * 検索機能
     */
    public void searchItem(String name) {
        List<String> search_item = new ArrayList<>();
        if (name == null) {
            return;
        }

        search_item = this.m_sql_learn_if.searchItem(name);

        this.userList.clear();
        this.userList.addAll(search_item);
    }

}