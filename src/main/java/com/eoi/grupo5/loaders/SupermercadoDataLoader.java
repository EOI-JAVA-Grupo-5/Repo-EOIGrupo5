package com.eoi.grupo5.loaders;

import com.eoi.grupo5.entities.Supermercado;
import com.eoi.grupo5.repositories.SupermercadoRepository;
import com.eoi.grupo5.services.SupermercadoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SupermercadoDataLoader implements CommandLineRunner {

    private final SupermercadoRepository supermercadoRepository;
    private final SupermercadoService supermercadoService;

    public SupermercadoDataLoader(SupermercadoRepository supermercadoRepository,
                                  SupermercadoService supermercadoService) {
        this.supermercadoRepository = supermercadoRepository;
        this.supermercadoService = supermercadoService;
    }

    @Override
    public void run(String... args) {
        // Add debug logging
        System.out.println("SupermercadoDataLoader - Inicio de carga");
        System.out.println("SupermercadoDataLoader - Número actual de supermercados: " + supermercadoRepository.count());

        // Check if we already have data
        if (supermercadoRepository.count() == 0) {
            System.out.println("Cargando datos de prueba de supermercados...");

            Supermercado s1 = new Supermercado();
            s1.setNombre("Mercadona");
            s1.setLogoURL("https://i.pinimg.com/736x/66/bc/4e/66bc4e8d1482e6ec4525987c1691c83e.jpg");
            s1.setCadena("Mercadona");
            s1.setZona("Norte");

            Supermercado s2 = new Supermercado();
            s2.setNombre("Carrefour");
            s2.setLogoURL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTXPHkhGOxIJKf9VZX1Yqmc7AcL9Jtc9pP8Q&s");
            s2.setCadena("Carrefour");
            s2.setZona("Sur");

            Supermercado s3 = new Supermercado();
            s3.setNombre("Dia");
            s3.setLogoURL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnETTDv2PLzTStD1bp57EYBMyx9g_RM8MBPQ&s");
            s3.setCadena("Dia");
            s3.setZona("Centro");

            supermercadoRepository.saveAll(Arrays.asList(s1, s2, s3));
            System.out.println("Datos de prueba cargados correctamente");
        }
        System.out.println("SupermercadoDataLoader - Fin de carga. Total supermercados: " + supermercadoRepository.count());

    }
}
