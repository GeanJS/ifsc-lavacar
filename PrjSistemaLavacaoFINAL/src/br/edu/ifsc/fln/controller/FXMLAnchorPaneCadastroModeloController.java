/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroModeloController implements Initializable {

    @FXML
    private TableView<Modelo> tableView;

    @FXML
    private TableColumn<Modelo, String> tableColumnNome;

    @FXML
    private Label lbModeloId;

    @FXML
    private Label lbModeloDescricao;

    @FXML
    private Label lbModeloMarca;
    
    @FXML
    private Label lbModeloCombustivel;
    
    @FXML
    private Label lbModeloPotencia;
    
    @FXML
    private Label lbModeloCategoria;

    @FXML
    private Button btInserir;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btRemover;

    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        
        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableView() {
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listaModelos = modeloDAO.listar();
        
        
        observableListModelos = FXCollections.observableArrayList(listaModelos);
        tableView.setItems(observableListModelos);
    }
    
    public void selecionarItemTableView(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(Integer.toString(modelo.getId()));
            lbModeloDescricao.setText(modelo.getDescricao());
            lbModeloMarca.setText(modelo.getMarca().getNome());
            lbModeloCategoria.setText(modelo.getCategoria().name());
            lbModeloCombustivel.setText(modelo.getMotor().getCombustivel().name());
            lbModeloPotencia.setText(Double.toString(modelo.getMotor().getPotencia()));
        } else {
            lbModeloId.setText("");
            lbModeloDescricao.setText("");
            lbModeloMarca.setText("");
            lbModeloCombustivel.setText("");
            lbModeloPotencia.setText("");
            lbModeloCategoria.setText("");
        }
    }
    

    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
        if (buttonConfirmarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableView();
        }
    }
    
    @FXML
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableView.getSelectionModel().getSelectedItem();
       
        
        if (modelo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
            if (buttonConfirmarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtRemover() throws IOException {
        Modelo modelo = tableView.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            // Mostrar um alerta de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir este Modelo?");

            // Configurar botões do alerta
            ButtonType buttonTypeSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            // Mostrar o alerta e processar a resposta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSim) {
                // Se o usuário clicou em "Sim", então remover o Serviço
                try {
                    modeloDAO.setConnection(connection);
                    modeloDAO.remover(modelo);
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
            alert.setHeaderText("Por favor, escolha um modelo na tabela!");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosModelosDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloDialogController.class.getResource( 
            "../view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de modelos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o modelo ao controller
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);
        controller.setMotor(modelo.getMotor());
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }


}
