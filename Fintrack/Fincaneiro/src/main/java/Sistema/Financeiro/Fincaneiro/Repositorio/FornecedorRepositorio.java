package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;

@Repository
public interface FornecedorRepositorio extends JpaRepository<Fornecedor, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    List<Fornecedor> findByAtivo(boolean b);

    Fornecedor findByIdAndUsuario(Long id, Usuario usuario);

    List<Fornecedor> findByUsuario(Usuario usuario);

    List<Fornecedor> findByUsuarioAndAtivo(Usuario usuario, boolean ativa);

    boolean existsByCpfAndUsuario(String cpf, Usuario usuario);

    boolean existsByCpfAndUsuarioAndIdNot(String cpf, Usuario usuario, Long idFornecedor);

    boolean existsByCnpjAndUsuario(String cnpj, Usuario usuario);

    boolean existsByCnpjAndUsuarioAndIdNot(String cnpj, Usuario usuario, Long idFornecedor);

    Long countByUsuario(Usuario usuarioLogado);

    Long countByUsuarioAndAtivoTrue(Usuario usuarioLogado);
}
