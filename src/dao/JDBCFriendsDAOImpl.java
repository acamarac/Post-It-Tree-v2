package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Friend;


public class JDBCFriendsDAOImpl implements FriendsDAO{

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCFriendsDAOImpl.class.getName());
	
	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<Integer> getAllByUser(long idu) {
		if (conn == null) return null;

		ArrayList<Integer> friends = new ArrayList<Integer>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM Friends WHERE idu1="+idu+" OR idu2="+idu);
			}
			while ( rs.next() ) {
				if(rs.getInt("idu1")==idu) {
					friends.add(rs.getInt("idu2"));
				} else {
					friends.add(rs.getInt("idu1"));
				}
				
				logger.info("fetching friends of user: "+idu);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friends;
	}
	
	@Override
	public Friend getFriendship(long idu1, long idu2) {
		if (conn == null) return null;

		Friend friendship = null;
		
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM Friends WHERE idu1="+idu1+" AND idu2="+idu2);
			}
			if (!rs.next()) return null; 
			friendship  = new Friend();	 
			friendship.setIdu1(rs.getInt("idu1"));
			friendship.setIdu2(rs.getInt("idu2"));

			logger.info("fetching friendship between: "+idu1 + " and " + idu2);


		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friendship;
	}
	
	@Override
	public boolean add(long idu1, long idu2) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Friends (idu1,idu2) VALUES('"+
									idu1+"','"+
									idu2+"')");
						
				logger.info("creating Friendship between:("+idu1+" "+idu2);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean delete(long idu1, long idu2) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Friends WHERE idu1 ="+idu1+" AND idu2="+idu2);
				logger.info("deleting Relationship between: "+idu1+" and "+idu2);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean deleteAllByUser(long idu1) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Friends WHERE idu1 ="+idu1+" OR idu2="+idu1);
				logger.info("deleting all Relationship of: "+idu1);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean areFriends(long idu1, long idu2) {
		if (conn == null) return false;
		
		boolean friends = false;

		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM Friends WHERE idu1="+idu1+" AND idu2="+idu2);
			}
			if ( rs.next() ) {
				friends = true;
				
				logger.info("check friendship between: "+idu1 + " " +idu2);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friends;
	}
	
}
