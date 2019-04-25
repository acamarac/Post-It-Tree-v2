package dao;

import java.sql.Connection;
import java.util.List;

import model.ProfileImage;

public interface ProfileImageDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets the ProfileImage from the DB using idi.
	 * 
	 * @param idi
	 *            Image Identifier.
	 * 
	 * @return ProfileImage with that idi.
	 */
	public ProfileImage get(long idi);
	
	/**
	 * Gets all the ProfileImages from the DB.
	 * 
	 * @return List with all ProfileImage in the DBS.
	 */
	public List<ProfileImage> getAll();
	
}
