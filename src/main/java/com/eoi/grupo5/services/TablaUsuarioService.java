package com.eoi.grupo5.services;

import com.eoi.grupo5.dtos.DatosGraficaDTO;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.utils.exceptions.ListaNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class TablaUsuarioService {

    private final ListaService listaService;

    public TablaUsuarioService(ListaService listaService) {
        this.listaService = listaService;
    }

    /**
     * Devuelve una fecha Date con horas, minutos y segundos a 0
     * @param date - Fecha a formatear
     * @return Fecha formateada
     */
    public Date fechaSinHora(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Saca los datos de listas de la última semana del usuario
     * @param usuario - Usuario
     * @return Lista de los datos para la gráfica del perfil del usuario
     */
    public List<DatosGraficaDTO> datosUltimaSemana(Usuario usuario){
        List<Lista> listas = listaService.getListasDeUsuario(usuario)
                .orElseThrow(()->new ListaNotFoundException("No se encontraron listas para " + usuario.getUsername()));


        List<DatosGraficaDTO> datos = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        Date hoy = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(Calendar.DAY_OF_YEAR, -7);
        Date hace7Dias = cal.getTime();

        for(Lista lista : listas) {
            //Comprueba si la fecha en la lista esta entre los 7 ultimos dias
            if(lista.getFecha()!=null && lista.getFecha().before(hoy) && lista.getFecha().after(hace7Dias)) {
                if(datos.isEmpty()){
                    datos.add(new DatosGraficaDTO(fechaSinHora(lista.getFecha()), lista.getCosteTotal().doubleValue()));
                }else{

                    boolean encontrado = false;
                    for (int i = 0; i < datos.size(); i++) {
                        //Comprueba si la fecha ya existe en los datos metidos
                        if (fechaSinHora(datos.get(i).getFecha()).equals(fechaSinHora(lista.getFecha()))) {
                            datos.get(i).setDineroGastado(datos.get(i).getDineroGastado() + lista.getCosteTotal().doubleValue());
                            i = datos.size();
                            encontrado = true;
                        }
                    }

                    if(!encontrado){
                        datos.add(new DatosGraficaDTO(fechaSinHora(lista.getFecha()), lista.getCosteTotal().doubleValue()));
                    }
                }
            }
        }

        datos.sort(Comparator.comparing(DatosGraficaDTO::getFecha));

        return datos;

    }

    /**
     * Desglosa los datos para sacar una lista del dinero gastado por día
     * @param datosCompletos - Lista de los datos en los últimos 7 días
     * @return Lista del dinero gastado cada día
     */
    public List<Double> datosDineroUltimaSemana(List<DatosGraficaDTO> datosCompletos){
        List<Double> datosDinero = new ArrayList<>();

        for (DatosGraficaDTO dato : datosCompletos){
            datosDinero.add(dato.getDineroGastado());
        }

        return  datosDinero;
    }

    /**
     * Desglosa los datos para sacar una lista de los días en los que se gasta dinero
     * @param datosCompletos - Lista de los datos en los últimos 7 días
     * @return Lista de los días en los que se gasta dinero
     */
    public List<Date> datosDiasUltimaSemana(List<DatosGraficaDTO> datosCompletos){
        List<Date> datosFecha = new ArrayList<>();

        for (DatosGraficaDTO dato : datosCompletos){
            datosFecha.add(dato.getFecha());
        }

        return  datosFecha;
    }
}
