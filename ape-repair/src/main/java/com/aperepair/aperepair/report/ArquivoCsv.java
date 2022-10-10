package com.aperepair.aperepair.report;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ArquivoCsv {

    public static void gravaArquivoCsv(ListaObj<Musica> lista, String nomeArquivo) {
        FileWriter arquivo = null; //objeto que representa o arquivo de gravação
        Formatter saida = null; //objeto usado para escrever no arquivo
        Boolean deuRuim = false;
        nomeArquivo += ".csv"; //acrescenta a extensão .csv ao arquivo

        try {
            arquivo = new FileWriter(nomeArquivo);
            saida = new Formatter(arquivo);
        }
        catch (IOException ex) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }

        try {
            for (int i = 0; i < lista.getTamanho(); i++) {
                Musica m = lista.getElemento(i);
                saida.format(
                        "%d;%s;%.2f;%d\n",
                        m.getId(),
                        m.getNome(),
                        m.getDuracao(),
                        m.getQtdArtistas()
                );
            }
        }
        catch (FormatterClosedException ex) {
            System.out.println("Erro ao gravar arquivo");
            deuRuim = true;
        }
        finally {
            saida.close();
            try {
                arquivo.close();
            }
            catch (IOException ex) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }

    public static void leArquivoCsv(String nomeArquivo) {
        FileReader arquivo = null;
        Scanner entrada = null;
        Boolean deuRuim = false;
        nomeArquivo += ".csv";

        try {
            arquivo = new FileReader(nomeArquivo);
            entrada = new Scanner(arquivo).useDelimiter(";|\\n");
        }
        catch (FileNotFoundException ex) {
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
                System.out.printf("%06d %-12s %11.2f %11d\n", id, nome, valor, estoque);
            }
        }
        catch (NoSuchElementException ex) {
            System.out.println("Arquivo com problemas");
            deuRuim = true;
        }
        catch (IllegalStateException ex) {
            System.out.println("Erro na leitura do arquivo");
            deuRuim = true;
        }
        finally {
            entrada.close();
            try {
                arquivo.close();
            }
            catch (IOException ex) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }
}
