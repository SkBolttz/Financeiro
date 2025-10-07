package Sistema.Financeiro.Fincaneiro.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;

@Repository
public interface FornecedorRepositorio extends JpaRepository<Fornecedor, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
