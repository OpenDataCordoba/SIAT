//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.cache.IndeterminadoCache;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioConvenioCuotaContainer;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.TipoBoleta;

/**
 * Facade de Indeterminado
 * 
 * @author tecso
 *
 */
public class IndeterminadoFacade {
		
	private static Logger log = Logger.getLogger(IndeterminadoFacade.class);
	private static IndeterminadoFacade INSTANCE = null;
	private boolean cacheActivo = true;
	
	/**
	 * Constructor privado
	 */
	private IndeterminadoFacade() {
	}

	/**
	 * Devuelve unica instancia
	 */
	public synchronized static IndeterminadoFacade getInstance() throws Exception {
		if (INSTANCE == null) {
			INSTANCE = new IndeterminadoFacade();
		}
		return INSTANCE;
	}
	
	/**
	 * Indica si una deuda esta indeterminada en el sistema de indeterminados. 
	 * @param deuda
	 * @throws Exception
	 */
	public boolean getEsIndeterminada(Deuda deuda) throws Exception {
		
		if (cacheActivo) {
			long anio;
			long periodo;
			String nroCuenta;
			String clave;
			Long nroSistema = deuda.getSistema().getNroSistema();
			Long resto; 
			if (deuda.getCodRefPag()==null || deuda.getCodRefPag()==0){
				
		    	nroCuenta = deuda.getCuenta().getNumeroCuenta();
				resto = deuda.getResto();
				
				anio = deuda.getAnio();
				periodo = deuda.getPeriodo();
				clave = new Long(anio*100 + periodo).toString();
			}else{
				nroCuenta = deuda.getCodRefPag().toString();
				clave = TipoBoleta.TIPODEUDA.getId().toString()+"00";
				resto=0L;
			}
			Long count = IndeterminadoCache.getInstance().get(nroSistema, nroCuenta, clave, resto);

			/* ## No necesario para Santa Fe ##
			// si no lo encuentra y se trata de nroSistem 2, entonces lo volvemos a buscar
			// pero con sistema 1. (1 y 2 es deuda TGI, en ejercicio vencida y actual)
			if (count == 0 && nroSistema.longValue() == 2L) {
				count = IndeterminadoCache.getInstance().get(1L, nroCuenta, clave, resto);
			} else if (count == 0 && nroSistema.longValue() == 1L) {
				count = IndeterminadoCache.getInstance().get(2L, nroCuenta, clave, resto);
			} */
			
			return count > 0;
		} else {
			//si queres saltear el cache, descomenta esto:
			String nroCuenta = "";
			String resto = "0";
			String nroSistema = StringUtil.completarCerosIzq(deuda.getSistema().getNroSistema().toString(), 2);
			String nroClave;
			if (deuda.getCodRefPag()==null || deuda.getCodRefPag()==0){
				nroClave = String.valueOf(deuda.getAnio()*100 + deuda.getPeriodo());
				nroCuenta = deuda.getCuenta().getNumeroCuenta();
				try { resto  = new Long(deuda.getResto()).toString(); } catch (Exception e) {}
			}else{
				nroClave= StringUtil.completarCerosIzq(TipoBoleta.TIPODEUDA.toString(),4)+"00";
				try { nroCuenta = deuda.getCodRefPag().toString(); } catch (Exception e) {}
				resto = "0";
			}
			boolean ret = BalDAOFactory.getIndeterminadoDAO().getEsIndeterminada(nroSistema, nroCuenta, nroClave, resto); // Se reemplaza la llamada del IndeterminadoJDBCDAO por el IndeterminadoDAO
			/* ## No necesario para Santa Fe ##
			 * if (!ret && nroSistema.equals("02")) {
				ret = BalDAOFactory.getIndeterminadoJDBCDAO().getEsIndeterminada("01", nroCuenta, nroClave, resto);
			} else if (!ret && nroSistema.equals("01")) {
				ret = BalDAOFactory.getIndeterminadoJDBCDAO().getEsIndeterminada("02", nroCuenta, nroClave, resto);				
			}*/
			return ret;
		}
	}	
	
