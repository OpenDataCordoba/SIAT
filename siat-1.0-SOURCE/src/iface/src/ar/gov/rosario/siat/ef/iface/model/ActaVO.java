//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.HOUR_MINUTE_MASK;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ActaVO
 * @author tecso
 *
 */
public class ActaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "actaVO";
	
	private OrdenControlVO ordenControl;

	private TipoActaVO tipoActa = new TipoActaVO();

	private Long numeroActa;

	private Date fechaVisita;

	private Date horaVisita;

	private PersonaVO persona = new PersonaVO();

	private String enCaracter;

	private Date fechaPresentacion;

	private Date horaPresentacion;

	private String lugarPresentacion;
	
	private List<OrdConDocVO> listOrdConDoc = new ArrayList<OrdConDocVO>();
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaPresentacionView = "";
	private String fechaVisitaView = "";
	private String horaPresentacionView = "";
	private String horaVisitaView = "";


	private String numeroActaView = "";


	// Constructores
	public ActaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ActaVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public TipoActaVO getTipoActa() {
		return tipoActa;
	}

	public void setTipoActa(TipoActaVO tipoActa) {
		this.tipoActa = tipoActa;
	}

	public Long getNumeroActa() {
		return numeroActa;
	}

	public void setNumeroActa(Long numeroActa) {
		this.numeroActa = numeroActa;
		this.numeroActaView = StringUtil.formatLong(numeroActa);
	}

	public Date getFechaVisita() {
		return fechaVisita;
	}

	public void setFechaVisita(Date fechaVisita) {
		this.fechaVisita = fechaVisita;
		this.fechaVisitaView = DateUtil.formatDate(fechaVisita, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getHoraVisita() {
		return horaVisita;
	}

	@HOUR_MINUTE_MASK
	public void setHoraVisita(Date horaVisita) {
		this.horaVisita = horaVisita;
		this.horaVisitaView = DateUtil.formatDate(horaVisita, DateUtil.HOUR_MINUTE_MASK);
	}

	

	public PersonaVO getPersona() {
		return persona;
	}

	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}

	

	public String getEnCaracter() {
		return enCaracter;
	}

	public void setEnCaracter(String enCaracter) {
		this.enCaracter = enCaracter;
	}

	public Date getFechaPresentacion() {
		return fechaPresentacion;
	}

	public void setFechaPresentacion(Date fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
		this.fechaPresentacionView = DateUtil.formatDate(fechaPresentacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getHoraPresentacion() {
		return horaPresentacion;
	}

	@HOUR_MINUTE_MASK
	public void setHoraPresentacion(Date horaPresentacion) {
		this.horaPresentacion = horaPresentacion;
		this.horaPresentacionView = DateUtil.formatDate(horaPresentacion, DateUtil.HOUR_MINUTE_MASK);
	}

	public String getLugarPresentacion() {
		return lugarPresentacion;
	}

	public void setLugarPresentacion(String lugarPresentacion) {
		this.lugarPresentacion = lugarPresentacion;
	}

	
	
	public List<OrdConDocVO> getListOrdConDoc() {
		return listOrdConDoc;
	}

	public void setListOrdConDoc(List<OrdConDocVO> listOrdConDoc) {
		this.listOrdConDoc = listOrdConDoc;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	

	// View getters
	public void setNumeroActaView(String numeroActaView) {
		this.numeroActaView = numeroActaView;
	}
	public String getNumeroActaView() {
		return numeroActaView;
	}

	public void setFechaPresentacionView(String fechaPresentacionView) {
		this.fechaPresentacionView = fechaPresentacionView;
	}
	public String getFechaPresentacionView() {
		return fechaPresentacionView;
	}

	public void setFechaVisitaView(String fechaVisitaView) {
		this.fechaVisitaView = fechaVisitaView;
	}
	public String getFechaVisitaView() {
		return fechaVisitaView;
	}

	public void setHoraPresentacionView(String horaPresentacionView) {
		this.horaPresentacionView = horaPresentacionView;
	}
	public String getHoraPresentacionView() {
		return horaPresentacionView;
	}

	public void setHoraVisitaView(String horaVisitaView) {
		this.horaVisitaView = horaVisitaView;
	}
	public String getHoraVisitaView() {
		return horaVisitaView;
	}

}
