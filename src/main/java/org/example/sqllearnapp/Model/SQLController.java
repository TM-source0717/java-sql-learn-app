package org.example.sqllearnapp.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLController {
    private static final String DB_URL = "jdbc:sqlite:sample.db";

    public SQLController() {
        // インスタンス生成時に自動でテーブルを作る
        createNewTable();
    }

    /**
     * テーブル作成 (CREATE)
     */
    private void createNewTable() {
        // DBファイル内にテーブルが存在しないときに、新しく作成する。
        String department_sql = "CREATE TABLE IF NOT EXISTS departments ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL"
                + ");";

        String users_sql = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL,"
                + " department_id INTEGER,"
                + " role TEXT DEFAULT '一般',"
                + " FOREIGN KEY (department_id) REFERENCES departments(id)"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(department_sql);
            stmt.execute(users_sql);

            // 初期状態の部署データが存在するかチェックし、なければ投入する
            String check_sql = "SELECT COUNT(*) AS count FROM departments";
            try (ResultSet rs = stmt.executeQuery(check_sql)) {
                if (rs.next() && rs.getInt("count") == 0) {
                    // 開発部、営業部、総務部をあらかじめ入れておく
                    stmt.execute("INSERT INTO departments (name) VALUES ('開発部')");
                    stmt.execute("INSERT INTO departments (name) VALUES ('営業部')");
                    stmt.execute("INSERT INTO departments (name) VALUES ('総務部')");
                }
            }
        } catch (SQLException e) {
            System.out.println("DB接続エラー: " + e.getMessage());
        }
    }

    /**
     * データ取得
     */
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();

        // SELECT * FROM xxxで指定のテーブル内のデータを全権取得
        String sql = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // 検索に該当したすべてのデータから、nameテーブルのみを抜き出す。
            while (rs.next()) {
                users.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("データ取得エラー: " + e.getMessage());
        }
        return users;
    }

    /**
     * データ取得(部署フィルター済み)
     */
    public List<UserRecord> getUsersByDepartment(String department_name) {
        List<UserRecord> users_list = new ArrayList<>();
        int department_id = this.getDepartmentIdByName(department_name);
        // name に加えて role も取得するように変更
        String sql = "SELECT id, name, role FROM users WHERE department_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, department_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // UserRecordクラスにid, name, roleを渡すコンストラクタがあると便利です
                users_list.add(new UserRecord(
                        rs.getInt("id"),
                        rs.getString("name"),
                        department_name, // 部署名
                        rs.getString("role") // DBから実際の役割を取得
                ));
            }
        } catch (SQLException e) {
            System.out.println("データ取得エラー: " + e.getMessage());
        }
        return users_list;
    }

    /**
     * データを追加する (INSERT)
     */
    public void insertUser(String name, String department_name, String role) { // roleを追加
        int department_id = this.getDepartmentIdByName(department_name);

        // SQLのINSERT文に role カラムを追加
        String sql = "INSERT INTO users(name, department_id, role) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, department_id);
            pstmt.setString(3, role); // 権限を保存
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("データ追加エラー: " + e.getMessage());
        }
    }

    /**
     * データを削除する (DELETE)
     */
    public void deleteUser(String name) {
        String sql = "DELETE FROM users WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("データ削除エラー: " + e.getMessage());
        }
    }

    /**
     * 検索機能
     */
    // SQLController.java
    public List<UserRecord> searchItem(String name, String department_name) {
        List<UserRecord> search_list = new ArrayList<>();
        int department_id = this.getDepartmentIdByName(department_name);

        String sql = "SELECT id, name, role FROM users WHERE name LIKE ? AND department_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            pstmt.setInt(2, department_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // UserRecord にデータを詰める
                search_list.add(new UserRecord(
                        rs.getInt("id"),
                        rs.getString("name"),
                        department_name,
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println("検索エラー: " + e.getMessage());
        }
        return search_list;
    }

    /**
     * 部署名の一覧を全件取得する
     */
    public List<String> getAllDepartments() {
        List<String> depts = new ArrayList<>();
        String sql = "SELECT name FROM departments";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                depts.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("部署取得エラー: " + e.getMessage());
        }
        return depts;
    }

    /**
     * DBから全ユーザー情報を詳細付きで取得する
     */
    public List<UserRecord> getAllUsersWithDetails() {
        List<UserRecord> list = new ArrayList<>();

        // JOINを使って、usersのdepartment_idとdepartmentsのidを紐付ける
        String sql = "SELECT u.id, u.name, d.name AS dept_name, u.role " +
                "FROM users u " +
                "JOIN departments d ON u.department_id = d.id";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UserRecord record = new UserRecord(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("dept_name"),
                        rs.getString("role")
                );
                list.add(record);
            }
        } catch (SQLException e) {
            System.out.println("詳細取得エラー: " + e.getMessage());
        }
        return list;
    }

    /**
     * 部署名（String）から部署ID(数値)に変換する
     */
    private int getDepartmentIdByName(String department_name) {
        int department_id = 0;

        String sql = "SELECT id FROM departments WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // プレースホルダーの指定
            pstmt.setString(1, department_name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                department_id = rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("変換エラー: " + e.getMessage());
        }
        return department_id;
    }
}