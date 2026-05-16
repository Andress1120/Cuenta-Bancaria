package com.banco.modelo;

import com.banco.excepcion.MontoInvalidoException;
import com.banco.excepcion.SaldoInsuficienteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modelo principal que representa una cuenta bancaria.
 *
 * Principios POO aplicados:
 *  - Encapsulación   : todos los campos son privados; acceso solo por métodos.
 *  - Responsabilidad única: la clase gestiona su propio estado y registro interno.
 *  - Apertura/cierre : abierta para extensión (herencia), cerrada para modificación.
 */
public class CuentaBancaria {

    // ── Estado ───────────────────────────────────────────────────────────────

    private final String numeroCuenta;
    private final String titular;
    private double saldo;
    private final List<Transaccion> historial;

    // ── Constructor ──────────────────────────────────────────────────────────

    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial)
            throws MontoInvalidoException {

        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new IllegalArgumentException("El número de cuenta no puede estar vacío.");
        }
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("El titular no puede estar vacío.");
        }
        if (saldoInicial < 0) {
            throw new MontoInvalidoException(saldoInicial);
        }

        this.numeroCuenta = numeroCuenta.trim();
        this.titular      = titular.trim();
        this.saldo        = saldoInicial;
        this.historial    = new ArrayList<>();

        if (saldoInicial > 0) {
            registrar(TipoTransaccion.DEPOSITO, saldoInicial, "Saldo inicial de apertura");
        }
    }

    // ── Operaciones básicas ──────────────────────────────────────────────────

    /**
     * Deposita dinero en la cuenta.
     */
    public void depositar(double monto) throws MontoInvalidoException {
        validarMonto(monto);
        saldo += monto;
        registrar(TipoTransaccion.DEPOSITO, monto, "Depósito en cuenta");
    }

    /**
     * Retira dinero de la cuenta.
     */
    public void retirar(double monto)
            throws MontoInvalidoException, SaldoInsuficienteException {
        validarMonto(monto);
        if (monto > saldo) {
            throw new SaldoInsuficienteException(saldo, monto);
        }
        saldo -= monto;
        registrar(TipoTransaccion.RETIRO, monto, "Retiro de cuenta");
    }

    // ── Funcionalidad 1: Transferencias ─────────────────────────────────────

    /**
     * Transfiere dinero desde esta cuenta hacia {@code destino}.
     *
     * @param destino cuenta receptora
     * @param monto   cantidad a transferir (debe ser > 0)
     */
    public void transferirA(CuentaBancaria destino, double monto)
            throws MontoInvalidoException, SaldoInsuficienteException {

        if (destino == null) {
            throw new IllegalArgumentException("La cuenta destino no puede ser nula.");
        }
        if (destino == this) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta.");
        }

        validarMonto(monto);
        if (monto > saldo) {
            throw new SaldoInsuficienteException(saldo, monto);
        }

        // Débito en cuenta origen
        saldo -= monto;
        registrar(TipoTransaccion.TRANSFERENCIA_ENVIADA, monto,
                  "Transferencia enviada a cuenta " + destino.getNumeroCuenta());

        // Crédito en cuenta destino
        destino.recibirTransferencia(monto, this.numeroCuenta);
    }

    /**
     * Recibe el crédito de una transferencia entrante.
     * Solo accesible desde el paquete modelo para mantener integridad.
     */
    void recibirTransferencia(double monto, String cuentaOrigen) {
        saldo += monto;
        registrar(TipoTransaccion.TRANSFERENCIA_RECIBIDA, monto,
                  "Transferencia recibida de cuenta " + cuentaOrigen);
    }

    // ── Funcionalidad 2: Historial de transacciones ──────────────────────────

    /**
     * Devuelve una vista no modificable del historial completo.
     */
    public List<Transaccion> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    /**
     * Devuelve solo las transacciones de un tipo específico.
     */
    public List<Transaccion> getHistorialPorTipo(TipoTransaccion tipo) {
        List<Transaccion> filtrado = new ArrayList<>();
        for (Transaccion t : historial) {
            if (t.getTipo() == tipo) {
                filtrado.add(t);
            }
        }
        return Collections.unmodifiableList(filtrado);
    }

    // ── Funcionalidad 3: Intereses y cargos ─────────────────────────────────

    /**
     * Aplica una tasa de interés al saldo actual.
     * Ej.: tasaAnual = 0.05 → 5% de interés.
     */
    public double aplicarInteres(double tasaAnual) throws MontoInvalidoException {
        if (tasaAnual <= 0 || tasaAnual > 1) {
            throw new MontoInvalidoException(tasaAnual);
        }
        double interes = saldo * tasaAnual;
        saldo += interes;
        registrar(TipoTransaccion.INTERES_APLICADO, interes,
                  String.format("Interés anual aplicado (%.2f%%)", tasaAnual * 100));
        return interes;
    }

    /**
     * Aplica un cargo fijo a la cuenta (ej.: mantenimiento).
     */
    public void aplicarCargo(double monto, String concepto)
            throws MontoInvalidoException, SaldoInsuficienteException {
        validarMonto(monto);
        if (monto > saldo) {
            throw new SaldoInsuficienteException(saldo, monto);
        }
        saldo -= monto;
        String desc = (concepto != null && !concepto.isBlank()) ? concepto : "Cargo de cuenta";
        registrar(TipoTransaccion.CARGO_APLICADO, monto, desc);
    }

    // ── Helpers privados ─────────────────────────────────────────────────────

    private void validarMonto(double monto) throws MontoInvalidoException {
        if (monto <= 0) {
            throw new MontoInvalidoException(monto);
        }
    }

    private void registrar(TipoTransaccion tipo, double monto, String descripcion) {
        historial.add(new Transaccion(tipo, monto, saldo, descripcion));
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular()      { return titular; }
    public double getSaldo()        { return saldo; }

    // ── Representación ───────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
            "Cuenta: %-10s | Titular: %-20s | Saldo: $%,.2f",
            numeroCuenta, titular, saldo
        );
    }
}
