package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exceptions.DAOException;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Geanj
 */
public class ServicoDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Servico servico) {
        final String sql = "INSERT INTO servico(descricao, valor, ponto, categoria) VALUES(?,?,?,?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setInt(3, Servico.getPontos()); //altereir algo aquir !!!! nao sei oq
            stmt.setString(4, servico.geteCategoria().name());
            stmt.execute();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Servico servico) {
        String sql = "UPDATE servico SET descricao=?, valor=?, ponto=?, categoria=? WHERE "
                + "id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setInt(3, servico.getPontos());
            stmt.setString(4, servico.geteCategoria().name());
            stmt.setInt(5, servico.getId());
            stmt.execute();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Servico> listar(Servico servico) {
        String sql = "SELECT * FROM servico WHERE id = ?;";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                servico = populateVO(resultado);
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Servico> listar() {
        String sql = "SELECT * FROM servico";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Servico servico = new Servico();
                servico = populateVO(resultado);
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Servico buscar(Servico servico) {
        String sql = "SELECT * FROM servico WHERE id = ?;";
        Servico retorno = new Servico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Servico populateVO(ResultSet rs) throws SQLException {
        Servico servico = new Servico();
        servico.setId(rs.getInt("id"));
        servico.setDescricao(rs.getString("descricao"));
        servico.setValor(rs.getDouble("valor"));
        servico.setPontos(rs.getInt("ponto"));
        servico.seteCategoria(Enum.valueOf(ECategoria.class, rs.getString("categoria")));

        return servico;
    }

    public boolean remover(Servico servico) throws DAOException {
        String sql = "DELETE FROM servico WHERE id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
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

}

