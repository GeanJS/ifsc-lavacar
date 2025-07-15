package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author J
 */
public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {
    
    @FXML
    private TextField tfDescricao;
    
    @FXML
    private TextField tfValor;
    
    @FXML
    private TextField tfPontos;
    
    @FXML
    private ComboBox<ECategoria> cbCategoria;
    
    @FXML
    private Button btConfirmar;
    
    @FXML
    private Button btCancelar;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Servico servico;
    
    
    
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);
        carregarComboBoxCategorias();
    }    
    
    public void carregarComboBoxCategorias() {
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        tfDescricao.setText(servico.getDescricao());
        tfValor.setText(Double.toString(servico.getValor()));
        tfPontos.setText(Integer.toString(Servico.getPontos()));
        cbCategoria.getSelectionModel().select(servico.geteCategoria());
    }

    @FXML
    private void handleBtConfirmar(ActionEvent event) {
        if (validarEntradadeDados()) {
            servico.setDescricao(tfDescricao.getText());
            servico.setValor(Double.valueOf(tfValor.getText()));
            servico.setPontos(Integer.valueOf(tfPontos.getText()));
            servico.seteCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    private boolean validarEntradadeDados() {
        String errorMessage = "";
        if (this.tfDescricao == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Não deu";
        }

        if (this.tfValor == null || this.tfValor.getText().length() == 0) {
            errorMessage += "Não deu";
        }

        if (this.tfPontos == null || this.tfPontos.getText().length() == 0) {
            errorMessage += "Não deu";
        }

        if (this.cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Não deu";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }

    }
    
    
    
    
    
    
}
