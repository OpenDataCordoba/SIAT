//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * Adapter de Emision
 * 
 * @author tecso
 */
public class EmisionAdapter extends SiatAdapterModel {
 
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "emisionAdapterVO";
	
	private EmisionVO emision = new EmisionVO();
    
	private CuentaVO cuenta = new CuentaVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	//Emisiones Concretas
	private EmisionCdMAdapter 	emisionCdM 	 = new EmisionCdMAdapter();
	
	private ImpresionCdMAdapter impresionCdM = new ImpresionCdMAdapter();
	
	private EmisionCorCdMAdapter emisionCorCdM = new EmisionCorCdMAdapter();
	
	private EmisionTgiAdapter 	emisionTgi 	 = new EmisionTgiAdapter();
	
	private EmisionExtAdapter 	emisionExt 	 = new EmisionExtAdapter();
	
	private EmisionResumenLiqDeuAdapter emisionResumenLiqDeu = new EmisionResumenLiqDeuAdapter(); 
	
	
	// Constructores
    public EmisionAdapter(){
    	super();
    }

    //Getters y Setters
	public EmisionVO getEmision() {
		return emision;
	}

	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public EmisionCdMAdapter getEmisionCdM() {
		return emisionCdM;
	}

	public void setEmisionCdM(EmisionCdMAdapter emisionCdM) {
		this.emisionCdM = emisionCdM;
	}

	public ImpresionCdMAdapter getImpresionCdM() {
		return impresionCdM;
	}

	public void setImpresionCdM(ImpresionCdMAdapter impresionCdM) {
		this.impresionCdM = impresionCdM;
	}

	public EmisionCorCdMAdapter getEmisionCorCdM() {
		return emisionCorCdM;
	}

	public void setEmisionCorCdM(EmisionCorCdMAdapter emisionCorCdM) {
		this.emisionCorCdM = emisionCorCdM;
	}

	public EmisionTgiAdapter getEmisionTgi() {
		return emisionTgi;
	}
	public void setEmisionTgi(EmisionTgiAdapter emisionTgi) {
		this.emisionTgi = emisionTgi;
	}

	public EmisionExtAdapter getEmisionExt() {
		return emisionExt;
	}

	public void setEmisionExt(EmisionExtAdapter emisionExt) {
		this.emisionExt = emisionExt;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public EmisionResumenLiqDeuAdapter getEmisionResumenLiqDeu() {
		return emisionResumenLiqDeu;
	}

	public void setEmisionResumenLiqDeu(
			EmisionResumenLiqDeuAdapter emisionResumenLiqDeu) {
		this.emisionResumenLiqDeu = emisionResumenLiqDeu;
	}

}


	

