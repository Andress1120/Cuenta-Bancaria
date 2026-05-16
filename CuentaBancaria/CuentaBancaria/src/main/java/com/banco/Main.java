package com.banco;

import com.banco.excepcion.CuentaInvalidaException;
import com.banco.excepcion.MontoInvalidoException;
import com.banco.excepcion.SaldoInsuficienteException;
import com.banco.modelo.CuentaBancaria;
import com.banco.modelo.TipoTransaccion;
import com.banco.servicio.BancaServicio;
import com.banco.servicio.IBancaServicio;
import com.banco.util.ConsolaUtil;

/**
 * Punto de entrada de la aplicación.
 * Demuestra las tres funcionalidades nuevas:
 *   1. Transferencias entre cuentas
 *   2. Historial de transacciones (completo y filtrado)
 *   3. Aplicación de intereses y cargos
 */
public class Main {

    public static void main(String[] args) {

        // Dependencia invertida: usamos la interfaz, no la clase concreta
        IBancaServicio banco = new BancaServicio();

        // ── Configuración inicial ────────────────────────────────────────────

        ConsolaUtil.imprimirEncabezado("Banco POO - Demo de nuevas funcionalidades");

        try {
            // Crear dos cuentas de ejemplo
            CuentaBancaria cuentaAna  = banco.crearCuenta("COL001", "Ana García",  1_000_000);
            CuentaBancaria cuentaLuis = banco.crearCuenta("COL002", "Luis Mendoza",   500_000);

            ConsolaUtil.ok("Cuenta creada: " + cuentaAna);
            ConsolaUtil.ok("Cuenta creada: " + cuentaLuis);

            // ── Depósito y retiro básico ─────────────────────────────────────
            ConsolaUtil.imprimirEncabezado("Operaciones básicas");

            banco.depositar("COL001", 200_000);
            ConsolaUtil.ok("Depósito de $200.000 en COL001");

            banco.retirar("COL001", 50_000);
            ConsolaUtil.ok("Retiro de $50.000 de COL001");

            // ────────────────────────────────────────────────────────────────
            //  FUNCIONALIDAD 1 — TRANSFERENCIAS
            // ────────────────────────────────────────────────────────────────
            ConsolaUtil.imprimirEncabezado("Funcionalidad 1 – Transferencias");

            banco.transferir("COL001", "COL002", 300_000);
            ConsolaUtil.ok("Transferencia de $300.000: COL001 → COL002");

            ConsolaUtil.imprimirInfoCuenta(banco.buscarCuenta("COL001"));
            ConsolaUtil.imprimirInfoCuenta(banco.buscarCuenta("COL002"));

            // Intento de transferencia con saldo insuficiente (manejo de error)
            System.out.println("\n  [Prueba] Transferencia mayor al saldo disponible:");
            try {
                banco.transferir("COL002", "COL001", 999_999_999);
            } catch (SaldoInsuficienteException e) {
                ConsolaUtil.error(e.getMessage());
            }

            // ────────────────────────────────────────────────────────────────
            //  FUNCIONALIDAD 2 — HISTORIAL DE TRANSACCIONES
            // ────────────────────────────────────────────────────────────────

            // Historial completo de COL001
            ConsolaUtil.imprimirHistorial(
                "Historial completo – COL001 (" + cuentaAna.getTitular() + ")",
                banco.obtenerHistorial("COL001")
            );

            // Historial filtrado: solo transferencias enviadas
            ConsolaUtil.imprimirHistorial(
                "Transferencias enviadas – COL001",
                banco.obtenerHistorialPorTipo("COL001", TipoTransaccion.TRANSFERENCIA_ENVIADA)
            );

            // ────────────────────────────────────────────────────────────────
            //  FUNCIONALIDAD 3 — INTERESES Y CARGOS
            // ────────────────────────────────────────────────────────────────
            ConsolaUtil.imprimirEncabezado("Funcionalidad 3 – Intereses y cargos");

            // Aplicar interés del 5 % anual a COL001
            double interes = banco.aplicarInteres("COL001", 0.05);
            ConsolaUtil.ok(String.format("Interés del 5%% aplicado a COL001: +$%,.2f", interes));

            // Aplicar cargo de mantenimiento a COL002
            banco.aplicarCargo("COL002", 15_000, "Cuota de mantenimiento mensual");
            ConsolaUtil.ok("Cargo de $15.000 aplicado a COL002");

            // Intento de cargo mayor al saldo (manejo de error)
            System.out.println("\n  [Prueba] Cargo mayor al saldo disponible:");
            try {
                banco.aplicarCargo("COL002", 999_999_999, "Cargo imposible");
            } catch (SaldoInsuficienteException e) {
                ConsolaUtil.error(e.getMessage());
            }

            // Estado final de ambas cuentas
            ConsolaUtil.imprimirEncabezado("Estado final de las cuentas");
            for (CuentaBancaria c : banco.listarCuentas()) {
                ConsolaUtil.imprimirInfoCuenta(c);
            }

            // Historial completo de COL002
            ConsolaUtil.imprimirHistorial(
                "Historial completo – COL002 (" + cuentaLuis.getTitular() + ")",
                banco.obtenerHistorial("COL002")
            );

            // Prueba cuenta no existente
            System.out.println("\n  [Prueba] Buscar cuenta inexistente:");
            try {
                banco.buscarCuenta("XXXXX");
            } catch (CuentaInvalidaException e) {
                ConsolaUtil.error(e.getMessage());
            }

        } catch (MontoInvalidoException | SaldoInsuficienteException
                 | CuentaInvalidaException e) {
            ConsolaUtil.error("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n" + "═".repeat(90));
        System.out.println("  Fin de la demostración.");
        System.out.println("═".repeat(90) + "\n");
    }
}
