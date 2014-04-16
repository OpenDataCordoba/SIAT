//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Indeterminado
 * 
 * @author tecso
 *
 */
public class BalIndeterminadoManager {

	private static final BalIndeterminadoManager INSTANCE = new BalIndeterminadoManager();
	
	/**
	 * Constructor privado
	 */
	private BalIndeterminadoManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalIndeterminadoManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Indeterminado	
	public Indeterminado createIndeterminado(Indeterminado indeterminado) throws Exception {

		// Validaciones de negocio
		if (!indeterminado.validateCreate()) {
			return indeterminado;
		}

		BalDAOFactory.getIndeterminadoDAO().update(indeterminado);

		return indeterminado;
	}
	
	public Indeterminado updateIndeterminado(Indeterminado indeterminado) throws Exception {
		
		// Validaciones de negocio
		if (!indeterminado.validateUpdate()) {
			return indeterminado;
		}
		
		BalDAOFactory.getIndeterminadoDAO().update(indeterminado);
		
	    return indeterminado;
	}
	
	public Indeterminado deleteIndeterminado(Indeterminado indeterminado) throws Exception {

		
		// Validaciones de negocio
		if (!indeterminado.validateDelete()) {
			return indeterminado;
		}
		
		BalDAOFactory.getIndeterminadoDAO().delete(indeterminado);
		
		return indeterminado;
	}
	// <--- ABM Indeterminado
	
	// ---> ABM Duplice	
	public Duplice createDuplice(Duplice duplice) throws Exception {

		// Validaciones de negocio
		if (!duplice.validateCreate()) {
			return duplice;
		}

		BalDAOFactory.getDupliceDAO().update(duplice);

		return duplice;
	}
	
	public Duplice updateDuplice(Duplice duplice) throws Exception {
		
		// Validaciones de negocio
		if (!duplice.validateUpdate()) {
			return duplice;
		}
		
		BalDAOFactory.getDupliceDAO().update(duplice);
		
	    return duplice;
	}
	
	public Duplice deleteDuplice(Duplice duplice) throws Exception {

		
		// Validaciones de negocio
		if (!duplice.validateDelete()) {
			return duplice;
		}
		
		BalDAOFactory.getDupliceDAO().delete(duplice);
		
		return duplice;
	}
	// <--- ABM Duplice
	
	
	// ---> ABM ReingIndet	
	public ReingIndet createReingIndet(ReingIndet reingIndet) throws Exception {

		// Validaciones de negocio
		if (!reingIndet.validateCreate()) {
			return reingIndet;
		}

		BalDAOFactory.getReingIndetDAO().update(reingIndet);

		return reingIndet;
	}
	
	public ReingIndet updateReingIndet(ReingIndet reingIndet) throws Exception {
		
		// Validaciones de negocio
		if (!reingIndet.validateUpdate()) {
			return reingIndet;
		}
		
		BalDAOFactory.getReingIndetDAO().update(reingIndet);
		
	    return reingIndet;
	}
	
	public ReingIndet deleteReingIndet(ReingIndet reingIndet) throws Exception {

		
		// Validaciones de negocio
		if (!reingIndet.validateDelete()) {
			return reingIndet;
		}
		
		BalDAOFactory.getReingIndetDAO().delete(reingIndet);
		
		return reingIndet;
	}
	// <--- ABM ReingIndet
	
	// ---> ABM IndetAudit	
	public IndetAudit createIndetAudit(IndetAudit indetAudit) throws Exception {

		// Validaciones de negocio
		if (!indetAudit.validateCreate()) {
			return indetAudit;
		}

		BalDAOFactory.getIndetAuditDAO().update(indetAudit);

		return indetAudit;
	}
	
	public IndetAudit updateIndetAudit(IndetAudit indetAudit) throws Exception {
		
		// Validaciones de negocio
		if (!indetAudit.validateUpdate()) {
			return indetAudit;
		}
		
		BalDAOFactory.getIndetAuditDAO().update(indetAudit);
		
	    return indetAudit;
	}
	
	public IndetAudit deleteIndetAudit(IndetAudit indetAudit) throws Exception {

		
		// Validaciones de negocio
		if (!indetAudit.validateDelete()) {
			return indetAudit;
		}
		
		BalDAOFactory.getIndetAuditDAO().delete(indetAudit);
		
		return indetAudit;
	}
	// <--- ABM IndetAudit
	
	/**
	 * Prepara un Indeterminado con con los datos del IndetVO
	 * 
	 * @return
	 */
	public Indeterminado indetVOToIndeterminado(IndetVO indetVO) throws Exception {
		Indeterminado indeterminado = Indeterminado.getByIdNull(indetVO.getId()); 
		if(indeterminado == null)
			indeterminado = new Indeterminado();
	
		if(indetVO.getSistema() != null)
			indeterminado.setSistema(Long.valueOf(indetVO.getSistema()));
		else
			indeterminado.setSistema(null);
		if(indetVO.getNroComprobante() != null)
			indeterminado.setNroComprobante(Long.valueOf(indetVO.getNroComprobante()));
		else
			indeterminado.setNroComprobante(null);
		indeterminado.setClave(indetVO.getClave());
		if(indetVO.getResto() != null)
			indeterminado.setResto(Long.valueOf(indetVO.getResto()));
		else
			indeterminado.setResto(null);
		indeterminado.setImporte(indetVO.getImporteCobrado());
		indeterminado.setImporteBasico(indetVO.getImporteBasico());
		indeterminado.setImporteCalculado(indetVO.getImporteCalculado());
		indeterminado.setIndice(indetVO.getIndice());
		indeterminado.setRecargo(indetVO.getRecargo());
		indeterminado.setPartida(Partida.getByCod(indetVO.getPartida()));
		if(indetVO.getCodIndet() != null)
			indeterminado.setTipoIndet(TipoIndet.getByCodigo(indetVO.getCodIndet().toString()));
		else
			indeterminado.setTipoIndet(null);
		indeterminado.setFechaPago(indetVO.getFechaPago());
		if(indetVO.getCaja() != null)
			indeterminado.setCaja(Long.valueOf(indetVO.getCaja()));
		else
			indeterminado.setCaja(null);
		if(indetVO.getCodPago() != null)
			indeterminado.setCodPago(Long.valueOf(indetVO.getCodPago()));
		else
			indeterminado.setCodPago(null);
		indeterminado.setFechaBalance(indetVO.getFechaBalance());
		indeterminado.setReciboTr(indetVO.getReciboTr());
		
		return indeterminado;
	}
	
