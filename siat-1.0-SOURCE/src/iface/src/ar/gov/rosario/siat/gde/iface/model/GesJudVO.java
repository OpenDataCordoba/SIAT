//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class GesJudVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desGesJud = "";
	private ProcuradorVO procurador = new ProcuradorVO();
	private Date fechaCaducidad = new Date();
	private Date fechaAlta=new Date();
	private EstadoGesJudVO estadoGesJudVO = new EstadoGesJudVO();
	private String juzgado = "";
	private String observacion = "";
	private String usrCreador = "";
	private TipoJuzgadoVO tipoJuzgado= new TipoJuzgadoVO();
	private CasoVO caso = new CasoVO();
	private Long nroExpediente;
	private Long anioExpediente;	
	
	private List<GesJudDeuVO> listGesJudDeu = new ArrayList<GesJudDeuVO>();
	private List<GesJudEventoVO> listGesJudEvento = new ArrayList<GesJudEventoVO>();
	private List<HistGesJudVO> listHistGesJud = new ArrayList<HistGesJudVO>();

	private String fechaCaducidadView="";
	private String nroExpedienteView="";
	private String anioExpedienteView="";
	
	
	//flags de permisos
	private boolean registrarCaducidadBussEnabled=false;

	private String fechaAltaView="";
	
	// Getters y Setters
	public String getDesGesJud() {
		return desGesJud;
	}

	public void setDesGesJud(String desGesJud) {
		this.desGesJud = desGesJud;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
		this.fechaCaducidadView = DateUtil.formatDate(fechaCaducidad, DateUtil.ddSMMSYYYY_MASK);
		this.fechaCaducidadView = DateUtil.formatDate(fechaCaducidad, "dd/MM/yyyy");
	}

	public String getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(String juzgado) {
		this.juzgado = juzgado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<GesJudDeuVO> getListGesJudDeu() {
		return listGesJudDeu;
	}

	public void setListGesJudDeu(List<GesJudDeuVO> listGesJudDeu) {
		this.listGesJudDeu = listGesJudDeu;
	}

	public List<HistGesJudVO> getListHistGesJud() {
		return listHistGesJud;
	}

	public void setListHistGesJud(List<HistGesJudVO> listHistGesJud) {
		this.listHistGesJud = listHistGesJud;
	}

	public EstadoGesJudVO getEstadoGesJudVO() {
		return estadoGesJudVO;
	}

	public void setEstadoGesJudVO(EstadoGesJudVO estadoGesJudVO) {
		this.estadoGesJudVO = estadoGesJudVO;
	}

	public List<GesJudEventoVO> getListGesJudEvento() {
		return listGesJudEvento;
	}

	public void setListGesJudEvento(List<GesJudEventoVO> listGesJudEvento) {
		this.listGesJudEvento = listGesJudEvento;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}

	public TipoJuzgadoVO getTipoJuzgado() {
		return tipoJuzgado;
	}

	public void setTipoJuzgado(TipoJuzgadoVO tipoJuzgado) {
		this.tipoJuzgado = tipoJuzgado;
	}

	public String getFechaAltaView() {
		return fechaAltaView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public String getUsrCreador() {
		return usrCreador;
	}

	public void setUsrCreador(String usrCreador) {
		this.usrCreador = usrCreador;
	}
	
	public Long getNroExpediente() {
		return nroExpediente;
	}

	public void setNroExpediente(Long nroExpediente) {
		this.nroExpediente = nroExpediente;
		this.nroExpedienteView = StringUtil.formatLong(nroExpediente);
	}

	public Long getAnioExpediente() {
		return anioExpediente;
	}

	public void setAnioExpediente(Long anioExpediente) {
		this.anioExpediente = anioExpediente;
		this.anioExpedienteView = StringUtil.formatLong(anioExpediente);
	}

	// view getters
	public String getFechaCaducidadView() {
		return fechaCaducidadView;
	}

	public void setFechaCaducidadView(String fechaCaducidadView) {
		this.fechaCaducidadView = fechaCaducidadView;		
	}
	
	// flags getters
	public boolean isRegistrarCaducidadBussEnabled() {
		return registrarCaducidadBussEnabled;
	}

	public void setRegistrarCaducidadBussEnabled(
			boolean registrarCaducidadBussEnabled) {
		this.registrarCaducidadBussEnabled = registrarCaducidadBussEnabled;
	}

	public String getNroExpedienteView() {
		return nroExpedienteView;
	}

	public void setNroExpedienteView(String nroExpedienteView) {
		this.nroExpedienteView = nroExpedienteView;
	}

	public String getAnioExpedienteView() {
		return anioExpedienteView;
	}

	public void setAnioExpedienteView(String anioExpedienteView) {
		this.anioExpedienteView = anioExpedienteView;
	}

	
}
