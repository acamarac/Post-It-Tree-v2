package dao;

import java.sql.Connection;
import java.util.List;

import model.Friend;


public interface FriendsDAO {

	/**
	 * Sets the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets all the friends identifiers of that user from the database.
	 * 
	 * @return List of all the users identifiers friends of the user from the database.
	 * 
	 * @param idu of the user 
	 */
	public List<Integer> getAllByUser(long idu);
	
	/**
	 * Gets a Friend object fron the database
	 * 
	 * @return Friend instance of both users
	 * 
	 * @param idu1 of the user 1
	 * @param idu2 of the user 2
	 */
	public Friend getFriendship(long idu1, long idu2);
	
	/**
	 * Adds a new friendship to the database.
	 * 
	 * @param idu1
	 *            Id of the first user.
	 *            
	 * @param idu2
	 *			  Id of the second user.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean add(long idu1, long idu2);
	
	/**
	 * Deletes a relationship from the database.
	 * 
	 * @param idu1
	 *            Id of the first user.
	 *            
	 * @param idu2
	 * 			  Id of the second user.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean delete(long idu1, long idu2);
	
	/**
	 * Deletes all relationships of that user.
	 * 
	 * @param idu1
	 *            Id of the user.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean deleteAllByUser(long idu1);
	
	/**
	 * Check if two users are already friends.
	 * 
	 * @param idu1
	 *            Id of the first user.
	 *            
	 * @param idu2
	 * 			  Id of the second user.
	 * 
	 * @return True if they are friends and False if they are not.
	 */
	public boolean areFriends(long idu1, long idu2);
	
}
