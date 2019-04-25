package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.ProfileImage;

public class JDBCProfileImageDAOImpl implements ProfileImageDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCProfileImageDAOImpl.class.getName());
	
	@Override
	public ProfileImage get(long idi) {
		if (conn == null) return null;
		
		ProfileImage pImg = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ProfileImage WHERE idi ="+idi);			 
			if (!rs.next()) return null; 
			pImg  = new ProfileImage();
			
			pImg.setIdi(rs.getInt("idi"));
			pImg.setUrlimage(rs.getString("urlimage"));
		
			logger.info("fetching ProfileImage by idi: "+idi+" -> "+pImg.getIdi()+" "+pImg.getUrlimage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pImg;
	}
	
	@Override
	public List<ProfileImage> getAll() {
		
		if (conn == null) return null;
		
		ArrayList<ProfileImage> profileImageList = new ArrayList<ProfileImage>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM ProfileImage");
			}
			while ( rs.next() ) {
				ProfileImage profileImage = new ProfileImage();
				profileImage.setIdi(rs.getInt("idi"));
				profileImage.setUrlimage(rs.getString("urlimage"));
				
				profileImageList.add(profileImage);
				logger.info("fetching users: "+profileImage.getIdi()+" "+profileImage.getUrlimage());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return profileImageList;
	}
	
	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
}
