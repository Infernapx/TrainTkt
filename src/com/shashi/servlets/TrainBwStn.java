package com.shashi.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
@WebServlet("/trainbwstn")
public class TrainBwStn extends HttpServlet {
	private TrainService trainService = new TrainServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);
		try {
			String fromStation = req.getParameter("fromstation");
			String toStation = req.getParameter("tostation");
			List<TrainBean> trains = trainService.getTrainsBetweenStations(fromStation, toStation);
			RequestDispatcher rd = req.getRequestDispatcher("TrainBwStn.html");
			rd.include(req, res);
			if (trains != null && !trains.isEmpty()) {
				pw.println("<div class='tab'>"
						+ "<h3 style='font-family:Outfit,sans-serif; font-size:19px; font-weight:700; color:#0f172a; margin-bottom:14px;'>"
						+ "&#128646; Trains Between <strong>" + fromStation + "</strong> and <strong>" + toStation + "</strong></h3>");
				pw.println("<table><tr><th>Train Name</th><th>Train No</th>"
						+ "<th>From Station</th><th>To Station</th><th>Time</th><th>Available Seats</th><th>Fare (INR)</th><th>Action</th></tr>");
				for (TrainBean train : trains) {
					int hr = (int) (Math.random() * 24);
					int min = (int) (Math.random() * 60);
					String time = (hr < 10 ? ("0" + hr) : hr) + ":" + ((min < 10) ? "0" + min : min);

					pw.println("<tr><td>" + train.getTr_name() + "</td><td>" + train.getTr_no() + "</td>"
							+ "<td>" + train.getFrom_stn() + "</td><td>" + train.getTo_stn() + "</td><td>"
							+ time + "</td><td><span style='background:#ecfdf5; color:#065f46; font-weight:700; padding:3px 8px; border-radius:6px;'>"
							+ train.getSeats() + "</span></td><td><strong>&#8377; " + train.getFare()
							+ "</strong></td><td><a href='booktrainbyref?trainNo=" + train.getTr_no() + "&fromStn="
							+ train.getFrom_stn() + "&toStn=" + train.getTo_stn()
							+ "' class='btn-book' style='padding:6px 14px; font-size:12.5px; text-decoration:none;'>Book Now</a></td></tr>");
				}
				pw.println("</table></div>");
			} else {
				pw.println("<div style='max-width:900px; margin: 12px auto; width:100%;'>"
						+ "<div class='alert-card-danger'>"
						+ "  <div class='alert-card-icon'>&#9888;</div>"
						+ "  <div style='flex:1;'>"
						+ "    <div class='alert-card-title'>No Direct Trains Found</div>"
						+ "    <div class='alert-card-desc'>There are no direct trains scheduled between <strong>"
						+ fromStation + "</strong> and <strong>" + toStation
						+ "</strong>. Try searching major junction stations like HOWRAH, DELHI, PATNA, or MUMBAI.</div>"
						+ "  </div>"
						+ "</div>"
						+ "</div>");
			}
		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}

	}
}
