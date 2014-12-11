package org.bigdata.handlerequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

public class RequestHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) {

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.addHeader("Access-Control-Allow-Origin", "*");
		// the user indicates what operation he wants to perform
		String uid = req.getParameter("uid");
		int numBusinesses;
		try {
			numBusinesses = Integer.parseInt(req.getParameter("numBusinesses"));
		} catch (Exception e) {
			System.out.println("Number of businesses must be a valid integer");
			return;
		}
		RecoLister recoLister = new RecoLister();
		JSONArray recoList = recoLister.getRecos(uid, numBusinesses);
		resp.getWriter().println(recoList.toString());
	}
}
