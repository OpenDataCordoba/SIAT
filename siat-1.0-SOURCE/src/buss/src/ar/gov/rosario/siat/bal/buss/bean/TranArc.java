//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a las Transacciones tomadas de los Archivos
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tranArc")
public class TranArc extends BaseBO {

	private static final long serialVersionUID = 1L;
		
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idArchivo") 
	private Archivo archivo;

	@Column(name = "linea")
	private String linea;

	@Column(name = "nroLinea")
	private Long nroLinea;

	@Column(name = "importe")
	private Double importe;

	// Constructores 
	public TranArc(){
		super();
	}

	// Getters y Setters
	public Archivo getArchivo() {
		return archivo;
	}
	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public Long getNroLinea() {
		return nroLinea;
	}
	public void setNroLinea(Long nroLinea) {
		this.nroLinea = nroLinea;
	}

	// Metodos de clase	
	public static TranArc getById(Long id) {
		return (TranArc) BalDAOFactory.getTranArcDAO().getById(id);
	}
	
	public static TranArc getByIdNull(Long id) {
		return (TranArc) BalDAOFactory.getTranArcDAO().getByIdNull(id);
	}
		
	public static List<TranArc> getList() {
		return (ArrayList<TranArc>) BalDAOFactory.getTranArcDAO().getList();
	}
	
