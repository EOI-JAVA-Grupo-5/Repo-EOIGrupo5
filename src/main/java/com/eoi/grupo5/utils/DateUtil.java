package com.eoi.grupo5.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("dateUtil")
public class DateUtil {

    public String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public String tiempoDesde(LocalDateTime fecha) {
        Duration duracion = Duration.between(fecha, LocalDateTime.now());
        long horas = duracion.toHours();
        long minutos = duracion.toMinutes() % 60;

        if (horas > 0 && minutos > 0) {
            return String.format("hace %d hora%s %d minuto%s",
                    horas, horas == 1 ? "" : "s",
                    minutos, minutos == 1 ? "" : "s");
        } else if (horas > 0) {
            return String.format("hace %d hora%s", horas, horas == 1 ? "" : "s");
        } else if (minutos > 0) {
            return String.format("hace %d minuto%s", minutos, minutos == 1 ? "" : "s");
        } else {
            return "hace unos segundos";
        }
    }
}
