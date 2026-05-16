package com.banco.excepcion;

/**
 * Excepción lanzada cuando una cuenta no tiene saldo suficiente
 * para completar una operación.
 */
public class SaldoInsuficienteException extends Exception {

    private final double saldoActual;
    private final double montoRequerido;

    public SaldoInsuficienteException(double saldoActual, double montoRequerido) {
        super(String.format(
            "Saldo insuficiente. Saldo actual: $%.2f | Monto requerido: $%.2f",
            saldoActual, montoRequerido
        ));
        this.saldoActual = saldoActual;
        this.montoRequerido = montoRequerido;
    }

    public double getSaldoActual() { return saldoActual; }
    public double getMontoRequerido() { return montoRequerido; }
}
