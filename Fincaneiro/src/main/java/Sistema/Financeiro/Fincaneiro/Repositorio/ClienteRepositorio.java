package Sistema.Financeiro.Fincaneiro.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);

}
