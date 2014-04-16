//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Tributo
 * @author tecso
 *
 */
@Entity
@Table(name = "def_tributo")
public class Tributo extends BaseBO {

	@Column(name = "codTributo")
	private String codTributo;

	@Column(name = "desTributo")
	private String desTributo;

	public Tributo() {
		super();
	}

	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

	public String getDesTributo() {
		return desTributo;
	}
	public void setDesTributo(String desTributo) {
		this.desTributo = desTributo;
	}	
}
