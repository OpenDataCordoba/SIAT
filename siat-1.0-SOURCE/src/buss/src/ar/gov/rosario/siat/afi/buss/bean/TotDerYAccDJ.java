//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TotDerYAccDJ - Totales de Derecho y accesorios 
 * de la DJ para el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_totderyaccdj")
public class TotDerYAccDJ extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="idForDecJur")
	private ForDecJur forDecJur;
	
	@Column(name = "codImpuesto")
	private Integer  codImpuesto;
	
	@Column(name = "concepto")
	private Integer  concepto;
	
	@Column(name = "totalMontoIngresado")
	private Double  totalMontoIngresado;
	
	
	// Constructores
	public TotDerYAccDJ(){
		super();
		// Seteo de valores default			
	}
	
	public TotDerYAccDJ(Long id){
		super();
		setId(id);
	}
		
	
	
	// Getters y setters	
	public static TotDerYAccDJ getById(Long id) {
		return (TotDerYAccDJ) AfiDAOFactory.getTotDerYAccDJDAO().getById(id);
	}
		
	public void setForDecJur(ForDecJur forDecJur) {
		this.forDecJur = forDecJur;
	}

	public ForDecJur getForDecJur() {
		return forDecJur;
	}

	public Integer getCodImpuesto() {
		return codImpuesto;
	}

	public void setCodImpuesto(Integer codImpuesto) {
		this.codImpuesto = codImpuesto;
	}

	public Integer getConcepto() {
		return concepto;
	}

	public void setConcepto(Integer concepto) {
		this.concepto = concepto;
	}

	public Double getTotalMontoIngresado() {
		return totalMontoIngresado;
	}

	public void setTotalMontoIngresado(Double totalMontoIngresado) {
		this.totalMontoIngresado = totalMontoIngresado;
	}

	// Metodos de Clase
	public static TotDerYAccDJ getByIdNull(Long id) {
		return (TotDerYAccDJ) AfiDAOFactory.getTotDerYAccDJDAO().getByIdNull(id);
	}
	
	public static List<TotDerYAccDJ> getList() {
		return (ArrayList<TotDerYAccDJ>) AfiDAOFactory.getTotDerYAccDJDAO().getList();
	}
	
	public static List<TotDerYAccDJ> getListActivos() {			
		return (ArrayList<TotDerYAccDJ>) AfiDAOFactory.getTotDerYAccDJDAO().getListActiva();
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
		//limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
//		if (StringUtil.isNullOrEmpty(getCodTotDerechoAcc())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesTotDerechoAcc())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
//		
//		if (hasError()) {
//			return false;
//		}
//		
//		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codTotDerechoAcc");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TotDerechoAcc. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getTotDerYAccDJDAO().update(this);
	}

	/**
	 * Desactiva el TotDerechoAcc. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getTotDerYAccDJDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TotDerechoAcc
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TotDerechoAcc
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}	
	
}
	