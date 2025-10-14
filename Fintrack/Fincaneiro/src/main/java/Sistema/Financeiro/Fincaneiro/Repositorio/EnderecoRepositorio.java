package Sistema.Financeiro.Fincaneiro.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;

@Repository
public interface EnderecoRepositorio extends JpaRepository<Endereco, Long> {

}
