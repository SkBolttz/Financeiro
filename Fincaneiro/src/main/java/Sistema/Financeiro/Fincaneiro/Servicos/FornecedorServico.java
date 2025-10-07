package Sistema.Financeiro.Fincaneiro.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Sistema.Financeiro.Fincaneiro.DTO.AlterarFornecedorDTO;
import Sistema.Financeiro.Fincaneiro.DTO.FornecedorDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Fornecedor.FornecedorCadastradoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Fornecedor.FornecedorNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Repositorio.FornecedorRepositorio;

@Service
public class FornecedorServico {

    private final FornecedorRepositorio fornecedorRepositorio;
    private final EnderecoServico enderecoServico;

    public FornecedorServico(FornecedorRepositorio fornecedorRepositorio, EnderecoServico enderecoServico) {
        this.fornecedorRepositorio = fornecedorRepositorio;
        this.enderecoServico = enderecoServico;
    }

    @Transactional
    public Fornecedor adicionarFornecedor(FornecedorDTO dto) {
        if (dto.getCnpj() != null && fornecedorRepositorio.existsByCnpj(dto.getCnpj())) {
            throw new FornecedorCadastradoException("Fornecedor com este CNPJ já cadastrado");
        }
        if (dto.getCpf() != null && fornecedorRepositorio.existsByCpf(dto.getCpf())) {
            throw new FornecedorCadastradoException("Fornecedor com este CPF já cadastrado");
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial(dto.getRazaoSocial());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setCpf(dto.getCpf());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());

        Endereco endereco = dto.getEndereco();
        if (endereco != null) {
            endereco = enderecoServico.salvarEndereco(endereco);
            fornecedor.setEndereco(endereco);
        }

        fornecedor.setFormaPagamento(dto.getFormaPagamento());
        fornecedor.setBanco(dto.getBanco());
        fornecedor.setAgencia(dto.getAgencia());
        fornecedor.setConta(dto.getConta());
        fornecedor.setPessoaContato(dto.getPessoaContato());
        fornecedor.setObservacao(dto.getObservacao());
        fornecedor.setTipo(dto.getTipo());
        fornecedor.setAtivo(true); 

        return fornecedorRepositorio.save(fornecedor);
    }

    @Transactional
    public Fornecedor alterarFornecedor(AlterarFornecedorDTO dto) {
        Fornecedor fornecedor = fornecedorRepositorio.findById(dto.getId())
                .orElseThrow(() -> new FornecedorNaoLocalizadoException("Fornecedor não localizado"));

        if (dto.getRazaoSocial() != null)
            fornecedor.setRazaoSocial(dto.getRazaoSocial());
        if (dto.getCnpj() != null)
            fornecedor.setCnpj(dto.getCnpj());
        if (dto.getCpf() != null)
            fornecedor.setCpf(dto.getCpf());
        if (dto.getTelefone() != null)
            fornecedor.setTelefone(dto.getTelefone());
        if (dto.getEmail() != null)
            fornecedor.setEmail(dto.getEmail());

        // Atualiza ou salva o endereço
        if (dto.getEndereco() != null) {
            Endereco endereco = dto.getEndereco();
            if (endereco.getId() != 0) {
                endereco = enderecoServico.atualizarEndereco(endereco);
            } else {
                endereco = enderecoServico.salvarEndereco(endereco);
            }
            fornecedor.setEndereco(endereco);
        }

        if (dto.getFormaPagamento() != null)
            fornecedor.setFormaPagamento(dto.getFormaPagamento());
        if (dto.getBanco() != null)
            fornecedor.setBanco(dto.getBanco());
        if (dto.getAgencia() != null)
            fornecedor.setAgencia(dto.getAgencia());
        if (dto.getConta() != null)
            fornecedor.setConta(dto.getConta());
        if (dto.getPessoaContato() != null)
            fornecedor.setPessoaContato(dto.getPessoaContato());
        if (dto.getObservacao() != null)
            fornecedor.setObservacao(dto.getObservacao());
        if (dto.getTipo() != null)
            fornecedor.setTipo(dto.getTipo());

        return fornecedorRepositorio.save(fornecedor);
    }

    @Transactional
    public Fornecedor desativarFornecedor(Long id) {
        Fornecedor fornecedor = fornecedorRepositorio.findById(id)
                .orElseThrow(() -> new FornecedorNaoLocalizadoException("Fornecedor não localizado"));
        fornecedor.setAtivo(false);
        return fornecedorRepositorio.save(fornecedor);
    }

    @Transactional
    public Fornecedor ativarFornecedor(Long id) {
        Fornecedor fornecedor = fornecedorRepositorio.findById(id)
                .orElseThrow(() -> new FornecedorNaoLocalizadoException("Fornecedor não localizado"));
        fornecedor.setAtivo(true);
        return fornecedorRepositorio.save(fornecedor);
    }

    public List<Fornecedor> listarFornecedores() {
        return fornecedorRepositorio.findAll();
    }

    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepositorio.findById(id)
                .orElseThrow(() -> new FornecedorNaoLocalizadoException("Fornecedor não localizado"));
    }
}
