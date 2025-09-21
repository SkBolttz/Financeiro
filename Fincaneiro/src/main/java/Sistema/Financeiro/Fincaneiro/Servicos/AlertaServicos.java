package Sistema.Financeiro.Fincaneiro.Servicos;

import org.springframework.stereotype.Service;

import Sistema.Financeiro.Fincaneiro.Repositorio.AlertaRepositorio;

@Service
public class AlertaServicos {

    private final AlertaRepositorio alertaRepositorio;

    public AlertaServicos(AlertaRepositorio alertaRepositorio) {
        this.alertaRepositorio = alertaRepositorio;
    }
}
