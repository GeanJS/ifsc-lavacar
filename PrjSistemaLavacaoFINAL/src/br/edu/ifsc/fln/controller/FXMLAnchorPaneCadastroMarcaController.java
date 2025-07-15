/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Marca;
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
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroMarcaController implements Initializable {

    
    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbMarcaNome;

    @FXML
    private Label lbMarcaId;

    @FXML
    private TableColumn<Marca, String> tableColumnMarcaNome;

    @FXML
    private TableView<Marca> tableViewMarcas;
    
    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarTableViewMarca();
        
        tableViewMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewMarcas(newValue));
    }     
    
    public void carregarTableViewMarca() {
        tableColumnMarcaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        listaMarcas = marcaDAO.listar();
        
        observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        tableViewMarcas.setItems(observableListMarcas);
    }
    
    public void selecionarItemTableViewMarcas(Marca marca) {
        if (marca != null) {
            lbMarcaId.setText(String.valueOf(marca.getId())); 
            lbMarcaNome.setText(marca.getNome());
        } else {
            lbMarcaId.setText(""); 
            lbMarcaNome.setText("");
        }
        
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Marca marca = new Marca();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
        if (btConfirmarClicked) {
            marcaDAO.inserir(marca);
            carregarTableViewMarca();
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
            if (btConfirmarClicked) {
                marcaDAO.alterar(marca);
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws DAOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            // Mostrar um alerta de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir esta Marca?");

            // Configurar botões do alerta
            ButtonType buttonTypeSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            // Mostrar o alerta e processar a resposta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSim) {
                // Se o usuário clicou em "Sim", então remover a Marca
                try {
                    marcaDAO.remover(marca);
                } catch (DAOException ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro ao excluir Marca");
                    errorAlert.setHeaderText(ex.getMessage());
                    errorAlert.show();
                }
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }


    private boolean showFXMLAnchorPaneCadastroMarcaDialog(Marca marca) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroMarcaController.class.getResource("../view/FXMLAnchorPaneCadastroMarcaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Marca");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto marca para o controller
        FXMLAnchorPaneCadastroMarcaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMarca(marca);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
