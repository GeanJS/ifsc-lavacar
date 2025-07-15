package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author J
 */
public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {
  
    
     @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private DatePicker dpDataCadastro;

    @FXML
    private DatePicker dpDataNascimento;

    @FXML
    private RadioButton rbPessoaFisica;

    @FXML
    private RadioButton rbPessoaJuridica;

    @FXML
    private TextField tfCelular;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfInscricaoEstadual;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfRegistro;
    
     @FXML
    private Group gpTipo;


    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

        this.tfNome.setText(this.cliente.getNome());
        this.tfCelular.setText(this.cliente.getCelular());
        this.tfEmail.setText(this.cliente.getEmail());
        this.dpDataCadastro.setValue(this.cliente.getDataCadastro());
        this.gpTipo.setDisable(true);
        
        //habilita para pessoa fisica ou juridica
        if (cliente instanceof PessoaFisica) {
            rbPessoaFisica.setSelected(true);
            tfRegistro.setText(((PessoaFisica) this.cliente).getCpf());
            dpDataNascimento.setValue(((PessoaFisica) this.cliente).getDataNascimento());
            dpDataNascimento.setDisable(false);
            tfInscricaoEstadual.setText("");
            tfInscricaoEstadual.setDisable(true);
        } else {
            rbPessoaJuridica.setSelected(true);
            tfInscricaoEstadual.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
            tfInscricaoEstadual.setDisable(false);
            dpDataNascimento.setValue(null);
            dpDataNascimento.setDisable(true);
        }
        this.tfNome.requestFocus();
    }
    
    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());
            cliente.setCelular(tfCelular.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setDataCadastro(dpDataCadastro.getValue());
            if (rbPessoaFisica.isSelected()) {
                ((PessoaFisica) cliente).setCpf(tfRegistro.getText());
                ((PessoaFisica) cliente).setDataNascimento(dpDataNascimento.getValue());
            } else {
                ((PessoaJuridica) cliente).setCnpj(tfRegistro.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfInscricaoEstadual.getText());
            }
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    void handleRbPessoaFisica(ActionEvent event) {
        this.tfInscricaoEstadual.setText("");
        this.tfInscricaoEstadual.setDisable(true);
    }

    @FXML
    void handleRbPessoaJuridica(ActionEvent event) {
        this.tfInscricaoEstadual.setText("");
        this.tfInscricaoEstadual.setDisable(false);
        this.dpDataNascimento.setValue(null);
        this.dpDataNascimento.setDisable(true);
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Nome Inválido.\n";
        }
        
        if (this.tfCelular.getText() == null || this.tfCelular.getText().length() == 0) {
            errorMessage += "Celular Inválido.\n";
        }
        
        if (this.tfEmail.getText() == null || this.tfEmail.getText().length() == 0) {
            errorMessage += "Email Inválido.\n";
        }
        
        
        if (this.dpDataCadastro.getValue() == null || this.dpDataCadastro.getValue().toString().length() == 0) {
            errorMessage += "Data de Cadastro Inválida.\n";
        }
        
        
        
        if (rbPessoaFisica.isSelected()) {
            
            if (this.tfRegistro.getText() == null || this.tfRegistro.getText().length() == 0) {
                errorMessage += "CPF Inválido.\n";
            }
        } else {
            if (this.tfRegistro.getText() == null || this.tfRegistro.getText().length() == 0) {
                errorMessage += "CNPJ inválido.\n";
            }
            if (this.tfInscricaoEstadual.getText() == null || this.tfInscricaoEstadual.getText().length() == 0) {
                errorMessage += "Informe a Incrição Estadual.\n";
            }
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
