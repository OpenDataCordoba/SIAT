//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.ActaVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Acta
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_acta")
public class Acta extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTipoActa")
	private TipoActa tipoActa;

	@Column(name = "numeroActa")
	private Long numeroActa;

	@Column(name = "fechaVisita")
	private Date fechaVisita;

	@Column(name = "horaVisita")
	private Date horaVisita;

	@Column(name = "idPersona")
	private Integer idPersona;

	@Column(name = "enCaracter")
	private String enCaracter;

	@Column(name = "fechaPresentacion")
	private Date fechaPresentacion;

	@Column(name = "horaPresentacion")
	private Date horaPresentacion;

	@Column(name = "lugarPresentacion")
	private String lugarPresentacion;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idActa")
	@OrderBy(clause="idDocumentacion")
	private List<OrdConDoc> listOrdConDoc = new ArrayList<OrdConDoc>();
	
	// <#Propiedades#>

	// Constructores
	public Acta() {
		super();
		// Seteo de valores default
	}

	public Acta(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Acta getById(Long id) {
		return (Acta) EfDAOFactory.getActaDAO().getById(id);
	}

	public static Acta getByIdNull(Long id) {
		return (Acta) EfDAOFactory.getActaDAO().getByIdNull(id);
	}

	public static List<Acta> getList() {
		return (ArrayList<Acta>) EfDAOFactory.getActaDAO().getList();
	}

	public static List<Acta> getListActivos() {
		return (ArrayList<Acta>) EfDAOFactory.getActaDAO().getListActiva();
	}

	public static Acta getByOrdenControlTipoActa(Long idOrdenControl,Long tipoActa){
		return EfDAOFactory.getActaDAO().getByOrdenControlTipoActa(idOrdenControl,tipoActa);
	}
	/**
	 * Devuelve el proximo numero de acta, de la secuencia
	 * @return
	 */
	public static Long getNextNroActa(){
		return EfDAOFactory.getActaDAO().getNextVal();
	}
	
	// Getters y setters
	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public TipoActa getTipoActa() {
		return tipoActa;
	}

	public void setTipoActa(TipoActa tipoActa) {
		this.tipoActa = tipoActa;
	}

	public Long getNumeroActa() {
		return numeroActa;
	}

	public void setNumeroActa(Long numeroActa) {
		this.numeroActa = numeroActa;
	}

	public Date getFechaVisita() {
		return fechaVisita;
	}

	public void setFechaVisita(Date fechaVisita) {
		this.fechaVisita = fechaVisita;
	}

	public Date getHoraVisita() {
		return horaVisita;
	}

	public void setHoraVisita(Date horaVisita) {
		this.horaVisita = horaVisita;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
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
	}

	public Date getHoraPresentacion() {
		return horaPresentacion;
	}

	public void setHoraPresentacion(Date horaPresentacion) {
		this.horaPresentacion = horaPresentacion;
	}

	public String getLugarPresentacion() {
		return lugarPresentacion;
	}

	public void setLugarPresentacion(String lugarPresentacion) {
		this.lugarPresentacion = lugarPresentacion;
	}
	
	public List<OrdConDoc> getListOrdConDoc() {
		return listOrdConDoc;
	}

	public void setListOrdConDoc(List<OrdConDoc> listOrdConDoc) {
		this.listOrdConDoc = listOrdConDoc;
	}

	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

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
		if (GenericDAO.hasReference(this, OrdConDoc.class, "acta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							EfError.ACTA_LABEL, EfError.ORDCONDOC_LABEL );
		}

		if(tipoActa.getId().equals(TipoActa.ID_TIPO_PROCEDIMIENTO)){
			List<OrdConDoc> listByActaProcedimiento = OrdConDoc.getListByActaProcedimiento(this);
			if(listByActaProcedimiento!=null && listByActaProcedimiento.size()>0){
				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
						EfError.ACTA_LABEL, EfError.ORDCONDOC_LABEL );				
			}
			
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el Acta. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getActaDAO().update(this);
	}

	/**
	 * Desactiva el Acta. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getActaDAO().update(this);
	}

	/**
	 * Valida la activacion del Acta
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
	 * Valida la desactivacion del Acta
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
	public OrdConDoc createOrdConDoc(OrdConDoc ordConDoc) throws Exception {
		// Validaciones de negocio
		if (!ordConDoc.validateCreate()) {
			return ordConDoc;
		}
		EfDAOFactory.getOrdConDocDAO().update(ordConDoc);
				
		return ordConDoc;
	}
	
	public OrdConDoc updateOrdConDoc(OrdConDoc ordConDoc) throws Exception {
		// Validaciones de negocio
		if (!ordConDoc.validateUpdate()) {
			return ordConDoc;
		}
		EfDAOFactory.getOrdConDocDAO().update(ordConDoc);
				
		return ordConDoc;
	}	
	
	public OrdConDoc deleteOrdConDoc(OrdConDoc ordConDoc) throws Exception {

		// Validaciones de negocio
		if (!ordConDoc.validateDelete()) {
			return ordConDoc;
		}

		EfDAOFactory.getOrdConDocDAO().delete(ordConDoc);

		return ordConDoc;
	}	
	
	public ActaVO toVOForPrint()throws Exception{
	 	ActaVO actaVO = (ActaVO) this.toVO(0, false);
		actaVO.setOrdenControl((OrdenControlVO) this.ordenControl.toVO4Print());
	
		// Lista de OrdConDoc
		if(listOrdConDoc!=null){
			List<OrdConDocVO> listOrdConDocVO = new ArrayList<OrdConDocVO>();
			for(OrdConDoc p:listOrdConDoc){
				listOrdConDocVO.add(p.toVO4Print());
			}
			actaVO.setListOrdConDoc(listOrdConDocVO);
		}
		return actaVO;
	}
	
}
