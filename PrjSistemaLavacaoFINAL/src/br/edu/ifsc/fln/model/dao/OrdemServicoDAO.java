package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Geanj
 */
public class OrdemServicoDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    
    public void inserir(OrdemServico ordemServico) throws SQLException{
    String sql = "INSERT INTO ordem_servico(total, agenda, desconto, status, id_veiculo) VALUES(?,?,?,?,?)";
    
    PreparedStatement stmt = connection.prepareStatement(sql);
    connection.setAutoCommit(false);
    stmt.setBigDecimal(1, ordemServico.getTotal());
    stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
    stmt.setDouble(3, ordemServico.getTaxaDesconto());
    
    if(ordemServico.getStatus() != null) {
        stmt.setString(4, ordemServico.getStatus().name());
    } else {
        stmt.setString(5, EStatus.ABERTA.name());
    }
    
    
    stmt.setInt(5, ordemServico.getVeiculo().getId());
    stmt.execute();
    

    ItemOSDAO itemOSDAO = new ItemOSDAO();
    itemOSDAO.setConnection(connection);
    

    ServicoDAO servicoDAO = new ServicoDAO();
    servicoDAO.setConnection(connection);
    

    int quantidade = 0;
    for (ItemOS itemOS: ordemServico.getItensOS()) {
        Servico servico = itemOS.getServico();
        itemOS.setOrdemServico(this.buscarUltimaOrdemServico());
        itemOSDAO.inserir(itemOS);
        quantidade += servico.getPontos();
    }
    
    
    PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
    pontuacaoDAO.setConnection(connection);
    ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(quantidade + pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao()));
    pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
    connection.commit();
    connection.setAutoCommit(true);
    }
    
    
    public boolean alterar(OrdemServico ordemServico) throws SQLException{
        String sql = "UPDATE ordem_servico SET total=?, agenda=?, desconto=?, id_veiculo=?, status=? WHERE numero=?";

        connection.setAutoCommit(false);
        ItemOSDAO itemOSDAO = new ItemOSDAO();
        itemOSDAO.setConnection(connection);
        PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
        pontuacaoDAO.setConnection(connection);

        OrdemServico ordemServicoAnterior = buscar(ordemServico);
        List<ItemOS> itemOS = itemOSDAO.listarPorOrdemServico(ordemServico);

        int quantidadeAnterior = 0;
        for(ItemOS iv : itemOS) {
            itemOSDAO.remover(iv);
            quantidadeAnterior += iv.getServico().getPontos();
        }
        int quantidade = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());

        ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(quantidade);
        ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(quantidadeAnterior);
        pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
        
        //atualiza os dados da venda
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setBigDecimal(1, ordemServico.getTotal());
        stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
        stmt.setDouble(3, ordemServico.getTaxaDesconto());
        stmt.setInt(4, ordemServico.getVeiculo().getId());

        if  (ordemServico.getStatus()!= null) {
            stmt.setString(5, ordemServico.getStatus().name());
        } else {
            stmt.setString(5, EStatus.ABERTA.name());
        }
        stmt.setInt(6, ordemServico.getNumero());
        stmt.execute();

        int quantidadeAtual = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());
        for (ItemOS iv: ordemServico.getItensOS()) {

            itemOSDAO.inserir(iv);
            quantidadeAtual += iv.getServico().getPontos();
        }
        
        ordemServico.getVeiculo().getCliente().getPontuacao().adicionar(quantidadeAtual);
        pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
        connection.commit();
        return true;
    }
    
    public void remover(OrdemServico ordemServico) throws SQLException{
    String sql = "DELETE FROM ordem_servico WHERE numero=?";
    
    PreparedStatement stmt = connection.prepareStatement(sql);
    connection.setAutoCommit(false);
    ItemOSDAO itemOSDAO = new ItemOSDAO();
    itemOSDAO.setConnection(connection);

    ServicoDAO servicoDAO = new ServicoDAO();
    servicoDAO.setConnection(connection);

    int quantidade = 0;                
    for (ItemOS iv : ordemServico.getItensOS()) {
        itemOSDAO.remover(iv);
        quantidade += iv.getServico().getPontos();
    }
    PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
    pontuacaoDAO.setConnection(connection);
    ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao()));
    ordemServico.getVeiculo().getCliente().getPontuacao().subtrair(quantidade);
    pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
    stmt.setInt(1, ordemServico.getNumero());
    stmt.execute();
    connection.commit();
    }
    
    
    public List<OrdemServico> listar() throws SQLException{
    String sql = "SELECT * FROM ordem_servico";
    List<OrdemServico> retorno = new ArrayList<>();
    PreparedStatement stmt = connection.prepareStatement(sql);
    ResultSet resultado = stmt.executeQuery();
    while (resultado.next()) {
        OrdemServico ordemServico = new OrdemServico();
        Veiculo veiculo = new Veiculo();
        List<ItemOS> itemOS = new ArrayList();

        ordemServico.setNumero(resultado.getInt("numero"));
        ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
        ordemServico.setTaxaDesconto(resultado.getDouble("desconto"));
        ordemServico.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
        veiculo.setId(resultado.getInt("id_veiculo"));

        //Obtendo os dados completos do Cliente associado à Venda
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        veiculoDAO.setConnection(connection);
        veiculo = veiculoDAO.buscar(veiculo);

        //Obtendo os dados completos dos Itens de Venda associados à Venda
        ItemOSDAO itemOSDAO = new ItemOSDAO();
        itemOSDAO.setConnection(connection);
        itemOS = itemOSDAO.listarPorOrdemServico(ordemServico);

        ordemServico.setVeiculo(veiculo);
        ordemServico.setItensOS(itemOS);
        retorno.add(ordemServico);
    }
    return retorno;
    }
    
    
    public OrdemServico buscar(OrdemServico ordemServico) throws SQLException{
    String sql = "SELECT * FROM ordem_servico WHERE numero=?";
    OrdemServico ordemServicoRetorno = new OrdemServico();
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, ordemServico.getNumero());
    ResultSet resultado = stmt.executeQuery();
    if (resultado.next()) {
        Veiculo veiculo = new Veiculo();
        ordemServicoRetorno.setNumero(resultado.getInt("numero"));
        ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
        ordemServicoRetorno.setTaxaDesconto(resultado.getDouble("desconto"));
        ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
        veiculo.setId(resultado.getInt("id_veiculo"));
        ordemServicoRetorno.setVeiculo(veiculo);
    }
    return ordemServicoRetorno;
    }
    
    
    public OrdemServico buscar(int id) throws SQLException{
    /*
        Método necessário para evitar que a instância de retorno seja 
        igual a instância a ser atualizada.
    */
    String sql = "SELECT * FROM ordem_servico WHERE numero=?";
    OrdemServico ordemServicoRetorno = new OrdemServico();
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, id);
    ResultSet resultado = stmt.executeQuery();
    if (resultado.next()) {
        Veiculo veiculo = new Veiculo();
        ordemServicoRetorno.setNumero(resultado.getInt("numero"));
        ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
        ordemServicoRetorno.setTaxaDesconto(resultado.getDouble("desconto"));
        ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
        veiculo.setId(resultado.getInt("id_veiculo"));
        ordemServicoRetorno.setVeiculo(veiculo);
        }
    return ordemServicoRetorno;
    } 
    
    
    public OrdemServico buscarUltimaOrdemServico() throws SQLException{
    String sql = "SELECT max(numero) as max FROM ordem_servico";

    OrdemServico retorno = new OrdemServico();
    PreparedStatement stmt = connection.prepareStatement(sql);
    ResultSet resultado = stmt.executeQuery();

    if (resultado.next()) {
        retorno.setNumero(resultado.getInt("max"));
        return retorno;
    }
    return retorno;
    }
    
    
    
}
