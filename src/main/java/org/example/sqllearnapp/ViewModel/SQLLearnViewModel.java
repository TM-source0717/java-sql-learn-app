package org.example.sqllearnapp.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.sqllearnapp.Model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLLearnViewModel {

    private final SQLLearnIF m_sql_learn_if;

    // Viewで監視しているユーザーリストとバインドするための変数
    private final ObservableList<String> m_user_display_list = FXCollections.observableArrayList();

    private final ObservableList<String> m_dept_list = FXCollections.observableArrayList();

    private List<User> m_internal_user_list = new ArrayList<>();

    /**
     * コンストラクタ
     */
    public SQLLearnViewModel(SQLLearnIF sql_learn_if) {
        this.m_sql_learn_if = sql_learn_if;

        m_dept_list.setAll(m_sql_learn_if.getAllDepartments());
        this.syncWithDatabase();
    }

    /**
     * ユーザーリスト取得関数
     */
    public ObservableList<String> getM_user_display_list() {
        return this.m_user_display_list;
    }

    /**
     * 部署名取得関数
     */
    public ObservableList<String> getDepartmentList() { return m_dept_list; }

    /**
     * ユーザーリスト取得（部署フィルター）
     */
    public void getUserBySearchDepartmentId(String name) {
        if (name == null) return;
        m_internal_user_list.clear();

        List<UserRecord> records = this.m_sql_learn_if.getUsersByDepartment(name);

        for (UserRecord record : records) {
            User user = UserFactory.createUser(
                    record.id,
                    record.name,
                    record.deptName,
                    record.role,
                    LocalDate.now(),
                    LocalDate.now()
            );
            m_internal_user_list.add(user);
        }
        this.updateDisplayList();
    }

    /**
     * ユーザーリスト更新関数
     */
    // SQLLearnViewModel.java
    public void refreshUserList(String department_name) {
        m_internal_user_list.clear();

        List<UserRecord> records = this.m_sql_learn_if.getUsersByDepartment(department_name);

        for (UserRecord record : records) {
            User user = UserFactory.createUser(
                    record.id,
                    record.name,
                    record.deptName,
                    record.role,
                    LocalDate.now(),
                    LocalDate.now()
            );
            m_internal_user_list.add(user);
        }

        this.updateDisplayList();
    }

    /*
     * ユーザー登録処理
     */
    public void registerUser(String name, String department_name, String role_type) {
        if (name == null || name.trim().isEmpty()) return;

        this.m_sql_learn_if.insertUser(name, department_name, role_type);

        // 以降、Model生成時の権限もrole_typeを使う
        User new_user = UserFactory.createUser(0, name, department_name, role_type, LocalDate.now(), LocalDate.now());
        m_internal_user_list.add(new_user);

        this.updateDisplayList();
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
    public void searchItem(String name, String department_name) {
        if (name == null) return;

        List<UserRecord> records = this.m_sql_learn_if.searchItem(name, department_name);

        m_internal_user_list.clear();
        for (UserRecord record : records) {
            User user = UserFactory.createUser(
                    record.id,
                    record.name,
                    record.deptName,
                    record.role,
                    LocalDate.now(),
                    LocalDate.now()
            );
            m_internal_user_list.add(user);
        }

        this.updateDisplayList();
    }

    /**
     * DBから全データを読み込み、Modelリストを再構築する
     */
    public void syncWithDatabase() {
        // 1. DBからレコードを取得（SQLControllerに追加したメソッドを呼び出す）
        List<UserRecord> records = m_sql_learn_if.getAllUsersWithDetails();

        // 2. Modelリストをクリアして再構築
        m_internal_user_list.clear();

        for (UserRecord record : records) {
            // Factoryを使って、DBのデータから適切なUserインスタンスを生成
            // ※birth_date等は一旦LocalDate.now()で代用していますが、
            // 将来的にはDBにカラムを追加して取得してください
            User user = UserFactory.createUser(
                    record.id,
                    record.name,
                    record.deptName,
                    record.role,
                    LocalDate.now(),
                    LocalDate.now()
            );
            m_internal_user_list.add(user);
        }

        // 3. 画面表示用リストを更新
        updateDisplayList();
    }

    /**
     * モデルリストをもとに表示用リスト(String)を更新する
     */
    private void updateDisplayList() {
        m_user_display_list.clear();
        for (User user : m_internal_user_list) {
            String text = String.format("%s (%s)", user.getName(), user.getRoleName());
            m_user_display_list.add(text);
        }
    }
}