//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

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

import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a PagoPriv - Pago de Privilegio
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_pagoPriv")
public class PagoPriv extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String SEQUENCE_NAME = "cyq_nrorecibo_sq";
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="IdProcedimiento") 
	private Procedimiento procedimiento;

	@Column(name = "descripcion")
	private String descripcion;

	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuentaBancaria") 
	private CuentaBanco cuentaBanco;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "importe")
	private Double importe;

	@Column(name = "tipoCancelacion")
	private Integer tipoCancelacion;
	
	@Column(name = "idCaso")
	private String idCaso;
	
	@Column(name = "nroRecibo")
	private Integer nroRecibo;
	
	@OneToMany(mappedBy="pagoPriv")
	@JoinColumn(name="idPagoPriv")
	private List<PagoPrivDeu> listPagoPrivDeu; 
	//<#Propiedades#>
	
	// Constructores
	public PagoPriv(){
		super();
		// Seteo de valores default			
	}
	
	public PagoPriv(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PagoPriv getById(Long id) {
		return (PagoPriv) CyqDAOFactory.getPagoPrivDAO().getById(id);
	}
	
	public static PagoPriv getByIdNull(Long id) {
		return (PagoPriv) CyqDAOFactory.getPagoPrivDAO().getByIdNull(id);
	}
	
	public static List<PagoPriv> getList() {
		return (ArrayList<PagoPriv>) CyqDAOFactory.getPagoPrivDAO().getList();
	}
	
	public static List<PagoPriv> getListActivos() {			
		return (ArrayList<PagoPriv>) CyqDAOFactory.getPagoPrivDAO().getListActiva();
	}
	
	public static Integer obtenerNroRecibo(){
		return new Integer("" + CyqDAOFactory.getPagoPrivDAO().getNextVal(SEQUENCE_NAME));		
	}

	
	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Procedimiento getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}

	public CuentaBanco getCuentaBanco() {
		return cuentaBanco;
	}

	public void setCuentaBanco(CuentaBanco cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public List<PagoPrivDeu> getListPagoPrivDeu() {
		return listPagoPrivDeu;
	}

	public void setListPagoPrivDeu(List<PagoPrivDeu> listPagoPrivDeu) {
		this.listPagoPrivDeu = listPagoPrivDeu;
	}

	public Integer getTipoCancelacion() {
		return tipoCancelacion;
	}
	public void setTipoCancelacion(Integer tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Integer getNroRecibo() {
		return nroRecibo;
	}
	public void setNroRecibo(Integer nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		
		if (procedimiento == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_LABEL);
		}
		
		// Requerido para transferencia
		if (tipoCancelacion == 1 && cuentaBanco == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CUENTABANCO_LABEL);
		}

		if (fecha == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PAGOPRIV_FECHA);
		}
		
		if (StringUtil.isNullOrEmpty(descripcion)) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PAGOPRIV_DESCRIPCION);
		}
		
		if (tipoCancelacion == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PAGOPRIV_TIPOCANCELACION);
		} else if (tipoCancelacion == 1){ // Transferencia			
			if (importe == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PAGOPRIV_TIPOCANCELACION);
			} else if ( importe.longValue() <= 0){		
				addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, CyqError.PAGOPRIV_IMPORTE);
			}	
		}
		
		if (this.hasError()) {
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
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodPagoPriv())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PAGOPRIV_CODPAGOPRIV );
		}*/
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	public PagoPrivDeu createPagoPrivDeu(PagoPrivDeu pagoPrivDeu) throws Exception {
		
		// Validaciones de negocio
		if (!pagoPrivDeu.validateCreate()) {
			return pagoPrivDeu;
		}

		CyqDAOFactory.getPagoPrivDeuDAO().update(pagoPrivDeu);

		return pagoPrivDeu;
	}

}
