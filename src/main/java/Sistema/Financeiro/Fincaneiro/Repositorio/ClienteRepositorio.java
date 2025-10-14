package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Sistema.Financeiro.Fincaneiro.DTO.ClientesPorMesDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoClienteFornecedor;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);

    List<Cliente> findByAtivoTrue();

    Cliente findByIdAndUsuario(Long id, Usuario usuario);

    List<Cliente> findByUsuario(Usuario usuario);

    List<Cliente> findByUsuarioAndAtivo(Usuario usuario, boolean b);

    boolean existsByCpfAndUsuario(String cpf, Usuario usuario);

    boolean existsByCpfAndUsuarioAndIdNot(String cpf, Usuario usuario, Long idCliente);

    boolean existsByCnpjAndUsuario(String cnpj, Usuario usuario);

    boolean existsByCnpjAndUsuarioAndIdNot(String cnpj, Usuario usuario, Long idCliente);

    @Query("""
                SELECT new Sistema.Financeiro.Fincaneiro.DTO.ClientesPorMesDTO(
                    EXTRACT(YEAR FROM c.dataCadastro),
                    EXTRACT(MONTH FROM c.dataCadastro),
                    COUNT(c)
                )
                FROM Cliente c
                WHERE c.usuario.id = :id
                  AND c.dataCadastro BETWEEN :inicio AND :hoje
                GROUP BY EXTRACT(YEAR FROM c.dataCadastro), EXTRACT(MONTH FROM c.dataCadastro)
                ORDER BY EXTRACT(YEAR FROM c.dataCadastro), EXTRACT(MONTH FROM c.dataCadastro)
            """)
    List<ClientesPorMesDTO> findClientesPorMes(@Param("id") long id,
            @Param("inicio") LocalDate inicio,
            @Param("hoje") LocalDate hoje);

    long countByUsuarioAndTipo(Usuario usuario, TipoClienteFornecedor string);

    Long countByUsuarioAndAtivoTrue(Usuario usuarioLogado);

}
