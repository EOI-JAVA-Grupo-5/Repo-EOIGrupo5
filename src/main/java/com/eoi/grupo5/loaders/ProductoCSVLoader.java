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
        productoRepository.deleteAll();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("productos-de-supermercados-sample.csv").getInputStream(), "UTF-8"));

        String line = reader.readLine(); // saltar cabecera

        while ((line = reader.readLine()) != null) {
            try {
                // Expresión segura que respeta comas entre comillas
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (data.length < 7) {
                    System.err.println("Línea con datos incompletos: " + line);
                    continue;
                }

                Producto p = new Producto();
                p.setId(Long.parseLong(data[0].trim()));
                p.setName(data[1].trim().replaceAll("^\"|\"$", "")); // quitar comillas externas
                p.setSupermarket(data[2].trim());
                p.setZipCode(Integer.parseInt(data[3].trim()));
                p.setPrice(Double.parseDouble(data[4].trim()));
                p.setUrl(data[5].trim());
                p.setCategory(data[6].trim());

                productoRepository.save(p);
            } catch (Exception e) {
                System.err.println("Error parsing line: " + line);
                e.printStackTrace();
            }
        }

        reader.close();
    }
}
