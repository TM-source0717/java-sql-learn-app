package org.example.sqllearnapp.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.sqllearnapp.ViewModel.SQLLearnViewModel;

public class SQLLearnView {

    @FXML private ListView<String> m_user_list_view;
    @FXML private ListView<String> m_dept_list_view;
    @FXML private ComboBox<String> m_dept_combo_box;
    @FXML private ComboBox<String> m_role_combo_box;

    @FXML private TextField m_input_text;
    @FXML private TextField m_search_text;
    @FXML private Label m_selected_name_label;
    @FXML private Button m_register_button;
    @FXML private Button m_delete_button;

    private final SQLLearnViewModel m_search_learn_vm;

    public SQLLearnView(SQLLearnViewModel search_learn_vm) {
        this.m_search_learn_vm = search_learn_vm;
    }

    @FXML
    public void initialize() {
        m_user_list_view.setItems(m_search_learn_vm.getM_user_display_list());
        m_dept_list_view.setItems(m_search_learn_vm.getDepartmentList());
        m_dept_combo_box.setItems(m_search_learn_vm.getDepartmentList());

        // 【追加】権限コンボボックスの初期化
        m_role_combo_box.getItems().addAll("一般", "管理者");
        m_role_combo_box.getSelectionModel().selectFirst();

        m_dept_combo_box.getSelectionModel().selectFirst();

        m_input_text.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isInvalid = (newValue == null || newValue.trim().isEmpty());
            m_register_button.setDisable(isInvalid);
        });

        // 選択制御
        m_user_list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                m_selected_name_label.setText(newSelection);
                m_delete_button.setDisable(false);
            } else {
                m_selected_name_label.setText("選択されていません");
                m_delete_button.setDisable(true);
            }
        });

        m_dept_list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            m_search_learn_vm.getUserBySearchDepartmentId(newSelection);
        });

        m_search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedDeptName = m_dept_combo_box.getSelectionModel().getSelectedItem();
            m_search_learn_vm.searchItem(newValue, selectedDeptName);
        });
    }

    @FXML
    protected void onRegisterButtonClick() {
        String inputText = m_input_text.getText();
        String selectedDeptName = m_dept_combo_box.getSelectionModel().getSelectedItem();
        // 【追加】選択された権限を取得
        String selectedRole = m_role_combo_box.getSelectionModel().getSelectedItem();

        // ViewModelへ渡す引数に role を追加
        m_search_learn_vm.registerUser(inputText, selectedDeptName, selectedRole);

        this.m_search_learn_vm.refreshUserList(selectedDeptName);
        m_input_text.clear();
        m_search_text.clear();
    }

    @FXML
    protected void onDeleteButtonClick() {
        String selectedName = m_user_list_view.getSelectionModel().getSelectedItem();
        String selectedDeptName = m_dept_combo_box.getSelectionModel().getSelectedItem();
        if (selectedName != null) {
            m_search_learn_vm.deleteUser(selectedName);
            this.m_search_learn_vm.refreshUserList(selectedDeptName);
            m_search_text.clear();
        }
    }
}