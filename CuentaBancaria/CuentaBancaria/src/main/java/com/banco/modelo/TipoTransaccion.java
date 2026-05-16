package com.banco.modelo;

/**
 * Tipos de transacción que puede registrar una cuenta bancaria.
 */
public enum TipoTransaccion {
    DEPOSITO("Depósito"),
    RETIRO("Retiro"),
    TRANSFERENCIA_ENVIADA("Transferencia enviada"),
    TRANSFERENCIA_RECIBIDA("Transferencia recibida"),
    INTERES_APLICADO("Interés aplicado"),
    CARGO_APLICADO("Cargo aplicado");

    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
