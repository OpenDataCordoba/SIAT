//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;

/**
 * Manejador del m&oacute;dulo Gde y submodulo definicion
 * 
 * @author tecso
 *
 */
public class GdeDefinicionManager {
		
	private static final GdeDefinicionManager INSTANCE = new GdeDefinicionManager();
	
	/**
	 * Constructor privado
	 */
	private GdeDefinicionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static GdeDefinicionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM DesGen
	public DesGen createDesGen(DesGen desGen) throws Exception {

		// Validaciones de negocio
		if (!desGen.validateCreate()) {
			return desGen;
		}

		GdeDAOFactory.getDesGenDAO().update(desGen);

		return desGen;
	}
	
	public DesGen updateDesGen(DesGen desGen) throws Exception {
		
		// Validaciones de negocio
		if (!desGen.validateUpdate()) {
			return desGen;
		}

		GdeDAOFactory.getDesGenDAO().update(desGen);
		
		return desGen;
	}
	
	public DesGen deleteDesGen(DesGen desGen) throws Exception {
	
		// Validaciones de negocio
		if (!desGen.validateDelete()) {
			return desGen;
		}
		
		GdeDAOFactory.getDesGenDAO().delete(desGen);
		
		return desGen;
	}
	// <--- ABM DesGen

	// ---> ABM DesEsp
	public DesEsp createDesEsp(DesEsp desEsp) throws Exception {
		// Validaciones de negocio
		if (!desEsp.validateCreate()) {
			return desEsp;
		}

		GdeDAOFactory.getDesEspDAO().update(desEsp);

		return desEsp;
	}

	public DesEsp updateDesEsp(DesEsp desEsp) throws Exception {
		
		// Validaciones de negocio
		if (!desEsp.validateUpdate()) {
			return desEsp;
		}

		GdeDAOFactory.getDesGenDAO().update(desEsp);
		
		return desEsp;
	}

	public DesEsp deleteDesEsp(DesEsp desEsp) {
		
		// Validaciones de negocio
		if (!desEsp.validateDelete()) {
			return desEsp;
		}
		
		GdeDAOFactory.getDesGenDAO().delete(desEsp);
		
		return desEsp;
	}
	
	// <--- ABM DesEsp
	
	
	//	 ---> ABM Plan
	public Plan createPlan(Plan plan) throws Exception {

		// Validaciones de negocio
		if (!plan.validateCreate()) {
			return plan;
		}

		GdeDAOFactory.getPlanDAO().update(plan);

		return plan;
	}
	
	public Plan updatePlan(Plan plan) throws Exception {
		
		// Validaciones de negocio
		if (!plan.validateUpdate()) {
			return plan;
		}

		GdeDAOFactory.getPlanDAO().update(plan);
		
		return plan;
	}
	
	public Plan deletePlan(Plan plan) throws Exception {
	
		// Validaciones de negocio
		if (!plan.validateDelete()) {
			return plan;
		}
		
		GdeDAOFactory.getPlanDAO().delete(plan);
		
		return plan;
	}
	// <--- ABM Plan
	
	// ---> ABM Procurador
	public Procurador createProcurador(Procurador procurador) throws Exception {

		// Validaciones de negocio
		if (!procurador.validateCreate()) {
			return procurador;
		}

		GdeDAOFactory.getProcuradorDAO().update(procurador);

		return procurador;
	}
	
	public Procurador updateProcurador(Procurador procurador) throws Exception {
		
		// Validaciones de negocio
		if (!procurador.validateUpdate()) {
			return procurador;
		}

		GdeDAOFactory.getProcuradorDAO().update(procurador);
		
		return procurador;
	}
	
	public Procurador deleteProcurador(Procurador procurador) throws Exception {
	
		// Validaciones de negocio
		if (!procurador.validateDelete()) {
			return procurador;
		}
		
		GdeDAOFactory.getProRecDAO().delete(procurador);
		GdeDAOFactory.getProcuradorDAO().delete(procurador);
		
		return procurador;
	}
	// <--- ABM Procurador
	
	// ---> ABM Evento
	public Evento createEvento(Evento evento) throws Exception {

		// Validaciones de negocio
		if (!evento.validateCreate()) {
			return evento;
		}

		GdeDAOFactory.getEventoDAO().update(evento);

		return evento;
	}

	public Evento updateEvento(Evento evento) throws Exception {

		// Validaciones de negocio
		if (!evento.validateUpdate()) {
			return evento;
		}

		GdeDAOFactory.getEventoDAO().update(evento);

		return evento;
	}
	
	public Evento deleteEvento(Evento evento) throws Exception {

		// Validaciones de negocio
		if (!evento.validateDelete()) {
			return evento;
		}

		GdeDAOFactory.getEventoDAO().delete(evento);

		return evento;
	}

	// <--- ABM Evento
	
	// ---> ABM Mandatario
	public Mandatario createMandatario(Mandatario mandatario) throws Exception {

		// Validaciones de negocio
		if (!mandatario.validateCreate()) {
			return mandatario;
		}

		GdeDAOFactory.getMandatarioDAO().update(mandatario);

		return mandatario;
	}
	
	public Mandatario updateMandatario(Mandatario mandatario) throws Exception {
		
		// Validaciones de negocio
		if (!mandatario.validateUpdate()) {
			return mandatario;
		}

		GdeDAOFactory.getMandatarioDAO().update(mandatario);
		
		return mandatario;
	}
	
