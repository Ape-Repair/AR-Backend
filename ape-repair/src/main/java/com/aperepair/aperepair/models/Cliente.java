package com.aperepair.aperepair.models;

import com.aperepair.aperepair.enums.Genero;
import com.aperepair.aperepair.interfaces.Contrato;

import javax.persistence.Entity;
import java.util.List;

@Entity(name="cliente")
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
