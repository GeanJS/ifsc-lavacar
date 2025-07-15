package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.PontuacaoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.domain.Pontuacao;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.control.ChoiceDialog;
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
public class FXMLAnchorPaneCadastroClienteController implements Initializable {
    
    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteCelular;

    @FXML
    private Label lbClienteDataCadastro;

    @FXML
    private Label lbClienteEmail;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteNome;

    @FXML
    private Label lbInscricaoEstadual;

    @FXML
    private Label lbRegistro;

    @FXML
    private Label lbTipoCliente;
    
    @FXML
    private Label lbDataNascimento;
    
    @FXML
    private TableColumn<Cliente, String> tableColumnCliente;

    @FXML
    private TableView<Cliente> tableViewCliente;

    private List<Cliente> listaCliente;
    private ObservableList<Cliente> observableListCliente;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        pontuacaoDAO.setConnection(connection);
        try {
            carregarTableViewCliente();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAnchorPaneCadastroClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tableViewCliente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCliente(newValue));
    }
    
    public void carregarTableViewCliente() throws SQLException {
        tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        listaCliente = clienteDAO.listar();
        
        observableListCliente = FXCollections.observableArrayList(listaCliente);
        tableViewCliente.setItems(observableListCliente);
    }
    
    public void selecionarItemTableViewCliente(Cliente cliente) {
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId())); 
            lbClienteNome.setText(cliente.getNome());
            lbClienteCelular.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            lbClienteDataCadastro.setText(String.valueOf(cliente.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            if (cliente instanceof PessoaFisica) {
                lbTipoCliente.setText("Pessoa Física");
                lbRegistro.setText(((PessoaFisica)cliente).getCpf());
                lbDataNascimento.setText(String.valueOf(
                    ((PessoaFisica) cliente).getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                lbInscricaoEstadual.setText("");
            } else {
                lbTipoCliente.setText("Pessoa Jurídica");
                lbRegistro.setText(((PessoaJuridica)cliente).getCnpj());
                lbDataNascimento.setText("");
                lbInscricaoEstadual.setText(((PessoaJuridica)cliente).getInscricaoEstadual());
            }
        } else {
            lbClienteId.setText(""); 
            lbClienteNome.setText("");
            lbClienteCelular.setText("");
            lbClienteEmail.setText("");
            lbClienteDataCadastro.setText("");
            lbTipoCliente.setText("");
            lbRegistro.setText("");
            lbDataNascimento.setText("");
            lbInscricaoEstadual.setText("");
        }
    }
    
    @FXML
    void handleBtAlterar(ActionEvent event) throws IOException, SQLException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtExcluir(ActionEvent event) throws DAOException, SQLException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            // Mostrar um alerta de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir o cliente " + cliente.getNome() + "?");

            // Configurar botões do alerta
            ButtonType buttonTypeSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            // Mostrar o alerta e processar a resposta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSim) {
                // Se o usuário clicou em "Sim", então remover o cliente
                clienteDAO.remover(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um cliente na tabela ao lado");
            alert.show();
        }
    }


    @FXML
    void handleBtInserir(ActionEvent event) throws IOException, SQLException {
        Cliente cliente = getTipoCliente();
        Pontuacao pontuacao = new Pontuacao();
        pontuacao.setQuantidade(0);
        
        cliente.setPontuacao(pontuacao);
        pontuacao.setCliente(cliente);
        
//        if (cliente != null ) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.inserir(cliente);
                cliente.getPontuacao().getCliente().setId(clienteDAO.getClienteAutoID(cliente));
                pontuacaoDAO.inserir(cliente.getPontuacao());
                
                carregarTableViewCliente();
            }
//        }
    }
    
    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Pessoa Física");
        opcoes.add("Pessoa Jurídica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Pessoa Física", opcoes);
        dialog.setTitle("Dialogo de Opções");
        dialog.setHeaderText("Escolha o tipo de cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("Pessoa Física")) 
                return new PessoaFisica();
            else 
                return new PessoaJuridica();
        } else {
            return null;
        }
    }
    
    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("../view/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto fornecedor para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
