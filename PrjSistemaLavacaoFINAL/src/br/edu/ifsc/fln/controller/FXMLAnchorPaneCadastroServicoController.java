package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Servico;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
 * @author Geanj
 */
public class FXMLAnchorPaneCadastroServicoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbServicoDescricao;

    @FXML
    private Label lbServicoId;

    @FXML
    private Label lbServicoPontos;

    @FXML
    private Label lbServicoValor;
    
    @FXML
    private Label lbServicoCategoria;
    
    @FXML
    private TableColumn<Servico, String> tableColumnServico;

    @FXML
    private TableView<Servico> tableViewServico;
    
    private List<Servico> listaServico;
    private ObservableList<Servico> observableListServico;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);

        try {
            carregarTableView();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAnchorPaneCadastroServicoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableViewServico.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }    
    
    public void carregarTableView() throws SQLException {
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        listaServico = servicoDAO.listar();
        observableListServico = FXCollections.observableArrayList(listaServico);
        tableViewServico.setItems(observableListServico);
    }
    
    public void selecionarItemTableView(Servico servico) {
        if (servico != null) {
            lbServicoId.setText(Integer.toString(servico.getId()));
            lbServicoDescricao.setText(servico.getDescricao());
            lbServicoValor.setText(Double.toString(servico.getValor()));
            lbServicoPontos.setText(Integer.toString(Servico.getPontos())); //alterei alguma coisa aqui!!!!!
            lbServicoCategoria.setText(servico.geteCategoria().name());
        } else {
            lbServicoId.setText("");
            lbServicoDescricao.setText("");
            lbServicoValor.setText("");
            lbServicoPontos.setText("");
            lbServicoCategoria.setText("");
        }
    }
    
    @FXML
    void handleBtAlterar(ActionEvent event) throws IOException, SQLException {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
            if (buttonConfirmarClicked) {
                servicoDAO.alterar(servico);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um venda na Tabela.");
            alert.show();
        }        
    }

    @FXML
    void handleBtExcluir(ActionEvent event) throws SQLException {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            // Mostrar um alerta de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir este Serviço?");

            // Configurar botões do alerta
            ButtonType buttonTypeSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            // Mostrar o alerta e processar a resposta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSim) {
                // Se o usuário clicou em "Sim", então remover o Serviço
                try {
                    servicoDAO.setConnection(connection);
                    servicoDAO.remover(servico);
                    carregarTableView();
                } catch (DAOException ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Integridade referencial");
                    errorAlert.setHeaderText(ex.getMessage());
                    errorAlert.show();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha um serviço na tabela!");
            alert.show();
        }
    }


    @FXML
    void handleBtInserir(ActionEvent event) throws IOException, SQLException {
        Servico servico = new Servico();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
        if (buttonConfirmarClicked) {
            servicoDAO.setConnection(connection);
            servicoDAO.inserir(servico);
            carregarTableView();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastroServicoDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoDialogController.class.getResource(
                "../view/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de serviços");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o venda ao controller
        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
    
    
    
    
}
