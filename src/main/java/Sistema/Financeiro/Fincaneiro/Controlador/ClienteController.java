package Sistema.Financeiro.Fincaneiro.Controlador;

import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarClienteDTO;
import Sistema.Financeiro.Fincaneiro.DTO.ClienteDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Servicos.ClienteServico;
import Sistema.Financeiro.Fincaneiro.Servicos.UsuarioServico;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

        private final ClienteServico clienteServico;
        private final UsuarioServico usuarioServico;

        public ClienteController(ClienteServico clienteServico, UsuarioServico usuarioServico) {
                this.clienteServico = clienteServico;
                this.usuarioServico = usuarioServico;
        }

        private Usuario getUsuarioLogado(Principal principal) {
                return usuarioServico.buscarPorEmail(principal.getName());
        }

        @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
        })
        @GetMapping("/listar")
        public ResponseEntity<List<Cliente>> listarClientes(Principal principal) {
                return ResponseEntity.ok(clienteServico.listarClientes(getUsuarioLogado(principal)));
        }

        @Operation(summary = "Listar todos os clientes ativos", description = "Retorna uma lista de todos os clientes ativos")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
        })
        @GetMapping("/listar/ativos")
        public ResponseEntity<List<Cliente>> listarClientesAtivos(Principal principal) {
                return ResponseEntity.ok(clienteServico.listarClientesAtivos(getUsuarioLogado(principal)));
        }

        @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente pelo ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @GetMapping("/buscar/{id}")
        public ResponseEntity<Cliente> buscarClientePorId(
                        @Parameter(description = "ID do cliente a ser buscado") @PathVariable Long id,
                        Principal principal) {
                return ResponseEntity.ok(clienteServico.buscarPorIdEUsuario(id, getUsuarioLogado(principal)));
        }

        @Operation(summary = "Adicionar novo cliente", description = "Cria um novo cliente com os dados fornecidos")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF/CNPJ já cadastrado")
        })
        @PostMapping("/adicionar")
        public ResponseEntity<Cliente> adicionarCliente(
                        @Parameter(description = "Dados do cliente para criação") @RequestBody ClienteDTO dto,
                        Principal principal) {
                Cliente cliente = clienteServico.adicionarCliente(dto, getUsuarioLogado(principal));
                return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        }

        @Operation(summary = "Alterar cliente", description = "Atualiza os dados de um cliente existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente alterado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @PutMapping("/editar/{id}")
        public ResponseEntity<Cliente> alterarCliente(
                        @Parameter(description = "ID do cliente a ser alterado") @PathVariable Long id,
                        @Parameter(description = "Novos dados do cliente") @RequestBody AlterarClienteDTO dto,
                        Principal principal) {
                dto.setId(id);
                return ResponseEntity.ok(clienteServico.alterarCliente(dto, getUsuarioLogado(principal)));
        }

        @Operation(summary = "Desativar cliente", description = "Marca um cliente como inativo")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente desativado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @PutMapping("/desativar/{id}")
        public ResponseEntity<Cliente> desativarCliente(
                        @Parameter(description = "ID do cliente a ser desativado") @PathVariable Long id,
                        Principal principal) {
                return ResponseEntity.ok(clienteServico.desativarCliente(id, getUsuarioLogado(principal)));
        }

        @Operation(summary = "Ativar cliente", description = "Marca um cliente como ativo")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente ativado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @PutMapping("/ativar/{id}")
        public ResponseEntity<Cliente> ativarCliente(
                        @Parameter(description = "ID do cliente a ser ativado") @PathVariable Long id,
                        Principal principal) {
                return ResponseEntity.ok(clienteServico.ativarCliente(id, getUsuarioLogado(principal)));
        }
}
