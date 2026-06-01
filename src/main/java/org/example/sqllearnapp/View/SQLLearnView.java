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
        m_search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            m_search_learn_vm.searchItem(newValue);
        });

        // リストの選択変更イベントをリアルタイムに監視（新規追加）
        m_user_list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // 行が選択されたら、その名前をラベルにセット
                m_selected_name_label.setText(newSelection);
            } else {
                // 選択が解除されたら初期状態に戻す
                m_selected_name_label.setText("選択されていません");
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