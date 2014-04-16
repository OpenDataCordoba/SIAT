//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoIndet
 * @author tecso
 *
 */
public class TipoIndetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoIndetVO";
	
	//<#Propiedades#>
	private String codTipoIndet;
	private String desTipoIndet="";
	private String codIndetMR;

//	private TipoDeudaVO tipoDeuda = new TipoDeudaVO();
//	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
//	private RecursoVO recurso = new RecursoVO();
	
//	private String codTipoIndetView="";
	
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoIndetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoIndetVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoIndet(desc);
	}

		
	// Getters y Setters
	

	public void setDesTipoIndet(String desTipoIndet) {
		this.desTipoIndet = desTipoIndet;
	}

	public String getCodTipoIndet() {
		return codTipoIndet;
	}

	public void setCodTipoIndet(String codTipoIndet) {
		this.codTipoIndet = codTipoIndet;
	}

	public String getDesTipoIndet() {
		return desTipoIndet;
	}

	public String getCodIndetMR() {
		return codIndetMR;
	}

	public void setCodIndetMR(String codIndetMR) {
		this.codIndetMR = codIndetMR;
	}
	
		
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
//	public String getCodTipoIndetView() {
//		return codTipoIndetView;
//	}
//
//	public void setCodTipoIndetView(String codTipoIndetView) {
//		this.codTipoIndetView = codTipoIndetView;
//	}
	
}
