package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
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
public class ItemOSDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    
    public boolean inserir(ItemOS itemOS) throws SQLException{
    String sql = "INSERT INTO item_os(valor_servico, observacao, id_servico, id_ordemServico) VALUES(?,?,?,?)";
    
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setBigDecimal(1, itemOS.getValorServico());
    stmt.setString(2, itemOS.getObservacao());
    stmt.setInt(3, itemOS.getServico().getId());
    stmt.setInt(4, itemOS.getOrdemServico().getNumero());

    stmt.execute();
    return false;
    }
    
    
    public boolean alterar(ItemOS itemOS) throws SQLException{
    String sql = "UPDATE item_os SET valor_servico = ?, observacao = ?, id_servico= ?, id_ordemServico = ? WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setBigDecimal(1, itemOS.getValorServico());
    stmt.setString(2, itemOS.getObservacao());
    stmt.setInt(3, itemOS.getServico().getId());
    stmt.setInt(4, itemOS.getOrdemServico().getNumero());
    stmt.setInt(5, itemOS.getId());
    stmt.execute();
    return false;
    }
    
    
    public boolean remover(ItemOS itemOS) throws SQLException{
        String sql = "DELETE FROM item_os WHERE id=?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, itemOS.getId());
        stmt.execute();
        return false;
    }
    
    
    public List<ItemOS> listar() throws SQLException{
        String sql = "SELECT * FROM item_os";
        List<ItemOS> retorno = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getBigDecimal("valor_servico"));
                itemOS.setObservacao(resultado.getString("observacao"));
                
                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setNumero(resultado.getInt("id_ordemServico"));
                
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);
                
                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico);
                
                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);
                
                retorno.add(itemOS);
            }
        return retorno;
    }
    
    
        public List<ItemOS> listarPorOrdemServico(OrdemServico ordemServico) throws SQLException{
        String sql = "SELECT * FROM item_os WHERE id_ordemServico=?";
        List<ItemOS> retorno = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, ordemServico.getNumero());
        ResultSet resultado = stmt.executeQuery();
        while (resultado.next()) {
            ItemOS itemOS = new ItemOS();
            Servico servico = new Servico();
            OrdemServico os = new OrdemServico();
            itemOS.setId(resultado.getInt("id"));
            itemOS.setValorServico(resultado.getBigDecimal("valor_servico"));
            itemOS.setObservacao(resultado.getString("observacao"));

            servico.setId(resultado.getInt("id_servico"));
            os.setNumero(resultado.getInt("id_ordemServico"));

            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(connection);
            servico = servicoDAO.buscar(servico);

            itemOS.setServico(servico);
            itemOS.setOrdemServico(os);

            retorno.add(itemOS);
        }
        return retorno;
    }
    
    
    public ItemOS buscar(ItemOS itemOS) throws SQLException{
    String sql = "SELECT * FROM item_os WHERE id=?";
    ItemOS retorno = new ItemOS();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, itemOS.getId());
        ResultSet resultado = stmt.executeQuery();
        if (resultado.next()) {
            Servico servico = new Servico();
            OrdemServico ordemServico = new OrdemServico();
            itemOS.setId(resultado.getInt("id"));
            itemOS.setValorServico(resultado.getBigDecimal("valor_servico"));
            itemOS.setObservacao(resultado.getString("observacao"));

            servico.setId(resultado.getInt("id_servico"));
            ordemServico.setNumero(resultado.getInt("id_ordemServico"));
            
            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(connection);
            servico = servicoDAO.buscar(servico);

            OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
            ordemServicoDAO.setConnection(connection);
            ordemServico = ordemServicoDAO.buscar(ordemServico);

            itemOS.setServico(servico);
            itemOS.setOrdemServico(ordemServico);

            retorno = itemOS;
        }
        return retorno;
    }
    
    
    public void alterarTodos(OrdemServico ordemServico) throws SQLException{
        String sql = "UPDATE item_os SET valor_servico = ?, observacao = ?, id_servico= ?, id_ordemServico = ? WHERE id = ?";
        for (ItemOS itemOS : ordemServico.getItensOS()) 
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, itemOS.getValorServico());
            stmt.setString(2, itemOS.getObservacao());
            stmt.setInt(3, itemOS.getServico().getId());
            stmt.setInt(4, itemOS.getOrdemServico().getNumero());
            stmt.setInt(5, itemOS.getId());
            stmt.execute();
        }
    }
    
    
}
