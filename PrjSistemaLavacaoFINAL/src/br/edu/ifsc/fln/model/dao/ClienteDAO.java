package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
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
public class ClienteDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public boolean inserir(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO cliente(nome, telefone, email, data_cadastro) VALUES(?, ?, ?, ?)";
        String sqlPF = "INSERT INTO pessoafisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), ?, ?)";
        String sqlPJ = "INSERT INTO pessoajuridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), ?, ?)";
        
        //armazena os dados da superclasse
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getCelular());
        stmt.setString(3, cliente.getEmail());
        stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
        stmt.execute();
        
        //armazena os dados da subclasse
        if (cliente instanceof PessoaFisica) {
            stmt = connection.prepareStatement(sqlPF);
            stmt.setString(1, ((PessoaFisica)cliente).getCpf());
            stmt.setDate(2, java.sql.Date.valueOf(((PessoaFisica)cliente).getDataNascimento()));
            stmt.execute();
        } else {
            stmt = connection.prepareStatement(sqlPJ);
            stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
            stmt.setString(2, ((PessoaJuridica)cliente).getInscricaoEstadual());
            stmt.execute();
        }
        return false;
    }
    
    
    public boolean alterar(Cliente cliente) throws SQLException{ 
        String sql = "UPDATE cliente SET nome=?, celular=?, email=?, data_cadastro=? WHERE id=?";
        String sqlPF = "UPDATE pessoafisica SET cpf=?, data_nascimento=? WHERE id_cliente = ?";
        String sqlPJ = "UPDATE pessoajuridica SET cnpj=?, inscricao_estadual=? WHERE id_cliente = ?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getCelular());
        stmt.setString(3, cliente.getEmail());
        stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
        stmt.setInt(5, cliente.getId());
        stmt.execute();
        
        if (cliente instanceof PessoaFisica) {
            stmt = connection.prepareStatement(sqlPF);
            stmt.setString(1, ((PessoaFisica)cliente).getCpf());
            stmt.setDate(2, java.sql.Date.valueOf(((PessoaFisica)cliente).getDataNascimento()));
            stmt.setInt(3, cliente.getId());
            stmt.execute();
        } else {
            stmt = connection.prepareStatement(sqlPJ);
            stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
            stmt.setString(2, ((PessoaJuridica)cliente).getInscricaoEstadual());
            stmt.setInt(3, cliente.getId());
            stmt.execute();
        }
        return false;
    }
    
    
    public boolean remover(Cliente cliente) throws SQLException{
        String sql = "DELETE FROM cliente WHERE id=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, cliente.getId());
        stmt.execute();
        return false;
    }
    
    
    public List<Cliente> listar() throws SQLException{
        String sql = "SELECT * FROM cliente c "
                        + "LEFT JOIN pessoafisica pf on pf.id_cliente = c.id "
                        + "LEFT JOIN pessoajuridica pj on pj.id_cliente = c.id;";
        List<Cliente> retorno = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultado = stmt.executeQuery();
        while (resultado.next()) {
            Cliente cliente = populateVO(resultado);
            retorno.add(cliente);
        }
        return retorno;
    }
    
    
    public Cliente buscar(Cliente cliente) throws SQLException{
        String sql = "SELECT * FROM cliente c "
                        + "LEFT JOIN pessoafisica pf on pf.id_cliente = c.id "
                        + "LEFT JOIN pessoajuridica pj on pj.id_cliente = c.id WHERE id=?";
        Cliente retorno = null;
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, cliente.getId());
        ResultSet resultado = stmt.executeQuery();
        if (resultado.next()) {
            retorno = populateVO(resultado);
        }
        return retorno;
    }
    
    
    public Cliente buscar(int id) throws SQLException {
    String sql = "SELECT * FROM cliente c "
                + "LEFT JOIN pessoafisica pf on pf.id_cliente = c.id "
                + "LEFT JOIN pessoajuridica pj on pj.id_cliente = c.id WHERE id=?";
    Cliente retorno = null;
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet resultado = stmt.executeQuery();
        if (resultado.next()) {
            retorno = populateVO(resultado);
        }
    return retorno;
    } 
    
    
    private Cliente populateVO(ResultSet rs) throws SQLException {
    Cliente cliente;
    if (rs.getString("cnpj") == null || rs.getString("cnpj").length() <= 0) {
        //é uma pessoa fisica
        cliente = new PessoaFisica();
        ((PessoaFisica)cliente).setCpf(rs.getString("cpf"));
        ((PessoaFisica)cliente).setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
    } else {
        //é uma pessoa juridica
        cliente = new PessoaJuridica();
        ((PessoaJuridica)cliente).setCnpj(rs.getString("cnpj"));
        ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("inscricao_estadual"));
    }
    cliente.setId(rs.getInt("id"));
    cliente.setNome(rs.getString("nome"));
    cliente.setCelular(rs.getString("telefone"));
    cliente.setEmail(rs.getString("email"));
    cliente.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());
    return cliente;
    }
    
    
    public int getClienteAutoID(Cliente cliente) throws SQLException{
    String sql1= "SELECT max(id) as id FROM cliente";
    int id = 0;
        PreparedStatement stmt = connection.prepareStatement(sql1);
        ResultSet resultado = stmt.executeQuery();
        while (resultado.next()) {
        id = resultado.getInt("id");
        }
    return id;
    }
    
    
    
    
}
