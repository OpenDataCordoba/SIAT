//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import ar.gov.rosario.siat.pad.iface.model.LetraCuit;
import coop.tecso.demoda.buss.bean.BaseBO;

public class Cuit extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	private Long numero;
	private LetraCuit letraCuit;
	
	
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public LetraCuit getLetraCuit() {
		return letraCuit;
	}
	public void setLetraCuit(LetraCuit letraCuit) {
		this.letraCuit = letraCuit;
	}

}
