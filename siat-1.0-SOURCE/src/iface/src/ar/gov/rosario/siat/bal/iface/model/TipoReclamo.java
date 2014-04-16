//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.IDemodaEmun;

public enum TipoReclamo implements IDemodaEmun {

	DEUDA_MIGRADA_RECIBO(1, "Deuda Migrada y con Recibo"),
	DEUDA_MIGRADA_SINRECIBO(2, "Deuda Migrada y sin Recibo"),
	DEUDA_SIAT_RECIBO(3, "Deuda Siat y con Recibo"),
	DEUDA_SIAT_SINRECIBO(4, "Deuda Siat y sin Recibo"),
	
	CUOTA_MIGRADA_RECIBO(5, "Cuota Migrada y con Recibo"),
	CUOTA_MIGRADA_SINRECIBO(6, "Cuota Migrada y sin Recibo"),
	CUOTA_SIAT_RECIBO(7, "Cuota Siat y con Recibo"),
	CUOTA_SIAT_SINRECIBO(8, "Cuota Siat y sin Recibo");

	private Integer id;
	private String value;


	private TipoReclamo(final Integer id, final String value) {
		this.id = id;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return "TipoEstadoCueExe: [" + id + ", " + value + "]";
	}

	public static TipoReclamo getById(Integer _id) {
		TipoReclamo[] tipoBoletas = TipoReclamo.values();
		for (int i = 0; i < tipoBoletas.length; i++) {
			TipoReclamo tipoBoleta = tipoBoletas[i];
			if (_id == null){
				if (tipoBoleta.id == null){
					return tipoBoleta;
				}  				   
			} else {
				if (tipoBoleta.id.intValue() == _id.intValue()){
					return tipoBoleta;
				}
			}
		}
		return null;
	}

	/**
	 * Devuelve un Id valido para persistir, en un create o update.
	 *
	 * @return el numero del SiNo.
	 */
	public Integer getBussId(){
		if (id < 0)
			return null;
		else
			return id;
	}

	public static List<TipoReclamo> getList(){

		List<TipoReclamo> listTipoBoleta = new ArrayList<TipoReclamo>();

		//Obtengo la lista de tipoBoleta
		TipoReclamo[] tipoBoleta = TipoReclamo.values();

		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
		for (int i = 0; i < tipoBoleta.length; i++) {
			if (tipoBoleta[i].getId() != null &&
					tipoBoleta[i].getId() >= 0)
				listTipoBoleta.add(tipoBoleta[i]);
		}
		return listTipoBoleta;
	}

	public static List<TipoReclamo> getListEstadosValidos(){

		List<TipoReclamo> listTipoBoleta = new ArrayList<TipoReclamo>();

		//Obtengo la lista de TipoBoleta
		TipoReclamo[] tipoBoleta = TipoReclamo.values();

		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
		for (int i = 0; i < 4; i++) {
			if (tipoBoleta[i].getId() != null &&
					tipoBoleta[i].getId() >= 0)
				listTipoBoleta.add(tipoBoleta[i]);
		}
		return listTipoBoleta;
	}
}
