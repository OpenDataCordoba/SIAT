//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;

/**
 * Manejador del m&oacute;dulo Ef y submodulo Fiscalizacion
 * 
 * @author tecso
 *
 */
public class EfFiscalizacionManager {
		
	private static Logger log = Logger.getLogger(EfFiscalizacionManager.class);
	
	private static final EfFiscalizacionManager INSTANCE = new EfFiscalizacionManager();
	
	
	private Map<Long,CompFuente> mapCompFuente;
	private Map<Long,PlaFueDat> mapPlaFueDat;
	/**
	 * Constructor privado
	 */
	private EfFiscalizacionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EfFiscalizacionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM CompFuente
	public CompFuente createCompFuente(CompFuente compFuente) throws Exception {

		// Validaciones de negocio
		if (!compFuente.validateCreate()) {
			return compFuente;
		}

		EfDAOFactory.getCompFuenteDAO().update(compFuente);

		return compFuente;
	}
	
	public CompFuente createCompFuente(Comparacion comparacion, PlaFueDat plaFueDat, Integer periodoDesde, Integer anioDesde, Integer periodoHasta, Integer anioHasta)throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		CompFuente compFuente = new CompFuente();
		try {
			session = SiatHibernateUtil.currentSession();
	
			// Copiado de propiadades de VO al BO
			compFuente.setPlaFueDat(plaFueDat);
			compFuente.setComparacion(comparacion);
			compFuente.setPeriodoDesde(periodoDesde);
			compFuente.setAnioDesde(anioDesde);
			compFuente.setPeriodoHasta(periodoHasta);
			compFuente.setAnioHasta(anioHasta);
			
			compFuente.setEstado(Estado.ACTIVO.getId());
			compFuente = createCompFuente(compFuente);
			
			// replica las columnas
			for(PlaFueDatCol plaFueDatCol:plaFueDat.getListPlaFueDatCol()){
				CompFuenteCol compFuenteCol = new CompFuenteCol();
				compFuenteCol.setCompFuente(compFuente);
				compFuenteCol.setColName(plaFueDatCol.getColName());
				compFuenteCol.setNroColumna(plaFueDatCol.getNroColumna());
				compFuenteCol.setOculta(plaFueDatCol.getOculta());
				compFuenteCol.setOrden(plaFueDatCol.getOrden());
				compFuenteCol.setSumaEnTotal(plaFueDatCol.getSumaEnTotal());
				
				compFuenteCol = compFuente.createCompFuenteCol(compFuenteCol);
				if(compFuenteCol.hasError()){
					compFuenteCol.passErrorMessages(compFuente);
					break;
				}
			}
			
			// recarga la fuente con la lista de columnas agregadas en el paso anterior
			session.refresh(compFuente);
			
			Double totalGral =0D;
			
			// replica los detalles
			log.debug("replicando detalles........");
			for(PlaFueDatDet plaFueDatDet: plaFueDat.getListPlaFueDatDet()){
				
				PlaFueDatCom plaFueDatCom = new PlaFueDatCom();
				plaFueDatCom.setCompFuente(compFuente);
				plaFueDatCom.setPeriodo(plaFueDatDet.getPeriodo());
				plaFueDatCom.setAnio(plaFueDatDet.getAnio());
				
				for(PlaFueDatCol plaFueDatCol: plaFueDat.getListPlaFueDatCol()){
					if(plaFueDatCol.getSumaEnTotal().equals(SiNo.SI.getId())){
						switch(plaFueDatCol.getNroColumna()){
							case 1:plaFueDatCom.setCol1(plaFueDatDet.getCol1());break;
							case 2:plaFueDatCom.setCol2(plaFueDatDet.getCol2());break;
							case 3:plaFueDatCom.setCol3(plaFueDatDet.getCol3());break;
							case 4:plaFueDatCom.setCol4(plaFueDatDet.getCol4());break;
							case 5:plaFueDatCom.setCol5(plaFueDatDet.getCol5());break;
							case 6:plaFueDatCom.setCol6(plaFueDatDet.getCol6());break;
							case 7:plaFueDatCom.setCol7(plaFueDatDet.getCol7());break;
							case 8:plaFueDatCom.setCol8(plaFueDatDet.getCol8());break;
							case 9:plaFueDatCom.setCol9(plaFueDatDet.getCol9());break;
							case 10:plaFueDatCom.setCol10(plaFueDatDet.getCol10());break;
							case 11:plaFueDatCom.setCol11(plaFueDatDet.getCol11());break;
							case 12:plaFueDatCom.setCol12(plaFueDatDet.getCol12());break;				
						}
					}
				}
				
				plaFueDatCom = compFuente.createPlaFueDatCom(plaFueDatCom);
				if(plaFueDatCom.hasError()){
					plaFueDatCom.passErrorMessages(compFuente);
					break;
				}     
				
				Double totalDetalle =0D;
				for(CompFuenteCol compFuenteCol: compFuente.getListCompFuenteCol()){
					if(compFuenteCol.getSumaEnTotal().equals(SiNo.SI.getId())){
			    		Double valor=0D;
			    		switch(compFuenteCol.getNroColumna()){
			    			case 1: valor = plaFueDatCom.getCol1();break;
			    			case 2: valor = plaFueDatCom.getCol2();break;
			    			case 3: valor = plaFueDatCom.getCol3();break;
			    			case 4: valor = plaFueDatCom.getCol4();break;
			    			case 5: valor = plaFueDatCom.getCol5();break;
			    			case 6: valor = plaFueDatCom.getCol6();break;
			    			case 7: valor = plaFueDatCom.getCol7();break;
			    			case 8: valor = plaFueDatCom.getCol8();break;
			    			case 9: valor = plaFueDatCom.getCol9();break;
			    			case 10: valor = plaFueDatCom.getCol10();break;
			    			case 11: valor = plaFueDatCom.getCol11();break;
			    			case 12: valor = plaFueDatCom.getCol12();break;
			    		}
			    		totalDetalle += valor;
					}            		
				}
				
				// Suma al total solo si el periodo esta dentro del rango ingresado en compFuente
				boolean sumar=false;
				
				// forma los tmp con el formato: aaaaMM
				Long periodoDesdeTmp = anioDesde*100+(periodoDesde!=null?periodoDesde:0L);
				Long periodoHastaTmp = anioHasta*100+(periodoHasta!=null?periodoHasta:0L);
				
				Long periodoEvaluar = null;
				
				periodoEvaluar = plaFueDatCom.getAnio()*100L;					
				if(plaFueDat.getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL.getId())){					
					periodoEvaluar +=plaFueDatCom.getPeriodo();
				}
				
				if(periodoEvaluar>=periodoDesdeTmp && periodoEvaluar<=periodoHastaTmp)
					sumar=true;
				
				if(sumar)
					totalGral +=totalDetalle;
			}
			
