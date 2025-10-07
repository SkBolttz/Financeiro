package Sistema.Financeiro.Fincaneiro.Servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarClienteDTO;
import Sistema.Financeiro.Fincaneiro.DTO.ClienteDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Cliente.ClienteCadastradoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Cliente.ClienteNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Repositorio.ClienteRepositorio;

@Service
public class ClienteServico {

    private final ClienteRepositorio clienteRepositorio;
    private final EnderecoServico enderecoServico;

    public ClienteServico(ClienteRepositorio clienteRepositorio, EnderecoServico enderecoServico) {
        this.clienteRepositorio = clienteRepositorio;
        this.enderecoServico = enderecoServico;
    }

    @Transactional
    public Cliente adicionarCliente(ClienteDTO dto) {
        if (dto.getCpf() != null && clienteRepositorio.existsByCpf(dto.getCpf())) {
            throw new ClienteCadastradoException("Cliente com este CPF já cadastrado");
        }
        if (dto.getCnpj() != null && clienteRepositorio.existsByCnpj(dto.getCnpj())) {
            throw new ClienteCadastradoException("Cliente com este CNPJ já cadastrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setCnpj(dto.getCnpj());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());

        // Salva o endereço antes de associar
        Endereco endereco = dto.getEndereco();
        if (endereco != null) {
            endereco = enderecoServico.salvarEndereco(endereco);
            cliente.setEndereco(endereco);
        }

        cliente.setTipo(dto.getTipo());
        cliente.setAtivo(true);

        return clienteRepositorio.save(cliente);
    }

    @Transactional
    public Cliente alterarCliente(AlterarClienteDTO dto) {
        Cliente cliente = clienteRepositorio.findById(dto.getId())
                .orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não localizado"));

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
            if (endereco.getId() != 0) {
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
    public Cliente desativarCliente(Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não localizado"));
        cliente.setAtivo(false);
        return clienteRepositorio.save(cliente);
    }

    @Transactional
    public Cliente ativarCliente(Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não localizado"));
        cliente.setAtivo(true);
        return clienteRepositorio.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepositorio.findById(id)
                .orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não localizado"));
    }
}