	/**
	 * Prepara un Duplice con con los datos del IndetVO
	 * 
	 * @return
	 */
	public Duplice indetVOToDuplice(IndetVO indetVO) throws Exception {
		Duplice duplice = Duplice.getByIdNull(indetVO.getId());
		if(duplice == null)
			duplice = new Duplice();
		
		duplice.setSistema(Long.valueOf(indetVO.getSistema()));
		duplice.setNroComprobante(Long.valueOf(indetVO.getNroComprobante()));
		duplice.setClave(indetVO.getClave());
		duplice.setResto(Long.valueOf(indetVO.getResto()));
		duplice.setImporte(indetVO.getImporteCobrado());
		duplice.setImporteBasico(indetVO.getImporteBasico());
		duplice.setImporteCalculado(indetVO.getImporteCalculado());
		duplice.setIndice(indetVO.getIndice());
		duplice.setRecargo(indetVO.getRecargo());
		duplice.setPartida(Partida.getByCod(indetVO.getPartida()));
		duplice.setTipoIndet(TipoIndet.getByCodigo(indetVO.getCodIndet().toString()));
		duplice.setFechaPago(indetVO.getFechaPago());
		duplice.setCaja(Long.valueOf(indetVO.getCaja()));
		duplice.setCodPago(Long.valueOf(indetVO.getCodPago()));
		duplice.setFechaBalance(indetVO.getFechaBalance());
		duplice.setReciboTr(indetVO.getReciboTr());
		
		return duplice;
	}
	
	/**
	 * Prepara un ReingIndet con con los datos del IndetVO
	 * 
	 * @return
	 */
	public ReingIndet indetVOToReingIndet(IndetVO indetVO) throws Exception {
		ReingIndet reingIndet = ReingIndet.getByIdNull(indetVO.getId());
		if(reingIndet == null)
			reingIndet = new ReingIndet();
			
		reingIndet.setSistema(Long.valueOf(indetVO.getSistema()));
		reingIndet.setNroComprobante(Long.valueOf(indetVO.getNroComprobante()));
		reingIndet.setClave(indetVO.getClave());
		reingIndet.setResto(Long.valueOf(indetVO.getResto()));
		reingIndet.setImporte(indetVO.getImporteCobrado());
		reingIndet.setImporteBasico(indetVO.getImporteBasico());
		reingIndet.setImporteCalculado(indetVO.getImporteCalculado());
		reingIndet.setIndice(indetVO.getIndice());
		reingIndet.setRecargo(indetVO.getRecargo());
		reingIndet.setPartida(Partida.getByCod(indetVO.getPartida()));
		reingIndet.setTipoIndet(TipoIndet.getByCodigo(indetVO.getCodIndet().toString()));
		reingIndet.setFechaPago(indetVO.getFechaPago());
		reingIndet.setCaja(Long.valueOf(indetVO.getCaja()));
		reingIndet.setCodPago(Long.valueOf(indetVO.getCodPago()));
		reingIndet.setFechaBalance(indetVO.getFechaBalance());
		reingIndet.setReciboTr(indetVO.getReciboTr());
		reingIndet.setIdOrigen(indetVO.getNroIndeterminado());
		reingIndet.setFechaReingreso(indetVO.getFechaReing());
		
		return reingIndet;
	}
	
	/**
	 * Prepara un IndetAudit con con los datos del IndetVO
	 * 
	 * @return
	 */
	public IndetAudit indetVOToIndetAudit(IndetVO indetVO) throws Exception {
		IndetAudit indetAudit = new IndetAudit();
		
		indetAudit.setSistema(Long.valueOf(indetVO.getSistema()));
		indetAudit.setNroComprobante(Long.valueOf(indetVO.getNroComprobante()));
		indetAudit.setClave(indetVO.getClave());
		indetAudit.setResto(Long.valueOf(indetVO.getResto()));
		indetAudit.setImporte(indetVO.getImporteCobrado());
		indetAudit.setImporteBasico(indetVO.getImporteBasico());
		indetAudit.setImporteCalculado(indetVO.getImporteCalculado());
		indetAudit.setIndice(indetVO.getIndice());
		indetAudit.setRecargo(indetVO.getRecargo());
		indetAudit.setPartida(Partida.getByCod(indetVO.getPartida()));
		indetAudit.setTipoIndet(TipoIndet.getByCodigo(indetVO.getCodIndet().toString()));
		indetAudit.setFechaPago(indetVO.getFechaPago());
		indetAudit.setCaja(Long.valueOf(indetVO.getCaja()));
		indetAudit.setCodPago(Long.valueOf(indetVO.getCodPago()));
		indetAudit.setFechaBalance(indetVO.getFechaBalance());
		indetAudit.setReciboTr(indetVO.getReciboTr());
		indetAudit.setIdOrigen(indetVO.getNroIndeterminado());
		indetAudit.setFechaReingreso(indetVO.getFechaReing());
		
		return indetAudit;
	}

}

