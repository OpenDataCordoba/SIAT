//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Novedades de regimen simplificado
 * 
 * @author tecso
 */
@Entity
@Table(name = "dre_tipoTramiteRS")
public class TipoTramiteRS extends BaseBO {
	
	public static final Long ID_TRAMITE_ALTA=1L;
	public static final Long ID_TRAMITE_MODIFICACION=2L;
	public static final Long ID_TRAMITE_RECATEGORIZACION=3L;
	public static final Long ID_TRAMITE_BAJA=4L;
	public static final Long ID_TRAMITE_LISTARTRAMITES=5L;
	public static final Long ID_TRAMITE_VERTRAMITE=6L;
	
	private static final long serialVersionUID = 1L;


	@Column(name = "desTipoTramite")
	private String desTipoTramite;

	
	// Constructores
	public TipoTramiteRS(){
		super();
	}
	
	
	//Metodos de clase
	public static TipoTramiteRS getById(Long id) {
		return (TipoTramiteRS) RecDAOFactory.getTipoTramiteRSDAO().getById(id);  
	}

	public static TipoTramiteRS getByDes(String cod) {
		return (TipoTramiteRS) RecDAOFactory.getTipoTramiteRSDAO().getByDes(cod);  
	}
	
	public static TipoTramiteRS getByIdNull(Long id) {
		return (TipoTramiteRS) RecDAOFactory.getTipoTramiteRSDAO().getByIdNull(id);
	}
	
	public static List<TipoTramiteRS> getList() {
		return (List<TipoTramiteRS>) RecDAOFactory.getTipoTramiteRSDAO().getList();
	}
	
	public static List<TipoTramiteRS> getListActivos() {			
		return (List<TipoTramiteRS>) RecDAOFactory.getTipoTramiteRSDAO().getListActiva();
	}



	
	
	
	// Getters Y Setters 


	public String getDesTipoTramite() {
		return desTipoTramite;
	}


	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}



	

	
	// Metodos de Instancia
}