	public static List<TranArc> getListActivos() {			
		return (ArrayList<TranArc>) BalDAOFactory.getTranArcDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesTranArc())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_DESDISPAR);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_RECURSO);
		}*/
		
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
		
		/*if (GenericDAO.hasReference(this, TranArcDet.class, "disPar")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.DISPAR_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, TranArcRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, TranArcPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARPLA_LABEL);
		}*/
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	
	// Getters de propiedades sobre la linea (string) según el prefijo.
	
	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public Long getSistema(){
		Long sistema = null;
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC() || this.getArchivo().isPrefixOS()){
				sistema = new Long(this.getLinea().substring(0, 2).trim());
			}else if(this.getArchivo().isPrefixSE()){
				sistema = 88L; // Corresponde al Sistema que es Servicio Banco de código 88 (Sellado)
			}
		}catch(Exception e){
			sistema = null;
		}
		return sistema;
	}
	
	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public Long getNroComprobante(){
		Long nroComprobante = null;
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC() || this.getArchivo().isPrefixOS()){
				nroComprobante = new Long(this.getLinea().substring(2, 12).trim());
			}else if(this.getArchivo().isPrefixSE()){
				//nroComprobante = 0L;
				System.out.println("NroComprobante: "+this.getLinea().substring(5, 8).trim());
				nroComprobante = new Long(this.getLinea().substring(5, 8).trim()); // subTran == codSellado
			}
		}catch(Exception e){
			nroComprobante = null;
		}
		return nroComprobante;
	}
	
	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public String getClave(){
		String clave = null;
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC()){
				clave = this.getLinea().substring(12, 16).trim();
			}else if(this.getArchivo().isPrefixSE()){
				clave = "00"+Transaccion.TIPO_BOLETA_SELLADO.toString()+"000";
			}else if(this.getArchivo().isPrefixOS()){
				clave = this.getLinea().substring(12, 18).trim();
			}
		}catch(Exception e){
			clave = null;
		}
		return clave;
	}
	
	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public Double getImportePago(){
		Double importe = null;
		String importeStr = "";
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC()){
				importeStr = this.getLinea().substring(25, 35);
				if(importeStr.endsWith("+")){
					importeStr = importeStr.substring(0, importeStr.length()-1);
				}
				importeStr = importeStr.substring(0, importeStr.length()-2)+"."+importeStr.substring(importeStr.length()-2, importeStr.length());
				importe = new Double(importeStr.trim());
			}else if(this.getArchivo().isPrefixSE()){
				importeStr = this.getLinea().substring(8, 22).trim();
				importe = new Double(importeStr.trim());
			}else if (this.getArchivo().isPrefixOS()){
				importeStr = this.getLinea().substring(26, 36).trim();
				importe = new Double(importeStr.trim());
			}
		}catch(Exception e){
			importe = null;
		}
		return importe;
	}

	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public Long getCodPago(){
		Long codPago = null;
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC()){
				codPago = new Long(this.getLinea().substring(16, 17).trim());
			}else if(this.getArchivo().isPrefixSE() || this.getArchivo().isPrefixOS()){
				codPago = 0L;
			}
		}catch(Exception e){
			codPago = null;
		}
		return codPago;
	}
	
	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public Long getCaja(){
		Long caja = null;
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC()){
				caja = new Long(this.getLinea().substring(17, 19).trim());
			}else if(this.getArchivo().isPrefixSE()){
				caja = new Long(this.getLinea().substring(38, 40).trim()); 
			}else if (this.getArchivo().isPrefixOS()){
				caja = new Long(this.getLinea().substring(36, 39).trim());
			}
		}catch(Exception e){
			caja = null;
		}
		return caja;
	}
	
	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public Date getFechaPago(){
		Date fechaPago = null;
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC()){
				fechaPago = DateUtil.getDate(this.getLinea().substring(19, 25).trim(), DateUtil.ddMMYY_MASK);
			}else if(this.getArchivo().isPrefixSE()){
				fechaPago = DateUtil.getDate(this.getLinea().substring(22, 30).trim(), DateUtil.YYYYMMDD_MASK);
			}else if (this.getArchivo().isPrefixOS()){
				fechaPago = DateUtil.getDate(this.getLinea().substring(18, 26).trim(), DateUtil.YYYYMMDD_MASK);
			}
		}catch(Exception e){
			fechaPago = null;
		}
		return fechaPago;
	}
	
	/**
	 *  Toma el valor string desde la linea segun el prefijo del archivo e intenta convertirlo en long.
	 *  Si no puede devuelve null;
	 */
	public Long getPaquete(){
		Long paquete = null;
		try{
			if(this.getArchivo().isPrefixREOrTrOrCJ() || this.getArchivo().isPrefixGC()){
				paquete = new Long(this.getLinea().substring(38, 42).trim());
			}else if(this.getArchivo().isPrefixSE() || this.getArchivo().isPrefixOS()){
				paquete = 0L;
			}
		}catch(Exception e){
			paquete = null;
		}
		return paquete;
	}
	
	/**
	 *  Intenta obtener el primer codigo verificador solo para archivos de Sellado (prefijo=se). Para los archivos restantes devuelve null.
	 *
	 */
	public String getPrimerCodigoVerificador(){
		String clave = null;
		try{
			if(this.getArchivo().isPrefixSE()){
				clave = this.getLinea().substring(30, 35).trim();  
			}
		}catch(Exception e){
			clave = null;
		}
		return clave;
	}
	

	/**
	 *  Intenta obtener el segundo codigo verificador solo para archivos de Sellado (prefijo=se). Para los archivos restantes devuelve null.
	 *
	 */
	public String getSegundoCodigoVerificador(){
		String clave = null;
		try{
			if(this.getArchivo().isPrefixSE()){
				clave = this.getLinea().substring(54, 60).trim(); 
			}
		}catch(Exception e){
			clave = null;
		}
		return clave;
	}
	
	/**
	 * Intenta obtener el numero de Filial (Distrito?) para archivos de Sellados. Para el resto devuelve null.
	 */
	public String getFilial(){
		Long filial = null;
		try{
			if(this.getArchivo().isPrefixSE()){
				filial = Long.valueOf(this.getLinea().substring(0, 2).trim()); 
			}
		}catch(Exception e){
			filial = null;
		}
		if(filial != null)
			return filial.toString();
		else
			return null;
	}
	
	/**
	 * Intenta obtener el numero de Cajero para archivos de Sellados. Para el resto devuelve null.
	 */
	public String getCajero(){
		Long cajero = null;
		try{
			if(this.getArchivo().isPrefixSE()){
				cajero = Long.valueOf(this.getLinea().substring(2, 5).trim()); 
			}
		}catch(Exception e){
			cajero = null;
		}
		if(cajero != null)
			return cajero.toString();
		else
			return null;
	}
	
	/**
	 * Intenta obtener el numero de Codigo para archivos de Sellados. Para el resto devuelve null.
	 */
	public String getCodigoSellado(){
		Long codigo = null;
		try{
			if(this.getArchivo().isPrefixSE()){
				codigo = Long.valueOf(this.getLinea().substring(5, 8).trim()); 
			}
		}catch(Exception e){
			codigo = null;
		}
		if(codigo != null)
			return codigo.toString();
		else
			return null;
	}
	
	/**
	 * Intenta obtener el numero de Transaccion para archivos de Sellados. Para el resto devuelve null.
	 */
	public String getTransaccion(){
		Long codigo = null;
		try{
			if(this.getArchivo().isPrefixSE()){
				codigo = Long.valueOf(this.getLinea().substring(43, 46).trim()); 
			}
		}catch(Exception e){
			codigo = null;
		}
		if(codigo != null)
			return codigo.toString();
		else
			return null;
	}
	
	/**
	 * Intenta obtener el codigo de Formulario para archivos de origen Osiris. Para el resto devuelve null.
	 */
	public Integer getFormulario(){
		Integer codigo = null;
		try{
			if(this.getArchivo().isPrefixOS()){
				codigo = Integer.valueOf(this.getLinea().substring(39, 43).trim()); 
			}
		}catch(Exception e){
			codigo = null;
		}
		if(codigo != null)
			return codigo;
		else
			return null;
	}

	/**
	 * Intenta obtener el numero de trasnaccion afip para archivos de origen Osiris. Para el resto devuelve null.
	 */
	public Long getIdTranAfip(){
		Long nroTranAfip = null;
		try{
			if(this.getArchivo().isPrefixOS())
				nroTranAfip = Long.valueOf(this.getLinea().substring(43, 55).trim()); 
		}catch(Exception e){
			nroTranAfip = null;
		}

		return nroTranAfip;
	}

}
