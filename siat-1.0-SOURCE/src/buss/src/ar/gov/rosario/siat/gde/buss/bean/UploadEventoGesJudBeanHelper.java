//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.gde.iface.model.FilaUploadGesjudEvento;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class UploadEventoGesJudBeanHelper {

	private static Logger log = Logger.getLogger(UploadEventoGesJudBeanHelper.class);
	
	private EmisionExternaAdapter uploadEventoGesJudAdapter;
	private UserContext userContext;
	private int nroFila;		
	
	/**Contiene el resultado del analisis de la linea actual. Al inicio es TRUE y al agregar un log de error pasa a false.*/
	private boolean analisisLineaExito=true;
	
	
	/**
	 * Inicializa la clase y graba el archivo para analisis
	 * @param userContext
	 * @param uploadEventoGesJudAdapter
	 * @throws Exception
	 */
	public UploadEventoGesJudBeanHelper(UserContext userContext,EmisionExternaAdapter uploadEventoGesJudAdapter) throws Exception{
		UUID randomUUID = UUID.randomUUID();
		uploadEventoGesJudAdapter.setResultExito(true);// se resetea la variable
		uploadEventoGesJudAdapter.setFileName(uploadEventoGesJudAdapter.getFileName()+"_"+ randomUUID);
		String pathNameDatos = uploadEventoGesJudAdapter.getFileNameCompleto();
		DemodaUtil.grabarArchivo(pathNameDatos,uploadEventoGesJudAdapter.getFileData(), false);
		uploadEventoGesJudAdapter.setFileNameLogAnalisis("/tmp/logAnalisis_"+ randomUUID +".txt");

		this.uploadEventoGesJudAdapter = uploadEventoGesJudAdapter;
		this.userContext = userContext;
		
		log.debug("Archivo grabado exitosamente");
	}
	
	/**
	 * Realiza el analisis del archivo para encontrar potenciales errores. Agrega entradas en el archivo de log 
	 * @return El adapter con el resultado del analisis seteado (si fue exitoso o no).
	 */
	public EmisionExternaAdapter parseFile(){
		try {			
			log.debug("parseFile : enter");
			
			FileReader fr = new FileReader(uploadEventoGesJudAdapter.getFileNameCompleto());
			BufferedReader br = new BufferedReader(fr);
			String s;
			nroFila = 1;					
			while((s = br.readLine()) != null) {
				if(s!=null && s.length()>0 && s.charAt(0)!='#'){ // las que empiezan con # se toman como comentarios. Las que estan vacias no se tienen en cuenta
					analisisLineaExito=true;
					String[] linea = s.split(FilaUploadGesjudEvento.SEPARATOR);
					FilaUploadGesjudEvento fila = null;
					
					if(validarFormatoLinea(linea)){
						
						fila = new FilaUploadGesjudEvento(nroFila,NumberUtil.getLong(linea[0]), 
								linea[1], NumberUtil.getInt(linea[2]), DateUtil.getDate(linea[3]), linea[4], linea[5]);					
	
						// Analiza el procurador									
						Procurador procurador = Procurador.getByIdNull(fila.getIdProcurador());
						if(procurador!=null){
//							if(!userContext.getIdProcurador().equals(procurador.getId()))
//								agregarLogError("El Registro no corresponde al procurador logueado");
//							else{
								// Analiza la Gestion Judicial
								GesJud gesJud = GesJud.getByDes(fila.getDesGesJud());
								if(gesJud!=null){
									if(gesJud.getProcurador().getId().equals(fila.getIdProcurador())){
										Evento evento= Evento.getByCodigo(fila.getCodEvento());
	
										// Analiza si contiene el evento
										if(gesJud.contieneEvento(evento.getId().longValue()) && 
												evento.getEsUnicoEnGesJud().equals(1))
												agregarLogError("El evento ya existe en la Gestión Judicial y no puede agregarse nuevamente");
															
										// Analiza la fecha del evento con la fecha actual (que no sea posterior)
										if(DateUtil.isDateAfter(fila.getFechaEvento(), new Date())){
											agregarLogError("La fecha del evento no puede ser mayor a la fecha actual");
											
										}
										
										// Analiza la fecha del evento con la fecha de alta de la gesJud
										if(!DateUtil.isDateBefore(fila.getFechaEvento(), gesJud.getFechaAlta())){
										/*	// Verifica si la fila actual ya fue ingresada anteriormente (gesjud, evento y fecha)
											for(GesJudEvento gesJudEvento: gesJud.getListGesJudEvento()){
												uploadEventoGesJudAdapter.contieneFilaAgregar(fila)
												boolean fechasIguales = DateUtil.dateCompare(gesJudEvento.getFechaEvento(),fila.getFechaEvento())==0;
												boolean eventosIguales = gesJudEvento.getEvento().getCodigo().equals(fila.getCodEvento());
												
												if(fechasIguales && eventosIguales){
													agregarLogError("Evento duplicado para la Gestión Judicial");
													break;
												}
											}*/
											
										}else
											agregarLogError("La fecha del evento es menor a la fecha de alta de la Gestión Judicial");									
								
										// Verifica si la fecha del evento a agregar es menor a la del ultimo evento ingresado o a ingresar para la gesJud
										GesJudEvento ultimoEvento = gesJud.getUltimoEventoIngresado();
										log.debug("codEventoActual:"+fila.getCodEvento()+"         ultimoEvento:"+ultimoEvento+"   fecha:"+(ultimoEvento!=null?ultimoEvento.getFechaEvento():"no trajo nada"));
										if(ultimoEvento!=null && DateUtil.isDateBefore(fila.getFechaEvento(), ultimoEvento.getFechaEvento()))
											agregarLogError("La fecha del Evento es menor a la del último evento ingresado para la Gestión Judicial");
										else{
											Date ultimaFec = getFecUltimoEventoIngresar(uploadEventoGesJudAdapter, fila.getDesGesJud());
											if(ultimaFec!=null && DateUtil.isDateBefore(fila.getFechaEvento(), ultimaFec))
												agregarLogError("La fecha del Evento es menor a la del último evento a ingresar para la Gestión Judicial");
										}
								
										
										// Analiza si la fila ya existe en el archivo (esta duplicada)
										if(uploadEventoGesJudAdapter.contieneFilaAgregar(fila))
											agregarLogError("Fila duplicada en el archivo.");
										
										// Analiza si existen los precedentes del evento en la BD o ya analizados en el archivo
										if(!gesJud.contienePredecedores(evento) && !tienePrecedentesForAgregar(uploadEventoGesJudAdapter,fila))
											agregarLogError("La Gestión Judicial no contiene todos los eventos precedentes.");							
									}else
										agregarLogError("La Gestión Judicial no está asignada al procurador");						
								}else
									agregarLogError("No se encontró la Gestión Judicial");	
							//}
						}else{
							agregarLogError("No se encontro el procurador");
						}
					}
					
					if(!analisisLineaExito){
						// Inserta un espacio en blanco para separar los errores de las distintas lineas
						DemodaUtil.grabarArchivo(uploadEventoGesJudAdapter.getFileNameLogAnalisis(), "\n".getBytes(), true);
					}else{
						// El analisis de la linea fue exitoso, la agrega a la lista para grabar
						uploadEventoGesJudAdapter.getListFilaGrabar().add(fila);					
					}
				}
				nroFila++;
			}// fin while		
						
		} catch (FileNotFoundException e) {
			log.error("No se encontro el archivo a cargar");
			uploadEventoGesJudAdapter.addRecoverableError(GdeError.UPLOAD_EVENTO_MSG_FILE_NOTFOUND);
		} catch (Exception e){
			log.error(e);
		}
		
		log.debug("parseFile : exit");
		
		return uploadEventoGesJudAdapter;
	}
	
	/**
	 * Graba los eventos de las lineas marcadas durante el analisis en parseFile(), sin realizar validaciones, ya que se supone que se hicieron en ese metodo
	 * @return
	 * @throws Exception
	 */
	public static EmisionExternaAdapter grabarEventos(UserContext userContext, EmisionExternaAdapter uploadEventoGesJudAdapter) throws Exception{
		DemodaUtil.setCurrentUserContext(userContext);
		Transaction tx = null;		
		try {
			tx = SiatHibernateUtil.currentSession().beginTransaction();
			int cantGrabadas = 0;
			boolean superoCantMaxTx = false;
			for(FilaUploadGesjudEvento fila:uploadEventoGesJudAdapter.getListFilaGrabar()) {
															
				// Obtiene la gestion judicial
				GesJud gesJud = GesJud.getByDes(fila.getDesGesJud());
				
				// crea el evento para la gestion judicial					
				Evento evento= Evento.getByCodigo(fila.getCodEvento());
					
				GesJudEvento gesJudEvento = new GesJudEvento();
				gesJudEvento.setEvento(evento);
				gesJudEvento.setGesJud(gesJud);
				gesJudEvento.setFechaEvento(fila.getFechaEvento());
				gesJudEvento.setObservacion(fila.getStrObs());
				gesJudEvento.setEstado(Estado.ACTIVO.getId());
				//gesJudEvento.setUsuarioUltMdf(fila.getUsr());
				gesJudEvento.setUsuarioUltMdf(userContext.getUserName());
				
				// graba el evento creado
				gesJudEvento = gesJud.createGesJudEventoSinValidate(gesJudEvento);
				log.debug("grabo el evento");				
				
	            // Graba el historico estado de la gestion judicial
	            GdeGDeudaJudicialManager.getInstance().grabarHistoricoEstado(gesJud, HistGesJud.LOG_INCLUSION_EVENTO_ARCHIVO);
				
				// actualiza la cantidad de lineas grabadas
	            cantGrabadas++;
	            
	            if(cantGrabadas==10000){
	            	uploadEventoGesJudAdapter.addRecoverableError(GdeError.UPLOAD_EVENTO_CANT_TRANSAC_SUPERADAS);
	            	superoCantMaxTx=true;
	            	tx.rollback();
	            	break;
	            }
			}
			
			if(!superoCantMaxTx){
				tx.commit();					
				uploadEventoGesJudAdapter.setCantLineasGrabadas(cantGrabadas);
				DemodaUtil.eliminarArchivo(uploadEventoGesJudAdapter.getFileNameCompleto());
			}
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
		
		return uploadEventoGesJudAdapter;
	}
	
	/**
	 * Valida si el formato de la linea es correcto. Agrega los logs correspondientes a medida que va encontrando errores
	 * @param linea
	 * @param nroFila
	 * @param pathNameLog
	 * @return
	 * @throws Exception
	 */
	private boolean validarFormatoLinea(String[] linea) throws Exception{
		boolean valida = true;
		log.debug("validando linea "+nroFila+"    largo:"+linea.length);		
		if(linea!=null && linea.length==6){
			
			String strIdProcurador = linea[0];
			String desGesJud = linea[1];
			String strCodEvento = linea[2];
			String strFechaEvento = linea[3];
			
			// Analiza el procurador
			if(NumberUtil.getLong(strIdProcurador)==null){
				agregarLogError("Formato de Línea incorrecto: Nro Procurador");
				valida=false;
			}
			
			// Analiza la gestion judicial
			if(StringUtil.isNullOrEmpty(desGesJud)){
				agregarLogError("Formato de Línea incorrecto: No se ingresó la Gestión Judicial");
				valida=false;
			}
			
			// Analiza el codigo de evento
			if(NumberUtil.getInt(strCodEvento)==null){
				agregarLogError("Formato de Línea incorrecto: Código de Evento");
				valida=false;
			}
			
			// Analiza la fecha
			if(DateUtil.getDate(strFechaEvento)==null){
				agregarLogError("Formato de Línea incorrecto: Fecha - Debe ser con formato dd/MM/yyyy");
				valida=false;
			}
		}else{
			agregarLogError("Formato de Línea incorrecto: Cantidad de parametros incorrecto");
			valida=false;
		}
		log.debug("validarFormatoLinea - exit");
		return valida;
	}


	
	/**
	 * Agrega la cadena de error pasada como parametro al archivo de log. Incluye en el log el nro de fila que se esta analizando
	 * @param error: msj a loguear
	 * @author arobledo
	 * @throws Exception
	 */
	private void agregarLogError(String error) throws Exception{		
		String cadenaError = "Linea "+nroFila+"   - ERROR: "+error+"\n";
		String pathNameLog = uploadEventoGesJudAdapter.getFileNameLogAnalisis();
		log.debug("Va a agregar un error:"+cadenaError);
		DemodaUtil.grabarArchivo(pathNameLog, cadenaError.getBytes(), true);
		
		uploadEventoGesJudAdapter.setResultExito(false);
		analisisLineaExito=false;
	}
	

	
	/**
	 * Se utiliza para ver si el archivo ya analizo (insertara) los eventos predecesores del evento de la fila pasada como parametro, en filas anteriores
	 * @param evento
	 * @return
	 * @throws Exception 
	 */
	public boolean tienePrecedentesForAgregar(EmisionExternaAdapter adapter, FilaUploadGesjudEvento fila) throws Exception{
		log.debug("archivoTienePrecedentes - enter");
		if(uploadEventoGesJudAdapter.getListFilaGrabar().isEmpty())
			return false;
		
		List<Evento> listEventosPredecesores = Evento.getByCodigo(fila.getCodEvento()).getListEventosPredecesores();
		for(Evento evento: listEventosPredecesores){
			boolean loTiene = false;
			for(FilaUploadGesjudEvento f:uploadEventoGesJudAdapter.getListFilaGrabar()){
				log.debug("Va a comparar:"+f.getDesGesJud()+". con:"+fila.getDesGesJud());
				boolean igualGesJud = f.getDesGesJud().equals(fila.getDesGesJud());
				log.debug("Va a comparar:"+f.getCodEvento()+".   con:"+evento.getCodigo());
				boolean igualCodEvento = f.getCodEvento().intValue()==evento.getCodigo().intValue();
				if(igualCodEvento && igualGesJud){
					log.debug("Lo tiene");
					loTiene = true;
					break;
				}
				
			}
			
			if(!loTiene)// Si a al menos 1 no lo tiene, sale
				return false;
		}
		log.debug("archivoTienePrecedentes - exit TRUE");
		return true;
	}

	/**
	 * Obtiene la fecha del ultimo evento a ingresar (ya analizado), para la gesJud pasada como parametro
	 * @param adapter
	 * @param desGesJud
	 * @return null si no encuentra nada
	 */
	public Date getFecUltimoEventoIngresar(EmisionExternaAdapter adapter, String desGesJud){
		log.debug("getFecUltimoEventoIngresar - enter");
		Integer lastPos =null;
		for(FilaUploadGesjudEvento fila:adapter.getListFilaGrabar()){
			if(fila.getDesGesJud().equals(desGesJud))
				lastPos=new Integer(adapter.getListFilaGrabar().indexOf(fila));
		}
		
		log.debug("getFecUltimoEventoIngresar - exit - lastPos:"+lastPos);
		return (lastPos!=null?adapter.getListFilaGrabar().get(lastPos.intValue()).getFechaEvento():null);
	}
}
