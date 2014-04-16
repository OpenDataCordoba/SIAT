//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.Date;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo 
 * 
 * @author tecso
 *
 */
public class BalDefinicionManager {
			
	private static final BalDefinicionManager INSTANCE = new BalDefinicionManager();
	
	/**
	 * Constructor privado
	 */
	private BalDefinicionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalDefinicionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Sistema
	public Sistema createSistema(Sistema sistema) throws Exception {

		// Validaciones de negocio
		if (!sistema.validateCreate()) {
			return sistema;
		}

		BalDAOFactory.getSistemaDAO().update(sistema);

		return sistema;
	}
	
	public Sistema updateSistema(Sistema sistema) throws Exception {
		
		// Validaciones de negocio
		if (!sistema.validateUpdate()) {
			return sistema;
		}

		BalDAOFactory.getSistemaDAO().update(sistema);
		
		return sistema;
	}
	
	public Sistema deleteSistema(Sistema sistema) throws Exception {
	
		// Validaciones de negocio
		if (!sistema.validateDelete()) {
			return sistema;
		}
		
		BalDAOFactory.getSistemaDAO().delete(sistema);			
				
		return sistema;
	}
	// <--- ABM Sistema

	// ---> ABM Sellado
	public Sellado createSellado(Sellado sellado) throws Exception {
		// Validaciones de negocio
		if (!sellado.validateCreate()) {
			return sellado;
		}

		BalDAOFactory.getSelladoDAO().update(sellado);

		return sellado;
	}

	public Sellado updateSellado(Sellado sellado) throws Exception {
		// Validaciones de negocio
		if (!sellado.validateUpdate()) {
			return sellado;
		}

		BalDAOFactory.getSelladoDAO().update(sellado);

		return sellado;
	}

	public Sellado deleteSellado(Sellado sellado) {
		// Validaciones de negocio
		if (!sellado.validateDelete()) {
			return sellado;
		}
		
		BalDAOFactory.getImpSelDAO().delete(sellado);
		BalDAOFactory.getAccionSelladoDAO().delete(sellado);
		BalDAOFactory.getParSelDAO().delete(sellado);
	
		return sellado;
	}
	// <--- ABM Sellado	
	
	/**
	 * Aplica el costo del sellado al monto pasado como parametro, si existe.
	 * <br>Si el sellado es un monto fijo, setea el importe del sellado con este valor.
	 * <br>Si el sellado es un porcentaje, seteal el importe del sellado aplicando el porcentaje (valor entre 0 y 1) al monto pasado como parametro. 
	 */
	static public Sellado aplicarSellado(long idRecurso, long idAccion,Date fecha,int cantPeriodos, double monto){
		Sellado sellado = BalDAOFactory.getSelladoDAO().getSellado(idRecurso, idAccion, fecha, cantPeriodos);
		if(sellado!=null){
			ImpSel impSel = BalDAOFactory.getImpSelDAO().getBySellado(sellado.getId(), fecha);
			if(impSel != null){
				if (impSel.getTipoSellado().getId() == TipoSellado.ID_TIPO_MONTO_FIJO)
					sellado.setImporteSellado(impSel.getCosto());
				else if(impSel.getTipoSellado().getId() == TipoSellado.ID_TIPO_PORCENTAJE)
					sellado.setImporteSellado( monto * impSel.getCosto() );// el costo representa el % (valor entre 0 y 1) a aplicar sobre el monto del recibo
			}	
		}
		return sellado;
		//TODO habría que agregar una validacion al abm sellado, que si es de tipo %, el valor del costo sea entre 0 y 1 - arobledo
	}

	// ---> ABM Ejercicio
	public Ejercicio createEjercicio(Ejercicio ejercicio) throws Exception {

		// Validaciones de negocio
		if (!ejercicio.validateCreate()) {
			return ejercicio;
		}

		BalDAOFactory.getEjercicioDAO().update(ejercicio);

		return ejercicio;
	}
	
	public Ejercicio updateEjercicio(Ejercicio ejercicio) throws Exception {
		
		// Validaciones de negocio
		if (!ejercicio.validateUpdate()) {
			return ejercicio;
		}

		BalDAOFactory.getEjercicioDAO().update(ejercicio);
		
		return ejercicio;
	}
	
	public Ejercicio deleteEjercicio(Ejercicio ejercicio) throws Exception {
	
		// Validaciones de negocio
		if (!ejercicio.validateDelete()) {
			return ejercicio;
		}
		
		BalDAOFactory.getEjercicioDAO().delete(ejercicio);
		
		return ejercicio;
	}		
	// <--- ABM ejercicio	
	
	
	// ---> ABM Partida
	public Partida createPartida(Partida partida) throws Exception {

		// Validaciones de negocio
		if (!partida.validateCreate()) {
			return partida;
		}

		BalDAOFactory.getPartidaDAO().update(partida);

		return partida;
	}
	
	public Partida updatePartida(Partida partida) throws Exception {
		
		// Validaciones de negocio
		if (!partida.validateUpdate()) {
			return partida;
		}

		BalDAOFactory.getPartidaDAO().update(partida);
		
		return partida;
	}
	
	public Partida deletePartida(Partida partida) throws Exception {
	
		// Validaciones de negocio
		if (!partida.validateDelete()) {
			return partida;
		}
		
		BalDAOFactory.getPartidaDAO().delete(partida);
		
		return partida;
	}
	// <--- ABM Partida
	
