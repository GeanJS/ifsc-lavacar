package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Pontuacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Geanj
 */
public class PontuacaoDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Pontuacao pontuacao) throws SQLException  {
       String sql = "INSERT INTO pontuacao(id_cliente) VALUES(?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pontuacao.getCliente().getId());
            stmt.execute();
    }

    
    public void alterar(Pontuacao pontuacao) throws SQLException {
        String sql = "UPDATE pontuacao SET quantidade=? WHERE id_cliente=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, pontuacao.getQuantidade());
        stmt.setInt(2, pontuacao.getCliente().getId());
        stmt.execute();
    }

    public void remover(Pontuacao pontuacao) throws SQLException {
        String sql = "DELETE FROM pontuacao WHERE id_cliente=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, pontuacao.getCliente().getId());
        stmt.execute();
    }
    
    public List<Pontuacao> listar() throws SQLException{
        String sql = "SELECT * FROM pontuacao";
        List<Pontuacao> retorno = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultado = stmt.executeQuery();
        while (resultado.next()) {
            Pontuacao pontuacao = new Pontuacao();
            pontuacao.getCliente().setId(resultado.getInt("id_cliente"));
            pontuacao.setQuantidade(resultado.getInt("quantidade"));
            retorno.add(pontuacao);
        }
        return retorno;
    }
    
    public int buscarQuantidade(Pontuacao pontuacao) throws SQLException {
        String sql = "SELECT quantidade FROM pontuacao WHERE id_cliente = ?";
        Pontuacao retorno = new Pontuacao();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, pontuacao.getCliente().getId());
        ResultSet resultado = stmt.executeQuery();
        if (resultado.next()) {
            retorno.setQuantidade(resultado.getInt("quantidade"));
        }
        return retorno.getQuantidade();
    }
}
