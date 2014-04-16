//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.ws.drei;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.bean.ConsoleManager;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.pad.iface.model.MRCategoriaRS;
import ar.gov.rosario.siat.rec.iface.model.NovedadRSVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaMail;


public class DreiWebService {
	private static Logger log = Logger.getLogger(DreiWebService.class);

	public String echo(String in) {
		return in;
	}

	public String status() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ConsoleManager.getInstance().status(pw);
		return sw.toString();
	}

	
	public static void sendMailError(String txtEx, String msg) {
		try {
			DefServiceLocator.getConfiguracionService().updateSiatParam();

			String server = SiatParam.getString(SiatParam.MAIL_SERVER);
			String to = SiatParam.getString("NovObjImpMailTo", "ERROR");

			String from = SiatParam.getString("NovObjImpMailFrom", "");
			String subject = "[siat:wserror] Error durante el procesamiento de Novedades del WS AFIP";
			String body = "Se detecto un error al procesar novedad WS AFIP";
			body += "Exception: \n" + txtEx + "\n";
			body += "Datos novedad: \n" + msg + "\n";

			DemodaMail.send(server, to, from, subject, body, "");
			
		} catch (Exception e) {
			log.error("sendMailError(): Fallo envio de Mail.", e);
		}
	}

	public String adhesion(String recv) {
		Map<String, String> record = new TreeMap<String, String>();

		try {

			log.debug("**WS** - inicio adhesion");
			
			MapaHelper mh = new MapaHelper(recv, "ISO-8859-1");

			String tipoTransaccion = mh.getString("TIPO-TRANS");
				String usuario = mh.getString("USUARIO");
				Integer tipoUsuario = mh.getInteger("TIPO-USUARIO");
	
				String cuit = mh.getString("CUIT");
				String desCont = mh.getString("DES-CONT");
				String domLocal = mh.getString("DOM-LOCAL");
				String telefono = mh.getString("TELEFONO");
				String email = mh.getString("EMAIL");
				
				Integer tipoCont = mh.getInteger("TIPO-CONT");
	
			  	String isib = mh.getString("ISIB");
				String nroCuenta = mh.getString("NRO-CUENTA");
				String listActividades = mh.getString("ACTIVIDADES");
	
				Integer mesInicio = mh.getInteger("MES-INICIO");
				Integer anioInicio = mh.getInteger("ANIO-INICIO");
				
				Double precioUnitario = mh.getDouble("PRECIO-UNIT");
				Integer canPer = mh.getInteger("CANT-PERS") ;
	
				Double ingBruAnu = mh.getDouble("ING-BRU-ANU"); 
				Double supAfe = mh.getDouble("SUP-AFE");
				Double publicidad = mh.getDouble("ALIC-PUBLICIDAD") ;
				Double redHabSoc = mh.getDouble("ALIC-HAB-SOC");
				Integer adicEtur = mh.getInteger("ADIC-ETUR");
				
				Integer mesBaja = mh.getInteger("MES-BAJA");
				Integer anioBaja = mh.getInteger("ANIO-BAJA");
				Integer motivoBaja = mh.getInteger("MOTIVO-BAJA");
				
				Long idTramite = mh.getLong("ID-TRAMITE");
				
				boolean confirmado=false;
				if ( "S".equals(mh.getString("CONFIRMADO")) )
					confirmado = true;

				
			log.debug("**WS** - llama al servicio");
			
			// invoca al servicio de adhesion 
			MRCategoriaRS catRS = RecServiceLocator.getAdhesionRSWService().procesarServicioRS(
					tipoTransaccion, usuario, tipoUsuario, cuit, desCont, tipoCont, isib, nroCuenta,
					listActividades, mesInicio, anioInicio, ingBruAnu, supAfe, publicidad,
					redHabSoc, adicEtur, precioUnitario, canPer, confirmado, domLocal, telefono, email, mesBaja, anioBaja, motivoBaja, idTramite);
			
			// aqui construye la respuesta en funcion de la salida
			mh.setInteger("ERR-CODE", catRS.getCodError());
			mh.setString ("ERR-STRING", catRS.getDetalleError());
			
			
			log.debug("**WS** - despues del servicio: ERR:" + catRS.getCodError());
			
			String xmlRet ="";
			
			// procesa segun el tipo de transaccion
			if ("ADHESION".equals(tipoTransaccion) || "MODIFICACION".equals(tipoTransaccion) || 
					"RECATEGORIZACION".equals(tipoTransaccion) || "BAJA".equals(tipoTransaccion) ) {
				
				mh.setLong("ID-TRAMITE", catRS.getIdTramite());
				mh.setDate("FECHA-TRAN", catRS.getFechaTransaccion());
				
				mh.setInteger("CALC-CATEGORIA", catRS.getNroCategoria());
				mh.setDouble ("CALC-IMPORTE-DREI", catRS.getImporteDrei());
				mh.setDouble ("CALC-IMPORTE-ETUR", catRS.getImporteEtur());
				mh.setDouble ("CALC-IMPORTE-ADIC", catRS.getImporteAdicional());
				
				mh.setString("CALC-DES-CATEGORIA", catRS.getDesCategoria());
				mh.setString("CALC-DES-PUBLICIDAD", catRS.getDesPublicidad());
				mh.setString("CALC-DES-ETUR", catRS.getDesEtur());
				
				mh.setString ("CALC-CUMUR", catRS.getCuna());
				mh.setString ("CALC-COD-BAR", catRS.getCodBar()); //catRS.getCodBarComprimido()); // Se modifica pasando el codBar comprimido segun el pedido del mantis 4942. (SE VUELVE A MANDAR SIN COMPRIMIR POR PROBLEMAS DE LECTURA DE AFIP)

				
				mh.setString("CALC-DES-BAJA", catRS.getDesBaja());
				
				// se quita por error en parseo
				// mh.setString("CALC-MSG-DEUDA", catRS.getMsgDeuda());
				
				log.debug("**WS** - adentro" + mh.getXML());
				xmlRet= mh.getXML();
				
			} else if ("LISTAR-TRAMITES".equals(tipoTransaccion) || "VER-TRAMITE".equals(tipoTransaccion)) {

				log.debug("WS* DreiWebService - dentro de: LISTAR-TRAMITES O VER-TRAMITE" );

				if ( catRS.getCodError().intValue() != 0 ) {
					log.debug("WS* ERR <> 0" );
					xmlRet= mh.getXML();
					
				} else {
					
					log.debug("WS* ERR == 0" );
					
					mh = new MapaHelper("ISO-8859-1");
					xmlRet += mh.getXMLHeader();
					xmlRet += "<ric>\n";
					
					log.debug("WS**antes de iterar las nvedades" );
					
					// aqui tiene que recorrer la lista de novedades que vienen en la categoria y por cada una armar un bloque <ric>
					for (NovedadRSVO novedadRS : catRS.getListNovedadRS()) {
						
						mh = new MapaHelper("ISO-8859-1");
						
						mh.setLong("ID-TRAMITE", novedadRS.getId());
						mh.setDate("FECHA-TRAN", novedadRS.getFechaTransaccion());
	
						mh.setString("TIPO-TRANS", novedadRS.getTipoTransaccion());
						mh.setString("USUARIO", novedadRS.getUsuario());
						mh.setInteger("TIPO-USUARIO", novedadRS.getTipoUsuario());
	
						mh.setString("CUIT", novedadRS.getCuit());
						mh.setString("DES-CONT", novedadRS.getDesCont());
						mh.setString("DOM-LOCAL", novedadRS.getDomLocal());
						mh.setString("TELEFONO", novedadRS.getTelefono());
						mh.setString("EMAIL", novedadRS.getEmail());
						
						mh.setInteger("TIPO-CONT", novedadRS.getTipoCont());
	
					  	mh.setString ("ISIB", novedadRS.getIsib() );
						mh.setString ("NRO-CUENTA", novedadRS.getNroCuenta() );
						mh.setString ("ACTIVIDADES", novedadRS.getListActividades());
	
						mh.setInteger("MES-INICIO", novedadRS.getMesInicio());
						mh.setInteger("ANIO-INICIO", novedadRS.getAnioInicio());

						mh.setInteger("MES-BAJA", novedadRS.getMesBaja());
						mh.setInteger("ANIO-BAJA", novedadRS.getAnioBaja());
						mh.setInteger("MOTIVO-BAJA", novedadRS.getMotivoBaja());
						
						mh.setDouble ("PRECIO-UNIT", novedadRS.getPrecioUnitario());
						mh.setInteger("CANT-PERS", novedadRS.getCanPer()) ;
	
						mh.setDouble ("ING-BRU-ANU", novedadRS.getIngBruAnu()); 
						mh.setDouble ("SUP-AFE", novedadRS.getSupAfe() );
						mh.setDouble ("ALIC-PUBLICIDAD", novedadRS.getPublicidad()) ;
						mh.setDouble ("ALIC-HAB-SOC", novedadRS.getRedHabSoc());
						mh.setInteger("ADIC-ETUR", novedadRS.getAdicEtur());
	
						mh.setInteger("CALC-CATEGORIA", novedadRS.getNroCategoria());
						mh.setDouble ("CALC-IMPORTE-DREI", novedadRS.getImporteDrei());
						mh.setDouble ("CALC-IMPORTE-ETUR", novedadRS.getImporteEtur());
						mh.setDouble ("CALC-IMPORTE-ADIC", novedadRS.getImporteAdicional());
						
						mh.setString ("CALC-DES-CATEGORIA", novedadRS.getDesCategoria());
						mh.setString ("CALC-DES-PUBLICIDAD", novedadRS.getDesPublicidad());
						mh.setString ("CALC-DES-ETUR", novedadRS.getDesEtur());
	
						mh.setString ("CALC-MENSAJE", catRS.getDesMensaje());
						mh.setString ("CALC-DES-BAJA", catRS.getDesBaja());
						
						mh.setString ("CALC-CUMUR", novedadRS.getCuna());
						mh.setString ("CALC-COD-BAR", novedadRS.getCodBar()); //novedadRS.getCodBarComprimidoView());  (SE VUELVE A MANDAR SIN COMPRIMIR POR PROBLEMAS DE LECTURA DE AFIP)
						
						// se quita por error en el parseo
						// mh.setString("CALC-MSG-DEUDA", catRS.getMsgDeuda());
	
						xmlRet += mh.getXMLTRAMITE();
	
						log.debug("WS**agrego novedad:" + mh.getXMLTRAMITE() );

					}
					xmlRet += "</ric>\n";

				}
				
			} else if ("GENERAR-CUMUR".equals(tipoTransaccion) ) {

				log.debug("WS* DreiWebService - dentro de: LISTAR-TRAMITES O VER-TRAMITE" );
				
				mh = new MapaHelper("ISO-8859-1");
				mh.setString("LISTA-CUMUR", catRS.getListCUMUR() );
				xmlRet= mh.getXML();
				
				
			} else {
				// cuando no reconoce ni el tipo de tramite
				xmlRet= mh.getXML();

			}
				
			log.debug("**WS** -sale ok:");
			log.debug("**WS** -XML:" + xmlRet);
			
			// se pide el CML al MapaHelper para devolver
			return xmlRet;
			
		} catch (Exception ex) {
			log.debug("**WS ERROR INESPERADO** :");
			return handleError(MRCategoriaRS.ERR_INESPERADO, ex, recv);
		}
	}

	
	
	
	private String handleError(int excepcion, Exception ex, String msg) {
		StringWriter result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);

        String xmlRet="";
        try {
			MapaHelper mh = new MapaHelper("ISO-8859-1");
			mh.setInteger("ERR-CODE", -100);
			mh.setString ("ERR-STRING", "ERROR: Ha ocurrido un error inesperado. La transacción no se ha completado" );
			xmlRet= mh.getXML();
        
			log.error(result.toString());
			this.sendMailError(result.toString(), msg);
			
        } catch (Exception e) {
        	xmlRet = "ERROR TOTAL";
        }
        return xmlRet;
        
        
	}

	public Map<String, String> parse(String xml, String encoding, Map<String, String> map) throws Exception {
		byte[] bytes = xml.getBytes(encoding);
		InputStream in = new ByteArrayInputStream(bytes);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(in);
	
		if (!encoding.equalsIgnoreCase(parser.getEncoding())) {
			throw new Exception("xml encoding must be:" + encoding);
		}
		
		int event; //event type
		while(parser.hasNext()) {
			event = parser.next();
			switch (event) {	
				case XMLStreamConstants.START_ELEMENT:
					String ename = parser.getLocalName();
					if (ename.equals("ric")) {
						map.put("ric.name", parser.getAttributeValue(null, "name"));
						map.put("ric.data_quality", parser.getAttributeValue(null, "data_quality"));
					}

					if (ename.equals("fid")) {
						map.put(parser.getAttributeValue(null, "id"), parser.getElementText());
					}

					break;
			}
		}
		
		return map;
	}
	

}
