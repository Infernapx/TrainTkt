package com.shashi.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shashi.beans.TrainBean;
import com.shashi.beans.TrainException;
import com.shashi.constant.UserRole;
import com.shashi.service.TrainService;
import com.shashi.service.impl.TrainServiceImpl;
import com.shashi.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/useravail")
public class UserAvailServlet extends HttpServlet {
	private TrainService trainService = new TrainServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		try {
			String trainNo = req.getParameter("trainno");
			TrainBean train = trainService.getTrainById(trainNo);
			RequestDispatcher rd = req.getRequestDispatcher("Availability.html");
			rd.include(req, res);
			if (train != null) {
				pw.println("<div class='tab'>"
						+ "<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; flex-wrap:wrap; gap:12px;'>"
						+ "  <h3 style='font-family:Outfit,sans-serif; font-size:20px; font-weight:700; color:#0f172a; margin:0;'>&#128186; "
						+ train.getTr_name() + " (" + train.getTr_no() + ")</h3>"
						+ "  <a href='booktrainbyref?trainNo=" + train.getTr_no() + "&fromStn=" + train.getFrom_stn()
						+ "&toStn=" + train.getTo_stn()
						+ "' class='btn-book' style='padding:8px 20px; font-size:14px; text-decoration:none;'>Book Now</a>"
						+ "</div>"
						+ "<div class='alert-box alert-success' style='margin-bottom:16px;'>"
						+ "  <span class='alert-icon'>&#9989;</span>"
						+ "  <div><strong>Seats Available:</strong> There are <strong>" + train.getSeats()
						+ " seats</strong> currently available for booking.</div>"
						+ "</div>"
						+ "<table>"
						+ "<tr><th>Train Number</th><td>" + train.getTr_no() + "</td></tr>"
						+ "<tr><th>Train Name</th><td>" + train.getTr_name() + "</td></tr>"
						+ "<tr><th>Route</th><td>" + train.getFrom_stn() + " &#10142; " + train.getTo_stn() + "</td></tr>"
						+ "<tr><th>Fare (INR)</th><td><strong>&#8377; " + train.getFare() + "</strong></td></tr>"
						+ "</table>"
						+ "</div>");
			} else {
				String searched = (trainNo != null && !trainNo.trim().isEmpty()) ? trainNo.trim() : "Unknown";
				pw.println("<div style='max-width:900px; margin: 12px auto; width:100%;'>"
						+ "<div class='alert-card-danger'>"
						+ "  <div class='alert-card-icon'>&#9888;</div>"
						+ "  <div style='flex:1;'>"
						+ "    <div class='alert-card-title'>Train Not Found</div>"
						+ "    <div class='alert-card-desc'>Train No. <strong>" + searched + "</strong> is currently not available. Please verify the train number or view all <a href='userviewtrainfwd' style='color:#1d4ed8; font-weight:700; text-decoration:underline;'>Running Trains</a>.</div>"
						+ "  </div>"
						+ "</div>"
						+ "</div>");
			}
		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}

	}

}