	/**
	 * Indica si una Cuota esta indeterminada en el sistema de indeterminados. 
	 * @param convenioCuota
	 * @throws Exception
	 */
	public boolean getEsIndeterminada(ConvenioCuota convenioCuota) throws Exception {
		
		Long nroSistema;
		String nroConvenio;
		String clave;
		Long resto=0L;
		if (convenioCuota.getCodRefPag()==null || convenioCuota.getCodRefPag()==0){
			nroSistema = convenioCuota.getSistema().getNroSistema();
	    	nroConvenio = convenioCuota.getConvenio().getNroConvenio().toString();
			
			clave = convenioCuota.getNumeroCuota().toString();
			
			
		}else{
			nroSistema = convenioCuota.getSistema().getNroSistema();
			nroConvenio= convenioCuota.getCodRefPag().toString();
			
			clave=  TipoBoleta.TIPOCUOTA.getId().toString() + "00";
		}
		Long count = IndeterminadoCache.getInstance().get(nroSistema, nroConvenio, clave, resto);
		return count > 0;
	}
	
	/**
	 * Indica si una Cuota esta indeterminada en el sistema de indeterminados. 
	 * @param convenioCuota
	 * @throws Exception
	 */
	public boolean getEsIndeterminada(ConvenioConvenioCuotaContainer conCuoCont) throws Exception {
		
		Long nroSistema= Sistema.getById(conCuoCont.getIdSistema()).getNroSistema();
		String nroConvenio;
		Long resto = 0L;
		String clave;
		if (conCuoCont.getCodRefPag()==null || conCuoCont.getCodRefPag()==0){
			nroConvenio = conCuoCont.getNroConvenio().toString();
			clave = conCuoCont.getNroCuota().toString();
		}else{
			nroConvenio=conCuoCont.getCodRefPag().toString();
			clave = TipoBoleta.TIPOCUOTA.getId().toString()+"00";
		}
		
		Long count = IndeterminadoCache.getInstance().get(nroSistema, nroConvenio, clave, resto);
		return count > 0;
	}
	
	
	/**
	 *  Obtiene un registro de Indeterminado y devuelve un Bean SinIndet con los datos.
	 *  
	 * @param convenioCuota
	 * @return sinIndet
	 * @throws Exception
	 */
	public IndetVO getIndeterminada(ConvenioCuota convenioCuota) throws Exception {
		
		//String nroSistema = convenioCuota.getSistema().getNroSistema().toString();
    	//String nroConvenio = convenioCuota.getConvenio().getNroConvenio().toString();
		//String resto = "0";
		//String nroClave = new Long(convenioCuota.getNumeroCuota()).toString(); 
				
		//return BalDAOFactory.getIndeterminadoJDBCDAO().getIndeterminada(nroSistema, nroConvenio, nroClave, resto);
		
		Long nroSistema;
		String nroConvenio;
		String clave;
		Long resto=0L;
		if (convenioCuota.getCodRefPag()==null || convenioCuota.getCodRefPag()==0){
			nroSistema = convenioCuota.getSistema().getNroSistema();
	    	nroConvenio = convenioCuota.getConvenio().getNroConvenio().toString();
			
			clave = convenioCuota.getNumeroCuota().toString();
			
			
		}else{
			nroSistema = convenioCuota.getSistema().getNroSistema();
			nroConvenio= convenioCuota.getCodRefPag().toString();
			
			clave=  TipoBoleta.TIPOCUOTA.getId().toString() + "00";
		}
		IndetVO indetVO = IndeterminadoCache.getInstance().getIndetVO(nroSistema, nroConvenio, clave, resto);
		
		
		return indetVO;
	}
	
	/**
	 *  Actualiza el Cache de la DB de Indeterminado.
	 *  
	 * @return
	 */
	public boolean invalidateCache() {
		IndeterminadoCache.getInstance().invalidateCache();
		if(IndeterminadoCache.getInstance().getCacheException() != null)
			return false;
		else
			return true;
	}
	