			log.debug("totalllll:"+totalGral);
			
			//actualiza el total
			compFuente.setTotal(totalGral);
			compFuente = updateCompFuente(compFuente);
			
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
		
		return compFuente;
	}
	
	public CompFuente updateCompFuente(CompFuente compFuente) throws Exception {
		
		// Validaciones de negocio
		if (!compFuente.validateUpdate()) {
			return compFuente;
		}

		EfDAOFactory.getCompFuenteDAO().update(compFuente);
		
		return compFuente;
	}
	
	public CompFuente deleteCompFuente(CompFuente compFuente) throws Exception {
	
		// Validaciones de negocio
		if (!compFuente.validateDelete()) {
			return compFuente;
		}
		
		EfDAOFactory.getCompFuenteDAO().delete(compFuente);
		
		return compFuente;
	}
	// <--- ABM CompFuente
	
	// ---> ABM AliComFueCol
	public AliComFueCol createAliComFueCol(AliComFueCol aliComFueCol) throws Exception {

		// Validaciones de negocio
		if (!aliComFueCol.validateCreate()) {
			return aliComFueCol;
		}

		EfDAOFactory.getAliComFueColDAO().update(aliComFueCol);

		return aliComFueCol;
	}
	
	public AliComFueCol updateAliComFueCol(AliComFueCol aliComFueCol) throws Exception {
		
		// Validaciones de negocio
		if (!aliComFueCol.validateUpdate()) {
			return aliComFueCol;
		}

		EfDAOFactory.getAliComFueColDAO().update(aliComFueCol);
		
		return aliComFueCol;
	}
	
	public AliComFueCol deleteAliComFueCol(AliComFueCol aliComFueCol) throws Exception {
	
		// Validaciones de negocio
		if (!aliComFueCol.validateDelete()) {
			return aliComFueCol;
		}
		
		EfDAOFactory.getAliComFueColDAO().delete(aliComFueCol);
		
		return aliComFueCol;
	}
	// <--- ABM AliComFueCol
	public OrdenControl clonarFromOrdenControlOrigen(OrdenControl ordenControl) throws Exception{
		
		this.mapCompFuente=new HashMap<Long,CompFuente>();
		this.mapPlaFueDat=new HashMap<Long,PlaFueDat>();
		
		//OrdConCue
		if (!ListUtil.isNullOrEmpty(ordenControl.getOrdenControlOrigen().getListOrdConCue())){
			if (ordenControl.getListOrdConCue()==null)
				ordenControl.setListOrdConCue(new ArrayList<OrdConCue>());
			for (OrdConCue ordConCue: ordenControl.getOrdenControlOrigen().getListOrdConCue()){
				OrdConCue ordConCueNueva = (OrdConCue) ordConCue.cloneObject(ordConCue);
				ordConCueNueva.setOrdenControl(ordenControl);
				EfDAOFactory.getOrdConCueDAO().update(ordConCueNueva);
				ordenControl.getListOrdConCue().add(ordConCueNueva);
			}
		}
		
		SiatHibernateUtil.currentSession().flush();
		
		
		//PeriodoOrden
		if (!ListUtil.isNullOrEmpty(ordenControl.getOrdenControlOrigen().getListPeriodoOrden())){
			Cuenta cuentaAnt=null;
			OrdConCue ordConCue=null;
			for (PeriodoOrden periodoOrden: ordenControl.getOrdenControlOrigen().getListPeriodoOrden()){
				PeriodoOrden perOrdNueva = (PeriodoOrden) periodoOrden.cloneObject(periodoOrden);
				perOrdNueva.setOrdenControl(ordenControl);
				if (cuentaAnt ==null || !cuentaAnt.equals(periodoOrden.getOrdConCue().getCuenta())){
					cuentaAnt = periodoOrden.getOrdConCue().getCuenta();
					for(OrdConCue ordConCueActual: ordenControl.getListOrdConCue()){
						if(ordConCueActual.getCuenta().equals(perOrdNueva.getOrdConCue().getCuenta())){
								ordConCue=ordConCueActual;
								break;
						}
					}
				}
				perOrdNueva.setOrdConCue(ordConCue);
				EfDAOFactory.getOrdConCueDAO().update(perOrdNueva);
			}
		}
		
		SiatHibernateUtil.currentSession().flush();
		
		
		//PlaFueDat
		if (!ListUtil.isNullOrEmpty(ordenControl.getOrdenControlOrigen().getListPlaFueDat())){
			if (ordenControl.getListPlaFueDat()==null)
				ordenControl.setListPlaFueDat(new ArrayList<PlaFueDat>());
			
			
			for (PlaFueDat plaFueDat: ordenControl.getOrdenControlOrigen().getListPlaFueDat()){
				PlaFueDat plaFueDatNueva = (PlaFueDat) plaFueDat.cloneObject(plaFueDat);
				plaFueDatNueva.setOrdenControl(ordenControl);
				plaFueDatNueva.setListPlaFueDatCol(new ArrayList<PlaFueDatCol>());
				plaFueDatNueva.setListPlaFueDatDet(new ArrayList<PlaFueDatDet>());
				EfDAOFactory.getOrdConCueDAO().update(plaFueDatNueva);
				mapPlaFueDat.put(plaFueDat.getId().longValue(), plaFueDatNueva);
				
				
				for (PlaFueDatCol plaFueDatCol: plaFueDat.getListPlaFueDatCol()){
					PlaFueDatCol plaFueDatColNueva= (PlaFueDatCol)plaFueDatCol.cloneObject(plaFueDatCol);
					plaFueDatColNueva.setPlaFueDat(plaFueDatNueva);
					EfDAOFactory.getPlaFueDatColDAO().update(plaFueDatColNueva);
					plaFueDatNueva.getListPlaFueDatCol().add(plaFueDatColNueva);
				}
				
				for (PlaFueDatDet plaFueDatDet: plaFueDat.getListPlaFueDatDet()){
					PlaFueDatDet plaFueDatDetNueva = (PlaFueDatDet)plaFueDatDet.cloneObject(plaFueDatDet);
					plaFueDatDetNueva.setPlaFueDat(plaFueDatNueva);
					EfDAOFactory.getPlaFueDatDetDAO().update(plaFueDatDetNueva);
					plaFueDatNueva.getListPlaFueDatDet().add(plaFueDatDetNueva);
				}
				ordenControl.getListPlaFueDat().add(plaFueDatNueva);
			}
		}
		
		SiatHibernateUtil.currentSession().flush();
		
		//Comparaciones
		if (!ListUtil.isNullOrEmpty(ordenControl.getOrdenControlOrigen().getListComparacion()))
			for(Comparacion comparacion : ordenControl.getOrdenControlOrigen().getListComparacion()){
				Comparacion comparacionNueva = (Comparacion)comparacion.cloneObject(comparacion);
				comparacionNueva.setOrdenControl(ordenControl);
				EfDAOFactory.getComparacionDAO().update(comparacionNueva);
				
				if (!ListUtil.isNullOrEmpty(comparacion.getListCompFuente())){
					comparacionNueva.setListCompFuente(new ArrayList<CompFuente>());
					
					for(CompFuente compFuente:comparacion.getListCompFuente()){
						CompFuente compFuenteNueva = (CompFuente)compFuente.cloneObject(compFuente);
						compFuenteNueva.setComparacion(comparacionNueva);
						compFuenteNueva.setPlaFueDat((PlaFueDat)mapPlaFueDat.get(compFuente.getPlaFueDat().getId().longValue()));
						
						EfDAOFactory.getCompFuenteDAO().update(compFuenteNueva);
						
						comparacionNueva.getListCompFuente().add(compFuenteNueva);
						
						mapCompFuente.put(compFuente.getId().longValue(), compFuenteNueva);
						
					}
				}
				
				
				if (!ListUtil.isNullOrEmpty(comparacion.getListCompFuenteRes())){
					comparacionNueva.setListCompFuenteRes(new ArrayList<CompFuenteRes>());
					
					for (CompFuenteRes compFuenteRes:comparacion.getListCompFuenteRes()){
						CompFuenteRes compFuenteResNueva = (CompFuenteRes)compFuenteRes.cloneObject(compFuenteRes);
						compFuenteResNueva.setComparacion(comparacionNueva);
						compFuenteResNueva.setComFueMin((CompFuente) mapCompFuente.get(compFuenteRes.getComFueMin().getId().longValue()));
						compFuenteResNueva.setComFueSus((CompFuente) mapCompFuente.get(compFuenteRes.getComFueSus().getId().longValue()));
						
						EfDAOFactory.getCompFuenteResDAO().update(compFuenteResNueva);
						
						comparacionNueva.getListCompFuenteRes().add(compFuenteResNueva);
					}
				}
			}
		
		SiatHibernateUtil.currentSession().flush();
		
		//CompFuente sin comparaciones
		List<CompFuente>listCompFuenteSinComparacion=EfDAOFactory.getCompFuenteDAO().getListCompFuenteSinComparacion(ordenControl.getOrdenControlOrigen());
		
		if (!ListUtil.isNullOrEmpty(listCompFuenteSinComparacion)){
			for (CompFuente compFuenteSinComp: listCompFuenteSinComparacion){
				CompFuente compFuenteSinCompNueva = (CompFuente)compFuenteSinComp.cloneObject(compFuenteSinComp);
				compFuenteSinCompNueva.setPlaFueDat((PlaFueDat)mapPlaFueDat.get(compFuenteSinComp.getPlaFueDat().getId().longValue()));
				EfDAOFactory.getCompFuenteDAO().update(compFuenteSinCompNueva);
				
				mapCompFuente.put(compFuenteSinComp.getId().longValue(), compFuenteSinCompNueva);
			}
		}
		
		//OrdConBasImp
		if (!ListUtil.isNullOrEmpty(ordenControl.getOrdenControlOrigen().getListOrdConBasImp()))
			for (OrdConBasImp ordConBasImp: ordenControl.getOrdenControlOrigen().getListOrdConBasImp()){
				OrdConBasImp ordConBasImpNueva = (OrdConBasImp)ordConBasImp.cloneObject(ordConBasImp);
				ordConBasImpNueva.setOrdenControl(ordenControl);

				CompFuente compFuente=(CompFuente)mapCompFuente.get(ordConBasImp.getCompFuente().getId().longValue());
				ordConBasImpNueva.setCompFuente(compFuente);
				
				EfDAOFactory.getOrdConBasImpDAO().update(ordConBasImpNueva);
			}
		
		SiatHibernateUtil.currentSession().flush();
		
		return ordenControl;
	}
	
}
