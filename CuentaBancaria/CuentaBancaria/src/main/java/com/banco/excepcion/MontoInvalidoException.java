package com.banco.excepcion;

/**
 * Excepción lanzada cuando se intenta operar con un monto
 * negativo o igual a cero.
 */
public class MontoInvalidoException extends Exception {

    private final double monto;

    public MontoInvalidoException(double monto) {
        super(String.format(
            "Monto inválido: $%.2f. El monto debe ser mayor a cero.",
            monto
        ));
        this.monto = monto;
    }

    public double getMonto() { return monto; }
}