	public Mandatario deleteMandatario(Mandatario mandatario) throws Exception {
	
		// Validaciones de negocio
		if (!mandatario.validateDelete()) {
			return mandatario;
		}
		
		GdeDAOFactory.getMandatarioDAO().delete(mandatario);
		
		return mandatario;
	}
	// <--- ABM Mandatario

	// ---> ABM PerCob
	public PerCob createPerCob(PerCob perCob) throws Exception {

		// Validaciones de negocio
		if (!perCob.validateCreate()) {
			return perCob;
		}

		GdeDAOFactory.getPerCobDAO().update(perCob);

		return perCob;
	}
	
	public PerCob updatePerCob(PerCob perCob) throws Exception {
		
		// Validaciones de negocio
		if (!perCob.validateUpdate()) {
			return perCob;
		}

		GdeDAOFactory.getPerCobDAO().update(perCob);
		
		return perCob;
	}
	
	public PerCob deletePerCob(PerCob perCob) throws Exception {
	
		// Validaciones de negocio
		if (!perCob.validateDelete()) {
			return perCob;
		}
		
		GdeDAOFactory.getPerCobDAO().delete(perCob);
		
		return perCob;
	}
	// <--- ABM PerCob

	// ---> ABM AgeRet
	public AgeRet createAgeRet(AgeRet ageRet) throws Exception {

		// Validaciones de negocio
		if (!ageRet.validateCreate()) {
			return ageRet;
		}

		GdeDAOFactory.getAgeRetDAO().update(ageRet);

		return ageRet;
	}
	
	public AgeRet updateAgeRet(AgeRet ageRet) throws Exception {
		
		// Validaciones de negocio
		if (!ageRet.validateUpdate()) {
			return ageRet;
		}

		GdeDAOFactory.getAgeRetDAO().update(ageRet);
		
		return ageRet;
	}
	
	public AgeRet deleteAgeRet(AgeRet ageRet) throws Exception {
	
		// Validaciones de negocio
		if (!ageRet.validateDelete()) {
			return ageRet;
		}
		
		GdeDAOFactory.getAgeRetDAO().delete(ageRet);
		
		return ageRet;
	}
	// <--- ABM AgeRet

	// ---> ABM TipoMulta
	public TipoMulta createTipoMulta(TipoMulta tipoMulta) throws Exception {

		// Validaciones de negocio
		if (!tipoMulta.validateCreate()) {
			return tipoMulta;
		}

		GdeDAOFactory.getTipoMultaDAO().update(tipoMulta);

		return tipoMulta;
	}
	
	public TipoMulta updateTipoMulta(TipoMulta tipoMulta) throws Exception {
		
		// Validaciones de negocio
		if (!tipoMulta.validateUpdate()) {
			return tipoMulta;
		}

		GdeDAOFactory.getTipoMultaDAO().update(tipoMulta);
		
		return tipoMulta;
	}
	
	public TipoMulta deleteTipoMulta(TipoMulta tipoMulta) throws Exception {
	
		// Validaciones de negocio
		if (!tipoMulta.validateDelete()) {
			return tipoMulta;
		}
		
		GdeDAOFactory.getTipoMultaDAO().delete(tipoMulta);
		
		return tipoMulta;
	}
	// <--- ABM TipoMulta


	// ---> ABM IndiceCompensacion
	public IndiceCompensacion createIndiceCompensacion(IndiceCompensacion indiceCompensacion) throws Exception {

		// Validaciones de negocio
		if (!indiceCompensacion.validateCreate()) {
			return indiceCompensacion;
		}

		GdeDAOFactory.getIndiceCompensacionDAO().update(indiceCompensacion);

		return indiceCompensacion;
	}
	
	public IndiceCompensacion updateIndiceCompensacion(IndiceCompensacion indiceCompensacion) throws Exception {
		
		// Validaciones de negocio
		if (!indiceCompensacion.validateUpdate()) {
			return indiceCompensacion;
		}

		GdeDAOFactory.getIndiceCompensacionDAO().update(indiceCompensacion);
		
		return indiceCompensacion;
	}
	
	public IndiceCompensacion deleteIndiceCompensacion(IndiceCompensacion indiceCompensacion) throws Exception {
	
		// Validaciones de negocio
		if (!indiceCompensacion.validateDelete()) {
			return indiceCompensacion;
		}
		
		GdeDAOFactory.getIndiceCompensacionDAO().delete(indiceCompensacion);
		
		return indiceCompensacion;
	}
	// <--- ABM IndiceCompensacion

	// ---> ABM TipoPago
	public TipoPago createTipoPago(TipoPago tipoPago) throws Exception {

		// Validaciones de negocio
		if (!tipoPago.validateCreate()) {
			return tipoPago;
		}

		GdeDAOFactory.getTipoPagoDAO().update(tipoPago);

		return tipoPago;
	}
	
	public TipoPago updateTipoPago(TipoPago tipoPago) throws Exception {
		
		// Validaciones de negocio
		if (!tipoPago.validateUpdate()) {
			return tipoPago;
		}

		GdeDAOFactory.getTipoPagoDAO().update(tipoPago);
		
		return tipoPago;
	}
	
	public TipoPago deleteTipoPago(TipoPago tipoPago) throws Exception {
	
		// Validaciones de negocio
		if (!tipoPago.validateDelete()) {
			return tipoPago;
		}
		
		GdeDAOFactory.getTipoPagoDAO().delete(tipoPago);
		
		return tipoPago;
	}
	// <--- ABM TipoPago
	
}
