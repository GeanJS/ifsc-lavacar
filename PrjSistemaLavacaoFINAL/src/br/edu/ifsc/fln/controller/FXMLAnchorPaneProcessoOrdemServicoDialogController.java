package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.math.BigDecimal;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author J
 */
public class FXMLAnchorPaneProcessoOrdemServicoDialogController implements Initializable {

    @FXML
    private ComboBox<Servico> cbServico;

    @FXML
    private ComboBox<EStatus> cbStatus;

    @FXML
    private ComboBox<Veiculo> cbVeiculo;
    
    @FXML
    private Button btAdicionar;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private DatePicker dpAgenda;

    @FXML
    private TableColumn<ItemOS, String> tableColumnObservacao;

    @FXML
    private TableColumn<ItemOS, Servico> tableColumnServico;

    @FXML
    private TableColumn<ItemOS, BigDecimal> tableColumnValor;

    @FXML
    private TableView<ItemOS> tableView;

    @FXML
    private TextField tfCliente;

    @FXML
    private TextField tfDesconto;

    @FXML
    private TextField tfObservacao;

    @FXML
    private TextField tfValor;
    
    @FXML
    private MenuItem contextMenuItemAtualizarObs;

    @FXML
    private MenuItem contextMenuItemRemoverItem;

    private List<Veiculo> listaVeiculo;
    private List<Servico> listaServico;
    private ObservableList<Veiculo> observableListVeiculo;
    private ObservableList<Servico> observableListServico;
    private ObservableList<ItemOS> observableListItemOS;
    
    //acesso ao banco
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;
    
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        try {
            carregarComboBoxVeiculo();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAnchorPaneProcessoOrdemServicoDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        carregarComboBoxServico();
        carregarComboBoxStatus();
        
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));
    }    
    
    
    private void carregarComboBoxVeiculo() throws SQLException {
        
        listaVeiculo = veiculoDAO.listar();
        observableListVeiculo = FXCollections.observableArrayList(listaVeiculo);
        cbVeiculo.setItems(observableListVeiculo);
        
        cbVeiculo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tfCliente.setText(newValue.getCliente().getNome());
            }
        });
    }
    
    
    
    private void carregarComboBoxServico() {
        
        listaServico = servicoDAO.listar();
        observableListServico = FXCollections.observableArrayList(listaServico);
        cbServico.setItems(observableListServico);
    }
    
    
    
    public void carregarComboBoxStatus() {
        cbStatus.setItems(FXCollections.observableArrayList(EStatus.values()));
    }
    
    
    
    public Stage getDialogStage() {
        return dialogStage;
    }
    
    
    
    //nao entendi essas

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }
    
    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
        if (ordemServico.getNumero()!= 0) { 
            cbVeiculo.getSelectionModel().select(this.ordemServico.getVeiculo());
            dpAgenda.setValue(this.ordemServico.getAgenda());
            observableListItemOS = FXCollections.observableArrayList(
                    this.ordemServico.getItensOS());
            tableView.setItems(observableListItemOS);
            tfValor.setText(String.format("%.2f", this.ordemServico.getTotal()));
//            tfDesconto.setText(String.format("%.2f", this.ordemServico.getTaxaDesconto()));
            
            cbStatus.getSelectionModel().select(this.ordemServico.getStatus());
        }
    }
    
    
    
    @FXML
    public void handleBtAdicionar() {
        Servico servico;
        ItemOS itemOS = new ItemOS();
        if (cbServico.getSelectionModel().getSelectedItem() != null) {

            servico = cbServico.getSelectionModel().getSelectedItem();
            servico = servicoDAO.buscar(servico);
            
            itemOS.setServico(servico);
            itemOS.setObservacao(tfObservacao.getText());
            itemOS.setValorServico(BigDecimal.valueOf(servico.getValor()));
            itemOS.setOrdemServico(ordemServico);
            ordemServico.getItensOS().add(itemOS);
            observableListItemOS = FXCollections.observableArrayList(ordemServico.getItensOS());
            tableView.setItems(observableListItemOS);
            tfValor.setText(String.format("%.2f", ordemServico.getTotal()));
            
        }
    }
    
    
    
    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }
    
    
    @FXML
    void handleBtConfirmar(ActionEvent event) {
        ordemServico.setVeiculo(cbVeiculo.getSelectionModel().getSelectedItem());
        ordemServico.setAgenda(dpAgenda.getValue());
        ordemServico.setTaxaDesconto(Double.parseDouble(tfDesconto.getText()));
        ordemServico.setStatus((EStatus)cbStatus.getSelectionModel().getSelectedItem());
        ordemServico.setItensOS(observableListItemOS);
        buttonConfirmarClicked = true;
        dialogStage.close();
    }
    
    
    
    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {
        ItemOS itemOS
                = tableView.getSelectionModel().getSelectedItem();
        if (itemOS == null) {
            contextMenuItemAtualizarObs.setDisable(true);
            contextMenuItemRemoverItem.setDisable(true);
        } else {
            contextMenuItemAtualizarObs.setDisable(false);
            contextMenuItemRemoverItem.setDisable(false);
        }

    } 
    
    
    
    @FXML
    private void handleContextMenuItemAtualizarObs() {
        ItemOS itemOS = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        
        String obsAtualizada = inputDialog(itemOS.getObservacao());
        
        if (obsAtualizada != "Cancelado") {
            itemOS.setObservacao(obsAtualizada);
        }
        ordemServico.getItensOS().set(index, itemOS);
        tableView.refresh();
        tfValor.setText(String.format("%.2f", ordemServico.getTotal()));        
    }
    
    
    private String inputDialog(String value) {
        TextInputDialog dialog = new TextInputDialog(value);
        dialog.setTitle("Entrada de dados.");
        dialog.setHeaderText("Atualização do campo de observação");
        dialog.setContentText("Observação: ");

        Optional<String> result = dialog.showAndWait();
        
        if(result.isPresent()) {
            return result.get();
        } else {
            return "Cancelado";
        }
    }
    
    
    
    @FXML
    private void handleContextMenuItemRemoverItem() {
        ItemOS itemOS
                = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        ordemServico.getItensOS().remove(index);
        observableListItemOS = FXCollections.observableArrayList(ordemServico.getItensOS());
        tableView.setItems(observableListItemOS);

        tfValor.setText(String.format("%.2f", ordemServico.getTotal()));
    }
    
    
    
    
    
    
    
}
