//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo Conciliacion Osiris del modulo Balance 
 * @author tecso
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalConciliacionOsirisManager;
import ar.gov.rosario.siat.bal.buss.bean.BalEnvioOsirisManager;
import ar.gov.rosario.siat.bal.buss.bean.CierreBanco;
import ar.gov.rosario.siat.bal.buss.bean.EstadoEnvio;
import ar.gov.rosario.siat.bal.buss.bean.MovBan;
import ar.gov.rosario.siat.bal.buss.bean.MovBanDet;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoVO;
import ar.gov.rosario.siat.bal.iface.model.EnvNotOblVO;
import ar.gov.rosario.siat.bal.iface.model.MovBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.MovBanDetAdapter;
import ar.gov.rosario.siat.bal.iface.model.MovBanDetVO;
import ar.gov.rosario.siat.bal.iface.model.MovBanSearchPage;
import ar.gov.rosario.siat.bal.iface.model.MovBanVO;
import ar.gov.rosario.siat.bal.iface.model.NotaImptoVO;
import ar.gov.rosario.siat.bal.iface.model.TotMovBanDetHelper;
import ar.gov.rosario.siat.bal.iface.service.IBalConciliacionOsirisService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

public class BalConciliacionOsirisServiceHbmImpl implements IBalConciliacionOsirisService {

	private Logger log = Logger.getLogger(BalConciliacionOsirisServiceHbmImpl.class);

	public MovBanAdapter createMovBan(UserContext userContext, MovBanAdapter movBanAdapterVO )  throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			movBanAdapterVO.clearErrorMessages();

			// Se graba el archivo al repositorio.
			try {
				DemodaUtil.grabarArchivo(movBanAdapterVO.getPathTmp(), movBanAdapterVO.getFileData(), false);
			} catch(Exception e){
				log.error("ServiceError en: ", e);
				movBanAdapterVO.addRecoverableValueError("Ocurrio un error al intentar grabar el archivo.");
				return movBanAdapterVO; 
			}

			// Se llama a metodo para validar la consistencia del archivo a leer.
			movBanAdapterVO = this.validarMovBanFile(movBanAdapterVO);
			if(movBanAdapterVO.hasError()){
				return movBanAdapterVO;
			}

			// Se llama a metodo que parsea el archivo y crea el MovBan y sus Detalles (MovBanDet)
			movBanAdapterVO = this.procesarMovBanFile(movBanAdapterVO);
			
