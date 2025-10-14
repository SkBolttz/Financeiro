package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Sistema.Financeiro.Fincaneiro.DTO.AlterarClienteDTO;
import Sistema.Financeiro.Fincaneiro.DTO.ClienteDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Cliente.ClienteCadastradoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Cliente.ClienteNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Repositorio.ClienteRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.EnderecoRepositorio;

@Service
public class ClienteServico {

    private final ClienteRepositorio clienteRepositorio;
    private final EnderecoServico enderecoServico;
    private final EnderecoRepositorio enderecoRepositorio;

    public ClienteServico(ClienteRepositorio clienteRepositorio, EnderecoServico enderecoServico,
            EnderecoRepositorio enderecoRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.enderecoServico = enderecoServico;
        this.enderecoRepositorio = enderecoRepositorio;
    }

    @Transactional
    public Cliente adicionarCliente(ClienteDTO dto, Usuario usuario) {
        verificarDuplicidade(dto.getCpf(), dto.getCnpj(), null, usuario);

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setCnpj(dto.getCnpj());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setUsuario(usuario);
        cliente.setDataCadastro(LocalDate.now());

        if (dto.getEndereco() != null) {
            Endereco endereco = enderecoServico.salvarEndereco(dto.getEndereco());
            cliente.setEndereco(endereco);
        }

        cliente.setTipo(dto.getTipo());
        cliente.setAtivo(true);

        return clienteRepositorio.save(cliente);
    }

    @Transactional
    public Cliente alterarCliente(AlterarClienteDTO dto, Usuario usuario) {
        Cliente cliente = clienteRepositorio.findByIdAndUsuario(dto.getId(), usuario);
        if (cliente == null) {
            throw new ClienteNaoLocalizadoException("Cliente não localizado para este usuário");
        }

        verificarDuplicidade(dto.getCpf(), dto.getCnpj(), dto.getId(), usuario);

        if (dto.getNome() != null)
            cliente.setNome(dto.getNome());
        if (dto.getCpf() != null)
            cliente.setCpf(dto.getCpf());
        if (dto.getCnpj() != null)
            cliente.setCnpj(dto.getCnpj());
        if (dto.getTelefone() != null)
            cliente.setTelefone(dto.getTelefone());
        if (dto.getEmail() != null)
            cliente.setEmail(dto.getEmail());

        if (dto.getEndereco() != null) {
            Endereco endereco = dto.getEndereco();
            if (endereco.getId() != 0 && enderecoRepositorio.existsById(endereco.getId())) {
                endereco = enderecoServico.atualizarEndereco(endereco);
            } else {
                endereco = enderecoServico.salvarEndereco(endereco);
            }
            cliente.setEndereco(endereco);
        }

        if (dto.getTipo() != null)
            cliente.setTipo(dto.getTipo());

        return clienteRepositorio.save(cliente);
    }

    @Transactional
    public Cliente desativarCliente(Long id, Usuario usuario) {
        Cliente cliente = buscarPorIdEUsuario(id, usuario);
        cliente.setAtivo(false);
        return clienteRepositorio.save(cliente);
    }

    @Transactional
    public Cliente ativarCliente(Long id, Usuario usuario) {
        Cliente cliente = buscarPorIdEUsuario(id, usuario);
        cliente.setAtivo(true);
        return clienteRepositorio.save(cliente);
    }

    public List<Cliente> listarClientes(Usuario usuario) {
        return clienteRepositorio.findByUsuario(usuario);
    }

    public List<Cliente> listarClientesAtivos(Usuario usuario) {
        return clienteRepositorio.findByUsuarioAndAtivo(usuario, true);
    }

    public Cliente buscarPorIdEUsuario(Long id, Usuario usuario) {
        Cliente cliente = clienteRepositorio.findByIdAndUsuario(id, usuario);
        if (cliente == null) {
            throw new ClienteNaoLocalizadoException("Cliente não localizado para este usuário");
        }
        return cliente;
    }

    /**
     * Verifica duplicidade de CPF/CNPJ para o mesmo usuário.
     * Se idCliente não for null, ignora o próprio cliente (edição).
     */
    private void verificarDuplicidade(String cpf, String cnpj, Long idCliente, Usuario usuario) {
        if (cpf != null && !cpf.trim().isEmpty()) {
            boolean cpfDuplicado = idCliente == null
                    ? clienteRepositorio.existsByCpfAndUsuario(cpf, usuario)
                    : clienteRepositorio.existsByCpfAndUsuarioAndIdNot(cpf, usuario, idCliente);

            if (cpfDuplicado) {
                throw new ClienteCadastradoException("Cliente com este CPF já cadastrado para este usuário");
            }
        }

        if (cnpj != null && !cnpj.trim().isEmpty()) {
            boolean cnpjDuplicado = idCliente == null
                    ? clienteRepositorio.existsByCnpjAndUsuario(cnpj, usuario)
                    : clienteRepositorio.existsByCnpjAndUsuarioAndIdNot(cnpj, usuario, idCliente);

            if (cnpjDuplicado) {
                throw new ClienteCadastradoException("Cliente com este CNPJ já cadastrado para este usuário");
            }
        }
    }
}
