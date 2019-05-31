package com.servicios.orquestador.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PagosController {
	
	
	private String urlCuentas = "http://www.mocky.io/v2/5cec67a33300007e6f6d7a8b";
	private String urlTarjetas = "http://www.mocky.io/v2/5cec669d33000092726d7a89";
	private String urlRegistros = "http://www.mocky.io/v2/5cec66a5330000165f6d7a8a";
	
	
	
	@PutMapping("/pago")
	public Pago pagar(@RequestBody OrdenPago ordenPago) {
		Double saldo;
		Double deuda;
		
		saldo = llamarCuenta(ordenPago.getCuenta(), ordenPago.getMonto());
		if (saldo < 0){
			deuda = getDeuda(ordenPago.getTarjeta());
			return new Pago(-saldo, deuda, "Rechazado: Fondos insuficientes");
		} 
		
		
		deuda = llamarTarjeta(ordenPago.getTarjeta(), ordenPago.getMonto());
		if (deuda < 0) {
			saldo = devolverFondos(ordenPago.getCuenta(), ordenPago.getMonto());
			return new Pago(saldo, -deuda, "Rechazado: La tarjeta indicada no posee tal deuda");	
		}
		
		llamarRegistro(ordenPago);
		return new Pago(saldo, deuda, "Aceptado: Pago exitoso");
		
	}
	
	private Double llamarCuenta(String cuenta, double monto) {
		OrdenCuenta orden = new OrdenCuenta(cuenta, monto);
		
		HttpEntity<String> request = new HttpEntity<String>(orden.toString());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> res = restTemplate.exchange(urlCuentas, HttpMethod.PUT, request, String.class);
		
		String[] json = res.getBody().split(":");
		double valor = Double.parseDouble(json[json.length-1]);
		
		if(res.getStatusCodeValue() == 201) {
			return valor;
		} else {
			return -valor;
		}
	}
	
	private Double devolverFondos(String cuenta, double monto) {
		return 0.0;
	}

	private Double llamarTarjeta(String tarjeta, double monto) {
		
		OrdenTarjeta orden = new OrdenTarjeta(tarjeta, monto);
		
		System.out.println(orden);
		
		HttpEntity<String> request = new HttpEntity<String>(orden.toString());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> res = restTemplate.exchange(urlTarjetas, HttpMethod.PUT, request, String.class);
		
		String[] json = res.getBody().split(":");
		double valor = Double.parseDouble(json[json.length-1]);
		
		if(res.getStatusCodeValue() == 201) {
			return valor;
		} else {
			return -valor;
		}
	}
	
	private Double getDeuda(String tarjeta) {
		// TODO Auto-generated method stub
		return 0.0;
	}
	
	private boolean llamarRegistro(OrdenPago orden){
		
		HttpEntity<String> request = new HttpEntity<String>(orden.toString());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> res = restTemplate.exchange(urlRegistros, HttpMethod.POST, request, String.class);
		
		if(res.getStatusCodeValue() == 201) {
			return true;
		} else {
			return false;
		}
	}
	
}
