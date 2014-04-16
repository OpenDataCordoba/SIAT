//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.grs;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.UserContext;
import coop.tecso.grs.Grs;
import coop.tecso.grs.page.GrsPageContext;
import coop.tecso.grs.page.Page;

public class GrsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig cfg) throws ServletException {
		super.init(cfg);
	}

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		request(arg0, arg1);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		request(arg0, arg1);
	}

	@Override
	protected void doPut(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		request(arg0, arg1);
	}

	@Override
	protected void doDelete(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		request(arg0, arg1);
	}

	@SuppressWarnings("unchecked")
	protected void request(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			// init Siat user info
			UserContext uc = (UserContext) req.getSession().getAttribute("userSession");
			if (uc == null)
				return;
			
			uc.setIpRequest(req.getRemoteAddr());
			DemodaUtil.setCurrentUserContext(uc);
			
			// init Grs stuff
			GrsPageContext ctx = new GrsPageContext();
			ctx.parameters = req.getParameterMap();
			ctx.method = req.getMethod();
			ctx.uri = req.getRequestURI();
			ctx.writer = res.getWriter();
			ctx.contextPath = req.getContextPath();
			ctx.userName = uc.getUserName();
			
			parseUri(ctx);
			Page page = new Page(ctx);
			
			req.setAttribute("GrsPage", page);
			getServletContext().getRequestDispatcher(Grs.GrsPageTemplate).forward(req, res);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}		
		// request.getContextPath());  // /pagekit
		// request.getQueryString() ); // pk=init
		// request.getRequestURI() );  // /pagekit/a/PersonSeach.pk
		// request.getServletPath() ); // /a/PersonSeach.pk
		// request.getScheme() );      // http
	}
	
	// matchea algo como esto: /*/grs/{process}/{id}/{action}
	// process default es "" 
	// idrun default es 0 
	// action default es "get" 
	static void parseUri(GrsPageContext ctx) {
		String[] tk = ctx.uri.split("/");
		int i = 0;
		
		while (!"grs".equals(tk[i++]));

		ctx.process = "";
		if (i < tk.length) 
			ctx.process = tk[i++].trim();
	
		ctx.id = 0;
		if (i < tk.length) 
			try { ctx.id = Long.parseLong(tk[i++]); } catch (Exception e) {}

		ctx.function = "get";
		if (i < tk.length) 
			ctx.function = tk[i++].trim();
	}
	
}
