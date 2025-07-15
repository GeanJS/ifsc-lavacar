package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VeiculoDAO{

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setInt(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa=?, observacoes=?, id_modelo=?, id_cor=?, id_cliente WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setInt(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Veiculo veiculo) throws DAOException {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();

            return true;

        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não é possível remover este serviço por integridade referencial. Ele pode estar sendo usado por outro objeto");
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não é possível remover este serviço. ");
        }
    }

    public List<Veiculo> listar() {
        String sql =  "SELECT v.id as veiculo_id, v.placa as veiculo_placa, v.observacoes as veiculo_observacoes, "
                + "m.id as modelo_id, m.descricao as modelo_descricao, "
                + "c.id as cor_id, c.nome as cor_nome, "
                + "ma.id as marca_id, ma.nome as marca_nome, "
                + "cl.id as cliente_id "
                + "FROM veiculo v "
                + "INNER JOIN modelo m ON m.id = v.id_modelo "
                + "INNER JOIN cor c ON c.id = v.id_cor "
                + "INNER JOIN marca ma ON ma.id = m.id_marca "
                + "INNER JOIN cliente cl ON cl.id = v.id_cliente;";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public List<Veiculo> listarPorModelo(Modelo modelo) {
        String sql =  "SELECT v.id as veiculo_id, v.placa as veiculo_placa, v.observacoes as veiculo_observacoes,  "
                + "m.id as modelo_id, m.descricao as modelo_descricao,"
                + "c.id as cor_id, c.nome as cor_nome, "
                + "ma.id as marca_id, ma.nome as marca_nome "
                + "FROM veiculo v "
                + "INNER JOIN modelo m ON m.id = v.id_modelo "
                + "INNER JOIN cor c ON c.id = v.id_cor "
                + "INNER JOIN marca ma ON ma.id = m.id_marca WHERE m.id=?";
        
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Veiculo buscar(Veiculo veiculo) {
        String sql =  "SELECT v.id as veiculo_id, v.placa as veiculo_placa, v.observacoes as veiculo_observacoes, "
                + "m.id as modelo_id, m.descricao as modelo_descricao,"
                + "c.id as cor_id, c.nome as cor_nome, "
                + "ma.id as marca_id, ma.nome as marca_nome,"
                + "cl.id as cliente_id "
                + "FROM veiculo v "
                + "INNER JOIN modelo m ON m.id = v.id_modelo "
                + "INNER JOIN cor c ON c.id = v.id_cor "
                + "INNER JOIN marca ma ON ma.id = m.id_marca "
                + "INNER JOIN cliente cl ON cl.id = v.id_cliente WHERE v.id = ?;";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    private Veiculo populateVO(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        Modelo modelo = new Modelo();
        Marca marca = new Marca();
        Cor cor = new Cor();
        
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);
        veiculo.getModelo().setMarca(marca);

        

        veiculo.setId(rs.getInt("veiculo_id"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        veiculo.setObservacoes(rs.getString("veiculo_observacoes"));
        veiculo.getCor().setId(rs.getInt("cor_id"));
        veiculo.getCor().setNome(rs.getString("cor_nome"));
        veiculo.getModelo().getMarca().setId(rs.getInt("marca_id"));
        veiculo.getModelo().getMarca().setNome(rs.getString("marca_nome"));
        veiculo.getModelo().setId(rs.getInt("modelo_id"));
        veiculo.getModelo().setDescricao(rs.getString("modelo_descricao"));

        int idCliente = rs.getInt("cliente_id");
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.setConnection(connection);
        Cliente cliente = clienteDAO.buscar(idCliente);
        veiculo.setCliente(cliente);

        return veiculo;
    }    
}
