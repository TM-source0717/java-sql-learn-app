package org.example.sqllearnapp.Model;

// DBから取ってきた生のデータを一時的に運ぶだけのクラス
public class UserRecord {
    public int id;
    public String name;
    public String deptName;
    public String role;

    public UserRecord(int id, String name, String deptName, String role) {
        this.id = id;
        this.name = name;
        this.deptName = deptName;
        this.role = role;
    }
}