//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a TipoPago
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipoPago")
public class TipoPago extends BaseBO {
	
	public static final Long ID_BOLETA_NO_VENCIDA = 1L;
	public static final Long ID_BOLETA_VENCIDA = 2L;
	public static final Long ID_RECIBO_DE_RECONFECCION = 3L;
	public static final Long ID_PAGO_BUENO = 4L;
	public static final Long ID_PAGO_A_CUENTA = 5L;
	public static final Long ID_SALDO_A_FAVOR = 6L;
	public static final Long ID_COMPENSACION_DE_OFICIO = 7L;
	public static final Long ID_RECIBO_CUOTA = 10L;
	public static final Long ID_RECIBO_RECONFECCION_CUOTA = 11L;
	public static final Long ID_EXPEDIENTE_COMPENSACION = 12L;
	public static final Long ID_RETENCION_DECLARADA=13L;
	public static final Long ID_PERCEPCION_DECLARADA=14L;
	public static final Long ID_CARGA_MANUAL_PAGOS=15L; // tipo que dimos de alta para mig 
														// de pagos drei judicial. el programa convierte el tipo 7 
														// (que viene en migracion) y lo convierte a 15 
														// (carga manual) en siat
	
	public static final Long ID_CAMBIO_DE_COEFICIENTE=16L;
	public static final Long ID_POR_RESOLUCION=17L;
	public static final Long ID_RESTO_PERIODO_ANTERIOR=18L;
	
	
	public static final Long ID_PAGO_AFIP=19L; //Pago Asentado
	public static final Long ID_PAGO_DECLARADO_AFIP=20L; //Pago Declarado
	public static final Long ID_RETENCION_DECLARADA_AFIP=21L; //Retencion Declarada AFIP
	
	private static final long serialVersionUID = 1L;

	@Column(name = "desTipoPago")
	private String desTipoPago;
	
	// Constructores
	public TipoPago(){
		super();
	}

	// Getters Y Setters
	public String getDesTipoPago() {
		return desTipoPago;
	}
	public void setDesTipoPago(String desTipoPago) {
		this.desTipoPago = desTipoPago;
	}
	
	// Metodos de Clase
	public static TipoPago getById(Long id) {
		return (TipoPago) GdeDAOFactory.getTipoPagoDAO().getById(id);
	}

	public static TipoPago getByIdNull(Long id) {
		return (TipoPago) GdeDAOFactory.getTipoPagoDAO().getByIdNull(id);
	}
	
	public static List<TipoPago> getList() {
		return (ArrayList<TipoPago>) GdeDAOFactory.getTipoPagoDAO().getList();
	}
	
	public static List<TipoPago> getListActivos() {			
		return (ArrayList<TipoPago>) GdeDAOFactory.getTipoPagoDAO().getListActiva();
	}

	/**
	 * Devuelve el TipoPago correspondiente al TipPagDecJur pasado como parametro. Metodo feo feo
	 * 
	 * @param idTipPagDecJur
	 * @return
	 */
	public static TipoPago getByIdTipPagDecJur(Long idTipPagDecJur){
		
		Long idTipoPago = 0L;
		if(idTipPagDecJur.equals(TipPagDecJur.ID_RETENCION_OSIRIS)){
			idTipoPago = TipoPago.ID_RETENCION_DECLARADA_AFIP;
		}else if(idTipPagDecJur.equals(TipPagDecJur.ID_PAGO_OSIRIS)){
			idTipoPago = TipoPago.ID_PAGO_DECLARADO_AFIP;
		}else if(idTipPagDecJur.equals(TipPagDecJur.ID_RETENCION)){
			idTipoPago = TipoPago.ID_RETENCION_DECLARADA;
		}
		
		return getByIdNull(idTipoPago);
		
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
	
		if (GenericDAO.hasReference(this, PagoDeuda.class, "tipoPago")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.TIPOPAGO_LABEL, GdeError.PAGODEUDA_LABEL );
		}
		
		
		if (GenericDAO.hasReference(this, ConvenioCuota.class, "tipoPago")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.TIPOPAGO_LABEL, GdeError.CONVENIOCUOTA_LABEL );
		}
		
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getDesTipoPago())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOPAGO_DESTIPOPAGO);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoPago. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipoPagoDAO().update(this);
	}

	/**
	 * Desactiva el TipoPago. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipoPagoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoPago
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoPago
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
