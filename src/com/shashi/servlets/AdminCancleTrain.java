package com.shashi.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shashi.beans.TrainException;
import com.shashi.constant.ResponseCode;
import com.shashi.constant.UserRole;
import com.shashi.service.TrainService;
import com.shashi.service.impl.TrainServiceImpl;
import com.shashi.utility.TrainUtil;

@WebServlet("/admincancletrain")
public class AdminCancleTrain extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TrainService trainService = new TrainServiceImpl();

	/**
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);
		try {
			String trainNo = req.getParameter("trainno");
			String message = trainService.deleteTrainById(trainNo);
			if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(message)) {
				RequestDispatcher rd = req.getRequestDispatcher("CancleTrain.html");
				rd.include(req, res);
				pw.println("<div style='max-width:900px; margin: 12px auto; width:100%;'>"
						+ "<div class='alert-box alert-success'>"
						+ "  <span class='alert-icon'>&#9989;</span>"
						+ "  <div><strong>Train Deleted:</strong> Train No. <strong>" + trainNo + "</strong> has been permanently removed from the schedule.</div>"
						+ "</div>"
						+ "</div>");
			} else {
				RequestDispatcher rd = req.getRequestDispatcher("CancleTrain.html");
				rd.include(req, res);
				pw.println("<div style='max-width:900px; margin: 12px auto; width:100%;'>"
						+ "<div class='alert-card-danger'>"
						+ "  <div class='alert-card-icon'>&#9888;</div>"
						+ "  <div style='flex:1;'>"
						+ "    <div class='alert-card-title'>Train Not Found</div>"
						+ "    <div class='alert-card-desc'>Train No. <strong>" + (trainNo != null ? trainNo : "") + "</strong> could not be found or has already been removed.</div>"
						+ "  </div>"
						+ "</div>"
						+ "</div>");
			}
		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}

	}

}
