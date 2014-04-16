//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente al Log de Asignaci&oacute;n de Broche a Cuentas.
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_brocue")
public class BroCue extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idBroche") 
	private Broche broche;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	@Column(name = "fechaAlta")
	private Date fechaAlta;
	
	@Column(name = "fechaBaja")
	private Date fechaBaja;

	@Column(name="idCaso")
	private String idCaso;

	// Constructores
	public BroCue(){
		super();
	}

	public Broche getBroche() {
		return broche;
	}

	// Getters y Setters
	
	public void setBroche(Broche broche) {
		this.broche = broche;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	// Metodos de clase	
	public static BroCue getById(Long id) {
		return (BroCue) PadDAOFactory.getBroCueDAO().getById(id);
	}
	
	public static BroCue getByIdNull(Long id) {
		return (BroCue) PadDAOFactory.getBroCueDAO().getByIdNull(id);
	}
	
	public static List<BroCue> getList() {
		return (ArrayList<BroCue>) PadDAOFactory.getBroCueDAO().getList();
	}
	
	public static List<BroCue> getListActivos() {			
		return (ArrayList<BroCue>) PadDAOFactory.getBroCueDAO().getListActiva();
	}
	
	public static BroCue getVigenteByCuenta(Cuenta cuenta) {			
		return (BroCue) PadDAOFactory.getBroCueDAO().getVigenteByCuenta(cuenta);
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		// Valido que la Fecha de Baja no pueda ser null.
		if(getFechaBaja()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCUE_FECHABAJA);
		}
		
		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(getBroche()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCUE_BROCHE);
		}
		if(getCuenta()==null){
			//addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCUE_CUENTA);
			addRecoverableError(PadError.BROCHE_NROCUENTA_INVALIDO);
		}
		if(getFechaAlta()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCUE_FECHAALTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Valida que la Fecha Alta no sea mayor que la fecha Baja, y que la fecha Baja no sea mayor que la actual
		if(this.fechaBaja!=null){
			if(!DateUtil.isDateBeforeOrEqual(this.fechaAlta, this.fechaBaja)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, PadError.BROCUE_FECHAALTA, PadError.BROCUE_FECHABAJA);
			}			
			if(!DateUtil.isDateAfterOrEqual(new Date(), this.fechaBaja)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, PadError.BROCUE_FECHABAJA, BaseError.MSG_FECHA_ACTUAL);
			}			
		}
		return !hasError();
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
				
		return true;
	}

	/**
	 * Devuelve un VO del Bean realizando un toVO especifico. 
	 * <i>(En este caso se realiza un toVO de nivel 1, y se le agrega el DomicilioVO a la Cuenta, buscando la calle 
	 * en el jar correspondiente) </i>
	 * @return BroCueVO
	 * @throws Exception
	 */
	public BroCueVO toVOSpecific() throws Exception{
		BroCueVO broCueVO;
		broCueVO = (BroCueVO) this.toVO(1);
		if(this.getCuenta().getDomicilioEnvio()!=null){
			this.getCuenta().getDomicilioEnvio().loadDataFromMCR();
			broCueVO.getCuenta().setDomicilioEnvio((DomicilioVO) this.getCuenta().getDomicilioEnvio().toVO(1));
		}else{
			broCueVO.getCuenta().setDomicilioEnvio(null);
		}
		
		return broCueVO;
	}
	
	/**
	 * Devuelve una lista de VOs a partir de una lista de Beans realizando un toVO especifico. 
	 * 
	 * @param listBroCue - Lista de BroCues Bean
	 * @return listBroCueVO - Lista de BroCues VO
	 * @throws Exception
	 */
	public static List<BroCueVO> listToVOSpecific(List<BroCue> listBroCue) throws Exception{
		
		List<BroCueVO> listVO = null;
		
		if (listBroCue != null){
			listVO = new ArrayList<BroCueVO>();
			
			for(Iterator it = listBroCue.iterator(); it.hasNext();){
				BroCue broCue = (BroCue) it.next();
				listVO.add(broCue.toVOSpecific());
			}
		}
		return listVO;
	}
	
	@Override
	public String infoString() {
		String ret =" Asignacion de Broche a Cuenta ";

		if(broche!=null){
			ret+=" - Broche: "+broche.getDesBroche();
		
			if (broche.getTipoBroche() != null){
				ret+= " - Tipo de Broche: " + broche.getTipoBroche().getDesTipoBroche(); 
			}
		} 
		
		if(cuenta!=null){
			ret+=" - Cuenta: "+cuenta.getNumeroCuenta();
		}

		if(fechaAlta!=null){
			ret+=" - Fecha Alta: "+DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_HH_MM_MASK);
		}
		
		if(fechaBaja!=null){
			ret+=" - Fecha Baja: "+DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_HH_MM_MASK);
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		
		return ret;
	}

}
