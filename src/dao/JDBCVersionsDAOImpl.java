package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Note;
import model.NoteVersion;

public class JDBCVersionsDAOImpl implements VersionsDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCVersionsDAOImpl.class.getName());

	@Override
	public NoteVersion get(long idn, String timestamp) {
		if (conn == null) return null;

		NoteVersion noteVersion = null;		

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Versions WHERE idn ="+idn+ " AND timestamp=" + timestamp);			 
			if (!rs.next()) return null; 
			noteVersion  = new NoteVersion();	 
			noteVersion.setIdn(rs.getInt("idn"));
			noteVersion.setTimestamp(rs.getString("timestamp"));
			noteVersion.setTitle(rs.getString("title"));
			noteVersion.setContent(rs.getString("content"));
			noteVersion.setColor(rs.getInt("color"));

			logger.info("fetching Note by idn and timestamp: "+idn+" -> "+noteVersion.getIdn()+" "+noteVersion.getTitle()+" "+noteVersion.getContent()+" "+noteVersion.getColor());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return noteVersion;
	}
	
	@Override
	public List<NoteVersion> getAllByIdn(long idn) {
		
		if (conn == null) return null;
		
		ArrayList<NoteVersion> noteVersionList = new ArrayList<NoteVersion>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Versions WHERE idn="+idn);

			while ( rs.next() ) {
				NoteVersion noteVersion = new NoteVersion();
				noteVersion.setIdn(rs.getInt("idn"));
				noteVersion.setTimestamp(rs.getString("timestamp"));
				noteVersion.setTitle(rs.getString("title"));
				noteVersion.setContent(rs.getString("content"));
				noteVersion.setColor(rs.getInt("color"));
							
				noteVersionList.add(noteVersion);
				logger.info("fetching all noteVersion by idn: "+noteVersion.getIdn()+"-> "+noteVersion.getTimestamp()+" "+noteVersion.getTitle()
				+" "+noteVersion.getContent()+" "+noteVersion.getColor());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noteVersionList;
		
	}
	
	@Override
	public boolean add(NoteVersion noteVersion) {
		
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Versions (idn,timestamp,title,content,color) VALUES('"+
									noteVersion.getIdn()+"','"+
									noteVersion.getTimestamp()+"','"+
									noteVersion.getTitle()+"','"+
									noteVersion.getContent()+"','"+
									noteVersion.getColor()+"')");
						
				logger.info("creating NoteVersion:("+noteVersion.getIdn()+" "+noteVersion.getTimestamp()+" "+noteVersion.getTitle()+" "+noteVersion.getContent()+" "+noteVersion.getColor());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
		
	}
	
	@Override
	public boolean delete(long idn, String timestamp) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Versions WHERE idn ="+idn+" AND timestamp="+timestamp);
				logger.info("deleting NoteVersion: "+idn+" , timestamp="+timestamp);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean delete(long idn) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Versions WHERE idn="+idn);
				logger.info("deleting NoteVersions: idn="+idn);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	
	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}
	
	
}
