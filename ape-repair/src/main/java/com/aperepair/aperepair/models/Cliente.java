package com.aperepair.aperepair.models;

import com.aperepair.aperepair.enums.Genero;
import java.util.List;

public class Cliente extends Usuario{

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
    public Double getValorServico() {
        return null;
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