	/**
	 * Recupera todos los indet_tot cuya cuenta_o es esta en la lista de items.
	 * Sirve para realizar busquedas genericas en indeterminados. Sobre datos migrados
	 * El resultado y su funcionalidad depende de los item que se pasen y de la 
	 * manera en que se gravan los intedeterminados en la tabla indet_tot
	 * generados por el asentamiento SIAT. Por lo que esta info, 
	 * esta sujeta a cambios en generacion de indeterminado del asentamiento
	 * 
	 * <p>Formas de uso segun items:<br>
	 * 	Cuando un item es un nroCuenta matchea los indet:
	 * 		Deuda Migrada
	 * 		Deuda migrada desglose Recibo Deuda Migrado
	 * 		Deuda migrada desgloce Recibo Deuda Siat
	 *	
	 *	Cuando un item es nroRecibo matchea los indet:
	 * 		Recibo de Deuda Migrado
	 * 		Recibo Cuota Migrado
	 *	
	 *	Cuando un item es nroConvenio matchea los indet:	
	 * 		Cuota migrada
	 * 		Cuota Migrada desgloce Recibo Cuota Migrado
	 * 		Cuota migrada desgloce Recibo Cuota Siat
	 * 
	 * @param items este campo suele ser o un numero de cuenta o un nro de convenio.
	 * @return
	 * @throws Exception
	 */
	public List<IndetVO> findByCuentasO(List<Long> items) throws Exception {
		
		return BalDAOFactory.getIndeterminadoDAO().getListIndetByListNroComprobanteClave(items, null);  // Se reemplaza la llamada del IndeterminadoJDBCDAO por el IndeterminadoDAO
	}
	
	/**
	 * Recupera todos los indet_tot cuya cuenta_o es esta en la lista de items y clave_o 
	 * conincide con tipoBoleta.
	 * Sirve para realizar busquedas genericas en indeterminados. Sobre datos generados por SIAT
	 * El resultado y su funcionalidad depende de los item que se pasen y de la 
	 * manera en que se gravan los intedeterminados en la tabla indet_tot generados
	 * por el asentamiento SIAT. Por lo que esta info, 
	 * esta sujeta a cambios en generacion de indeterminado del asentamiento
	 * 
	 * <p>Formas de uso segun items, item simpre represetna un codRefPag, 
	 * el tipoBoleta indica de que tipo es:<br>
	 * 	Cuando un item es un codRefPag de Deuda (tipoBoleta=1) matchea los indet:
	 * 		Deuda Siat
	 * 		Deuda Siat desglose Recibo Deuda Siat
	 *	
	 * 	Cuando un item es un codRefPag de Recibo de Deuda (tipoBoleta=2) matchea los indet:
	 * 		Recibo Deuda Siat sobre Deuda Siat
	 * 		Recibo Deuda Siat sobre Deuda Migrada
	 *
	 * 	Cuando un item es un codRefPag de Cuota (tipoBoleta=3) matchea los indet:
	 * 		Cuota Siat
	 * 		Cuota Siat desglose Recibo Cuota Siat
	 * 
	 * Cuando un item es un codRefPag de Recibo de Cuota (tipoBoleta=4) matchea los indet:
	 * 		Recibo Cuota Siat sobre Cuota Siat
	 * 		Recibo Cuota Siat sobre Cuota Migrada
	 * 
	 * @param items este campo suele ser o un numero de cuenta o un nro de convenio.
	 * @return
	 * @throws Exception
	 */
	public List<IndetVO> findByCuentasO(List<String> items, Long tipoBoleta) throws Exception {
		return null;
	}

