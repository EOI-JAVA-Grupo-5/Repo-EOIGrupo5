package com.eoi.grupo5.services;


import com.eoi.grupo5.entities.Carrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @Service: Marca esta clase como un componente de servicio de Spring
// @Scope("session"): Define el ámbito de este bean. Una instancia por sesión HTTP del usuario.
// scopedProxyMode = ScopedProxyMode.TARGET_CLASS: Necesario para inyectar beans de sesión en otros beans de ámbito singleton.
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoService {

    private final ProductoService productoService; // Servicio para acceder a los productos de la BD
    private List<Carrito> itemsCarrito; // La lista de ítems del carrito, ligada a la sesión

    @Autowired
    public CarritoService(ProductoService productoService) {
        this.productoService = productoService;
        this.itemsCarrito = new ArrayList<>(); // Se inicializa un nuevo carrito para cada sesión
        // Opcional: Aquí podrías cargar un carrito persistido si tienes esa funcionalidad (ej. de DB para usuarios logueados)
    }

    /**
     * Devuelve todos los ítems actuales en el carrito de la sesión.
     * @return Una lista de objetos Carrito.
     */
    public List<Carrito> obtenerItemsCarrito() {
        return new ArrayList<>(itemsCarrito); // Devuelve una copia para evitar modificaciones externas directas
    }



    /**
     * Actualiza la cantidad de un producto específico en el carrito.
     * Si la nueva cantidad es 0 o menor, el producto se elimina.
     * @param productoId El ID del producto a actualizar.
     * @param nuevaCantidad La nueva cantidad deseada.
     * @return true si se actualizó/eliminó, false si el producto no estaba en el carrito.
     */
    public boolean actualizarCantidad(Long productoId, int nuevaCantidad) {
        Optional<Carrito> itemOpt = itemsCarrito.stream()
                .filter(item -> item.getProductoId().equals(productoId))
                .findFirst();

        if (itemOpt.isPresent()) {
            Carrito item = itemOpt.get();
            if (nuevaCantidad > 0) {
                item.setCantidad(nuevaCantidad);
            } else {
                itemsCarrito.remove(item); // Eliminar si la cantidad es 0 o menos
            }
            return true;
        }
        return false; // El producto no estaba en el carrito
    }

    /**
     * Elimina un producto del carrito por su ID.
     * @param productoId El ID del producto a eliminar.
     * @return true si se eliminó, false si no se encontró en el carrito.
     */
    public boolean eliminarProducto(Long productoId) {
        return itemsCarrito.removeIf(item -> item.getProductoId().equals(productoId));
    }

    /**
     * Vacía completamente el carrito.
     */
    public void vaciarCarrito() {
        itemsCarrito.clear();
    }

    /**
     * Calcula el número total de unidades en el carrito.
     * @return La suma de las cantidades de todos los productos en el carrito.
     */
    public int calcularTotalUnidades() {
        return itemsCarrito.stream()
                .mapToInt(Carrito::getCantidad)
                .sum();
    }

    /**
     * Calcula el precio total de todos los productos en el carrito.
     * @return La suma de los precios totales de cada ítem en el carrito.
     */
    public double calcularPrecioTotal() {
        return itemsCarrito.stream()
                .mapToDouble(Carrito::getPrecio_unidad) // Usamos el método corregido de la entidad Carrito
                .sum();
    }
}