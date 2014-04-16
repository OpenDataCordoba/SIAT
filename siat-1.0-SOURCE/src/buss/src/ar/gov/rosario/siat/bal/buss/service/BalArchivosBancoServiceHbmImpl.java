//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Archivo;
import ar.gov.rosario.siat.bal.buss.bean.BalArchivosBancoManager;
import ar.gov.rosario.siat.bal.buss.bean.EstadoArc;
import ar.gov.rosario.siat.bal.buss.bean.TipoArc;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.ArchivoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ArchivoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ArchivoVO;
import ar.gov.rosario.siat.bal.iface.model.EstadoArcVO;
import ar.gov.rosario.siat.bal.iface.model.TipoArcVO;
import ar.gov.rosario.siat.bal.iface.model.TranArcVO;
import ar.gov.rosario.siat.bal.iface.service.IBalArchivosBancoService;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Implementacion de servicios del submodulo Archivos Banco del modulo Balance
 * @author tecso
 */
public class BalArchivosBancoServiceHbmImpl implements IBalArchivosBancoService {

	private Logger log = Logger.getLogger(BalArchivosBancoServiceHbmImpl.class);

	public ArchivoVO deleteArchivo(UserContext userContext, ArchivoVO archivoVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Archivo archivo = Archivo.getById(archivoVO.getId());

			BalArchivosBancoManager.getInstance().deleteArchivo(archivo);

            if (archivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            archivo.passErrorMessages(archivoVO);
            
            log.debug(funcName + ": exit");
            return archivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ArchivoAdapter getArchivoAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Archivo archivo = Archivo.getById(commonKey.getId());			
			
			ArchivoAdapter archivoAdapter = new ArchivoAdapter();

			archivoAdapter.setArchivo((ArchivoVO) archivo.toVO(1, false));
			
			archivoAdapter.getArchivo().setListTranArc((ArrayList<TranArcVO>) ListUtilBean.toVO(archivo.getListTranArc(), 0));
			
			log.debug(funcName + ": exit");
			return archivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ArchivoSearchPage getArchivoSearchPageInit(UserContext userContext, ArchivoSearchPage archivoSPFiltro)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			ArchivoSearchPage archivoSearchPage = new ArchivoSearchPage();
		
			EstadoArcVO estadoArcFiltro = null;
			
			if(archivoSPFiltro != null){
				estadoArcFiltro = archivoSPFiltro.getArchivo().getEstadoArc();
				archivoSearchPage.setParamExBalance(true);
				archivoSearchPage.setBalance(archivoSPFiltro.getBalance());
			}

			if (ModelUtil.isNullOrEmpty(estadoArcFiltro)){
				archivoSearchPage.setListEstadoArc((ArrayList<EstadoArcVO>)
						ListUtilBean.toVO(EstadoArc.getListActivos(),
						new EstadoArcVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				archivoSearchPage.getArchivo().getEstadoArc().setId(-1L);
			}else{
				estadoArcFiltro =  (EstadoArcVO) EstadoArc.getById(estadoArcFiltro.getId()).toVO(0, false);
				archivoSearchPage.getListEstadoArc().add(estadoArcFiltro);	
				archivoSearchPage.getArchivo().setEstadoArc(estadoArcFiltro);
			}
			
			//	Seteo la lista de Tipo de Archivo
			archivoSearchPage.setListTipoArc((ArrayList<TipoArcVO>)
					ListUtilBean.toVO(TipoArc.getListActivos(),
					new TipoArcVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return archivoSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ArchivoSearchPage getArchivoSearchPageResult(
			UserContext userContext, ArchivoSearchPage archivoSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			archivoSearchPage.clearError();

			if(!ModelUtil.isNullOrEmpty(archivoSearchPage.getBalance())){
				archivoSearchPage.setPaged(false);
			}
			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Archivo> listArchivo = BalDAOFactory.getArchivoDAO().getListBySearchPage(archivoSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
	   		//Aqui pasamos BO a VO
	   		List<ArchivoVO> listArchivoVO = (ArrayList<ArchivoVO>) ListUtilBean.toVO(listArchivo,1,false);
	   		for(ArchivoVO archivoVO: listArchivoVO){
	   			if(archivoVO.getEstadoArc().getId().longValue() == EstadoArc.ID_ACEPTADO
	   					|| archivoVO.getEstadoArc().getId().longValue() == EstadoArc.ID_ANULADO){
	   				archivoVO.setAceptarBussEnabled(false);
	   				archivoVO.setAnularBussEnabled(false);
	   			}
	   		}
	   		
	   		archivoSearchPage.setListResult(listArchivoVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return archivoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ArchivoVO anularArchivo(UserContext userContext, ArchivoVO archivoVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			archivoVO.clearErrorMessages();
			
			Archivo archivo = Archivo.getById(archivoVO.getId());
	        
			if(!archivoVO.validateVersion(archivo.getFechaUltMdf())) return archivoVO;
			
			archivo.setObservacion(archivoVO.getObservacion());
			EstadoArc estadoArc = EstadoArc.getByIdNull(EstadoArc.ID_ANULADO);
			archivo.setEstadoArc(estadoArc);
			
            BalArchivosBancoManager.getInstance().updateArchivo(archivo); 

            if (archivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				//archivoVO =  (ArchivoVO) archivo.toVO(1 ,false);
			}
            archivo.passErrorMessages(archivoVO);
            
            log.debug(funcName + ": exit");
            return archivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
		
	public ArchivoVO aceptarArchivo(UserContext userContext, ArchivoVO archivoVO)
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			archivoVO.clearErrorMessages();
			
			Archivo archivo = Archivo.getById(archivoVO.getId());
	        
			if(!archivoVO.validateVersion(archivo.getFechaUltMdf())) return archivoVO;
			
			EstadoArc estadoArc = EstadoArc.getByIdNull(EstadoArc.ID_ACEPTADO);
			archivo.setEstadoArc(estadoArc);
			
	        BalArchivosBancoManager.getInstance().updateArchivo(archivo); 
	
	        if (archivo.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
	        archivo.passErrorMessages(archivoVO);
	        
	        log.debug(funcName + ": exit");
	        return archivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ArchivoSearchPage imprimirArchivos(UserContext userContext, ArchivoSearchPage archivoSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ReportVO report = archivoSearchPage.getReport();

			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Consulta de Archivos de Transacciones");
			
			// Creamos Tabla Cabecera con Filtros
			TablaVO tablaFiltro = new TablaVO("cabecera");//contenedorPrincipal.getTablaCabecera();
			tablaFiltro.setTitulo("Filtros de la Consulta");
			if(!StringUtil.isNullOrEmpty(archivoSearchPage.getArchivo().getPrefix())){
				fila = new FilaVO();
				fila.add(new CeldaVO(archivoSearchPage.getArchivo().getPrefix(),"prefijo","Prefijo"));
				tablaFiltro.add(fila);				
			}
			if(archivoSearchPage.getArchivo().getNroBanco() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(archivoSearchPage.getArchivo().getNroBancoView(),"numero","Nro. Banco"));
				tablaFiltro.add(fila);				
			}
			if(archivoSearchPage.getFechaBancoDesde() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(archivoSearchPage.getFechaBancoDesdeView(),"fechaBancoDesde","Fecha Banco Desde"));
				tablaFiltro.add(fila);				
			}
			if(archivoSearchPage.getFechaBancoHasta() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(archivoSearchPage.getFechaBancoHastaView(),"fechaBancoHasta","Fecha Banco Hasta"));
				tablaFiltro.add(fila);				
			}
			if(!ModelUtil.isNullOrEmpty(archivoSearchPage.getArchivo().getTipoArc())){
				TipoArc tipoArc = TipoArc.getByIdNull(archivoSearchPage.getArchivo().getTipoArc().getId());
				if(tipoArc != null){
					fila = new FilaVO();
					fila.add(new CeldaVO(tipoArc.getDescripcion(),"tipoArchivo","Tipo de Archivo"));
					tablaFiltro.add(fila);									
				}
			}
			if(!ModelUtil.isNullOrEmpty(archivoSearchPage.getArchivo().getEstadoArc())){
				EstadoArc estadoArc = EstadoArc.getByIdNull(archivoSearchPage.getArchivo().getEstadoArc().getId());
				if(estadoArc != null){
					fila = new FilaVO();
					fila.add(new CeldaVO(estadoArc.getDescripcion(),"estadoArchivo","Estado de Archivo"));
					tablaFiltro.add(fila);									
				}
			}
			contenedorPrincipal.setTablaFiltros(tablaFiltro);
			
			// Creamos Tabla con Totales por Fecha y prefijo
			TablaVO tablaTotales = new TablaVO("totales");
			tablaTotales.setTitulo("Totales de Archivos");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha de Banco"));
			filaCabecera.add(new CeldaVO("Prefijo"));
			filaCabecera.add(new CeldaVO("Total Importe"));
			tablaTotales.setFilaCabecera(filaCabecera);
			
			// . realizar busqueda (query con filtros)  (se setea sin paginación al llamar al DAO)
			archivoSearchPage.setPaged(false);
			List<Archivo> listArchivo = BalDAOFactory.getArchivoDAO().getListBySearchPage(archivoSearchPage);  
			archivoSearchPage.setPaged(true);
			
			// . recorrer lista de archivo y acumular en un mapa por 'fecha/pre' el total
			Map<String, Double> mapTotales = new HashMap<String, Double>();
			for(Archivo archivo: listArchivo){
				String clave =  DateUtil.formatDate(archivo.getFechaBanco(), DateUtil.ddSMMSYYYY_MASK)+"|"+archivo.getPrefix().trim();
				Double importe = null;
				importe = mapTotales.get(clave);
				if(importe == null){
					importe = 0D;
				}
				importe += archivo.getTotal();
				mapTotales.put(clave, importe);
			}
			
			// . recorrer lista del mapa (recorrer lista de keys y buscar por cada uno el value) y armar la tabla. Una fila con la fecha y sin mas datos 
			// y la lista de Filas de 'Prefijo|Total' de la tabla (identificar cambio de fecha para generar la nueva fila)
			Double total = 0D;
			String fechaActual = "";
			Double totalFecha = 0D;
			List<String> listClave = new ArrayList<String>(); 
			listClave.addAll(mapTotales.keySet());
	    	listClave = this.ordenarListClave(listClave);
			for(String clave: listClave){
				Double importe = mapTotales.get(clave);
				Datum datum = Datum.parse(clave);
				if(!fechaActual.equals(datum.getCols(0))){
					if(!"".equals(fechaActual)){
						// Fila de Fecha Anterior Final
						fila = new FilaVO();
						fila.add(new CeldaVO(""));
						fila.add(new CeldaVO("Total"));
						fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalFecha, 2))));
						tablaTotales.add(fila);						
					}
					
					fechaActual = datum.getCols(0);
					// Fila de Fecha Nueva Inicio
					fila = new FilaVO();
					fila.add(new CeldaVO(fechaActual));
					fila.add(new CeldaVO(""));
					fila.add(new CeldaVO(""));
					tablaTotales.add(fila);
					totalFecha = 0D;
				}
				// Fila de Prefijo
				fila = new FilaVO();
				fila.add(new CeldaVO(""));
				fila.add(new CeldaVO(datum.getCols(1)));
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importe, 2))));
				tablaTotales.add(fila);
				
				totalFecha += importe;
				total += importe;
			}
			// Fila de Ultima Fecha Final
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO("Total"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalFecha, 2))));
			tablaTotales.add(fila);		
			fila = new FilaVO();
			fila.add(new CeldaVO("Total Archivos"));
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(total, 2))));
			tablaTotales.add(fila);
			
			contenedorPrincipal.add(tablaTotales);
			
			BalDAOFactory.getArchivoDAO().imprimirGenerico(contenedorPrincipal, report);
			
			log.debug(funcName + ": exit");
			return archivoSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public List<String> ordenarListClave(List<String> listClave){
		if(ListUtil.isNullOrEmpty(listClave)){
			return listClave;
		}
    	Comparator<String> comparator = new Comparator<String>(){
			public int compare(String clave1, String clave2) {
				Datum datum1 = Datum.parse(clave1);
				Datum datum2 = Datum.parse(clave2);
				String fechaStr1 = datum1.getCols(0);
				String fechaStr2 = datum2.getCols(0);
				Date fecha1 = DateUtil.getDate(fechaStr1,  DateUtil.ddSMMSYYYY_MASK);
				Date fecha2 = DateUtil.getDate(fechaStr2,  DateUtil.ddSMMSYYYY_MASK);
				
				return DateUtil.dateCompare(fecha1, fecha2);	
				//return codPartida1.compareTo(codPartida2);
			}    		
    	};    	
    	Collections.sort(listClave, comparator);
    	return listClave;
    }

}
