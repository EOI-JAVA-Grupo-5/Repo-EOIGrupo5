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
            s1.setLogoURL("https://placehold.co/150x150");
            s1.setCadena("Mercadona");
            s1.setZona("Norte");

            Supermercado s2 = new Supermercado();
            s2.setNombre("Carrefour");
            s2.setLogoURL("https://placehold.co/150x150");
            s2.setCadena("Carrefour");
            s2.setZona("Sur");

            supermercadoRepository.saveAll(Arrays.asList(s1, s2));
            System.out.println("Datos de prueba cargados correctamente");
        }
        System.out.println("SupermercadoDataLoader - Fin de carga. Total supermercados: " + supermercadoRepository.count());

    }
}
