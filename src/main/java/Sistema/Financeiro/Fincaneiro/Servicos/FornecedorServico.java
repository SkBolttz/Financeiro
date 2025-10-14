package Sistema.Financeiro.Fincaneiro.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarFornecedorDTO;
import Sistema.Financeiro.Fincaneiro.DTO.FornecedorDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Fornecedor.FornecedorCadastradoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Fornecedor.FornecedorNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Repositorio.EnderecoRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.FornecedorRepositorio;

@Service
public class FornecedorServico {

    private final FornecedorRepositorio fornecedorRepositorio;
    private final EnderecoServico enderecoServico;
    private final EnderecoRepositorio enderecoRepositorio;

    public FornecedorServico(FornecedorRepositorio fornecedorRepositorio, EnderecoServico enderecoServico,
            EnderecoRepositorio enderecoRepositorio) {
        this.fornecedorRepositorio = fornecedorRepositorio;
        this.enderecoServico = enderecoServico;
        this.enderecoRepositorio = enderecoRepositorio;
    }

    @Transactional
    public Fornecedor adicionarFornecedor(FornecedorDTO dto, Usuario usuario) {
        verificarDuplicidade(dto.getCpf(), dto.getCnpj(), null, usuario);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial(dto.getRazaoSocial());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setCpf(dto.getCpf());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setUsuario(usuario);

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
    public Fornecedor alterarFornecedor(AlterarFornecedorDTO dto, Usuario usuario) {
        Fornecedor fornecedor = fornecedorRepositorio.findByIdAndUsuario(dto.getId(), usuario);
        if (fornecedor == null) {
            throw new FornecedorNaoLocalizadoException("Fornecedor não localizado para este usuário");
        }

        verificarDuplicidade(dto.getCpf(), dto.getCnpj(), dto.getId(), usuario);

        if (dto.getRazaoSocial() != null)
            fornecedor.setRazaoSocial(dto.getRazaoSocial());
        if (dto.getCpf() != null)
            fornecedor.setCpf(dto.getCpf());
        if (dto.getCnpj() != null)
            fornecedor.setCnpj(dto.getCnpj());
        if (dto.getTelefone() != null)
            fornecedor.setTelefone(dto.getTelefone());
        if (dto.getEmail() != null)
            fornecedor.setEmail(dto.getEmail());

        if (dto.getEndereco() != null) {
            Endereco endereco = dto.getEndereco();
            if (endereco.getId() != 0 && enderecoRepositorio.existsById(endereco.getId())) {
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
    public Fornecedor desativarFornecedor(Long id, Usuario usuario) {
        Fornecedor fornecedor = buscarPorIdEUsuario(id, usuario);
        fornecedor.setAtivo(false);
        return fornecedorRepositorio.save(fornecedor);
    }

    @Transactional
    public Fornecedor ativarFornecedor(Long id, Usuario usuario) {
        Fornecedor fornecedor = buscarPorIdEUsuario(id, usuario);
        fornecedor.setAtivo(true);
        return fornecedorRepositorio.save(fornecedor);
    }

    public List<Fornecedor> listarFornecedores(Usuario usuario) {
        return fornecedorRepositorio.findByUsuario(usuario);
    }

    public List<Fornecedor> listarFornecedoresAtivos(Usuario usuario) {
        return fornecedorRepositorio.findByUsuarioAndAtivo(usuario, true);
    }

    public Fornecedor buscarPorIdEUsuario(Long id, Usuario usuario) {
        Fornecedor fornecedor = fornecedorRepositorio.findByIdAndUsuario(id, usuario);
        if (fornecedor == null) {
            throw new FornecedorNaoLocalizadoException("Fornecedor não localizado para este usuário");
        }
        return fornecedor;
    }

    /**
     * Verifica duplicidade de CPF/CNPJ para o mesmo usuário.
     * Se idFornecedor for diferente de null, ignora o próprio fornecedor (edição).
     */
    private void verificarDuplicidade(String cpf, String cnpj, Long idFornecedor, Usuario usuario) {
        if (cpf != null && !cpf.trim().isEmpty()) {
            boolean cpfDuplicado = idFornecedor == null
                    ? fornecedorRepositorio.existsByCpfAndUsuario(cpf.trim(), usuario)
                    : fornecedorRepositorio.existsByCpfAndUsuarioAndIdNot(cpf.trim(), usuario, idFornecedor);

            if (cpfDuplicado) {
                throw new FornecedorCadastradoException("Fornecedor com este CPF já cadastrado para este usuário");
            }
        }

        if (cnpj != null && !cnpj.trim().isEmpty()) {
            boolean cnpjDuplicado = idFornecedor == null
                    ? fornecedorRepositorio.existsByCnpjAndUsuario(cnpj.trim(), usuario)
                    : fornecedorRepositorio.existsByCnpjAndUsuarioAndIdNot(cnpj.trim(), usuario, idFornecedor);

            if (cnpjDuplicado) {
                throw new FornecedorCadastradoException("Fornecedor com este CNPJ já cadastrado para este usuário");
            }
        }
    }
}
