package com.aperepair.aperepair;

import com.aperepair.aperepair.models.Servico;

public interface Contrato {

    //Um contrato é composto por 1 cliente, 1 prestador e 1 serviço;
    public Double getValorServico(Servico servico);

}
