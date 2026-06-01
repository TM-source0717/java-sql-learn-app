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
        // DBファイル内にuserテーブルが存在しないときに、新しく作成する。
        // id,nameのcolumnで作成
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
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
     * データを追加する (INSERT)
     */
    public void insertUser(String name) {
        // 入力文字が可変のため、先に条件のみを設定したのちに、対象のテキストを設定する
        // 入力が自由なので、意図せずSQL構文のテキストが入力されてもDB操作を行わないようにする
        String sql = "INSERT INTO users(name) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // プレースホルダーの指定
            pstmt.setString(1, name);
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
    public List<String> searchItem(String name) {
        List<String> search_list = new ArrayList<>();

        // 入力文字が可変のため、先に条件のみを設定したのちに、対象のテキストを設定する
        // 入力が自由なので、意図せずSQL構文のテキストが入力されてもDB操作を行わないようにする
        String sql = "SELECT * FROM users WHERE name LIKE ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // プレースホルダーの指定
            // 部分一致なので、"%"を使用
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            // 検索に該当したすべてのデータから、nameテーブルのみを抜き出す。
            while (rs.next()) {
                search_list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("検索エラー: " + e.getMessage());
        }
        return search_list;
    }
}