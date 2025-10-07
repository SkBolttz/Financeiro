package Sistema.Financeiro.Fincaneiro.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Repositorio.EnderecoRepositorio;

@Service
public class EnderecoServico {

    private final EnderecoRepositorio enderecoRepositorio;

    public EnderecoServico(EnderecoRepositorio enderecoRepositorio) {
        this.enderecoRepositorio = enderecoRepositorio;
    }

    @Transactional
    public Endereco salvarEndereco(Endereco endereco) {
        return enderecoRepositorio.save(endereco);
    }

    @Transactional
    public Endereco atualizarEndereco(Endereco endereco) {
        if (endereco.getId() == 0 || !enderecoRepositorio.existsById(endereco.getId())) {
            throw new RuntimeException("Endereço não localizado para atualização");
        }
        return enderecoRepositorio.save(endereco);
    }

    public Endereco buscarPorId(Long id) {
        return enderecoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não localizado"));
    }

    public List<Endereco> listarEnderecos() {
        return enderecoRepositorio.findAll();
    }
}
