//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * Adapter del DeudaPrivilegio
 * 
 * @author tecso
 */
public class DeudaPrivilegioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "deudaPrivilegioAdapterVO";
	
    private DeudaPrivilegioVO deudaPrivilegio = new DeudaPrivilegioVO();
    
    private List<TipoPrivilegioVO>  listTipoPrivilegio = new ArrayList<TipoPrivilegioVO>();
    private List<RecursoVO>  listRecurso = new ArrayList<RecursoVO>();
    private List<CuentaVO>  listCuenta = new ArrayList<CuentaVO>();

    
    // Constructores
    public DeudaPrivilegioAdapter(){
    	super(CyqSecurityConstants.ABM_DEUDAPRIVILEGIO);
    }
    
    //  Getters y Setters
	public DeudaPrivilegioVO getDeudaPrivilegio() {
		return deudaPrivilegio;
	}
	public void setDeudaPrivilegio(DeudaPrivilegioVO deudaPrivilegioVO) {
		this.deudaPrivilegio = deudaPrivilegioVO;
	}
	
	public String getName(){
		return NAME;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoPrivilegioVO> getListTipoPrivilegio() {
		return listTipoPrivilegio;
	}

	public void setListTipoPrivilegio(List<TipoPrivilegioVO> listTipoPrivilegio) {
		this.listTipoPrivilegio = listTipoPrivilegio;
	}

	public List<CuentaVO> getListCuenta() {
		return listCuenta;
	}
	public void setListCuenta(List<CuentaVO> listCuenta) {
		this.listCuenta = listCuenta;
	}
	
	// View getters
	
}
