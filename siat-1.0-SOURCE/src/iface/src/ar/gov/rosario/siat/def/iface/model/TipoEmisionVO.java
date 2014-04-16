//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Representa los tipos de Emisión que admite un Recurso.
 * @author tecso
 *
 */
public class TipoEmisionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoEmisionVO";
	
	public static final Long ID_INDIVIDUAL 	   = 1L;
	public static final Long ID_MASIVA 	  	   = 2L;
	public static final Long ID_CORREGIDA 	   = 3L;
	public static final Long ID_EXTRAORDINARIA = 4L;
	
	// A deprecar
	public static final String ID_TIPOEMISION = "idTipoEmision";
 	public static final Long ID_EMISIONCDM 	  = 100L;
	public static final Long ID_IMPRESIONCDM  = 101L;
	public static final Long ID_EMISIONCORCDM = 102L;

	private String desTipoEmision;
	
	// Constructores
	public TipoEmisionVO(){
		super();
	}
	
	// Constructores
	public TipoEmisionVO(Long id){
		super(id);
	}

	public TipoEmisionVO(int id, String desTipoEmision) {
		super(id);
		setDesTipoEmision(desTipoEmision);
	}
	
	// Getters y Setters
	public String getDesTipoEmision(){
		return desTipoEmision;
	}
	
	public void setDesTipoEmision(String desTipoEmision){
		this.desTipoEmision = desTipoEmision;
	}

	// A DEPRECAR
	public boolean getEsEmisionCdm(){
		return ID_EMISIONCDM.equals(this.getId());
	}
	public boolean getEsImpresionCdm(){
		return ID_IMPRESIONCDM.equals(this.getId());
	}
	public boolean getEsEmisionCorCdm(){
		return ID_EMISIONCORCDM.equals(this.getId());
	}
	
}
