package com.eoi.grupo5.config;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ProductoCSVLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("productos-de-supermercados-sample.csv").getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 4) {
                Producto p = new Producto();
                p.setNombre(data[0]);
                p.setSupermercado(data[1]);
                p.setPrecio(Double.parseDouble(data[2]));
                p.setImagenUrl(data[3]);
                productoRepository.save(p);
            }
        }
    }
}
