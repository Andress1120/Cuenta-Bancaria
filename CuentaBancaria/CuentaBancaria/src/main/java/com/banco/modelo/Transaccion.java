package com.banco.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un movimiento individual en el historial de una cuenta.
 * Inmutable: una vez creada no puede modificarse (encapsulación total).
 */
public class Transaccion {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final TipoTransaccion tipo;
    private final double monto;
    private final double saldoResultante;
    private final String descripcion;
    private final LocalDateTime fecha;

    public Transaccion(TipoTransaccion tipo,
                       double monto,
                       double saldoResultante,
                       String descripcion) {
        this.tipo             = tipo;
        this.monto            = monto;
        this.saldoResultante  = saldoResultante;
        this.descripcion      = descripcion;
        this.fecha            = LocalDateTime.now();
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public TipoTransaccion getTipo()           { return tipo; }
    public double          getMonto()          { return monto; }
    public double          getSaldoResultante(){ return saldoResultante; }
    public String          getDescripcion()    { return descripcion; }
    public LocalDateTime   getFecha()          { return fecha; }

    // ── Representación ──────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
            "[%s] %-26s | Monto: %+10.2f | Saldo: %10.2f | %s",
            fecha.format(FORMATO_FECHA),
            tipo.getDescripcion(),
            (tipo == TipoTransaccion.RETIRO ||
             tipo == TipoTransaccion.TRANSFERENCIA_ENVIADA ||
             tipo == TipoTransaccion.CARGO_APLICADO)
                ? -monto : monto,
            saldoResultante,
            descripcion
        );
    }
}
