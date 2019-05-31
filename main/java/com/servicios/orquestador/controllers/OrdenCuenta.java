package com.servicios.orquestador.controllers;

/**
 * Clase encargada de manejar el formato que recibe el API de Cuentas de ahorro
 * @author jjparra
 *
 */
public class OrdenCuenta {

	/**
	 * Nombres de variables como las solicita el API
	 */
	private String numCuenta;
	private Double monto;
	
	
	public OrdenCuenta() {}
	
	public OrdenCuenta(String numTarjeta, Double monto) {
		this.numCuenta = numTarjeta;
		this.monto = monto;
	}
	
	public String getCuenta() {
		return numCuenta;
	}
	public void setCuenta(String tarjeta) {
		this.numCuenta = tarjeta;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	
	public String toString() {
		return "{ \"numCuenta\":\""+ numCuenta + "\", \"monto\":"  + monto + "}";
	}
}
