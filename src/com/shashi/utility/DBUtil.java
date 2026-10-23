package com.shashi.utility;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.shashi.beans.TrainException;
import com.shashi.constant.ResponseCode;

public class DBUtil {
	private static Connection con;
	private static ResourceBundle rb;

	static {
		try {
			rb = ResourceBundle.getBundle("application");
			Class.forName(rb.getString("driverName").trim());
			initConnection();
			initDatabaseTables();
		} catch (Exception e) {
			System.err.println("DBUtil initialization error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static synchronized void initConnection() {
		try {
			if (con == null || con.isClosed()) {
				String url = rb.getString("connectionString").trim();
				String user = rb.getString("username").trim();
				String pass = rb.getString("password").trim();
				con = DriverManager.getConnection(url, user, pass);
				System.out.println("Database Connected Successfully!");
			}
		} catch (SQLException e) {
			System.err.println("Failed to connect to database: " + e.getMessage());
		}
	}

	private static void initDatabaseTables() {
		if (con == null) return;
		try {
			DatabaseMetaData meta = con.getMetaData();
			ResultSet rs = meta.getTables(null, null, "CUSTOMER", null);
			boolean tablesExist = rs.next();
			rs.close();

			if (!tablesExist) {
				System.out.println("Initializing Database Schema and Dummy Data...");
				Statement stmt = con.createStatement();

				stmt.execute("CREATE TABLE IF NOT EXISTS CUSTOMER ("
						+ "MAILID VARCHAR(60) PRIMARY KEY, "
						+ "PWORD VARCHAR(50) NOT NULL, "
						+ "FNAME VARCHAR(50) NOT NULL, "
						+ "LNAME VARCHAR(50), "
						+ "ADDR VARCHAR(200), "
						+ "PHNO BIGINT NOT NULL)");

				stmt.execute("CREATE TABLE IF NOT EXISTS ADMIN ("
						+ "MAILID VARCHAR(60) PRIMARY KEY, "
						+ "PWORD VARCHAR(50) NOT NULL, "
						+ "FNAME VARCHAR(50) NOT NULL, "
						+ "LNAME VARCHAR(50), "
						+ "ADDR VARCHAR(200), "
						+ "PHNO BIGINT NOT NULL)");

				stmt.execute("CREATE TABLE IF NOT EXISTS TRAIN ("
						+ "TR_NO BIGINT PRIMARY KEY, "
						+ "TR_NAME VARCHAR(100) NOT NULL, "
						+ "FROM_STN VARCHAR(50) NOT NULL, "
						+ "TO_STN VARCHAR(50) NOT NULL, "
						+ "SEATS INT NOT NULL, "
						+ "FARE DOUBLE NOT NULL)");

				stmt.execute("CREATE TABLE IF NOT EXISTS HISTORY ("
						+ "TRANSID VARCHAR(40) PRIMARY KEY, "
						+ "MAILID VARCHAR(60), "
						+ "TR_NO BIGINT, "
						+ "\"DATE\" VARCHAR(50), "
						+ "FROM_STN VARCHAR(50) NOT NULL, "
						+ "TO_STN VARCHAR(50) NOT NULL, "
						+ "SEATS INT NOT NULL, "
						+ "AMOUNT DOUBLE NOT NULL)");

				// Seed default Admin
				stmt.execute("MERGE INTO ADMIN KEY(MAILID) VALUES('admin@demo.com','admin','System','Admin','Demo Address 123 colony', 9874561230)");

				// Seed default Customer
				stmt.execute("MERGE INTO CUSTOMER KEY(MAILID) VALUES('shashi@demo.com','shashi','Shashi','Raj','Kolkata, West Bengal', 9547452220)");

				// Seed initial Trains
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10001,'JODHPUR EXP','HOWRAH','JODHPUR', 152, 490.50)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10002,'YAMUNA EXP','GAYA','DELHI', 52, 550.50)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10003,'NILANCHAL EXP','GAYA','HOWRAH', 92, 451.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10004,'JAN SATABDI EXP','RANCHI','PATNA', 182, 550.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10005,'GANGE EXP','MUMBAI','KERALA', 12, 945.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10006,'GARIB RATH EXP','PATNA','DELHI', 15, 1450.75)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10007,'AJMER-SEALDAH EXP','SEALDAH','AJMER', 120, 1000.50)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10008,'MUMBAI MAIL','HOWRAH','MUMBAI', 100, 2150.75)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10010,'VANDE BHARAT EXP','NEW DELHI','VARANASI', 180, 1750.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10103,'MAHABODHI EXP','GAYA','DELHI', 73, 840.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(10104,'GARIB RATH EXPRESS','PATNA','MUMBAI', 26, 920.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(12367,'VIKRAMSHILA EXP','PATNA','ANAND VIHAR', 240, 620.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(12397,'MAHABODHI EXPRESS','GAYA','NEW DELHI', 180, 580.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(12301,'RAJDHANI EXP','HOWRAH','NEW DELHI', 120, 1850.00)");
				stmt.execute("MERGE INTO TRAIN KEY(TR_NO) VALUES(12004,'SHATABDI EXP','NEW DELHI','LUCKNOW', 150, 1120.00)");

				// Seed initial History
				stmt.execute("MERGE INTO HISTORY KEY(TRANSID) VALUES('BBC374-NSDF-4673','shashi@demo.com',10001,'02-Feb-2026', 'HOWRAH', 'JODHPUR', 2, 981.00)");
				stmt.execute("MERGE INTO HISTORY KEY(TRANSID) VALUES('BBC375-NSDF-4675','shashi@demo.com',10004,'12-Jan-2026', 'RANCHI', 'PATNA', 1, 550.00)");
				stmt.execute("MERGE INTO HISTORY KEY(TRANSID) VALUES('BBC373-NSDF-4674','shashi@demo.com',10006,'22-Jul-2026', 'PATNA', 'DELHI', 3, 4352.25)");

				stmt.close();
				System.out.println("Database Schema and Seed Data Initialized Successfully!");
			}
		} catch (Exception e) {
			System.err.println("Error initializing database tables: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static synchronized Connection getConnection() throws TrainException {
		try {
			if (con == null || con.isClosed()) {
				initConnection();
			}
		} catch (SQLException e) {
			throw new TrainException(ResponseCode.DATABASE_CONNECTION_FAILURE);
		}
		if (con == null)
			throw new TrainException(ResponseCode.DATABASE_CONNECTION_FAILURE);
		return con;
	}
}
