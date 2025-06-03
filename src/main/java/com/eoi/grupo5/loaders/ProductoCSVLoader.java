package com.eoi.grupo5.loaders;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.repositories.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ProductoCSVLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public ProductoCSVLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("productos-de-supermercados-sample.csv").getInputStream()));

        String line = reader.readLine(); // saltar cabecera

        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");

            if (data.length >= 7) {
                Producto p = new Producto();
                p.setId(Long.parseLong(data[0]));
                p.setName(data[1]);
                p.setSupermarket(data[2]);
                p.setZipCode(Integer.parseInt(data[3]));
                p.setPrice(Double.parseDouble(data[4]));
                p.setUrl(data[5]);
                p.setCategory(data[6]);

                productoRepository.save(p);
            }
        }
    }
}
