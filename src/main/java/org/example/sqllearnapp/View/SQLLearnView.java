package org.example.sqllearnapp.View;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.sqllearnapp.ViewModel.SQLLearnViewModel;

public class SQLLearnView {

    // ユーザーの表示部分
    @FXML
    private ListView<String> m_user_list_view;

    // 部署一覧表示部分
    @FXML
    private ListView<String> m_dept_list_view;

    // 登録用の部署選択コンボボックス
    @FXML
    private ComboBox<String> m_dept_combo_box;

    // 入力テキスト部分
    @FXML
    private TextField m_input_text;

    // 検索テキスト部分
    @FXML
    private TextField m_search_text;

    @FXML
    private Label m_selected_name_label;

    @FXML
    private Button m_register_button;

    @FXML
    private Button m_delete_button;

    private final SQLLearnViewModel m_search_learn_vm;

    /**
     * コンストラクタ
     */
    public SQLLearnView(SQLLearnViewModel search_learn_vm) {
        this.m_search_learn_vm = search_learn_vm;
    }

    /**
     * 初期化
     */
    @FXML
    public void initialize() {
        // 各リストのバインド
        m_user_list_view.setItems(m_search_learn_vm.getUserList());
        m_dept_list_view.setItems(m_search_learn_vm.getDepartmentList());
        m_dept_combo_box.setItems(m_search_learn_vm.getDepartmentList());

        m_dept_combo_box.getSelectionModel().selectFirst(); // 初期選択として先頭の部署を選んでおく

        // 新規入力テキストを監視し、空文字なら登録ボタンを無効化
        m_input_text.textProperty().addListener((observable, oldValue, newValue) -> {
            // トリム（前後の空白除去）した結果が空、またはnullならボタンを無効化
            boolean isInvalid = (newValue == null || newValue.trim().isEmpty());
            m_register_button.setDisable(isInvalid);
        });

        // リストの選択変更イベントを監視し、詳細表示と削除ボタンを制御
        // 選択されているアイテムがないときは、Deleteボタンを非活性
        m_user_list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                m_selected_name_label.setText(newSelection);
                m_delete_button.setDisable(false);
            } else {
                m_selected_name_label.setText("選択されていません");
                m_delete_button.setDisable(true);
            }
        });

        // 部署リストの選択変更イベントを監視
        m_dept_list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            // 部署が選ばれたら、その部署に所属するユーザーを絞り込むロジックをViewModelに通知する
            m_search_learn_vm.getUserBySearchDepartmentId(newSelection);
        });

        // 検索アイテムのイベントを監視
        m_search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedDeptName = m_dept_combo_box.getSelectionModel().getSelectedItem();
            m_search_learn_vm.searchItem(newValue,selectedDeptName);
        });
    }

    /**
     * 登録ボタン押下時イベント
     */
    @FXML
    protected void onRegisterButtonClick() {
        String inputText = m_input_text.getText();
        String selectedDeptName = m_dept_combo_box.getSelectionModel().getSelectedItem();
        m_search_learn_vm.registerUser(inputText, selectedDeptName);

        this.m_search_learn_vm.refreshUserList(selectedDeptName);

        m_input_text.clear();

        // 削除後に検索テキストアイテムをクリアして全件表示に戻す
        m_search_text.clear();
    }

    /**
     * 削除ボタン押下時イベント
     */
    @FXML
    protected void onDeleteButtonClick() {
        String selectedName = m_user_list_view.getSelectionModel().getSelectedItem();
        String selectedDeptName = m_dept_combo_box.getSelectionModel().getSelectedItem();
        if (selectedName != null) {
            m_search_learn_vm.deleteUser(selectedName);

            this.m_search_learn_vm.refreshUserList(selectedDeptName);
            // 削除後に検索テキストアイテムをクリアして全件表示に戻す
            m_search_text.clear();
        }
    }
}