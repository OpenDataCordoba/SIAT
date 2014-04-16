//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del OpeInvCon
 * @author tecso
 *
 */
public class OpeInvConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvConVO";
	
	private ContribuyenteVO contribuyente= new ContribuyenteVO();
	
	private OpeInvBusVO opeInvBus = new OpeInvBusVO();

	private String datosContribuyente;

	private EstadoOpeInvConVO estadoOpeInvCon = new EstadoOpeInvConVO();

	private String obsExclusion="";

	private String obsClasificacion="";

	private ZonaVO zona = new ZonaVO();

	private DomicilioVO domicilio = new DomicilioVO();

	private CuentaVO cuenta= new CuentaVO();

	private InvestigadorVO investigador = new InvestigadorVO();
	
	private ActaInvVO actaInv = new ActaInvVO();
	
	private OpeInvVO opeInv = new OpeInvVO();
	
	private boolean esContribuyente=true;
	
	private OrdenControlVO ordenControl;
	
	private List<OpeInvConCueVO> listOpeInvConCue = new ArrayList <OpeInvConCueVO>();
	
	private List<HisEstOpeInvConVO> listHisEstOpeInvCon = new ArrayList <HisEstOpeInvConVO>();
		
	private Date fechaVisita;
	private String fechaVisitaView="";
	
	private String desDomicilio="";
	
	// Buss Flags
	private Boolean excluirDeSelecBussEnabled = true;
	
	// View Constants
	
	
	// Constructores
	public OpeInvConVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OpeInvConVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public String getDatosContribuyente() {
		return datosContribuyente;
	}

	public void setDatosContribuyente(String datosContribuyente) {
		this.datosContribuyente = datosContribuyente;
	}

	public EstadoOpeInvConVO getEstadoOpeInvCon() {
		return estadoOpeInvCon;
	}

	public void setEstadoOpeInvCon(EstadoOpeInvConVO estadoOpeInvCon) {
		this.estadoOpeInvCon = estadoOpeInvCon;
	}

	public String getObsExclusion() {
		return obsExclusion;
	}

	public void setObsExclusion(String obsExclusion) {
		this.obsExclusion = obsExclusion;
	}

	public String getObsClasificacion() {
		return obsClasificacion;
	}

	public void setObsClasificacion(String obsClasificacion) {
		this.obsClasificacion = obsClasificacion;
	}

	public ZonaVO getZona() {
		return zona;
	}

	public void setZona(ZonaVO zona) {
		this.zona = zona;
	}

	public DomicilioVO getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(DomicilioVO domicilio) {
		this.domicilio = domicilio;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public InvestigadorVO getInvestigador() {
		return investigador;
	}

	public void setInvestigador(InvestigadorVO investigador) {
		this.investigador = investigador;
	}

	public OpeInvVO getOpeInv() {
		return opeInv;
	}

	public void setOpeInv(OpeInvVO opeInv) {
		this.opeInv = opeInv;
	}

	public boolean getEsContribuyente() {
		return esContribuyente;
	}

	public void setEsContribuyente(boolean esContribuyente) {
		this.esContribuyente = esContribuyente;
	}

	public OpeInvBusVO getOpeInvBus() {
		return opeInvBus;
	}

	public void setOpeInvBus(OpeInvBusVO opeInvBus) {
		this.opeInvBus = opeInvBus;
	}

	public List<OpeInvConCueVO> getListOpeInvConCue() {
		return listOpeInvConCue;
	}

	public void setListOpeInvConCue(List<OpeInvConCueVO> listOpeInvConCue) {
		this.listOpeInvConCue = listOpeInvConCue;
	}

	public List<HisEstOpeInvConVO> getListHisEstOpeInvCon() {
		return listHisEstOpeInvCon;
	}

	public void setListHisEstOpeInvCon(List<HisEstOpeInvConVO> listHisEstOpeInvCon) {
		this.listHisEstOpeInvCon = listHisEstOpeInvCon;
	}

	public Date getFechaVisita() {
		return fechaVisita;
	}

	public void setFechaVisita(Date fechaVisita) {
		this.fechaVisita = fechaVisita;
		this.fechaVisitaView = DateUtil.formatDate(fechaVisita, DateUtil.ddSMMSYY_MASK);
	}

	public String getFechaVisitaView() {
		return fechaVisitaView;
	}

	public void setFechaVisitaView(String fechaVisitaView) {
		this.fechaVisitaView = fechaVisitaView;
	}

	
	public ActaInvVO getActaInv() {
		return actaInv;
	}

	public void setActaInv(ActaInvVO actaInv) {
		this.actaInv = actaInv;
	}

	
	
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	// Buss flags getters y setters
	/**
	 * Verifica si alguna de las cuentas de la lista de ListOpeInvConCue esta seleccionada
	 */
	public boolean getTieneCuentaSeleccionada(){
		for(OpeInvConCueVO opeInvConCueVO: listOpeInvConCue){
			if(opeInvConCueVO.getEsSeleccionada())
				return true;
		}
		return false;
	}

	public boolean getTieneCuenta4Recurso(Long idRecurso) {
		for(OpeInvConCueVO opeInvConCueVO: listOpeInvConCue){
			if(opeInvConCueVO.getCuenta().getRecurso().getId().equals(idRecurso))
				return true;
		}
		return false;
	}

	
	/**
	 * Obtiene el OpeInvConCue de la listOpeInvConCue, que tiene la marca "esSeleccionada" 
	 * @return null si no tiene ninguna cuenta seleccionada
	 */
	public OpeInvConCueVO getOpeInvConCueCuentaSelec() {
		if(listOpeInvConCue!=null){
			for(OpeInvConCueVO opeInvConCue: listOpeInvConCue){
				if(opeInvConCue.getEsSeleccionada())
					return opeInvConCue;
			}
		}
		return new OpeInvConCueVO();
	}

	
	public Boolean getExcluirDeSelecBussEnabled() {
		return excluirDeSelecBussEnabled;
	}

	public void setExcluirDeSelecBussEnabled(Boolean excluirDeSelec) {
		this.excluirDeSelecBussEnabled = excluirDeSelec;
	}

	public String getDesDomicilio() {
		return desDomicilio;
	}

	public void setDesDomicilio(String desDomicilio) {
		this.desDomicilio = desDomicilio;
	}

	
	
	// View flags getters
	
	
	// View getters
}
