package com.aperepair.aperepair.models;

import com.aperepair.aperepair.enums.Genero;
import com.aperepair.aperepair.interfaces.Contrato;

import java.util.ArrayList;
import java.util.List;

public class Prestador extends Usuario implements Contrato{

    private String cnpj;

    private Boolean hasPago; //analisar necessidade deste atributo

    private List<Especialidade> especialidades;

    public Prestador(
            Integer id,
            String nome,
            String email,
            String senha,
            Genero genero,
            String cpf,
            Telefone telefone,
            Endereco endereco,
            String cnpj,
            Boolean hasPago,
            List<Especialidade> especialidades
    ) {
        super(id, nome, email, senha, genero, cpf, telefone, endereco);
        this.cnpj = cnpj;
        this.hasPago = hasPago;
        especialidades = new ArrayList();
    }

    public List<Servico> getServicoById() {
        return null;
    }

    public void addEspecialidade(Especialidade especialidade) {
        for (Especialidade especial : especialidades) {
            if (especial.equals(especialidade)) {
                System.out.println(String.format("%s já é uma especialidade deste prestador", especialidade));
                return;
            }
        }
        especialidades.add(especialidade);
        System.out.println("A especialidade %s foi cadastrada");
    }

    @Override
    public Double getValorServico(Servico servico) {
        Double total = 0.0;
        total = (servico.getCustoServico() * servico.getDuracaoServicoDias());
        return total;
    }

    //GETTERS AND SETTERS
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Boolean getHasPago() {
        return hasPago;
    }

    public void setHasPago(Boolean hasPago) {
        this.hasPago = hasPago;
    }
}
