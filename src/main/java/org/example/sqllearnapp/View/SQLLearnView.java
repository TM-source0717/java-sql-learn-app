package org.example.sqllearnapp.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.sqllearnapp.ViewModel.SQLLearnViewModel;

public class SQLLearnView {

    // ユーザーの表示部分
    @FXML
    private ListView<String> m_user_list_view;

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
        // リストのバインド
        m_user_list_view.setItems(m_search_learn_vm.getUserList());

        // 検索テキスト入力イベントをリアルタイムに監視
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
    }

    /**
     * 登録ボタン押下時イベント
     */
    @FXML
    protected void onRegisterButtonClick() {
        String inputText = m_input_text.getText();
        m_search_learn_vm.registerUser(inputText);
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
        if (selectedName != null) {
            m_search_learn_vm.deleteUser(selectedName);

            // 削除後に検索テキストアイテムをクリアして全件表示に戻す
            m_search_text.clear();
        }
    }
}