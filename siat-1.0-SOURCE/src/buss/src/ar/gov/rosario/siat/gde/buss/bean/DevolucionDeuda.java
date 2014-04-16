//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

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

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.gde.iface.model.DevDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.DevolucionDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DemodaUtil;

/**
 * Representa la anulacion del envio de deuda a Judiciales
 * 
 */
@Entity
@Table(name = "gde_devolucionDeud")
public class DevolucionDeuda extends BaseBO {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DevolucionDeuda.class);
	

	@Column(name = "fechaDevolucion") 
	private Date fechaDevolucion;     // DATETIME YEAR TO DAY NOT NULL
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;  // NOT NULL

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcurador") 
	private Procurador procurador;  // NOT NULL

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;  // NOT NULL

	@Column(name = "observacion") 
	private String observacion; // VARCHAR(255)

	@Column(name = "usuarioAlta")
	private String    usuarioAlta; // CHAR(10) NOT NULL
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idDevolucionDeuda")
	private List<DevDeuDet> listDevDeuDet;

	// Contructores 
	public DevolucionDeuda() {
		super();
	}
	
	//	 Getters y Setters
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}
	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public List<DevDeuDet> getListDevDeuDet() {
		return listDevDeuDet;
	}
	public void setListDevDeuDet(List<DevDeuDet> listDevDeuDet) {
		this.listDevDeuDet = listDevDeuDet;
	}

	// Metodos de clase
	public static DevolucionDeuda getById(Long id) {
		return (DevolucionDeuda) GdeDAOFactory.getDevolucionDeudaDAO().getById(id);
	}

	public static DevolucionDeuda getByIdNull(Long id) {
		return (DevolucionDeuda) GdeDAOFactory.getDevolucionDeudaDAO().getByIdNull(id);
	}

	public static List<DevolucionDeuda> getList() {
		return (ArrayList<DevolucionDeuda>) GdeDAOFactory.getDevolucionDeudaDAO().getList();
	}

	public static List<DevolucionDeuda> getListActivos() {			
		return (ArrayList<DevolucionDeuda>) GdeDAOFactory.getDevolucionDeudaDAO().getListActiva();
	}

	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de requeridos y unicidad comunes
		this.validate();
		
		// Recurso 
		if(this.getRecurso() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEVOLUCIONDEUDA_RECURSO);
		}
		// procurador
		if(this.getProcurador() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEVOLUCIONDEUDA_PROCURADOR);
		}
		// cuenta
		if(this.getCuenta() == null){
			addRecoverableError(GdeError.DEVOLUCIONDEUDA_CUENTA_NO_ASOCIADA_RECURSO);
		}
		// fecha
		if(this.getFechaDevolucion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEVOLUCIONDEUDA_FECHA_DEVOLUCION);
		}

		if (hasError()) {
			return false;
		}
		
		// que la cuenta ingresada este asociada a por lo menos a un registro de deuda en via judicial asignado al procurador
		List<DeudaJudicial> listDeudaJudicial = DeudaJudicial.getByNroCtaYProcurador(
				this.getCuenta().getNumeroCuenta(), this.getProcurador().getId());
		
		if (listDeudaJudicial.size() == 0){
			addRecoverableError(GdeError.DEVOLUCIONDEUDA_CUENTA_PRO_ORI_SIN_DEUDAS_JUDICIAL);
		}

		return !hasError();
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de requeridos y unicidad comunes
		this.validate();

		if (hasError()) {
			return false;
		}

		return !hasError();
	}


	private boolean validate(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		//Validaciones de Negocio

		return (!hasError());
	}

	/**
	 * Valida la eliminacion
	 * @author 
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();

		//Validaciones de VO
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		// que no posea DevDeuDet
		// verificamos que no tenga DevDeuDet val asociados
		if (GenericDAO.hasReference(this, DevDeuDet.class, "devolucionDeuda")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				GdeError.DEVOLUCIONDEUDA_LABEL , GdeError.DEVOLUCIONDEUDA_LABEL);
			
		}


		return !hasError();
	}
	
	public DevDeuDet createDevDeuDet(DevDeuDet devDeuDet) throws Exception {

		// Validaciones de negocio
		if (!devDeuDet.validateCreate()) {
			return devDeuDet;
		}

		GdeDAOFactory.getDevDeuDetDAO().update(devDeuDet);
		
		DeudaJudicial dj = devDeuDet.getDeudaJudicial();
		
		GesJudDeu gesJudDeu = dj.getGetJudDeu();
		
		if (gesJudDeu != null){
			GesJud gesJud = gesJudDeu.getGesJud();
			
			HistGesJud histGesJud = new HistGesJud();
			histGesJud.setFecha(this.getFechaDevolucion());
			histGesJud.setDescripcion("Devolución de deudas incluidas en la Gestión Judicial");
			histGesJud.setGesJud(gesJud);
			histGesJud.setUsuarioUltMdf(this.getUsuarioUltMdf());
			histGesJud = gesJudDeu.getGesJud().createHistGesJud(histGesJud);
			
			// paso de errores desde el historico al traDeuDet creado
			histGesJud.addErrorMessages(devDeuDet);
		}

		return devDeuDet;
	}

	public DevolucionDeudaVO toVOForDevDeuDet() throws Exception{
		DevolucionDeudaVO devolucionDeudaVO = (DevolucionDeudaVO) this.toVO(1);
		
		// es necesario limpiar la lista de DevDeuDet porque el toVO de nivel 1 la carga
		devolucionDeudaVO.setListDevDeuDet(new ArrayList<DevDeuDetVO>());

		for (DevDeuDet devDeuDet : this.getListDevDeuDet()) {
			DevDeuDetVO     devDeuDetVO     = (DevDeuDetVO) devDeuDet.toVO(1);
			
			Deuda deuda = Deuda.getById(devDeuDet.getIdDeuda());
			devDeuDetVO.setDeuda((DeudaVO) deuda.toVO(1,false));
			// se agrega este id para que no envie null a ver detalle deuda
			devDeuDetVO.getDeuda().setEstadoDeuda((EstadoDeudaVO) deuda.getEstadoDeuda().toVO(1, false));
			
			if (devDeuDet.getConstanciaDeuOri() != null){
				devDeuDetVO.setVerConstanciaBussEnabled(true);
				ConDeuDet conDeuDet = devDeuDet.getConstanciaDeuOri().getConDeuDetByIdDeuda(devDeuDet.getIdDeuda());
				if(conDeuDet == null){
					log.error("Error de Datos: DevDeuDet con constanciaOrigen y la deuda no pertenece a ningun detalle de la constanciaOrigen");
				}
				devDeuDetVO.setConDeuDet((ConDeuDetVO) conDeuDet.toVO(0));
			}else{
				devDeuDetVO.setVerConstanciaBussEnabled(false);
			}
			devolucionDeudaVO.getListDevDeuDet().add(devDeuDetVO);
		}

		return devolucionDeudaVO;
	}


	//	 Metodos de negocio

	public void moverDeudaAAdmin() throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		int r1 = GdeDAOFactory.getDeudaAdminDAO().copiarDeudaAAdmin(this);  // TODO ver delegaciones de estos metodos 
		int c1 = GdeDAOFactory.getDeuAdmRecConDAO().copiarAAdmin(this);        
		int c2 = GdeDAOFactory.getDeuJudRecConDAO().deleteListDeuJudRecConByDevolucionDeuda(this);
		int r2 = GdeDAOFactory.getDeudaJudicialDAO().deleteListDeudaJudByDevolucionDeuda(this); 
		
		log.debug("ctd reg deudaAdmin copiados          = " + r1);
		log.debug("ctd reg conceptos deuAdmRec copiados = " + c1);
		log.debug("ctd reg deudaJud borrados            = " + c2);
		log.debug("ctd reg conceptos deuJudRec borrados = " + r2);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

	}

}
