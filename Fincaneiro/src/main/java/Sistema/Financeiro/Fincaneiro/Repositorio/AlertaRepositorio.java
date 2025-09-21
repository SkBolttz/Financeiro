package Sistema.Financeiro.Fincaneiro.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Alerta;

@Repository
public interface AlertaRepositorio extends JpaRepository<Alerta, Long> {

}
