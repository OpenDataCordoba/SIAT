//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * ClasificacionDeuda
 * @author tecso
 *
 */
@Entity
@Table(name = "def_clasificDeuda")
public class ClasificDeuda extends BaseBO {
	
	@Column(name = "desClasificDeuda")
	private String desClasificDeuda;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idTributo") 
	private Tributo tributo;
	
	public ClasificDeuda() {
		super();
	}

	public String getDesClasificDeuda() {
		return desClasificDeuda;
	}

	public void setDesClasificDeuda(String desClasificDeuda) {
		this.desClasificDeuda = desClasificDeuda;
	}

	public Tributo getTributo() {
		return tributo;
	}

	public void setTributo(Tributo tributo) {
		this.tributo = tributo;
	}
	
}
