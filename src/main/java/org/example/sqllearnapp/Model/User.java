package org.example.sqllearnapp.Model;

import java.time.LocalDate;
import java.time.Period;

public abstract class User {
    private int m_id;
    private String m_name;
    private String m_departmentName;
    private LocalDate m_birth_date;
    private LocalDate m_join_data;

    public User(int id, String name, String departmentName, LocalDate birthDate, LocalDate joinDate) {
        this.m_id = id;
        this.m_name = name;
        this.m_departmentName = departmentName;
        this.m_birth_date = birthDate;
        this.m_join_data = joinDate;
    }

    public int GetAge() {
        if (m_birth_date == null) return 0;
        return Period.between(m_birth_date, LocalDate.now()).getYears();
    }

    // 【Java Silver頻出: 日付API】入社日から現在の勤続年数を「〇年〇ヶ月」の形式で取得する
    public String getServicePeriod() {
        String service_period = "";

        Period p = Period.between(m_join_data, LocalDate.now());
        return String.format("%d年%dヶ月", p.getYears(), p.getMonths());
    }

    public abstract String getRoleName(); // "一般" または "管理者" を返す

    public int getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public String getDepartmentName() {
        return m_departmentName;
    }
}
