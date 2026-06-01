package org.example.sqllearnapp.View;

import javafx.fxml.FXML;
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

    private final SQLLearnViewModel m_search_learn_vm;

    /**
     * コンストラクタ
     */
    public SQLLearnView(SQLLearnViewModel serach_learn_vm) {
        this.m_search_learn_vm = serach_learn_vm;
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
            // 文字が1文字変わるたびに、ViewModelの検索ロジックを呼び出す
            m_search_learn_vm.searchItem(newValue);
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