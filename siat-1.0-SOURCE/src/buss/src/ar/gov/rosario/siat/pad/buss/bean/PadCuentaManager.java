//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;



/**
 * Manejador del m&oacute;dulo Pad y submodulo Cuenta
 * 
 * @author tecso
 *
 */
public class PadCuentaManager {

	//private static Logger log = Logger.getLogger(PadCuentaManager.class);
	
	private static final PadCuentaManager INSTANCE = new PadCuentaManager();
	
	/**
	 * Constructor privado
	 */
	private PadCuentaManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static PadCuentaManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Cuenta
	
	public Cuenta createCuenta(Cuenta cuenta) throws Exception {
		
		
		// Si la cuenta no tiene setada la lista de RecAtrCueV, setea
		if(ListUtil.isNullOrEmpty(cuenta.getListRecAtrCueV())){
			if (cuenta.getRecurso() != null){
				List<RecAtrCue> listRecAtrCueVigentes = cuenta.getRecurso().getListRecAtrCueVigentes(new Date());

				if (cuenta.getListRecAtrCueV() == null)
            		cuenta.setListRecAtrCueV(new ArrayList<RecAtrCueV>());
				
				for (RecAtrCue recAtrCue:listRecAtrCueVigentes){
					RecAtrCueV recAtrCueV = new RecAtrCueV();
	            	recAtrCueV.setCuenta(cuenta);
	            	recAtrCueV.setRecAtrCue(recAtrCue);
	            	recAtrCueV.setFechaDesde(new Date());
	            	recAtrCueV.setValor(recAtrCue.getValorDefecto());
	            	cuenta.getListRecAtrCueV().add(recAtrCueV);
				}
			}
		}
		
		// Validaciones de negocio
		if (!cuenta.validateCreate()) {
			return cuenta;
		}
		
		PadDAOFactory.getCuentaDAO().update(cuenta);
		
		// Guardamos cada atributo valorizado
		if (!ListUtil.isNullOrEmpty(cuenta.getListRecAtrCueV())) {
			for (RecAtrCueV recAtrCueV:cuenta.getListRecAtrCueV()){
				if (!StringUtil.isNullOrEmpty(recAtrCueV.getValor()))
					PadDAOFactory.getRecAtrCueVDAO().update(recAtrCueV);
			}
		}
		
		//  instancia CuentaAtrVal, seta valor unico (porque es alta)
		//  seta objimp padre
		//  cuenta.createCuentaAtrVal(cuentaAtrVal)
		
		return cuenta;
	}

	
	public Cuenta updateCuenta(Cuenta cuenta) throws Exception {
		
		// Validaciones de negocio
		if (!cuenta.validateUpdate()) {
			return cuenta;
		}

		PadDAOFactory.getCuentaDAO().update(cuenta);
		
		return cuenta;
	}
	
	public Cuenta deleteCuenta(Cuenta cuenta) throws Exception {
	
		// Validaciones de negocio
		if (!cuenta.validateDelete()) {
			return cuenta;
		}
		
		PadDAOFactory.getCuentaDAO().delete(cuenta);
		
		return cuenta;
	}
	// <--- ABM Cuenta
	
	// ---> ABM EstCue
	public EstCue createEstCue(EstCue estCue) throws Exception {

		// Validaciones de negocio
		if (!estCue.validateCreate()) {
			return estCue;
		}

		PadDAOFactory.getEstCueDAO().update(estCue);

		return estCue;
	}
	
	public EstCue updateEstCue(EstCue estCue) throws Exception {
		
		// Validaciones de negocio
		if (!estCue.validateUpdate()) {
			return estCue;
		}

		PadDAOFactory.getEstCueDAO().update(estCue);
		
		return estCue;
	}
	
	public EstCue deleteEstCue(EstCue estCue) throws Exception {
	
		// Validaciones de negocio
		if (!estCue.validateDelete()) {
			return estCue;
		}
		
		PadDAOFactory.getEstCueDAO().delete(estCue);
		
		return estCue;
	}
	// <--- ABM EstCue
	
	
	// ---> ABM CueExcSel
	public CueExcSel createCueExcSel(CueExcSel cueExcSel) throws Exception {

		// Validaciones de negocio
		if (!cueExcSel.validateCreate()) {
			return cueExcSel;
		}
		
		PadDAOFactory.getCueExcSelDAO().update(cueExcSel);
		
		return cueExcSel;
	}
	// <--- ABM CueExcSel
	
	
	
	public BroCue updateBroCue(BroCue broCue) throws Exception {
		
		PadDAOFactory.getBroCueDAO().update(broCue);
		
		return broCue;
	}
}
