package similarityMetric;

import java.sql.*;
import java.util.ArrayList;

public class ProbaseAccess {
	private String url = "jdbc:mysql://10.15.62.29:3306/probase?useUnicode=true&characterEncoding=utf8";
	private String user = "root";
	private String psw = "123";
	private String classForName = "com.mysql.jdbc.Driver";
	
	public void initializeParameter(String url, String user, String psw, String classForName){
		this.url = url;
		this.user = user;
		this.psw = psw;
		this.classForName = classForName;
	}
	
	public  Connection getConnectionMySql() {
		Connection conn = null;
		try {
			Class.forName(classForName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn =  DriverManager.getConnection(url, user, psw);
			conn.setAutoCommit(false);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String[]> getConceptByEntity(Connection conn, String entity) throws SQLException{
		ArrayList<String[]> conceptList = new ArrayList<String[]>();
		if(entity.length() <= 2)
			return conceptList;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select  *  from isa_core where Entity like '%" + entity + "%'");
		//Concept, entity, frequency, ConceptFrequency, EntityFrequency
	    while (rs.next())
	    {
	    	String[] concept = new String[5];
	    	concept[0] = rs.getString("Concept");
	    	concept[1] = rs.getString("entity");
	    	concept[2] = rs.getString("frequency");
	    	concept[3] = rs.getString("ConceptFrequency");
	    	concept[4] = rs.getString("EntityFrequency");
	        conceptList.add(concept);
	    }
	    rs.close();    
		return conceptList;
	}
}


