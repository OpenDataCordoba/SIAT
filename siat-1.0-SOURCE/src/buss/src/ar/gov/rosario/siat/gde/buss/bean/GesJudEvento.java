//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.EventoVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a GesJudEvento
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_gesJudEvento")
public class GesJudEvento extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idGesJud")
	private GesJud gesJud;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEvento")
	private Evento evento;

	@Column(name = "fechaEvento")
	private Date fechaEvento;

	@Column(name = "observacion")
	private String observacion;

	// <#Propiedades#>

	// Constructores
	public GesJudEvento() {
		super();
		// Seteo de valores default
	}

	public GesJudEvento(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static GesJudEvento getById(Long id) {
		GesJudEvento e = (GesJudEvento) GdeDAOFactory.getGesJudEventoDAO().getById(id);
		e.getGesJud().setTipoJuzgado(TipoJuzgado.getBycodigo(String.valueOf(e.getGesJud().getCodTipoJuzgado())));
		return e;
	}

	public static GesJudEvento getByIdNull(Long id) {
		return (GesJudEvento) GdeDAOFactory.getGesJudEventoDAO()
				.getByIdNull(id);
	}

	public static List<GesJudEvento> getList() {
		return (List<GesJudEvento>) GdeDAOFactory.getGesJudEventoDAO()
				.getList();
	}

	public static List<GesJudEvento> getListActivos() {
		return (List<GesJudEvento>) GdeDAOFactory.getGesJudEventoDAO()
				.getListActiva();
	}

	/**
	 * Hace el toVO(0, false). Setea gesJud con toVO(0, false) y el evento con toVO(1, false)
	 * @return
	 * @throws Exception
	 */
	public GesJudEventoVO toVoForView() throws Exception{
		GesJudEventoVO gesJudEventoVO = (GesJudEventoVO) this.toVO(0,false);
		gesJudEventoVO.setGesJud((GesJudVO) this.gesJud.toVO(0,false));
		gesJudEventoVO.setEvento((EventoVO) this.evento.toVO(1, false));
		return gesJudEventoVO;
	}
	
	// Getters y setters
	public GesJud getGesJud() {
		return gesJud;
	}

	public void setGesJud(GesJud gesJud) {
		this.gesJud = gesJud;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Date getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio
		if(!getGesJud().contienePredecedores(getEvento())){
				addRecoverableError(GdeError.GESJUD_ERRORS_INSERT_EVENTO_NO_CONTIENE_PREDECESORES);
				return false;
		}
		
		GesJudEvento ultimoEvento = getGesJud().getUltimoEventoIngresado();
		if(ultimoEvento!=null){
			if(DateUtil.isDateBefore(getFechaEvento(), ultimoEvento.getFechaEvento())){
				addRecoverableError(GdeError.GESJUD_ERRORS_INSERT_EVENTO_FECHA_INVALIDA);
				return false;
			}
		}
		
		
		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>
		if(getGesJud().getUltimoEventoIngresado().getId()!=getId()){
			addRecoverableError(GdeError.GESJUD_ERRORS_DELETE_EVENTO_NO_ES_EL_ULTIMO);
			return false;
		}
			
		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if(getEvento()== null || getEvento().getId()<0)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.EVENTO_LABEL);
		
		if (getFechaEvento()==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.GESJUDEVENTO_FECHAEVENTO_LABEL);
		
		else if(DateUtil.isDateBefore(getFechaEvento(), getGesJud().getFechaAlta()))			
			addRecoverableError(GdeError.GESJUDEVENTO_ERROR_FECHAEVENTO_ANTERIOR_FECHAALTA);
		
		if(DateUtil.isDateAfter(getFechaEvento(), new Date()))			
				addRecoverableError(GdeError.GESJUDEVENTO_ERROR_FECHA_ANTERIOR_FECHAACTUAL);
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el GesJudEvento. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getGesJudEventoDAO().update(this);
	}

	/**
	 * Desactiva el GesJudEvento. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getGesJudEventoDAO().update(this);
	}

	/**
	 * Valida la activacion del GesJudEvento
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del GesJudEvento
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}


	// <#MetodosBeanDetalle#>
}
