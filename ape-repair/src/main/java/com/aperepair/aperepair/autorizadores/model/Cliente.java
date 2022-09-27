package com.aperepair.aperepair.autorizadores.model;

import com.aperepair.aperepair.autorizadores.model.enums.Genero;
import com.aperepair.aperepair.matchs.service.Contrato;
import com.aperepair.aperepair.matchs.models.Servico;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Cliente extends Usuario implements Contrato {

    private Boolean isPago;

    public Cliente(
            Integer id,
            String nome,
            String email,
            String senha,
            Genero genero,
            String cpf,
            Telefone telefone,
            Endereco endereco
    ) {
        super(id, nome, email, senha, genero, cpf, telefone, endereco);
        isPago = false;
    }

    public List<Servico> getServicoById(){
        return null;
    }

    @Override
    public Double getValorServico(Servico servico) {
        Double total = 0.0;
        total = (servico.getCustoServico() * servico.getDuracaoServicoDias()) * 1.1;
        return total;
    }

    //GETTERS AND SETTERS
    public Boolean getPago() {
        return isPago;
    }

    public void setPago(Boolean pago) {
        isPago = pago;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "isPago=" + isPago +
                "} " + super.toString();
    }
}
