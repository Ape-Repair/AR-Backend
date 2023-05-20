package com.aperepair.aperepair.report.domain.service.impl;

import com.aperepair.aperepair.domain.model.Provider;
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
                        p.getGenre()
                );
            }
        } catch (FormatterClosedException ex) {
            logger.error("Error writing data to CSV file");
            ex.printStackTrace();
        } finally {
            formatter.close();
            try {
                assert fileWriter != null;
                fileWriter.close();
            } catch (IOException ex) {
                logger.error("Error closing CSV file");
                ex.printStackTrace();
            }
        }
    }

    private static final Logger logger = LogManager.getLogger(CsvFile.class.getName());
}