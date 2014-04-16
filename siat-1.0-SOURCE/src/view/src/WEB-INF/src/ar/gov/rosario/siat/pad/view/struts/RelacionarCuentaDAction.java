//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class RelacionarCuentaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(RelacionarCuentaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA, act); 
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		CuentaAdapter cuentaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {

			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (act.equals(BaseConstants.ACT_RELACIONAR)) {
				stringServicio = "getCuentaAdapterForRelacionar(userSession)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForRelacionar(userSession, commonKey);
				
				actionForward = mapping.findForward(PadConstants.FWD_RELACIONAR_CUENTA_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables

			// Tiene errores no recuperables
			if (cuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.REL_NAME, cuentaAdapterVO);
			}

			// Seteo los valores de navegacion en el adapter
			cuentaAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + CuentaAdapter.REL_NAME + ": "+ cuentaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.REL_NAME, cuentaAdapterVO);

			// Subo el apdater al userSession
			userSession.put(CuentaAdapter.REL_NAME, cuentaAdapterVO);

			saveDemodaMessages(request, cuentaAdapterVO);

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.REL_NAME);
		}
	}


	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		return baseVolver(mapping, form, request, response, CuentaAdapter.REL_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CuentaAdapter.REL_NAME);
	}

	// Metodos relacionados de la lista de CuentaRel de la Cuenta
	public ActionForward verCuentaRel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTAREL);
	}

	public ActionForward modificarCuentaRel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTAREL);
	}
	
	public ActionForward agregarCuentaRel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTAREL);		
	}
	
	public ActionForward eliminarCuentaRel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTAREL);
	}
}
