package com.aperepair.aperepair.reports.domain;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.reports.domain.service.impl.ListObj;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;

@Service
public class CsvFile {

    private static String FILE_NAME = "registered-providers";

    public static void writeCsvFile(List<Provider> providers) {
        FileWriter fileWriter = null; //objeto que representa o arquivo de gravação
        Formatter formatter = null; //objeto usado para escrever no arquivo
        FILE_NAME += ".csv"; //acrescenta a extensão .csv ao arquivo

        try {
            logger.info("Trying to generate CSV file");
            fileWriter = new FileWriter(FILE_NAME);
            formatter= new Formatter(fileWriter);
        } catch (IOException ex) {
            logger.error("Error to generate and opening CSV file");
            ex.printStackTrace();
        }

        logger.info("Add headers to CSV file");
        formatter.format("%s;%s;%s;%s;%s\n",
                "NAME",
                "EMAIL",
                "CPF",
                "CNPJ",
                "GENRE"
                );

        try {
            for (int i = 0; i < providers.size(); i++) {
                Provider p = providers.get(i);
                formatter.format(
                        "%s;%s;%s;%s;%s\n",
                        p.getName(),
                        p.getEmail(),
                        p.getCpf(),
                        p.getCnpj(),
                        p.getGenre()
                );
            }
        } catch (FormatterClosedException ex) {
            logger.error("Error writing data to CSV file");
            ex.printStackTrace();
        } finally {
            formatter.close();
            try {
                fileWriter.close();
            } catch (IOException ex) {
                logger.error("Error closing CSV file");
                ex.printStackTrace();
            }
        }
    }
/*
    public static void leArquivoCsv(String nomeArquivo) {
        FileReader arquivo = null;
        Scanner entrada = null;
        Boolean deuRuim = false;
        nomeArquivo += ".csv";

        try {
            arquivo = new FileReader(nomeArquivo);
            entrada = new Scanner(arquivo).useDelimiter(";|\\n");
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado");
            System.exit(1);
        }

        try {
            System.out.printf("%-6S %-12S %11S %-11S\n", "código", "nome", "duracao", "qtdArtistas");
            while (entrada.hasNext()) {
                int id = entrada.nextInt();
                String nome = entrada.next();
                Double valor = entrada.nextDouble();
                Integer estoque = entrada.nextInt();
                System.out.printf("%06d %-12s %101.2f %11d\n", id, nome, valor, estoque);
            }
        } catch (NoSuchElementException ex) {
            System.out.println("Arquivo com problemas");
            deuRuim = true;
        } catch (IllegalStateException ex) {
            System.out.println("Erro na leitura do arquivo");
            deuRuim = true;
        } finally {
            entrada.close();
            try {
                arquivo.close();
            } catch (IOException ex) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    } */

    private static final Logger logger = LogManager.getLogger(CsvFile.class.getName());
}