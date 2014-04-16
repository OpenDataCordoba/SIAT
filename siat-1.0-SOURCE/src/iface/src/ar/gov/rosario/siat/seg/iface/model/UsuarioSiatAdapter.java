//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;

/**
 * Adapter del UsuarioSiat
 * 
 * @author tecso
 */
public class UsuarioSiatAdapter extends SiatAdapterModel{
	
	public static final String NAME = "usuarioSiatAdapterVO";
	public static final String NO_ES_PROCURADOR = "No es procurador";
	public static final String NO_ES_INVESTIGADOR = "No es Investigador";
	public static final String NO_ES_ABOGADO = "No es Abogado";
	public static final String NO_ES_INSPECTOR = "No es Inspector";
	public static final String NO_ES_SUPERVISOR = "No es Supervisor";
	public static final String NO_ES_MANDATARIO = "No es Mandatario";
	
    private UsuarioSiatVO usuarioSiat = new UsuarioSiatVO();
    
    private List<AreaVO> listArea = new ArrayList<AreaVO>();
    private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
    private List<InvestigadorVO> listInvestigador = new ArrayList<InvestigadorVO>();
    private List<InspectorVO> listInspector = new ArrayList<InspectorVO>();
    private List<SupervisorVO> listSupervisor = new ArrayList<SupervisorVO>();
    private List<AbogadoVO> listAbogado = new ArrayList<AbogadoVO>();
    private List<MandatarioVO> listMandatario = new ArrayList<MandatarioVO>();
    
    // Constructores
    public UsuarioSiatAdapter(){
    	super(SegSecurityConstants.ABM_USUARIOSIAT);
    }
    
	//  Getters y Setters
	public UsuarioSiatVO getUsuarioSiat() {
		return usuarioSiat;
	}

	public void setUsuarioSiat(UsuarioSiatVO usuarioSiatVO) {
		this.usuarioSiat = usuarioSiatVO;
	}

	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<InvestigadorVO> getListInvestigador() {
		return listInvestigador;
	}

	public void setListInvestigador(List<InvestigadorVO> listInvestigador) {
		this.listInvestigador = listInvestigador;
	}

	public List<AbogadoVO> getListAbogado() {
		return listAbogado;
	}
	public void setListAbogado(List<AbogadoVO> listAbogado) {
		this.listAbogado = listAbogado;
	}

	public List<InspectorVO> getListInspector() {
		return listInspector;
	}

	public void setListInspector(List<InspectorVO> listInspector) {
		this.listInspector = listInspector;
	}

	public List<SupervisorVO> getListSupervisor() {
		return listSupervisor;
	}

	public void setListSupervisor(List<SupervisorVO> listSupervisor) {
		this.listSupervisor = listSupervisor;
	}
	

    public List<MandatarioVO> getListMandatario() {
		return listMandatario;
	}

	public void setListMandatario(List<MandatarioVO> listMandatario) {
		this.listMandatario = listMandatario;
	}

	// View getters
}