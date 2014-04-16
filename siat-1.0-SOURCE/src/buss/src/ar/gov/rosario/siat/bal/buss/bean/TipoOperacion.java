//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipo Operacion de transacciones de Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoOperacion")
public class TipoOperacion extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_TIPO_DJ=1L;
	public static final long ID_TIPO_PAGO=2L;
	public static final long ID_TIPO_DDJJ_Y_PAGO=3L;

	@Column(name="desTipoOperacion")
	private String desTipoOperacion;
	
	
	// Constructores
	public TipoOperacion(){
		super();
	}
	
	
	//Metodos de clase
	public static TipoOperacion getById(Long id) {
		return (TipoOperacion) BalDAOFactory.getTipoOperacionDAO().getById(id);  
	}
	
	public static TipoOperacion getByIdNull(Long id) {
		return (TipoOperacion) BalDAOFactory.getTipoOperacionDAO().getByIdNull(id);
	}
	
	public static List<TipoOperacion> getList() {
		return (List<TipoOperacion>) BalDAOFactory.getTipoOperacionDAO().getList();
	}
	
	public static List<TipoOperacion> getListActivos() {			
		return (List<TipoOperacion>) BalDAOFactory.getTipoOperacionDAO().getListActiva();
	}


	public String getDesTipoOperacion() {
		return desTipoOperacion;
	}


	public void setDesTipoOperacion(String desTipoOperacion) {
		this.desTipoOperacion = desTipoOperacion;
	}

	
	// Metodos de Instancia

	
	
}
