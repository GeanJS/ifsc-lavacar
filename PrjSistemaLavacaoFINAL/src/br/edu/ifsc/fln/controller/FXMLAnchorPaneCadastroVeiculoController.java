/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

    @FXML
    private TableView<Veiculo> tableView;

    @FXML
    private TableColumn<Veiculo, String> tableColumnModelo;
    
    @FXML
    private TableColumn<Veiculo, String> tableColumnPlaca;

    @FXML
    private Label lbVeiculoId;

    @FXML
    private Label lbVeiculoPlaca;
    
    @FXML
    private Label lbObservacoes;

    @FXML
    private Label lbVeiculoModelo;
    
    @FXML
    private Label lbVeiculoMarca;
    
    @FXML
    private Label lbVeiculoCor;
    
    @FXML
    private Label lbVeiculoCliente;

    @FXML
    private Button btInserir;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btRemover;

    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        marcaDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        clienteDAO.setConnection(connection);
        
        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableView() {
        listaVeiculos = veiculoDAO.listar();
        
        tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        
        Callback<TableColumn.CellDataFeatures<Veiculo, String>, ObservableValue<String>> modeloDescricaoCallback = param -> {
            Veiculo veiculo = param.getValue();
            String descricao = veiculo.getModelo().getDescricao();
            return new SimpleStringProperty(descricao);
        };
        
        tableColumnModelo.setCellValueFactory(modeloDescricaoCallback);
        
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableView.setItems(observableListVeiculos);
    }
    
    public void selecionarItemTableView(Veiculo veiculo) {
        if (veiculo != null) {
            lbVeiculoId.setText(Integer.toString(veiculo.getId()));
            lbVeiculoPlaca.setText(veiculo.getPlaca());
            lbVeiculoMarca.setText(veiculo.getModelo().getMarca().getNome());
            lbObservacoes.setText(veiculo.getObservacoes());
            lbVeiculoCor.setText(veiculo.getCor().getNome());
            lbVeiculoModelo.setText(veiculo.getModelo().getDescricao());
            lbVeiculoCliente.setText(veiculo.getCliente().getNome());
        } else {
            lbVeiculoId.setText("");
            lbVeiculoPlaca.setText("");
            lbVeiculoMarca.setText("");
            lbObservacoes.setText("");
            lbVeiculoCor.setText("");
            lbVeiculoModelo.setText("");
        }
    }
    

    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo veiculo = new Veiculo();

        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosVeiculosDialog(veiculo);
        if (buttonConfirmarClicked) {
            veiculoDAO.inserir(veiculo);
            carregarTableView();
        }
    }
    
    @FXML
    public void handleBtAlterar() throws IOException {
        Veiculo veiculo = tableView.getSelectionModel().getSelectedItem();
        
        if (veiculo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosVeiculosDialog(veiculo);
            if (buttonConfirmarClicked) {
                veiculoDAO.alterar(veiculo);
               
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtRemover() throws DAOException {
        Veiculo veiculo = tableView.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            // Mostrar um alerta de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir este Veiculo?");

            // Configurar botões do alerta
            ButtonType buttonTypeSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            // Mostrar o alerta e processar a resposta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSim) {
                // Se o usuário clicou em "Sim", então remover o Serviço
                try {
                    veiculoDAO.setConnection(connection);
                    veiculoDAO.remover(veiculo);
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
            alert.setHeaderText("Por favor, escolha um veiculo na tabela!");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosVeiculosDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoDialogController.class.getResource( 
            "../view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de veiculos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o veiculo ao controller
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);
        
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }


}
