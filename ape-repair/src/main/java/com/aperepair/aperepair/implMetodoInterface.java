package com.aperepair.aperepair;

import com.aperepair.aperepair.enums.Categoria;
import com.aperepair.aperepair.enums.Genero;
import com.aperepair.aperepair.enums.Status;
import com.aperepair.aperepair.enums.Uf;
import com.aperepair.aperepair.models.*;

public class implMetodoInterface {

    public static void main(String[] args) {
        Endereco endereco = new Endereco(
                10,
                "haddock lobo",
                595,
                "Prédio",
                "04207-000",
                "Paulista",
                "São Paulo",
                Uf.SP
        );

        Telefone telefone = new Telefone("2894-7556", null);

        Cliente cliente = new Cliente(
                100,
                "Celia",
                "celia@gmail.com",
                "nota10",
                Genero.F,
                "123.456.789-00",
                telefone,
                endereco
        );

        Prestador prestador = new Prestador(
                500,
                "Zé",
                "ze@hotmail.com",
                "456",
                Genero.M,
                "987.654.321-00",
                telefone,
                endereco,
                null,
                false,
                null
        );

        Servico servico = new Servico(
                1,
                "Quarto",
                Status.EM_ANDAMENTO,
                "Entregavel Estrutura de dados",
                2.0,
                1.5,
                Categoria.REPARO,
                "Armário",
                50.0,
                2,
                4,
                cliente,
                prestador
        );

        System.out.println("METODO DA INTERFACE SENDO CHAMADO PELO SERVIÇO");
        System.out.println(servico.getValorServico(servico));
        System.out.println("METODO DA INTERFACE SENDO CHAMADO PELO CLIENTE");
        System.out.println(cliente.getValorServico(servico));
        System.out.println("METODO DA INTERFACE SENDO CHAMADO PELO PRESTADOR");
        System.out.println(prestador.getValorServico(servico));
    }
}
