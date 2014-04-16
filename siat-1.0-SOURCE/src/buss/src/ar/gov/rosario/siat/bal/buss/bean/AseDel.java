//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Bean correspondiente a Asentamiento Delegado (a Sis. Ext.)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_aseDel")
public class AseDel extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "fechaBalance")
	private Date fechaBalance;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBalance") 
	private Balance balance;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEjercicio") 
	private Ejercicio ejercicio;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idServicioBanco") 
	private ServicioBanco servicioBanco;
	
    @Column(name="idCaso") 
	private String idCaso;
	
	@Column(name = "observacion")
	private String observacion;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name = "idCorrida")
	private Corrida corrida;
	
	@Column(name = "usuarioAlta")
	private String usuarioAlta;
	
	@OneToMany(mappedBy="aseDel")
	@JoinColumn(name="idAseDel")
	@OrderBy(clause="fechaPago, id")
	private List<TranDel> listTranDel;
		
	//Constructores 
	public AseDel(){
		super();
	}

	// Getters Y Setters
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
	}
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public List<TranDel> getListTranDel() {
		return listTranDel;
	}
	public void setListTranDel(List<TranDel> listTranDel) {
		this.listTranDel = listTranDel;
	}
	
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	// Metodos de clase	
	public static AseDel getById(Long id) {
		return (AseDel) BalDAOFactory.getAseDelDAO().getById(id);
	}
	
	public static AseDel getByIdNull(Long id) {
		return (AseDel) BalDAOFactory.getAseDelDAO().getByIdNull(id);
	}
	
	public static List<AseDel> getList() {
		return (ArrayList<AseDel>) BalDAOFactory.getAseDelDAO().getList();
	}
	
	public static List<AseDel> getListActivos() {			
		return (ArrayList<AseDel>) BalDAOFactory.getAseDelDAO().getListActiva();
	}
	
	/**
	 *  Obtener el Asentamiento Delegado asociado al balance pasado y para el Servicio Banco.
	 *   
	 * @param balance
	 * @param servicioBanco
	 * @return AseDel
	 * @throws Exception
	 */
	public static AseDel getByBalanceYServicioBanco(Balance balance, ServicioBanco servicioBanco) throws Exception{
		return (AseDel) BalDAOFactory.getAseDelDAO().getByBalanceYServicioBanco(balance, servicioBanco);
	}
	
	/**
	 *  Verifica si existe un Asentamiento Delegado para el Servicio Banco pasado con estado "En Preparacion", 
	 *  "En espera comenzar", "Procesando" o "En espera continuar". Retorna true o false.
	 *  (Excluyendo al Asentamiento que realiza la consulta)
	 * @param servicioBanco
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existAseDelBySerBanForCreate(ServicioBanco servicioBanco) throws Exception{
		return BalDAOFactory.getAseDelDAO().existAseDelBySerBanForCreate(servicioBanco, this);
	}

	/*public List<String> exportReportesTransacciones(String fileDir) throws Exception{
		return BalDAOFactory.getAseDelDAO().exportReportesTransaccionesByAseDel(this, fileDir);
	}
	public List<String> exportReportesPagosAsentados(String fileDir) throws Exception{
		return BalDAOFactory.getAseDelDAO().exportReportesPagosAsentadosByAseDel(this, fileDir);
	}
	public List<String> exportReportesIndeterminados(String fileDir) throws Exception{
		return BalDAOFactory.getAseDelDAO().exportReportesIndeterminadosByAseDel(this, fileDir);
	}
	public List<String> exportReportesSaldosAFavor(String fileDir) throws Exception{
		return BalDAOFactory.getAseDelDAO().exportReportesSaldosAFavorByAseDel(this, fileDir);
	}
	 */
		
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
		if(getServicioBanco()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASEDEL_SERVICIO_BANCO);
		}
		if(getFechaBalance()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASEDEL_FECHABALANCE);
		}
		if(getEjercicio()==null){
			addRecoverableError(BalError.ASEDEL_EJERCICIO_NO_ENCONTRADO);
		}
		if(getCorrida()==null){
			addRecoverableError(BalError.ASEDEL_CORRIDA_NO_GENERADA);
		}
		if(StringUtil.isNullOrEmpty(getUsuarioAlta())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASEDEL_USUARIOALTA);
		}
		
		if (hasError()) {
			return false;
		}

		if(this.existAseDelBySerBanForCreate(this.getServicioBanco())){
			addRecoverableError(BalError.ASEDEL_EXISTENTE);
		}
		if(this.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_FUTURO){
			addRecoverableError(BalError.ASEDEL_EJERCICIO_FUTURO);
		}
		
		// Valida que la Fecha Balance no sea mayor que la fecha Actual
		if(!DateUtil.isDateBeforeOrEqual(this.fechaBalance, new Date())){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.ASEDEL_FECHABALANCE, BaseError.MSG_FECHA_ACTUAL);
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
		
		if (GenericDAO.hasReference(this, TranDel.class, "aseDel")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ASEDEL_LABEL , BalError.TRANDEL_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	// Administracion de TranDel
	public TranDel createTranDel(TranDel tranDel) throws Exception {
		
		// Validaciones de negocio
		if (!tranDel.validateCreate()) {
			return tranDel;
		}

		BalDAOFactory.getTranDelDAO().update(tranDel);
		
		return tranDel;
	}	

	public TranDel updateTranDel(TranDel tranDel) throws Exception {
		
		// Validaciones de negocio
		if (!tranDel.validateUpdate()) {
			return tranDel;
		}

		BalDAOFactory.getTranDelDAO().update(tranDel);
		
		return tranDel;
	}	

	public TranDel deleteTranDel(TranDel tranDel) throws Exception {
		
		// Validaciones de negocio
		if (!tranDel.validateDelete()) {
			return tranDel;
		}
				
		BalDAOFactory.getTranDelDAO().delete(tranDel);
		
		return tranDel;
	}	

	// Metodos Relacionados con el Proceso de Balance
	
	/**
	 * Valida condiciones necesarias para comenzar el proceso.
	 * (si falla alguna condicion agrega un error)
	 * <i>(paso 1.1)</i>
	 */
	public void validarConfiguracion() throws Exception{
		// Validar si el estado del Ejercicio asociado es distinto de Futuro
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_FUTURO){
			this.addRecoverableValueError("La Fecha de balance corresponde a un ejercicio Futuro");
		}
		
		// Si el servicioBanco tiene tipoAsentamiento 'mixto' se debe validar que haya terminado correctamente 
		// el proceso de Asentamiento de Pagos SIAT asociado. (Ya que las transacciones de entrada para el delegado, 
		// provienen del proceso SIAT)
		if(this.getServicioBanco().getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO.longValue() && this.getBalance() != null){
			Asentamiento asentamiento = Asentamiento.getByIdBalanceYIdSerBan(this.getBalance().getId(), this.getServicioBanco().getId());
			if(asentamiento != null && asentamiento.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_PROCESADO_CON_EXITO.longValue()){
				this.addRecoverableValueError("El proceso de Asentamiento asociado (Nro: "+asentamiento.getId()+") no se terminó de procesar correctamente. Esto es condición necesaria para comenzar el asentamiento delegado.");
				return;
			}			
		}
		
		
	}
	
	/**
	 *  Lee las transacciones asociadas al proceso (bal_tranDel) y genera un archivo para sincronismo en el directorio
	 *  salida. Ademas segun el Servicio Banco y el Sistema podria  grabarlas en una db externa.
	 * 
	 * <i>(paso 1.2)</i>
	 */
	public void obtenerTransacciones(String outputDir) throws Exception{
	
		// Genero el archivo de texto
		String fileName = "tran_"+this.getServicioBanco().getCodServicioBanco()+".txt";
		
		BufferedWriter buffer = new BufferedWriter(new FileWriter(outputDir+"/"+fileName, false));

		for(TranDel tranDel: this.getListTranDel()){
			// 02 |0011178802|200902 |0000 |4 |11  |00   |26012009 |0000049.3900|00000.0000|0     |5065   |0      |0000000000|30012009
			// SIS|    CTA   |AÑO-PER|RESTO|CP|CAJA|CODTR|FECHAPAGO|IMPORTE     |RECARGO   |FILLER|  PAQ  |MARCATR|RECIBO    |FECHABALANCE	 		
			// Cant. de digitos por Columna:
			// 2  |   10     |   6   |  4  |1 |2   |2    |8        | 10+'.'+4   | 10+'.'+4 |1     | 4     | 1     |    10    | 8
			
			// Sistema
			buffer.write(StringUtil.completarCerosIzq(tranDel.getSistema().toString(),2));
			// Cuenta (NroComprobante)
			buffer.write("|"+StringUtil.completarCerosIzq(tranDel.getNroComprobante().toString(),10));
			// Año+Periodo
			String clave = StringUtil.completarCerosIzq(tranDel.getAnioComprobante().toString(),4)+StringUtil.completarCerosIzq(tranDel.getPeriodo().toString(),2);
			// Si al armar la clave queda con mas de 6 caracteres se toman  los ultimos 6. Esto es asi porque existen casos en los cuales el anio puede ser cero y el periodo un nro mayor de 2 digitos
			if(clave.length() > 6){
				clave = clave.substring(clave.length()-6,clave.length());
			}
			buffer.write("|"+clave);
				
			// Resto
			if(tranDel.getResto() != null)
				buffer.write("|"+StringUtil.completarCerosIzq(tranDel.getResto().toString(),4));
			else
				buffer.write("|");
			// Codigo de Pago
			if(tranDel.getCodPago() != null)
				buffer.write("|" + StringUtil.completarCerosIzq(tranDel.getCodPago().toString(),1));
			else
				buffer.write("|");
			// Caja
			if(tranDel.getCaja() != null)
				buffer.write("|" + StringUtil.completarCerosIzq(tranDel.getCaja().toString(), 2));
			else
				buffer.write("|");
			// Codigo Tr
			if(tranDel.getCodTr() != null)
				buffer.write("|" +  StringUtil.completarCerosIzq(tranDel.getCodTr().toString().trim(),2));
			else
				buffer.write("|00");
			// Fecha de Pago
			buffer.write("|" +  DateUtil.formatDate(tranDel.getFechaPago(), DateUtil.ddMMYYYY_MASK));
			// Importe
			buffer.write("|" +  StringUtil.formatDouble(NumberUtil.truncate(tranDel.getImporte(),SiatParam.DEC_IMPORTE_DB), "0000000000.0000"));
			// Recargo
			if(tranDel.getRecargo() != null)
				buffer.write("|" +  StringUtil.formatDouble(NumberUtil.truncate(tranDel.getRecargo(),SiatParam.DEC_IMPORTE_DB), "0000000000.0000"));
			else
				buffer.write("|");
			// Filler 
			if(tranDel.getFiller() != null)
				buffer.write("|" +  StringUtil.completarCerosIzq(tranDel.getFiller().trim(),1));
			else
				buffer.write("|0");
			// Paquete
			if(tranDel.getPaquete() != null)
				buffer.write("|" +  StringUtil.completarCerosIzq(tranDel.getPaquete().toString(),4));
			else
				buffer.write("|");
			// Marca Tr
			if(tranDel.getMarcaTr() != null)
				buffer.write("|" +  StringUtil.completarCerosIzq(tranDel.getMarcaTr().toString(),1));
			else
				buffer.write("|");
			// Recibo
			if(tranDel.getReciboTr() != null)
				buffer.write("|" +  StringUtil.completarCerosIzq(tranDel.getReciboTr().toString(),10));
			else
				buffer.write("|");
			// Fecha de Balance
			buffer.write("|" +  DateUtil.formatDate(tranDel.getFechaBalance(), DateUtil.ddMMYYYY_MASK));
			buffer.write("|"); // Pipe al final de la fila
			buffer.newLine();

		} 
		buffer.close();
		
		// Copia del Archivo de Transacciones para Consultar desde la Administracion del Proceso y
		// al directorio ultimo para facilitar su lectura desde los procesos externos
		File fileFrom = new File(outputDir+"/"+fileName);		
		File fileTo = new File(outputDir+"/"+this.getId()+fileName);
		String fileNameCopy = outputDir+"/"+this.getId()+fileName;
		
		try { 
			AdpRun.copyFile(fileFrom, fileTo); // Copia para consulta 
			
			// Copia a directorio 'ultimo'
            File dirUltimo = new File(outputDir, "ultimo");
            dirUltimo.mkdir();
            AdpRun.copyFile(fileFrom, dirUltimo);
		} catch (Exception e){
			this.addRecoverableValueError("Error al realizar copia del archivo de sincronismo.");
			return;
		}
	
		String nombre = "Archivo de transacciones";
		String descripcion = "Lista de transacciones a procesar. Copia del archivo de sincronismo.";
		this.getCorrida().addOutputFile(nombre, descripcion, fileNameCopy);			
	}

	
	/**
	 *  Lee los datos de sincronismo de otros sistemas y los carga en SIAT
	 * 
	 * <i>(paso 2.2)</i>
	 */
	public void procesarArchivosSinc(File fileSinPartida,File fileSinIndet,File fileSinSaldo) throws Exception{
		// Procesar Partida 
		BufferedReader inputSinPartida  = new BufferedReader(new FileReader(fileSinPartida));
		if(inputSinPartida != null){
			this.procesarSinPartida(inputSinPartida);
			inputSinPartida.close();
		}
		
		// Procesar Indeterminados 	
		BufferedReader inputSinIndet  = new BufferedReader(new FileReader(fileSinIndet));
		if(inputSinIndet != null){
			this.procesarSinIndet(inputSinIndet);
			inputSinIndet.close();
		}
		
		// Procesar Saldos A Favor
		// (por el momento no se pueden cargar en siat los saldos a favor, ya que necesitariamos la cuenta migrada
		// y el idRecurso para obtenerla)
		/*BufferedReader inputSinSaldo  = new BufferedReader(new FileReader(fileSinSaldo));
		if(inputSinSaldo != null){
			this.procesarSinSaldo(inputSinSaldo);
			inputSinSaldo.close();
		}*/
	}

	/**
	 *  Lee los datos de sincronismo para Partidas y los carga en SIAT
	 * 
	 * <i>(paso 2.2.1)</i>
	 */
	public void procesarSinPartida(BufferedReader input) throws Exception{
		String line; 
		Datum datum;
		long c = 0;
		Double importeTotalRecaudadoAct = 0D;
		Double importeTotalRecaudadoVen = 0D;
	    while (( line = input.readLine()) != null) {
	     		c++;
	       		if(c%2500==0){
	      			//SiatHibernateUtil.currentSession().getTransaction().commit();
	     			//SiatHibernateUtil.currentSession().beginTransaction();
	       			SiatHibernateUtil.currentSession().flush();
	     		}
	       		datum = Datum.parse(line);	// Parseamos la linea de SinPartida
	    		if(datum.getColNumMax()<7){
	     			this.addRecoverableValueError("La línea del archivo de sincronismo de partida nro "+c+" no tiene la cantidad de columnas necesarias");
	     			continue;
	     		}
	     		
	       		// SERBAN|ANIO PAGO|MES PAGO|CODPARTIDA|MARCA|IMPORTEEJEACT|FECHABALANCE|
	       		// 81 	 |2009	   |1		|901101	   |A	 |1397460.57   |30012009	|
	    		DiarioPartida diarioPartida = new DiarioPartida();
				diarioPartida.setFecha(this.getFechaBalance());
				diarioPartida.setEjercicio(this.getEjercicio());
				String codPartida = null;
				try{ 
					codPartida = datum.getLong(3).toString();
					codPartida = codPartida.substring(1);
					codPartida = Long.valueOf(codPartida).toString();
				}catch(Exception e){
					this.addRecoverableValueError("Error en código de partida en la línea "+c+".");
	     			continue;
				}
				Partida partida = Partida.getByCod(codPartida);
				if(partida == null){
					this.addRecoverableValueError("No se encontró la Partida de código "+codPartida+" en la db. Línea nro"+c+".");
	     			continue;					
				}	
				diarioPartida.setPartida(partida);
				
				Double importe = 0D;
				importe = datum.getDouble(5);
				if(importe == null){
					this.addRecoverableValueError("Error en el 'importe' en la línea "+c+".");
	     			continue;					
				}
				Double importeEjeAct = 0D;
				Double importeEjeVen = 0D;
				if("A".equals(datum.getCols(4).toUpperCase())){
					importeEjeAct = importe;
				}else{
					importeEjeVen = importe;
				}
				diarioPartida.setImporteEjeAct(NumberUtil.truncate(importeEjeAct,SiatParam.DEC_IMPORTE_DB));
				diarioPartida.setImporteEjeVen(NumberUtil.truncate(importeEjeVen,SiatParam.DEC_IMPORTE_DB));
				TipOriMov tipOriMov = TipOriMov.getById(TipOriMov.ID_ASEDEL);
				diarioPartida.setTipOriMov(tipOriMov);
				diarioPartida.setIdOrigen(this.getId());
				EstDiaPar estDiaPar = EstDiaPar.getById(EstDiaPar.ID_CREADA);
				diarioPartida.setEstDiaPar(estDiaPar);
				diarioPartida.setBalance(this.getBalance());
				
				BalDAOFactory.getDiarioPartidaDAO().update(diarioPartida);
				
				importeTotalRecaudadoAct += diarioPartida.getImporteEjeAct();
				importeTotalRecaudadoVen += diarioPartida.getImporteEjeVen();
	    }
	    
		// Descargar el total recaudado de la Cuenta Puente que corresponde por Servicio Banco (ParCuePue)
		Partida parCuePue = this.getServicioBanco().getParCuePue();
		if(parCuePue != null){			
			// Creacion de Caja7 para Descarga de Cuentas Puentes
			Caja7 caja7 = new Caja7();
			
			caja7.setBalance(this.getBalance());
			caja7.setFecha(this.getFechaBalance());
			caja7.setDescripcion("Ajuste de descarga a Cuenta Puente. Asentamiento Nro: "+this.getId());
			caja7.setImporteEjeAct(NumberUtil.truncate((importeTotalRecaudadoAct+importeTotalRecaudadoVen)*(-1),SiatParam.DEC_IMPORTE_DB));
			caja7.setImporteEjeVen(0D);

			caja7.setPartida(parCuePue);
			
			BalDAOFactory.getCaja7DAO().update(caja7);
		}

	}

	/**
	 *  Lee los datos de sincronismo para Indeterminados y los carga en SIAT
	 * 
	 * <i>(paso 2.2.2)</i>
	 */
	public void procesarSinIndet(BufferedReader input) throws Exception{
		String line; 
		Datum datum;
		long c = 0;
	    while (( line = input.readLine()) != null) {
	     		c++;
	       		if(c%2500==0){
	       			SiatHibernateUtil.currentSession().flush();
	       			//SiatHibernateUtil.currentSession().getTransaction().commit();
	     			//SiatHibernateUtil.currentSession().beginTransaction();
	     			//AdpRun.changeRunMessage(c+" lineas de Indeterminados procesados.", 0);
	     		}
	       		datum = Datum.parse(line);	// Parseamos la linea de SinIndet
	    		if(datum.getColNumMax()<7){
	     			this.addRecoverableValueError("La línea del archivo de sincronismo de Indeterminados nro "+c+" no tiene la cantidad de columnas necesarias");
	     			continue;
	     		}
	     		
				// SISTEMA, NROCOMPROBANTE, CLAVE, RESTO, IMPORTECOBRADO, BASICO, CALCULADO, INDICE, RECARGO,
				// CODPARTIDA,CODINDETERMINADO, FECHAPAGO, CAJA, PAQUETE, CODPAGO, FECHABALANCE, FILLER, RECIBOTR, 
	    		// 81|706|3|0|27.75|0.0|0.0|0|0.0|972102|2|01102008|65|4154|4|02102008||0|
	    		IndetBal indetBal = new IndetBal();
	    		
	    		indetBal.setSistema(datum.getCols(0));
	    		indetBal.setNroComprobante(datum.getCols(1));
	    		indetBal.setClave(StringUtil.completarCerosIzq(datum.getCols(2), 6));
	    		indetBal.setResto(StringUtil.completarCerosIzq(datum.getCols(3), 4));
	    		indetBal.setImporteCobrado(datum.getDouble(4));
	    		indetBal.setImporteBasico(datum.getDouble(5));
	    		indetBal.setImporteCalculado(datum.getDouble(6));
	    		indetBal.setIndice(datum.getDouble(7));
	    		indetBal.setRecargo(datum.getDouble(8));
	    		indetBal.setPartida(datum.getCols(9));
	    		indetBal.setCodIndet(datum.getInteger(10));
	    		Date fechaPago = DateUtil.getDate(datum.getCols(11), DateUtil.ddMMYYYY_MASK);
	    		if(fechaPago == null){
	    			this.addRecoverableValueError("No se puedo convertira la fecha de pago '"+datum.getCols(11)+"' para la línea nro"+c+".");
	     			continue;					
	    		}
	    		indetBal.setFechaPago(fechaPago);
	    		indetBal.setCaja(datum.getInteger(12));
	    		indetBal.setPaquete(datum.getInteger(13));
	    		indetBal.setCodPago(datum.getInteger(14));
	    		indetBal.setFechaBalance(this.getFechaBalance()); 
	    		indetBal.setFiller(datum.getCols(16));
	    		indetBal.setReciboTr(datum.getLong(17));
	    		indetBal.setBalance(this.getBalance());
	    		
	    		BalDAOFactory.getBalanceDAO().update(indetBal);		
	    }
	}

	/**
	 *  Lee los datos de sincronismo para Saldos A Favor y los carga en SIAT
	 *  (TODO por el momento no se pueden cargar en siat los saldos a favor, ya que necesitariamos la cuenta migrada
	 *  y el idRecurso para obtenerla)
	 * <i>(paso 2.2.3)</i>
	 */
	public void procesarSinSaldo(BufferedReader input) throws Exception{
		/*String line; 
		Datum datum;
		long c = 0;
	    while (( line = input.readLine()) != null) {
	     		c++;
	       		if(c%2500==0){
	      			SiatHibernateUtil.currentSession().flush();
	     		}
	       		datum = Datum.parse(line);	// Parseamos la linea de SinSaldos
	    		if(datum.getColNumMax()<9){
	     			this.addRecoverableValueError("La línea del archivo de sincronismo de Saldos a Favor nro "+c+" no tiene la cantidad de columnas necesarias");
	     			continue;
	     		}
	     		
				// SISTEMA|NROCOMPROBANTE|ANIO|CUOTA|FILLER|IMPORTEPAGO|IMPORTEDEBPAG|FECHAPAGO|FECHABALANCE|
	       		// 2	  |12903309		 |2009|1	|0	   |93.15	   |92.48		 |20012009 |30012009	|
	    		// Generar SaldoAFavor
				SaldoBal saldoBal = new SaldoAFavor();
				
				saldoBal.setCuenta(cuenta);
				TipoOrigen tipoOrigen = TipoOrigen.getById(TipoOrigen.ID_ASEDEL);
				saldoBal.setTipoOrigen(tipoOrigen);
		        saldoBal.setFechaGeneracion(new Date());
		        Long idAreaUsr = (Long) userContext.getIdArea();
	       		Area area = (Area) Area.getById(idAreaUsr);
				saldoBal.setArea(area);
				Partida partida = Partida.getByCod(??);
				saldoBal.setPartida(partida);
				saldoBal.setImporte();
				EstSalAFav estSalAFav = EstSalAFav.getById(EstSalAFav.ID_CREADO);
				saldoBal.setEstSalAFav(estSalAFav);
				saldoBal.setDescripcion();
				
				
				BalSaldoAFavorManager.getInstance().createSaldoAFavor(saldoAFavor);
			
	    }*/

	}

	/**
	 * <b>Genera Formularios para control del paso 1:</b> 
	 * <p>- Reporte Totales por Sistema (5018): archivo pdf</p>
	 * <p>- Lista de Transacciones: txt</p>
	 * <i>(paso 1.5)</i>
	 */
	public void generarFormulariosPaso1(String outputDir) throws Exception{
		
		//-> Reporte de Totales por Sistema (5018) (PDF)
		String fileName = this.generarPdfTotalesPorSistema(outputDir);
		String nombre = "Totales por Sistema (5018)";
		String descripcion = "Permite consultar los totales de transacciones e importes por Sistema.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			
	}
	
	/**
	 * <b>Genera Formularios para control del paso 2:</b> 
	 * <p>- Totales por Partida(4007): archivo pdf</p>
	 * <i>(paso 2.3)</i>
	 */
	public void generarFormulariosPaso2(String outputDir) throws Exception{
		// -> Totales por Partida (PDF)
		String fileNamePdf = this.generarPdfTotalesPorPartida(outputDir);
		String nombre = "Totales por Partida";
		String descripcion = "Permite consultar los totales imputados a cada partida.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNamePdf);
	}
	
	/**
	 * Genera el Reporte pdf "Totales por Sistema (5018)" resultado del paso 1 del proceso de Asentamiento Delegado.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorSistema(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		List<Object[]> listResult = BalDAOFactory.getTranDelDAO().getListForReportByAsentamiento(this);
		Double importeTotal = 0D;
		Long cantTotal = 0L;

		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO1_1);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Asentamiento - Totales por Sistema");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Asentamiento Delegado - Totales por Sistema");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);
				
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("TotalesPorSistema");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Sistema","sistema"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(Object[] arrayResult: listResult){
			fila.add(new CeldaVO((String) arrayResult[0],"sistema"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		
		printModel.setData(tabla);
		printModel.setTopeProfundidad(3);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotales5018.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}

	/**
	 * Elimina todas las tablas auxiliares utilizadas en el Asentamiento.
	 * (Solo se llama al finalizar correctamente el ultimo paso)
	 *
	 */
	public void eliminarTablasTemporales(){
		// Eliminar las tablas temporales asociadas al Asentamiento:
		BalDAOFactory.getTranDelDAO().deleteAllByAseDel(this);
	}
	
	
	/**
	 * Genera el Reporte pdf "Totales por Partida" resultado del paso 2 del proceso de Asentamiento Delegado.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorPartida(String fileDir) throws Exception{
		//		Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		
		List<Object[]> listDetalleTotal = BalDAOFactory.getDiarioPartidaDAO().getListDetalleForReportByAseDel(this);
		
		Double importeTotalDetalle = 0D;
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO2_1);
		
		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Asentamiento Delegado de Pagos - Reporte Distribucion de Partidas");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Asentamiento Delegado de Pagos - Reporte Distribucion de Partidas");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);
		
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arman una tabla para el Reporte Total Por Partidas
		TablaVO tabla = new TablaVO("TotalesPartida");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listDetalleTotal){
			Double suma = (Double) arrayResult[2]+(Double) arrayResult[3];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotalDetalle += (Double) arrayResult[2]+(Double) arrayResult[3];
		}
	
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDetalle, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
		
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"AseDel_ReportePartida.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}
}