	/**
	 * Recupera un Indeterminado por nroIndeterminado
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getById(Long nroIndeterm) throws Exception {
		Indeterminado indeterminado = Indeterminado.getByIdNull(nroIndeterm);
		if(indeterminado != null)
			return indeterminado.toIndetVO();
		return null;
	}

	public IndetVO createIndet(IndetVO indet) throws Exception{
		Indeterminado indeterminado = BalIndeterminadoManager.getInstance().indetVOToIndeterminado(indet);
		indeterminado =  BalIndeterminadoManager.getInstance().createIndeterminado(indeterminado);
		return indeterminado.toIndetVO();
	}
	
	public IndetVO createDuplice(IndetVO indet) throws Exception{
		Duplice duplice = BalIndeterminadoManager.getInstance().indetVOToDuplice(indet);
		duplice =  BalIndeterminadoManager.getInstance().createDuplice(duplice);
		return duplice.toIndetVO();
	}
	
	public IndetVO updateIndet(IndetVO indet) throws Exception{
		Indeterminado indeterminado = BalIndeterminadoManager.getInstance().indetVOToIndeterminado(indet);
		indeterminado =  BalIndeterminadoManager.getInstance().updateIndeterminado(indeterminado);
		return indeterminado.toIndetVO();
	}

	public IndetVO deleteIndet(IndetVO indet) throws Exception{
		Indeterminado indeterminado = Indeterminado.getByIdNull(indet.getNroIndeterminado());
		indeterminado =  BalIndeterminadoManager.getInstance().deleteIndeterminado(indeterminado);
		return indet;
	}
	
	public IndetVO createReingreso(IndetVO indet) throws Exception{
		ReingIndet reingIndet = BalIndeterminadoManager.getInstance().indetVOToReingIndet(indet);
		reingIndet =  BalIndeterminadoManager.getInstance().createReingIndet(reingIndet);
		return reingIndet.toIndetVO();
	}
	
	/**
	 * Recupera un Reingreso por nroReingreso (nroReing)
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getReingresoById(Long nroIndeterm) throws Exception {
		ReingIndet reingIndet = ReingIndet.getByIdNull(nroIndeterm);
		if(reingIndet != null)
			return reingIndet.toIndetVO();
		return null;
	}
	
	public IndetVO deleteReingreso(IndetVO reingreso) throws Exception{
		ReingIndet reingIndet = ReingIndet.getById(reingreso.getNroIndeterminado());
		reingIndet =  BalIndeterminadoManager.getInstance().deleteReingIndet(reingIndet);
		return reingreso;
	}
	
	/**
	 *  Registro en tabla indet_modif, donde se guardan los datos originales de un indeterminado antes de modificarlo
	 *  efectivamente en indet_tot
	 * 
	 * @param indet
	 * @return
	 * @throws Exception
	 */
	public IndetVO createIndetModif(IndetVO indet) throws Exception{
		IndetAudit indetAudit = BalIndeterminadoManager.getInstance().indetVOToIndetAudit(indet);
		indetAudit =  BalIndeterminadoManager.getInstance().createIndetAudit(indetAudit);
		return indetAudit.toIndetVO();
	}
	
	/**
	 * Recupera un IndetAudit por nroIndeterminado
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getIndetModifByIdOrigen(Long nroIndeterm) throws Exception {
		IndetAudit indetAudit = IndetAudit.getByIdOrigen(nroIndeterm);
		if(indetAudit != null)
			return indetAudit.toIndetVO();
		return null;
	}
	
	/**
	 * Recupera un Duplice por nroIndeterminado
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getDupliceById(Long nroIndeterm) throws Exception {
		Duplice duplice = Duplice.getByIdNull(nroIndeterm);
		if(duplice != null)
			return duplice.toIndetVO();
		return null;
	}
	
	public IndetVO deleteDuplice(IndetVO indet) throws Exception{
		Duplice duplice = Duplice.getById(indet.getNroIndeterminado());
		duplice =  BalIndeterminadoManager.getInstance().deleteDuplice(duplice);
		return indet;
	}
		
	/**
	 * Recupera un intet_modif por nroIndeterminado
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getIndetModifById(Long nroIndeterm) throws Exception {
		return BalDAOFactory.getIndeterminadoJDBCDAO().getIndetModifById(nroIndeterm);
	}
	
}
