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
@WebServlet("/view")
public class UserViewLinkGet extends HttpServlet {
	TrainService trainService = new TrainServiceImpl();

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);
		try {
			String trainNo = req.getParameter("trainNo");
			TrainBean train = trainService.getTrainById(trainNo);
			if (train != null) {
				RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
				rd.include(req, res);
				pw.println("<div style='max-width:900px; margin: 12px auto; width:100%;'>"
						+ "<div class='train-result-card'>"
						+ "  <div class='train-result-header'>"
						+ "    <div class='train-identity'>"
						+ "      <span class='train-number-badge'>#" + train.getTr_no() + "</span>"
						+ "      <h3 class='train-name'>" + train.getTr_name() + "</h3>"
						+ "    </div>"
						+ "    <span class='status-badge-active'>&#9679; Active &amp; Scheduled</span>"
						+ "  </div>"
						+ "  <div class='train-route-visual'>"
						+ "    <div class='route-point'>"
						+ "      <span class='route-city'>" + train.getFrom_stn() + "</span>"
						+ "      <span class='route-label'>Departure Station</span>"
						+ "    </div>"
						+ "    <div class='route-path'>"
						+ "      <div class='path-line'></div>"
						+ "      <span class='path-icon'>&#128646;</span>"
						+ "    </div>"
						+ "    <div class='route-point' style='text-align: right;'>"
						+ "      <span class='route-city'>" + train.getTo_stn() + "</span>"
						+ "      <span class='route-label'>Destination Station</span>"
						+ "    </div>"
						+ "  </div>"
						+ "  <div class='train-meta-grid'>"
						+ "    <div class='meta-item'>"
						+ "      <span class='meta-label'>Available Seats</span>"
						+ "      <span class='meta-val seats-val'>&#128186; " + train.getSeats() + " Seats</span>"
						+ "    </div>"
						+ "    <div class='meta-item'>"
						+ "      <span class='meta-label'>Base Fare</span>"
						+ "      <span class='meta-val fare-val'>&#8377; " + train.getFare() + "</span>"
						+ "    </div>"
						+ "    <div class='meta-item' style='display:flex; justify-content:flex-end;'>"
						+ "      <a href='booktrainbyref?trainNo=" + train.getTr_no() + "&fromStn=" + train.getFrom_stn()
						+ "&toStn=" + train.getTo_stn() + "' class='btn-book-primary'>Book Ticket Now &#10142;</a>"
						+ "    </div>"
						+ "  </div>"
						+ "</div>"
						+ "</div>");
			} else {
				RequestDispatcher rd = req.getRequestDispatcher("SearchTrains.html");
				rd.include(req, res);
				String missing = (trainNo != null) ? trainNo : (req.getParameter("trainnumber") != null ? req.getParameter("trainnumber") : "Unknown");
				pw.println("<div style='max-width:900px; margin: 12px auto; width:100%;'>"
						+ "<div class='alert-card-danger'>"
						+ "  <div class='alert-card-icon'>&#9888;</div>"
						+ "  <div style='flex:1;'>"
						+ "    <div class='alert-card-title'>Train Not Found</div>"
						+ "    <div class='alert-card-desc'>Train No. <strong>" + missing + "</strong> is currently not available. Please verify the train number or view all <a href='userviewtrainfwd' style='color:#1d4ed8; font-weight:700; text-decoration:underline;'>Running Trains</a>.</div>"
						+ "  </div>"
						+ "</div>"
						+ "</div>");
			}
		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}

	}

}