	// ---> ABM Partida Cuenta Bancaria
	public ParCueBan createParCueBan(ParCueBan parCueBan) throws Exception {

		// Validaciones de negocio
		if (!parCueBan.validateCreate()) {
			return parCueBan;
		}

		BalDAOFactory.getParCueBanDAO().update(parCueBan);

		return parCueBan;
	}
	
	public ParCueBan updateParCueBan(ParCueBan parCueBan) throws Exception {
		
		// Validaciones de negocio
		if (!parCueBan.validateUpdate()) {
			return parCueBan;
		}

		BalDAOFactory.getParCueBanDAO().update(parCueBan);
		
		return parCueBan;
	}
	
	public ParCueBan deleteParCueBan(ParCueBan parCueBan) throws Exception {
	
		// Validaciones de negocio
		if (!parCueBan.validateDelete()) {
			return parCueBan;
		}
		
		BalDAOFactory.getParCueBanDAO().delete(parCueBan);
		
		return parCueBan;
	}
	// <--- ABM Partida Cuenta Bancaria
	
	// ---> ABM Cuenta Bancaria
	public CuentaBanco createCuentaBanco(CuentaBanco cuentaBanco) throws Exception {

		// Validaciones de negocio
		if (!cuentaBanco.validateCreate()) {
			return cuentaBanco;
		}

		BalDAOFactory.getCuentaBancoDAO().update(cuentaBanco);

		return cuentaBanco;
	}
	
	public CuentaBanco updateCuentaBanco(CuentaBanco cuentaBanco) throws Exception {
		
		// Validaciones de negocio
		if (!cuentaBanco.validateUpdate()) {
			return cuentaBanco;
		}

		BalDAOFactory.getCuentaBancoDAO().update(cuentaBanco);
		
		return cuentaBanco;
	}
	
	public CuentaBanco deleteCuentaBanco(CuentaBanco cuentaBanco) throws Exception {
	
		// Validaciones de negocio
		if (!cuentaBanco.validateDelete()) {
			return cuentaBanco;
		}
		
		BalDAOFactory.getCuentaBancoDAO().delete(cuentaBanco);
		
		return cuentaBanco;
	}
	// ---> ABM Cuenta Bancaria
	
	// <--- ABM Tipo Cuenta Bancaria
	
	public TipCueBan createTipCueBan(TipCueBan tipCueBan) throws Exception {

		// Validaciones de negocio
		if (!tipCueBan.validateCreate()) {
			return tipCueBan;
		}

		BalDAOFactory.getTipCueBanDAO().update(tipCueBan);

		return tipCueBan;
	}
	
	
    public TipCueBan updateTipCueBan(TipCueBan tipCueBan) throws Exception {
		
		// Validaciones de negocio
		if (!tipCueBan.validateUpdate()) {
			return tipCueBan;
		}

		BalDAOFactory.getTipCueBanDAO().update(tipCueBan);
		
		return tipCueBan;
	}
	
	public TipCueBan deleteTipCueBan(TipCueBan tipCueBan) throws Exception {
	
		// Validaciones de negocio
		if (!tipCueBan.validateDelete()) {
			return tipCueBan;
		}
		
		BalDAOFactory.getTipCueBanDAO().delete(tipCueBan);
		
		return tipCueBan;
	}
	// <--- ABM Cuenta Bancaria
	
	// ---> ABM LeyParAcu
	public LeyParAcu createLeyParAcu(LeyParAcu leyParAcu) throws Exception {

		// Validaciones de negocio
		if (!leyParAcu.validateCreate()) {
			return leyParAcu;
		}

		BalDAOFactory.getLeyParAcuDAO().update(leyParAcu);

		return leyParAcu;
	}
	
	public LeyParAcu updateLeyParAcu(LeyParAcu leyParAcu) throws Exception {
		
		// Validaciones de negocio
		if (!leyParAcu.validateUpdate()) {
			return leyParAcu;
		}

		BalDAOFactory.getLeyParAcuDAO().update(leyParAcu);
		
		return leyParAcu;
	}
	
	public LeyParAcu deleteLeyParAcu(LeyParAcu leyParAcu) throws Exception {
	
		// Validaciones de negocio
		if (!leyParAcu.validateDelete()) {
			return leyParAcu;
		}
		
		BalDAOFactory.getLeyParAcuDAO().delete(leyParAcu);			
				
		return leyParAcu;
	}
	// <--- ABM LeyParAcu
	
	// ---> ABM TipoIndet
	public TipoIndet createTipoIndet(TipoIndet tipoIndet) throws Exception {

		// Validaciones de negocio
		if (!tipoIndet.validateCreate()) {
			return tipoIndet;
		}

		BalDAOFactory.getSistemaDAO().update(tipoIndet);

		return tipoIndet;
	}
	
	public TipoIndet updateTipoIndet(TipoIndet tipoIndet) throws Exception {
		
		// Validaciones de negocio
		if (!tipoIndet.validateUpdate()) {
			return tipoIndet;
		}

		BalDAOFactory.getSistemaDAO().update(tipoIndet);
		
		return tipoIndet;
	}
	
	public TipoIndet deleteTipoIndet(TipoIndet tipoIndet) throws Exception {
	
		// Validaciones de negocio
		if (!tipoIndet.validateDelete()) {
			return tipoIndet;
		}
		
		BalDAOFactory.getSistemaDAO().delete(tipoIndet);			
				
		return tipoIndet;
	}
	// <--- ABM TipoIndet
}