			if (movBanAdapterVO.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			log.debug(funcName + ": exit");
			return movBanAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 *  Valida la consistencia del archivo de Movimientos Bancarios de AFIP.
	 * 
	 * @param movBanAdapter
	 * @throws Exception
	 */
	private MovBanAdapter validarMovBanFile(MovBanAdapter movBanAdapter) throws Exception{
		
		FileReader fr = new FileReader(movBanAdapter.getPathTmp());
		BufferedReader br = new BufferedReader(fr);
		String lineaLeida;
		int nroFila = 0;
		int cantDetalleInformada = 0;
		int cantDetalleEncontrada = 0;
		while ((lineaLeida = br.readLine()) != null) {
			if(!StringUtil.isNullOrEmpty(lineaLeida)){
				String linea = this.prepararLineaMovBanParaParser(lineaLeida);
				if(linea == null){
					movBanAdapter.addRecoverableValueError("Error al procesar la l�nea "+(nroFila+1)+" del archivo.");
					return movBanAdapter;
				}
				Datum datum = Datum.parse(linea);
				if(datum.getInteger(0).intValue() == 1){
					// Registro de Cabecera (MovBan)
					cantDetalleInformada = datum.getInteger(7);					
					if(DateUtil.getDate(datum.getCols(3), DateUtil.ddMMYYYY_MASK) == null){
						movBanAdapter.addRecoverableValueError("No se puede obtener la fecha de proceso del registro cabecera.");
						return movBanAdapter;						
					}
					if(DateUtil.getDate(datum.getCols(4), DateUtil.ddMMYYYY_MASK) == null){
						movBanAdapter.addRecoverableValueError("No se puede obtener la fecha de acreditaci�n del registro cabecera.");
						return movBanAdapter;						
					}else{
						// Validar que no exista un archivo movBan ya cargado para la misma fecha de acreditacion
						Date fechaAcredit = DateUtil.getDate(datum.getCols(4), DateUtil.ddMMYYYY_MASK);
						if(MovBan.existeParaFechaAcredit(fechaAcredit)){
							movBanAdapter.addRecoverableValueError("Existe un archivo registrado para la misma fecha de acreditaci�n.");
							return movBanAdapter;
						}
					}
				}
				if(datum.getInteger(0).intValue() == 2){
					// Registro de Detalle (MovBanDet)
					cantDetalleEncontrada++;
				}
				nroFila++;
			}
		}
		if(cantDetalleEncontrada != cantDetalleInformada){
			movBanAdapter.addRecoverableValueError("El archivo no posee la cantidad de detalles de movimientos bancarios informados en la cabecera.");
			return movBanAdapter;	
		}
		
		return movBanAdapter;
	}
	
	/**
	 * 
	 * @param linea
	 * @return
	 */
	private String prepararLineaMovBanParaParser(String linea){
		String lineaModif = "";
		
		try{
			if(linea.startsWith("01")){
				// Registro de Cabecera (MovBan)
				for(int i=0; i < linea.length(); i++){
					//   1		   2          3         4          5          6          7          8       
					if(i == 2 || i == 6 || i == 9 || i == 17 || i == 25 || i == 40 || i == 55 || i == 61)
						lineaModif += '|';
					lineaModif += linea.charAt(i);
				}	
			}else if(linea.startsWith("02")){
				// Registro de Detalle (MovBanDet)
				for(int i=0; i < linea.length(); i++){
					//   1		   2          3         4          5          6          7          8    	  9   
					if(i == 2 || i == 6 || i == 9 || i == 10 || i == 18 || i == 33 || i == 48 || i == 70 || i == 72)
						lineaModif += '|';
					lineaModif += linea.charAt(i);
				}	
			}else{
				lineaModif = null;
			}					
		}catch (Exception e) {
			lineaModif = null;
		}
		
		return lineaModif;
	}
	
	/**
	 *  Procesa el archivo de Movimientos Bancarios de AFIP creando un MovBan y una lista de MovBanDet.
	 * 
	 * @param movBanAdapter
	 * @throws Exception
	 */
	private MovBanAdapter procesarMovBanFile(MovBanAdapter movBanAdapter) throws Exception{
		
		FileReader fr = new FileReader(movBanAdapter.getPathTmp());
		BufferedReader br = new BufferedReader(fr);
		String lineaLeida;
		int nroFila = 0;
		
		MovBan movBan = new MovBan();
		movBan.setEstado(Estado.ACTIVO.getId());
		movBan.setConciliado(SiNo.NO.getId());
		if(movBan.getListMovBanDet() == null)
			movBan.setListMovBanDet(new ArrayList<MovBanDet>());
		
		while ((lineaLeida = br.readLine()) != null) {
			if(!StringUtil.isNullOrEmpty(lineaLeida)){
				String linea = this.prepararLineaMovBanParaParser(lineaLeida);
				if(linea == null){
					movBanAdapter.addRecoverableValueError("Error al procesar la l�nea "+nroFila+1+" del archivo.");
					return movBanAdapter;
				}
				Datum datum = Datum.parse(linea);
				if(datum.getInteger(0).intValue() == 1){
					// Registro de Cabecera (MovBan)
					movBan.setIdOrgRecAfip(datum.getLong(1));
					movBan.setBancoAdm(datum.getLong(2));
					movBan.setFechaProceso(DateUtil.getDate(datum.getCols(3), DateUtil.ddMMYYYY_MASK));
					movBan.setFechaAcredit(DateUtil.getDate(datum.getCols(4), DateUtil.ddMMYYYY_MASK));
					movBan.setTotalDebito(datum.getDouble(5)/100);
					movBan.setTotalCredito(datum.getDouble(6)/100);
					movBan.setCantDetalle(datum.getLong(7));
				}
				if(datum.getInteger(0).intValue() == 2){
					// Registro de Detalle (MovBanDet)
					MovBanDet movBanDet = new MovBanDet();
					movBanDet.setMovBan(movBan);
					movBanDet.setImpuesto(datum.getLong(1));
					movBanDet.setBancoRec(datum.getLong(2));
					movBanDet.setAnexoOperativo(datum.getInteger(3));
					movBanDet.setNroCierreBanco(datum.getLong(4));
					movBanDet.setDebito(datum.getDouble(5)/100);
					movBanDet.setCredito(datum.getDouble(6)/100);
					movBanDet.setNroCuenta(datum.getCols(7));
					movBanDet.setMoneda(datum.getInteger(8));
					movBanDet.setConciliado(SiNo.NO.getId());
					
					movBan.getListMovBanDet().add(movBanDet);
				}
				nroFila++;
			}
		}

		// Se persisten los datos
		movBan = BalConciliacionOsirisManager.getInstance().createMovBan(movBan);
		for(MovBanDet movBanDet: movBan.getListMovBanDet()){
			movBan.createMovBanDet(movBanDet);
			if(movBanDet.hasError()){
				movBanDet.passErrorMessages(movBan);
				break;
			}
		}
		
		return movBanAdapter;		
	}

	public MovBanVO deleteMovBan(UserContext userContext, MovBanVO movBanVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			movBanVO.clearErrorMessages();

			MovBan movBan = MovBan.getById(movBanVO.getId());
			
			movBan = BalConciliacionOsirisManager.getInstance().deleteMovBan(movBan);

			if (movBan.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				movBanVO =  (MovBanVO) movBan.toVO(0,false);
			}
			movBan.passErrorMessages(movBanVO);

			log.debug(funcName + ": exit");
			return movBanVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanAdapter getMovBanAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			MovBanAdapter movBanAdapter = new MovBanAdapter();
			
			log.debug(funcName + ": exit");
			return movBanAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public MovBanAdapter getMovBanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			MovBan movBan = MovBan.getById(commonKey.getId());

			MovBanAdapter movBanAdapter = new MovBanAdapter();
			movBanAdapter.setMovBan((MovBanVO) movBan.toVO(1,false));

			// Mapa para el calculo de subtotales por cuenta
			Map<String, TotMovBanDetHelper> mapSubTotalesPorCuenta = new HashMap<String, TotMovBanDetHelper>();
			List<MovBanDetVO> listMovBanDetVO = new ArrayList<MovBanDetVO>();
			for(MovBanDet movBanDet: movBan.getListMovBanDet()){
				MovBanDetVO movBanDetVO = (MovBanDetVO) movBanDet.toVO(1, false);
				if(movBanDet.getConciliado().intValue() == SiNo.SI.getId().intValue()){
					movBanDetVO.setConciliarBussEnabled(false);
	   			}
				listMovBanDetVO.add(movBanDetVO);
				
				// Calculo de subtotales por cuenta
				String cuentaKey = movBanDet.getNroCuenta();
				TotMovBanDetHelper subTotalesCta = mapSubTotalesPorCuenta.get(cuentaKey);
				if(subTotalesCta == null){
					subTotalesCta = new TotMovBanDetHelper(cuentaKey, 0D, 0D);
				}
				subTotalesCta.setDebito(subTotalesCta.getDebito()+movBanDet.getDebito());
				subTotalesCta.setCredito(subTotalesCta.getCredito()+movBanDet.getCredito());
				mapSubTotalesPorCuenta.put(cuentaKey, subTotalesCta);
			}
			movBanAdapter.getMovBan().setListMovBanDet(listMovBanDetVO);
			List<TotMovBanDetHelper> listSubtotalesPorCuenta = new ArrayList<TotMovBanDetHelper>();
			listSubtotalesPorCuenta.addAll(mapSubTotalesPorCuenta.values());
			movBanAdapter.setListSubtotalesPorCuenta(listSubtotalesPorCuenta);

			
			log.debug(funcName + ": exit");
			return movBanAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanSearchPage getMovBanSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			MovBanSearchPage movBanSearchPage = new MovBanSearchPage();
		
			List<SiNo> listSiNo = new ArrayList<SiNo>();
			listSiNo.add(SiNo.getById(SiNo.OpcionTodo.getId()));
			listSiNo.add(SiNo.getById(SiNo.SI.getId()));
			listSiNo.add(SiNo.getById(SiNo.NO.getId()));
			movBanSearchPage.setListSiNo(listSiNo);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return movBanSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanSearchPage getMovBanSearchPageResult(UserContext userContext, MovBanSearchPage movBanSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			movBanSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<MovBan> listMovBan = BalDAOFactory.getMovBanDAO().getListBySearchPage(movBanSearchPage);  

			// Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<MovBanVO> listMovBanVO = new ArrayList<MovBanVO>();
	   		for(MovBan movBan: listMovBan){
	   			MovBanVO movBanVO = (MovBanVO) movBan.toVO(1,false);
	   			if(movBan.getConciliado().intValue() == SiNo.SI.getId().intValue()){
	   				movBanVO.setConciliarBussEnabled(false);
	   				movBanVO.setEliminarBussEnabled(false);
	   			}
	   			listMovBanVO.add(movBanVO);
	   		}
	   		
			//Aqui pasamos BO a VO
	   		movBanSearchPage.setListResult(listMovBanVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return movBanSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanAdapter imprimirMovBan(UserContext userContext, MovBanAdapter movBanAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			MovBan movBan = MovBan.getById(movBanAdapterVO.getMovBan().getId());

			BalDAOFactory.getMovBanDAO().imprimirGenerico(movBan, movBanAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return movBanAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
		} 
	}

	public MovBanVO updateMovBan(UserContext userContext, MovBanVO movBanVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			movBanVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			MovBan movBan = MovBan.getById(movBanVO.getId());

			if(!movBanVO.validateVersion(movBan.getFechaUltMdf())) return movBanVO;
			
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			movBan = BalConciliacionOsirisManager.getInstance().updateMovBan(movBan);

			if (movBan.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				movBanVO =  (MovBanVO) movBan.toVO(0,false);
			}
			movBan.passErrorMessages(movBanVO);

			log.debug(funcName + ": exit");
			return movBanVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanSearchPage imprimirMovBan(UserContext userContext, MovBanSearchPage movBanSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ReportVO report = movBanSearchPage.getReport();

			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Consulta de Movimientos Bancarios AFIP");
			
			// Creamos Tabla Cabecera con Filtros
			TablaVO tablaFiltro = new TablaVO("cabecera");
			tablaFiltro.setTitulo("Filtros de la Consulta");
			if(movBanSearchPage.getFechaDesde() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(movBanSearchPage.getFechaDesdeView(),"fechaDesde","Fecha Acreditaci�n Desde"));
				tablaFiltro.add(fila);				
			}
			if(movBanSearchPage.getFechaHasta() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(movBanSearchPage.getFechaHastaView(),"fechaHasta","Fecha Acreditaci�n Hasta"));
				tablaFiltro.add(fila);				
			}
			if(movBanSearchPage.getMovBan().getBancoAdm() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(movBanSearchPage.getMovBan().getBancoAdmView(),"bancoAdm","C�d. Banco Administrador"));
				tablaFiltro.add(fila);				
			}
			if(movBanSearchPage.getMovBan().getConciliado() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(movBanSearchPage.getMovBan().getConciliado().getValue(),"conciliado","Conciliado"));
				tablaFiltro.add(fila);				
			}

			contenedorPrincipal.setTablaFiltros(tablaFiltro);
			
			// Creamos Tabla con lista de MovBan
			TablaVO tablaTotales = new TablaVO("totales");
			tablaTotales.setTitulo("Lista de Movimientos Bancarios");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha Acreditaci�n"));
			filaCabecera.add(new CeldaVO("Banco Adm."));
			filaCabecera.add(new CeldaVO("Total D�bito"));
			filaCabecera.add(new CeldaVO("Total Cr�dito"));
			filaCabecera.add(new CeldaVO("Cant. Detalle"));
			filaCabecera.add(new CeldaVO("Conciliado"));
			tablaTotales.setFilaCabecera(filaCabecera);
			
			// . realizar busqueda (query con filtros)  (se setea sin paginaci�n al llamar al DAO)
			movBanSearchPage.setPaged(false);
			List<MovBan> listMovBan= BalDAOFactory.getMovBanDAO().getListBySearchPage(movBanSearchPage);  
			movBanSearchPage.setPaged(true);
			
			Double totalDebito = 0D;
			Double totalCredito = 0D;
			Long totalDetalles = 0L;
			for(MovBan movBan: listMovBan){
				fila = new FilaVO();
				fila.add(new CeldaVO(DateUtil.formatDateForReport(movBan.getFechaAcredit())));
				fila.add(new CeldaVO(StringUtil.formatLong(movBan.getBancoAdm())));
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(movBan.getTotalDebito(), 3))));
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(movBan.getTotalCredito(), 3))));
				fila.add(new CeldaVO(StringUtil.formatLong(movBan.getCantDetalle())));
				fila.add(new CeldaVO(SiNo.getById(movBan.getConciliado()).getValue()));
				tablaTotales.add(fila);
				
				totalDebito += movBan.getTotalDebito();
				totalCredito += movBan.getTotalCredito();
				totalDetalles += movBan.getCantDetalle();
			}
			// Fila de Ultima Fecha Final
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO("Totales"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalDebito, 3))));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalCredito, 3))));
			fila.add(new CeldaVO(StringUtil.formatLong(totalDetalles)));
			fila.add(new CeldaVO(""));
			tablaTotales.add(fila);		
			
			contenedorPrincipal.add(tablaTotales);
			
			BalDAOFactory.getMovBanDAO().imprimirGenerico(contenedorPrincipal, report);
			
			log.debug(funcName + ": exit");
			return movBanSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanVO conciliarMovBan(UserContext userContext, MovBanVO movBanVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			movBanVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			MovBan movBan = MovBan.getById(movBanVO.getId());

			if(!movBanVO.validateVersion(movBan.getFechaUltMdf())) return movBanVO;
			
			// Se fuerza la conciliacion de todos los detalles
			for(MovBanDet movBanDet: movBan.getListMovBanDet()){
				movBanDet.setConciliado(SiNo.SI.getId());
				movBanDet = movBan.updateMovBanDet(movBanDet);
				movBanDet.passErrorMessages(movBan);
			}
			// Se pasa a Conciliado el MovBan
			movBan.setConciliado(SiNo.SI.getId());
			movBan = BalConciliacionOsirisManager.getInstance().updateMovBan(movBan);

			if (movBan.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				movBanVO =  (MovBanVO) movBan.toVO(0,false);
			}
			movBan.passErrorMessages(movBanVO);

			log.debug(funcName + ": exit");
			return movBanVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanDetVO conciliarMovBanDet(UserContext userContext, MovBanDetVO movBanDetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			movBanDetVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			MovBanDet movBanDet = MovBanDet.getById(movBanDetVO.getId());

			if(!movBanDetVO.validateVersion(movBanDet.getFechaUltMdf())) return movBanDetVO;
			
			// Se pasa a Conciliado el MovBan
			movBanDet.setConciliado(SiNo.SI.getId());
			movBanDet = movBanDet.getMovBan().updateMovBanDet(movBanDet);

			if (movBanDet.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				movBanDetVO =  (MovBanDetVO) movBanDet.toVO(0,false);
			}
			movBanDet.passErrorMessages(movBanDetVO);

			log.debug(funcName + ": exit");
			return movBanDetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanDetAdapter getMovBanDetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			MovBanDet movBanDet = MovBanDet.getById(commonKey.getId());

			MovBanDetAdapter movBanDetAdapter = new MovBanDetAdapter();
			movBanDetAdapter.setMovBanDet((MovBanDetVO) movBanDet.toVO(1,true));

			log.debug(funcName + ": exit");
			return movBanDetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MovBanDetAdapter imprimirMovBanDet(UserContext userContext, MovBanDetAdapter movBanDetAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			MovBanDet movBanDet = MovBanDet.getById(movBanDetAdapterVO.getMovBanDet().getId());

			BalDAOFactory.getMovBanDetDAO().imprimirGenerico(movBanDet, movBanDetAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return movBanDetAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
		} 
	}

	public CierreBancoAdapter getCierreBancoAdapterForConciliar(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CierreBanco cierreBanco = CierreBanco.getById(commonKey.getId());			

			CierreBancoAdapter cierreBancoAdapter = new CierreBancoAdapter();		

			// Obtener total de transacciones y de importe sin contar las transacciones anuladas o con error.
			
			//Este metodo calcula el Importe Total de Transacciones sumando totos los importes de las tranAfip
//			Object[] totalesTran = BalDAOFactory.getTranAfipDAO().getListTotalesByCierreBanco(cierreBanco);
			
			//Este metodo calcula el Importe Total de Transacciones sumando totos los importes de los detallePago de las tranAfip
			Object[] totalesTran = BalDAOFactory.getTranAfipDAO().getListTotalesForCierreBancoByDetallePago(cierreBanco);
			
			// Obtener total por impuesto y cuenta corriente para los MovBanDet con Banco y NroCierreBanco de este CierreBanco 
			List<TotMovBanDetHelper> listTotMovBanDet = BalDAOFactory.getMovBanDetDAO().getListTotalesByCierreBanco(Long.valueOf(cierreBanco.getBanco()),
																													Long.valueOf(cierreBanco.getNroCierreBanco()));
			Double totalDebito = 0D;
			Double totalCredito = 0D;
			
			for(TotMovBanDetHelper totMovBanDet: listTotMovBanDet){
				totalDebito += totMovBanDet.getDebito();
				totalCredito += totMovBanDet.getCredito();
			}
				
			cierreBancoAdapter.setListTotMovBanDet(listTotMovBanDet);
			cierreBancoAdapter.setTotalDebito(totalDebito);
			cierreBancoAdapter.setTotalCredito(totalCredito);

			CierreBancoVO cierreBancoVO = (CierreBancoVO) cierreBanco.toVO(2,false);
			// Obtengo total de NotaImpto para este CierreBanco
			List<NotaImptoVO> listNotaImpto = ListUtilBean.toVO(cierreBanco.getListNotaImpto(),0,false);

			//Totales Nota Tipo 1
			Double sumTotalNota1 = 0D;
			Double sumTotalIVANota1 =0D;
			//Totales Nota Tipo 2
			Double sumTotalNota2 = 0D;
			Double sumTotalIVANota2 =0D;
			//Totales Nota Tipo 2
			Double sumTotalNota3 = 0D;
			Double sumTotalIVANota3 =0D;
			//importe = importe tipo nota1 - importe tipo nota 2 + importe tipo nota 3
			for(NotaImptoVO notaImpto: listNotaImpto){
				//Nota Tipo 1
				if(notaImpto.getTipoNota() == 1){
					sumTotalNota1+=notaImpto.getImporte();
					sumTotalIVANota1+= notaImpto.getImporteIVA();
					continue;
				}
				//Nota Tipo 2 (estos montos restan)				 
				if (notaImpto.getTipoNota() == 2) {
					sumTotalNota2+=notaImpto.getImporte();
					sumTotalIVANota2+=notaImpto.getImporteIVA();
					//Multiplico por -1 asi en el grilla se muestran en negativo
					notaImpto.setImporte(notaImpto.getImporte() * (-1));
					notaImpto.setImporteIVA(notaImpto.getImporteIVA() * (-1));
					continue;
				}
				//Nota Tipo 3
				if(notaImpto.getTipoNota() == 3){
					sumTotalNota3+=notaImpto.getImporte();
					sumTotalIVANota3+= notaImpto.getImporteIVA();
				}
			}
			
			Double totalNotaImpto = sumTotalNota1 - sumTotalNota2 + sumTotalNota3;
			Double totalIVANotaImpto = sumTotalIVANota1 - sumTotalIVANota2 + sumTotalIVANota3;
			
			cierreBancoVO.setListNotaImpto(listNotaImpto);
			cierreBancoAdapter.setTotalIVANotaImpto(totalIVANotaImpto);
			cierreBancoAdapter.setTotalNotaImpto(totalNotaImpto);
			
			//Nro de TranAfip asociadas al CierreBanco
			cierreBancoVO.setCantTransaccionCal((Long) totalesTran[0]);
			//Sumatoria de importePago de los DetallePago asociados a las TranAfip
			cierreBancoVO.setImporteTotal((Double) totalesTran[1]);
			
			//--> Conciliaci�n Movimientos Bancarios
			//.Importe Total Movimientos Bancarios (cr�dito - d�bito)
			cierreBancoAdapter.setTotalMovBan(totalCredito - totalDebito);
			//.Importe Total Calculado sobre Notas de Abono (notas 1 - notas 2)
			cierreBancoAdapter.setTotalNotaImptoN1N2(sumTotalNota1 - sumTotalNota2 - sumTotalIVANota2);
			//.Importe Total Calculado sobre Notas de Abono (notas 1 - notas 2 + notas 3)
			cierreBancoAdapter.setTotalNotaImptoN1N2N3(sumTotalNota1 - sumTotalNota2 - sumTotalIVANota2 + sumTotalNota3);
			//.Importe Total Calculado sobre Notas de Obligacion
			cierreBancoAdapter.setTotalCalSobreNotaOblig(cierreBanco.getTotalAcreditado());
			

			//--> Conciliaci�n rendici�n de Transacciones
			//.Importe Total de transacciones SIAT
			cierreBancoAdapter.setTotalImporteDetallePago(cierreBanco.getTotalImporteDetallePago());
			//.Importe total de transacciones rendidas (s/Notas obligacion)
			cierreBancoAdapter.setTotalRendido(cierreBanco.getTotalRendido());
			
			//Importe Total Calculado: totalNotaImpto + totalIVANotaImpto
			cierreBancoVO.setImporteTotalCalPorNotaImpto(totalNotaImpto + totalIVANotaImpto);
			
			// Obtengo la lista Envios de Nota de Obligacion para este CierreBanco
			List<EnvNotOblVO> listEnvNotObl = ListUtilBean.toVO(cierreBanco.getListEnvNotObl(),0,false);
			cierreBancoVO.getEnvioOsiris().setListEnvNotObl(listEnvNotObl);
			
			// Validaci�n de Conciliaci�n sobre Movimientos Bancarios
			if (!NumberUtil.isDoubleEqualToDouble(cierreBancoAdapter.getTotalNotaImptoN1N2(), cierreBancoAdapter.getTotalMovBan(), 0.01) ||
					!NumberUtil.isDoubleEqualToDouble(cierreBancoAdapter.getTotalNotaImptoN1N2N3(), cierreBancoAdapter.getTotalCalSobreNotaOblig(), 0.01)) {
				
				cierreBancoAdapter.addMessage(BalError.CIERREBANCO_CONCILIACION_MOVBAN_MSG);
			}
			
			// Validaci�n de Conciliaci�n sobre Rendici�n de Transacciones
			if (!NumberUtil.isDoubleEqualToDouble(cierreBancoAdapter.getTotalRendido(), cierreBancoAdapter.getTotalImporteDetallePago(), 0.01)) {
				cierreBancoAdapter.addMessage(BalError.CIERREBANCO_CONCILIACION_RENTRAN_MSG);
			}
			
			// Mantis 0007822: AFIP - Conciliar Env�o. importe transacciones inconsistentes eliminadas 
			Double importe = BalDAOFactory.getLogTranAfipDAO().getImporteTotalTranAfipEliminadasByCierreBanco(cierreBanco);
			if (null == importe ) importe = 0D;
			cierreBancoAdapter.setImporteTotalTraIncEli(importe);
			
			cierreBancoAdapter.setCierreBanco(cierreBancoVO);
			
			log.debug(funcName + ": exit");
			return cierreBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CierreBancoSearchPage getCierreBancoSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			CierreBancoSearchPage cierreBancoSearchPage = new CierreBancoSearchPage();
			// Se fija el valor del "Conciliado" como "NO" para que siempre filtre y obtenga los que todavia no fueron conciliados
			cierreBancoSearchPage.getCierreBanco().setConciliado(SiNo.NO);
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cierreBancoSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CierreBancoSearchPage getCierreBancoSearchPageResult(UserContext userContext, CierreBancoSearchPage cierreBancoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cierreBancoSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<CierreBanco> listCierreBanco = BalDAOFactory.getCierreBancoDAO().getListBySearchPage(cierreBancoSearchPage);  

			// Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<CierreBancoVO> listCierreBancoVO = new ArrayList<CierreBancoVO>();
	   		for (CierreBanco cierreBanco : listCierreBanco) {
				CierreBancoVO cierreBancoVO = (CierreBancoVO) cierreBanco.toVO(1,false);
				/*
				 * Este metodo calcula el Importe Total de Transacciones sumando totos los importes de las tranAfip
				 */
				Object[] totalesTran = BalDAOFactory.getTranAfipDAO().getListTotalesForCierreBancoByDetallePago(cierreBanco);
			
				//.Cantidad de TranAfip asociadas al CierreBanco
				cierreBancoVO.setCantTransaccion((Long) totalesTran[0]);
				//.Sumatoria de totMontoIngresado de las TranAfip asociadas al CierreBanco
				cierreBancoVO.setImporteTotal((Double) totalesTran[1]);

				listCierreBancoVO.add(cierreBancoVO);
			}
	   		
	   		cierreBancoSearchPage.setListResult(listCierreBancoVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cierreBancoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CierreBancoVO conciliarCierreBanco(UserContext userContext, CierreBancoVO cierreBancoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			CierreBanco cierreBanco = CierreBanco.getById(cierreBancoVO.getId());
			if(!cierreBancoVO.validateVersion(cierreBanco.getFechaUltMdf())) return cierreBancoVO;
						
			// Obtiene los MovBanDet involucrados
			List<MovBanDet> listMovBanDet = BalDAOFactory.getMovBanDetDAO().getListByCierreBanco(Long.valueOf(cierreBanco.getBanco()),
																								 Long.valueOf(cierreBanco.getNroCierreBanco()));
			// Validar que al menos exista un Detalle de Movimiento Bancario con el que se haya conciliado.
			if(ListUtil.isNullOrEmpty(listMovBanDet)){
				cierreBancoVO.addRecoverableError(BalError.CIERREBANCO_CONCILIACION_ERROR);
				return cierreBancoVO;
			}
			
			// Pasar a conciliado los MovBanDet involucrados
			Map<String, MovBan> mapMovBan = new HashMap<String, MovBan>();
			for(MovBanDet movBanDet: listMovBanDet){
				movBanDet.setConciliado(SiNo.SI.getId());
				movBanDet.getMovBan().updateMovBanDet(movBanDet);
				mapMovBan.put(movBanDet.getMovBan().getId().toString(),movBanDet.getMovBan());
			}
			// Pasar a conciliado los MovBan involucrados que no contengan detalles no conciliados
			for(MovBan movBan : mapMovBan.values()){
				if(!movBan.existeDetalleSinConciliar()){
					movBan.setConciliado(SiNo.SI.getId());
					BalConciliacionOsirisManager.getInstance().updateMovBan(movBan);
				}
			}
			
			// Pasar a conciliado el CierreBanco
			cierreBanco.setConciliado(SiNo.SI.getId());
			cierreBanco.getEnvioOsiris().updateCierreBanco(cierreBanco);
			
			// Pasar a conciliado el Envio Osiris si todos sus cierres bancos se encuentran conciliados 
			if(!cierreBanco.getEnvioOsiris().existenCierreBancoSinConciliar()){
				cierreBanco.getEnvioOsiris().setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_CONCILIADO));
				BalEnvioOsirisManager.getInstance().updateEnvioOsiris(cierreBanco.getEnvioOsiris());
			}
			
			if (cierreBanco.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cierreBancoVO =  (CierreBancoVO) cierreBanco.toVO(0,false);
			}
			cierreBanco.passErrorMessages(cierreBancoVO);

			log.debug(funcName + ": exit");
			return cierreBancoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public CierreBancoAdapter imprimirCierreBancoPDF(UserContext userContext, CierreBancoAdapter cierreBancoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ReportVO report = cierreBancoAdapter.getReport();
			CierreBancoVO cierreBanco = cierreBancoAdapter.getCierreBanco();

			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Reporte de Cierre de Banco");
			
			// Creamos Tabla Cabecera con Filtros
			TablaVO tablaFiltro = new TablaVO("cabecera");
			tablaFiltro.setTitulo("Datos del Cierre Banco");

			//Banco
			fila = new FilaVO();
			fila.add(new CeldaVO(cierreBanco.getBancoView(), "cierreBanco.banco","Banco"));
			tablaFiltro.add(fila);	
			
			//N\u00FAmero de Cierre
			fila = new FilaVO();
			fila.add(new CeldaVO(cierreBanco.getNroCierreBancoView(), "cierreBanco.nroCierreBanco","N\u00FAmero de Cierre"));
			tablaFiltro.add(fila);
			
			//Fecha de Cierre
			fila = new FilaVO();
			fila.add(new CeldaVO(cierreBanco.getFechaCierreView(), "cierreBanco.fechaCierre","Fecha de Cierre"));
			tablaFiltro.add(fila);
		
			//Env\u00EDo AFIP
			fila = new FilaVO();
			fila.add(new CeldaVO(cierreBanco.getEnvioOsiris().getIdEnvioAfipView(), "envioOsiris.idEnvioAfip","Env\u00EDo AFIP"));
			tablaFiltro.add(fila);
			
			//N\u00FAmero de Transacciones
			fila = new FilaVO();
			fila.add(new CeldaVO(cierreBanco.getCantTransaccionView(), "envioOsiris.cantTransaccion","N\u00FAmero de Transacciones"));
			tablaFiltro.add(fila);

			//Cant. Tran. de Pago Pendientes
			fila = new FilaVO();
			fila.add(new CeldaVO(cierreBanco.getCantTransaccionCalView(), "envioOsiris.cantTransaccionCal","Cant. Tran. de Pago Pendientes"));
			tablaFiltro.add(fila);

			contenedorPrincipal.setTablaFiltros(tablaFiltro);
			
			// Creamos Tabla Conciliaci�n de Movimientos Bancarios
			TablaVO tablaConciliacionMovBan = new TablaVO("tablaConciliacionMovBan");
			tablaConciliacionMovBan.setTitulo("Conciliaci\u00F3n de Movimientos Bancarios");
			
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO(""));
			filaCabecera.add(new CeldaVO(""));
			tablaConciliacionMovBan.setFilaCabecera(filaCabecera);
			
			//Total Movimientos Bancarios (cr�dito - d�bito)
			fila = new FilaVO();
			CeldaVO celdaVO = new CeldaVO("Total Movimientos Bancarios (cr\u00E9dito - d\u00E9bito)");
			celdaVO.setTextAlignLeft();
			fila.add(celdaVO);
			fila.add(new CeldaVO("$ "+cierreBancoAdapter.getTotalMovBanView()));
			tablaConciliacionMovBan.add(fila);
			
			//Total calculado s/Notas de Abono (notas 1 - notas 2)
			fila = new FilaVO();
			celdaVO = new CeldaVO("Total calculado s/Notas de Abono (notas 1 - notas 2)");
			celdaVO.setTextAlignLeft();
			fila.add(celdaVO);
			fila.add(new CeldaVO("$ "+cierreBancoAdapter.getTotalNotaImptoN1N2View()));
			tablaConciliacionMovBan.add(fila);
			
			//Total calculado s/Notas de Abono (notas 1 - notas 2 + notas 3)
			fila = new FilaVO();
			celdaVO = new CeldaVO("Total calculado s/Notas de Abono (notas 1 - notas 2 + notas 3)"); 
			celdaVO.setTextAlignLeft();
			fila.add(celdaVO);
			fila.add(new CeldaVO("$ "+cierreBancoAdapter.getTotalNotaImptoN1N2N3View()));
			tablaConciliacionMovBan.add(fila);
			
			//Total calculado s/Notas de Obligaci�n
			fila = new FilaVO();
			celdaVO = new CeldaVO("Total Calculado sobre Notas de Obligacion");
			celdaVO.setTextAlignLeft();
			fila.add(celdaVO);
			fila.add(new CeldaVO("$ "+cierreBancoAdapter.getTotalCalSobreNotaObligView()));
			tablaConciliacionMovBan.add(fila);
			
			contenedorPrincipal.add(tablaConciliacionMovBan);
			
			// Creamos Tabla Conciliaci�n Rendici�n de Transacciones
			TablaVO tablaRendicion = new TablaVO("tablaConciliacionRenTran");
			tablaRendicion.setTitulo("Conciliaci\u00F3n de Rendici\u00F3n de Transacciones");
			
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO(""));
			filaCabecera.add(new CeldaVO(""));
			tablaRendicion.setFilaCabecera(filaCabecera);
			
			//Importe Total de transacciones SIAT
			fila = new FilaVO();
			celdaVO = new CeldaVO("Importe Total de transacciones SIAT");
			celdaVO.setTextAlignLeft();
			fila.add(celdaVO);
			fila.add(new CeldaVO("$ "+cierreBancoAdapter.getTotalImporteDetallePagoView()));
			tablaRendicion.add(fila);
			
			//Importe total de transacciones rendidas (s/Notas obligacion)
			fila = new FilaVO();
			celdaVO = new CeldaVO("Importe total de transacciones rendidas (s/Notas obligaci\u00F3n)"); 
			celdaVO.setTextAlignLeft();
			fila.add(celdaVO);
			fila.add(new CeldaVO("$ "+cierreBancoAdapter.getTotalRendidoView()));
			tablaRendicion.add(fila);
			
			//Importe total de transacciones inconsistentes eliminadas
			fila = new FilaVO();
			celdaVO = new CeldaVO("Importe total de transacciones inconsistentes eliminadas"); 
			celdaVO.setTextAlignLeft();
			fila.add(celdaVO);
			fila.add(new CeldaVO("$ "+cierreBancoAdapter.getImporteTotalTraIncEliView()));
			tablaRendicion.add(fila);
			
			contenedorPrincipal.add(tablaRendicion);
			
			// Creamos Tabla con Movimientos de Caja
			TablaVO tablaMovBanDet = new TablaVO("tablaMovBanDet");
			tablaMovBanDet.setTitulo("Datos de Movimientos Bancarios para Conciliaci\u00F3n");

			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha acreditaci\u00F3n"));
			filaCabecera.add(new CeldaVO("Impuesto"));
			CeldaVO celdaCC = new CeldaVO("N\u00FAmero Cuenta Corriente");
			celdaCC.setWidth(50);
			filaCabecera.add(celdaCC);
			filaCabecera.add(new CeldaVO("D\u00E9bito"));
			filaCabecera.add(new CeldaVO("Cr\u00E9dito"));
			
			for (TotMovBanDetHelper movBanDet : cierreBancoAdapter.getListTotMovBanDet()) {
				fila = new FilaVO();
				fila.add(new CeldaVO(movBanDet.getFechaAcreditView()));
				fila.add(new CeldaVO(movBanDet.getImpuestoView()));
				CeldaVO celdaNC = new CeldaVO(movBanDet.getNroCuenta());
				celdaNC.setWidth(50);
				fila.add(celdaNC);
				fila.add(new CeldaVO(movBanDet.getDebitoView()));
				fila.add(new CeldaVO(movBanDet.getCreditoView()));
				
				tablaMovBanDet.add(fila);
			}
			
			
			fila = new FilaVO();
			if (cierreBancoAdapter.getListTotMovBanDet().size() > 0) {
				tablaMovBanDet.setFilaCabecera(filaCabecera);
				
				fila.add(new CeldaVO(""));
				fila.add(new CeldaVO(""));
				CeldaVO celdaTot = new CeldaVO("Total ($)");
				celdaTot.setWidth(50);
				fila.add(celdaTot);
				fila.add(new CeldaVO(cierreBancoAdapter.getTotalDebitoView()));
				fila.add(new CeldaVO(cierreBancoAdapter.getTotalCreditoView()));
				tablaMovBanDet.add(fila);
			} else {
				CeldaVO celda = new CeldaVO("No existen registros");
				celda.setTextAlignCenter();
				fila.add(celda);
				tablaMovBanDet.setFilaCabecera(fila);
			}
			
			contenedorPrincipal.add(tablaMovBanDet);
			
			// Creamos Tabla con Notas de Abono
			TablaVO tablaNotaImpto = new TablaVO("tablaNotaImpto");
			tablaNotaImpto.setTitulo("Detalle de Notas de Abono");
			
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Tipo Nota"));
			filaCabecera.add(new CeldaVO("Impuesto"));
			filaCabecera.add(new CeldaVO("Moneda"));
			filaCabecera.add(new CeldaVO("N\u00FAmero Cuenta"));
			filaCabecera.add(new CeldaVO("Importe"));
			filaCabecera.add(new CeldaVO("Importe IVA"));

			for (NotaImptoVO notaImpt : cierreBanco.getListNotaImpto()) {
				fila = new FilaVO();
				fila.add(new CeldaVO(notaImpt.getTipoNotaView()));
				fila.add(new CeldaVO(notaImpt.getImpuestoView()));
				fila.add(new CeldaVO(notaImpt.getMonedaView()));
				fila.add(new CeldaVO(notaImpt.getNroCuenta()));
				fila.add(new CeldaVO(notaImpt.getImporteView()));
				fila.add(new CeldaVO(notaImpt.getImporteIVAView()));
				
				tablaNotaImpto.add(fila);
			}
			
			fila = new FilaVO();
			if (!cierreBanco.getListNotaImpto().isEmpty()) {
				tablaNotaImpto.setFilaCabecera(filaCabecera);
				
				fila.add(new CeldaVO(""));
				fila.add(new CeldaVO(""));
				fila.add(new CeldaVO(""));
				fila.add(new CeldaVO("Total ($)"));
				fila.add(new CeldaVO(cierreBancoAdapter.getTotalNotaImptoView()));
				fila.add(new CeldaVO(cierreBancoAdapter.getTotalIVANotaImptoView()));
				tablaNotaImpto.add(fila);
			} else {
				CeldaVO celda = new CeldaVO("No existen registros");
				celda.setTextAlignCenter();
				fila.add(celda);
				tablaNotaImpto.setFilaCabecera(fila);
			}
			contenedorPrincipal.add(tablaNotaImpto);
			
			// Creamos Tabla con Notas Obligaci�n
			TablaVO tablaNotaObl = new TablaVO("tablaNotaObl");
			tablaNotaObl.setTitulo("Detalle de Notas de Obligaci\u00F3n");
			 	  	  	  	 	
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha"));
			filaCabecera.add(new CeldaVO("Banco"));
			filaCabecera.add(new CeldaVO("Cierre Banco"));
			filaCabecera.add(new CeldaVO("Banco Original"));
			filaCabecera.add(new CeldaVO("Cierre Banco Original"));
			filaCabecera.add(new CeldaVO("Total Cr\u00E9dito"));
			filaCabecera.add(new CeldaVO("Total Neto"));

			for (EnvNotOblVO notObl : cierreBanco.getEnvioOsiris().getListEnvNotObl()) {
				fila = new FilaVO();
				fila.add(new CeldaVO(notObl.getFechaRegistroView()));
				fila.add(new CeldaVO(notObl.getBancoView()));
				fila.add(new CeldaVO(notObl.getNroCierreBancoView()));
				fila.add(new CeldaVO(notObl.getBancoOriginalView()));
				fila.add(new CeldaVO(notObl.getNroCieBanOrigView()));
				fila.add(new CeldaVO(notObl.getTotalCreditoView()));
				fila.add(new CeldaVO(notObl.getTotalAcreditadoView()));
				
				tablaNotaObl.add(fila);
			}
			fila = new FilaVO();

			if (cierreBanco.getEnvioOsiris().getListEnvNotObl().isEmpty()) {
				CeldaVO celda = new CeldaVO("No existen registros");
				celda.setTextAlignCenter();
				fila.add(celda);
				tablaNotaObl.setFilaCabecera(fila);
			}else {
				tablaNotaObl.setFilaCabecera(filaCabecera);
			}
			contenedorPrincipal.add(tablaNotaObl);
			
			report.getPathCompletoArchivoXsl();
			BalDAOFactory.getCierreBancoDAO().imprimirGenerico(contenedorPrincipal, report);
			
			log.debug(funcName + ": exit");
			return cierreBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

}
