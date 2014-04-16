//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;


/**
 * Bean correspondiente a UsuarioSiat
 * 
 * @author tecso
 */
public class UsuarioSiatVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;
	
	private String usuarioSIAT;
	private AreaVO area = new AreaVO();

	private ProcuradorVO procurador = new ProcuradorVO();
	private InvestigadorVO investigador = new InvestigadorVO();
	private AbogadoVO abogado = new AbogadoVO();
	private InspectorVO inspector = new InspectorVO();
	private SupervisorVO supervisor = new SupervisorVO();
	private MandatarioVO mandatario = new MandatarioVO();
	
	// TODO: cuando se definan crear los beans de estas entidades
	private Long idRNPA;
	// ------------------------------------------------------------

	// Constructores
	public UsuarioSiatVO(){
		super();
	}

	public UsuarioSiatVO(Long id){
		super();
		setId(id);
	}

	// Getters y setters
		
	public UsuarioSiatVO(int id, String user) {
		super();
		setId(new Long(id));
		setUsuario(user);
		
	}

	public MandatarioVO getMandatario() {
		return mandatario;
	}

	public void setMandatario(MandatarioVO mandatario) {
		this.mandatario = mandatario;
	}

	public Long getIdRNPA() {
		return idRNPA;
	}

	public InspectorVO getInspector() {
		return inspector;
	}

	public void setInspector(InspectorVO inspector) {
		this.inspector = inspector;
	}

	public void setIdRNPA(Long idRNPA) {
		this.idRNPA = idRNPA;
	}
	
	public String getUsuarioSIAT() {
		return usuarioSIAT;
	}

	public void setUsuarioSIAT(String usuarioSIAT) {
		this.usuarioSIAT = usuarioSIAT;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public InvestigadorVO getInvestigador() {
		return investigador;
	}

	public void setInvestigador(InvestigadorVO investigador) {
		this.investigador = investigador;
	}

	public AbogadoVO getAbogado() {
		return abogado;
	}
	public void setAbogado(AbogadoVO abogado) {
		this.abogado = abogado;
	}

	public SupervisorVO getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(SupervisorVO supervisor) {
		this.supervisor = supervisor;
	}

}
