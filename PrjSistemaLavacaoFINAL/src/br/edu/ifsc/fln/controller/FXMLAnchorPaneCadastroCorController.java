/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroCorController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbCorId;

    @FXML
    private Label lbCorNome;

    @FXML
    private TableColumn<Cor, String> tableColumnCorNome;

    @FXML
    private TableView<Cor> tableViewCor;

    private List<Cor> listaCor;
    private ObservableList<Cor> observableListCor;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CorDAO corDAO = new CorDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        corDAO.setConnection(connection);
        carregarTableViewCor();

        tableViewCor.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCor(newValue));
    }

    public void carregarTableViewCor() {
        tableColumnCorNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        listaCor = corDAO.listar();

        observableListCor = FXCollections.observableArrayList(listaCor);
        tableViewCor.setItems(observableListCor);
    }

    public void selecionarItemTableViewCor(Cor cor) {
        if (cor != null) {
            lbCorId.setText(String.valueOf(cor.getId()));
            lbCorNome.setText(cor.getNome());
        } else {
            lbCorId.setText("");
            lbCorNome.setText("");
        }

    }

    @FXML
    public void handleBtInserir() throws IOException {
        Cor cor = new Cor();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
        if (btConfirmarClicked) {
            corDAO.inserir(cor);
            carregarTableViewCor();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Cor cor = tableViewCor.getSelectionModel().getSelectedItem();
        if (cor != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
            if (btConfirmarClicked) {
                corDAO.alterar(cor);
                carregarTableViewCor();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cor na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Cor cor = tableViewCor.getSelectionModel().getSelectedItem();
        if (cor != null) {
            // Mostrar um alerta de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir esta Cor?");

            // Configurar botões do alerta
            ButtonType buttonTypeSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            // Mostrar o alerta e processar a resposta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSim) {
                // Se o usuário clicou em "Sim", então remover a Cor
                try {
                    corDAO.remover(cor);
                } catch (DAOException ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Integridade referencial");
                    alert.setHeaderText(ex.getMessage());
                    alert.show();
                }
                carregarTableViewCor();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }


    private boolean showFXMLAnchorPaneCadastroCorDialog(Cor cor) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroCorController.class.getResource("../view/FXMLAnchorPaneCadastroCorDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cor");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto cor para o controller
        FXMLAnchorPaneCadastroCorDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCor(cor);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}
