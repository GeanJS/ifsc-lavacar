/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private TextField tfDescricao;

    @FXML
    private ComboBox<Marca> cbMarca;
    
    @FXML
    private ComboBox<ETipoCombustivel> cbCombustivel;
    
    @FXML
    private ComboBox<ECategoria> cbCategoria;
    
    @FXML
    private Spinner<Double> spPotencia;
    
    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;
        
    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Modelo modelo; 
    private Motor motor;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        carregarComboBoxMarcas();
        carregarComboBoxCombustivel();
        carregarComboBoxCategoria();
        valorSpinnerPotencia();
        setFocusLostHandle();
    } 
    
    private void setFocusLostHandle() {
        tfDescricao.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfDescricao.requestFocus();
                }
            }
        });
    }
    

    
    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;
    
    public void carregarComboBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas = 
                FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    }
    
    public void carregarComboBoxCombustivel() {
        cbCombustivel.setItems(FXCollections.observableArrayList(ETipoCombustivel.values()));
    }
    
    public void carregarComboBoxCategoria() {
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
    }
    public void valorSpinnerPotencia(){
        SpinnerValueFactory<Double> valueFactory = 
                new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 10.0, 1.0, 0.1);
        
        
    
        spPotencia.setValueFactory(valueFactory);
    }

    
    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        tfDescricao.setText(modelo.getDescricao());
        cbMarca.getSelectionModel().select(modelo.getMarca());
        modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
    }
    public void setMotor(Motor motor) {
        this.motor = motor;
        motor.setCombustivel(cbCombustivel.getSelectionModel().getSelectedItem());
        motor.setPotencia(spPotencia.getValue());
    }
    public Motor getMotor() {
        return motor;
    }
    
    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setMarca(
                    cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());

            motor.setCombustivel(cbCombustivel.getSelectionModel().getSelectedItem());
            motor.setPotencia(spPotencia.getValue());
            
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }
    
        //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }

        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma marca!\n";
        }
        
        if (cbCombustivel.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um tipo de combustivel!\n";
        }
        
        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
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
