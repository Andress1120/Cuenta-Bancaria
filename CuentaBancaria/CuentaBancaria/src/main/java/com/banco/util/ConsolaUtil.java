package com.banco.util;

import com.banco.modelo.CuentaBancaria;
import com.banco.modelo.Transaccion;

import java.util.List;

/**
 * Utilidades de presentación en consola.
 * Responsabilidad única: formatear e imprimir información al usuario.
 */
public class ConsolaUtil {

    private static final String LINEA  = "─".repeat(90);
    private static final String LINEA2 = "═".repeat(90);

    private ConsolaUtil() { /* utilidad estática, no instanciar */ }

    public static void imprimirEncabezado(String titulo) {
        System.out.println("\n" + LINEA2);
        System.out.printf("  %s%n", titulo.toUpperCase());
        System.out.println(LINEA2);
    }

    public static void imprimirInfoCuenta(CuentaBancaria cuenta) {
        System.out.println(LINEA);
        System.out.println("  " + cuenta);
        System.out.println(LINEA);
    }

    public static void imprimirHistorial(String titulo, List<Transaccion> transacciones) {
        imprimirEncabezado(titulo);
        if (transacciones.isEmpty()) {
            System.out.println("  (sin movimientos registrados)");
        } else {
            for (int i = 0; i < transacciones.size(); i++) {
                System.out.printf("  %2d. %s%n", i + 1, transacciones.get(i));
            }
        }
        System.out.println(LINEA);
    }

    public static void ok(String mensaje) {
        System.out.println("  ✔  " + mensaje);
    }

    public static void error(String mensaje) {
        System.out.println("  ✘  ERROR: " + mensaje);
    }
}
