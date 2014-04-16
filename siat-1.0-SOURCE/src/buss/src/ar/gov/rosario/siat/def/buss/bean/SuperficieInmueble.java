//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a las Superficies de Inmuebles
 * habilitadas para la Emision de Tasa por Revision de
 * Planos
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_superficieInmueble")
public class SuperficieInmueble extends BaseBO {

	// Propiedades
	private static final long serialVersionUID = 1L;

	@Column(name = "desSuperficieInmueble") 
	private String desSuperficieInmueble;

	@Column(name = "valor") 
	private Double valor;

	// Constructores
	public SuperficieInmueble() {
		super();
	}

	// Metodos de clase
	public static SuperficieInmueble getById(Long id) {
		return (SuperficieInmueble) DefDAOFactory.getSuperficieInmuebleDAO().getById(id);
	}
	
	public static SuperficieInmueble getByIdNull(Long id) {
		return (SuperficieInmueble) DefDAOFactory.getSuperficieInmuebleDAO().getByIdNull(id);
	}

	public static List<SuperficieInmueble> getListActivas() {			
		return (ArrayList<SuperficieInmueble>) DefDAOFactory.getSuperficieInmuebleDAO().getListActiva();
	}

	// Getters y setters
	public String getDesSuperficieInmueble() {
		return desSuperficieInmueble;
	}

	public void setDesSuperficieInmueble(String desSuperficieInmueble) {
		this.desSuperficieInmueble = desSuperficieInmueble;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
}