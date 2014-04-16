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
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a PagoDeuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_pagoDeuda")
public class PagoDeuda extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "idDeuda")
	private Long idDeuda;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoPago") 
	private TipoPago tipoPago;

	@Column(name = "idPago")
	private Long idPago;

	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "actualizacion")
	private Double actualizacion;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idBancoPago") 
	private Banco bancoPago;

    @Column(name="idCaso") 
	private String idCaso;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@Column(name = "esPagoTotal")
	private Integer esPagoTotal;
	
	// Constructores
	public PagoDeuda(){
		super();
	}
	
	// Getters Y Setters 
	public Double getActualizacion() {
		return actualizacion;
	}
	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Banco getBancoPago() {
		return bancoPago;
	}
	public void setBancoPago(Banco bancoPago) {
		this.bancoPago = bancoPago;
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public Integer getEsPagoTotal() {
		return esPagoTotal;
	}
	public void setEsPagoTotal(Integer esPagoTotal) {
		this.esPagoTotal = esPagoTotal;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public Long getIdPago() {
		return idPago;
	}
	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public TipoPago getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	// Metodos de Clase
	public static PagoDeuda getById(Long id) {
		return (PagoDeuda) GdeDAOFactory.getPagoDeudaDAO().getById(id);
	}
	
	public static PagoDeuda getByIdNull(Long id) {
		return (PagoDeuda) GdeDAOFactory.getPagoDeudaDAO().getByIdNull(id);
	}
	
	public static List<PagoDeuda> getList() {
		return (ArrayList<PagoDeuda>) GdeDAOFactory.getPagoDeudaDAO().getList();
	}
	
	public static List<PagoDeuda> getListActivos() {			
		return (ArrayList<PagoDeuda>) GdeDAOFactory.getPagoDeudaDAO().getListActiva();
	}

	public static List<PagoDeuda> getByDeuda(Long idDeuda) {
		return (ArrayList<PagoDeuda>) GdeDAOFactory.getPagoDeudaDAO().getByDeuda(idDeuda);
	}
	
	public static List<PagoDeuda> getByDeudaFecha(Long idDeuda,Date fecha) {
		return (ArrayList<PagoDeuda>) GdeDAOFactory.getPagoDeudaDAO().getByDeudaFecha(idDeuda,fecha);
	}
	
	public static PagoDeuda getMoreRecentByDeudaAndTipoPago(Long idDeuda,Long idTipoPago) {
		return GdeDAOFactory.getPagoDeudaDAO().getMoreRecentByDeudaAndTipoPago(idDeuda,idTipoPago);
	}
	
	
	// Metodos de Instancia
	/**
	 * Obtiene la descripcion adecuada, dependiendo del TipoPago.<br>
	 * TODO hay que terminar de implementarlo
	 * @return Si no encuentra nada, devuelve una cadena vacia
	 */
	public String getDescripcion(){
		Long idTipoPago = getTipoPago().getId();
		
		if(idTipoPago.longValue()==TipoPago.ID_BOLETA_NO_VENCIDA){
			return "";
			
		}else if(idTipoPago.longValue()==TipoPago.ID_BOLETA_VENCIDA.longValue()){
			return "";
			
		}else if(idTipoPago.longValue()==TipoPago.ID_COMPENSACION_DE_OFICIO.longValue()){
			return "Comp. de Oficio";
			
		}else if(idTipoPago.longValue()==TipoPago.ID_EXPEDIENTE_COMPENSACION.longValue()){
			return "Exp. de Compensaci\u00F3n";

		}else if(idTipoPago.longValue()==TipoPago.ID_CARGA_MANUAL_PAGOS.longValue()){
			return "Alta Manual de Pago";
			
		}else if(idTipoPago.longValue()==TipoPago.ID_PAGO_A_CUENTA.longValue() || 
					idTipoPago.longValue()==TipoPago.ID_PAGO_BUENO.longValue()){
			// Busca el convenio y devuelve la descripcion
			if(getIdPago() != null){
				Convenio convenio = Convenio.getByIdNull(getIdPago());
				if (convenio!=null)
					return convenio.getPlan().getDesPlan() + "- Nro: " + convenio.getNroConvenio().toString() ;
				else
					return "No es posible recuperar el convenio";
			} else {
				return "No hay datos del convenio";
			}
		}else if(idTipoPago.longValue()==TipoPago.ID_RECIBO_DE_RECONFECCION.longValue()){
			//busca el recibo y devuelve el nro y codRefPago
			if(getIdPago() != null){
				Recibo recibo = Recibo.getById(getIdPago());
				if (recibo != null) {
					return recibo.getNroRecibo()+" - "+recibo.getCodRefPag();
				} else {
					return "No es posible recuperar el recibo";
				}
			} else {
				return "No hay datos del recibo";				
			}
		}else if(idTipoPago.longValue()==TipoPago.ID_SALDO_A_FAVOR.longValue()){
			return "";
		}
		
		return "";
	}

	
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
		
		if (hasError()) {
			return false;
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


}
