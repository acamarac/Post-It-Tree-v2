package dao;

import java.sql.Connection;
import java.util.List;

import model.Note;
import model.NoteVersion;

public interface VersionsDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets a noteVersion from the DB using idn adn timestamp
	 * 
	 * @param idn
	 *            Note Identifier.
	 *            
	 * @param timestamp
	 * 			 String timestamp
	 * 
	 * @return Note object with that idn and timestamp
	 */
	public NoteVersion get(long idn, String timestamp);
	
	/**
	 *Gets all the versions of this note
	 * 
	 * @param idn
	 *            Note identifier
	 * 
	 * @return List of all the versions of this note
	 */
	public List<NoteVersion> getAllByIdn(long idn);
	
	/**
	 * Adds a NoteVersion to the database.
	 * 
	 * @param noteVersion
	 *            NoteVersion object with the details of the version
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean add(NoteVersion noteVersion);
	
	/**
	 * Deletes a noteVersion from the database.
	 * 
	 * @param idn
	 *            Note identifier.
	 *            
	 * @param timestamp
	 *            Time that identifies the version
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean delete(long idn, String timestamp);
	
	/**
	 * Deletes all noteVersion with that idn from the database
	 * 
	 * @param idn
	 *            Note identifier.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean delete(long idn);
	
	
}
