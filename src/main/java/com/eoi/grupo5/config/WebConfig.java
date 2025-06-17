package com.eoi.grupo5.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @PostConstruct
    public void init() {
        System.out.println("🟢 WebConfig registrado");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta externa a nivel del sistema de archivos
        Path uploadDir = Paths.get("uploads/perfiles");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry
                .addResourceHandler("/images/perfiles/**")  // URL accesible desde el navegador
                .addResourceLocations("file:" + uploadPath + "/");  // Ruta física real

        System.out.println("🟢 Sirviendo imágenes desde: " + uploadPath);

        // Opcional: habilita cache control
        // .setCachePeriod(3600);  // 1 hora
    }
}
