package Clases;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 Gestor generico que administra elementos de tipo T identificados por una clave K.
 Internamente usa un Map (HashMap), por lo que no admite claves duplicadas.

 parametro <K> tipo de la clave
 parametro <T> tipo de los elementos almacenados
 */
public class GestorGenerico<K, T> {

    private Map<K, T> elementos;

    public GestorGenerico() {
        this.elementos = new HashMap<>();
    }

    /**
     Agrega un elemento asociado a una clave
     return true si se agrego, false si la clave ya existia (en ese caso no se modifica nada)
     */
    public boolean agregar(K clave, T elemento) {
        if (elementos.containsKey(clave)) {
            return false;
        }
        elementos.put(clave, elemento);
        return true;
    }

    /** Devuelve el elemento asociado a la clave, o null si no existe. */
    public T buscar(K clave) {
        return elementos.get(clave);
    }

    /** Indica si existe un elemento con esa clave. */
    public boolean existe(K clave) {
        return elementos.containsKey(clave);
    }

    /** Elimina el elemento de esa clave. Devuelve el elemento eliminado o null si no existía. */
    public T eliminar(K clave) {
        return elementos.remove(clave);
    }

    /** Devuelve todos los elementos almacenados. */
    public Collection<T> listar() {
        return elementos.values();
    }

    /** Cantidad de elementos almacenados. */
    public int cantidad() {
        return elementos.size();
    }
}
