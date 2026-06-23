package br.com.qualix.tvwall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TvWallApplication {

    public static void main(String[] args) {
        SpringApplication.run(TvWallApplication.class, args);
        System.out.println("\n\nO projeto TV-Wall foi iniciado com sucesso!\n########## >>");
    }
}
