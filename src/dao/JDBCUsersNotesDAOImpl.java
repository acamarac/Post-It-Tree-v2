package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.UsersNotes;



public class JDBCUsersNotesDAOImpl implements UsersNotesDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUsersNotesDAOImpl.class.getName());

	@Override
	public List<UsersNotes> getAll() {

		if (conn == null) return null;
						
		ArrayList<UsersNotes> usersNotesList = new ArrayList<UsersNotes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes");
						
			while ( rs.next() ) {
				UsersNotes usersNotes = new UsersNotes();
				usersNotes.setIdu(rs.getInt("idu"));
				usersNotes.setIdn(rs.getInt("idn"));
				usersNotes.setOwner(rs.getInt("owner"));
				usersNotes.setArchived(rs.getInt("archived"));
				usersNotes.setPinned(rs.getInt("pinned"));
				usersNotes.setTrash(rs.getInt("trash"));		
							
				usersNotesList.add(usersNotes);
				logger.info("fetching all usersNotes: "+usersNotes.getIdu()+" "+usersNotes.getIdn()+" "+usersNotes.getOwner()
							+" "+usersNotes.getArchived()+" "+usersNotes.getPinned()+" "+usersNotes.getTrash());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersNotesList;
	}

	@Override
	public List<UsersNotes> getAllByUser(long idu) {
		
		if (conn == null) return null;
						
		ArrayList<UsersNotes> usersNotesList = new ArrayList<UsersNotes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idu="+idu);

			while ( rs.next() ) {
				UsersNotes usersNotes = new UsersNotes();
				usersNotes.setIdu(rs.getInt("idu"));
				usersNotes.setIdn(rs.getInt("idn"));
				usersNotes.setOwner(rs.getInt("owner"));
				usersNotes.setArchived(rs.getInt("archived"));
				usersNotes.setPinned(rs.getInt("pinned"));
				usersNotes.setTrash(rs.getInt("trash"));
							
				usersNotesList.add(usersNotes);
				logger.info("fetching all usersNotes by idu: "+usersNotes.getIdu()+"->"+usersNotes.getIdn()+" "+usersNotes.getOwner()
				+" "+usersNotes.getArchived()+" "+usersNotes.getPinned()+" "+usersNotes.getTrash());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersNotesList;
	}
	
	@Override
	public List<UsersNotes> getAllInTrash(long idu) {
		
		if (conn == null) return null;
		
		ArrayList<UsersNotes> usersNotesList = new ArrayList<UsersNotes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idu="+idu + " AND trash=1");

			while ( rs.next() ) {
				UsersNotes usersNotes = new UsersNotes();
				usersNotes.setIdu(rs.getInt("idu"));
				usersNotes.setIdn(rs.getInt("idn"));
				usersNotes.setOwner(rs.getInt("owner"));
				usersNotes.setArchived(rs.getInt("archived"));
				usersNotes.setPinned(rs.getInt("pinned"));
				usersNotes.setTrash(rs.getInt("trash"));
							
				usersNotesList.add(usersNotes);
				logger.info("fetching all usersNotes by idu in trash: "+usersNotes.getIdu()+"->"+usersNotes.getIdn()+" "+usersNotes.getOwner()
				+" "+usersNotes.getArchived()+" "+usersNotes.getPinned()+" "+usersNotes.getTrash());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersNotesList;
		
	}
	
	@Override
	public List<UsersNotes> getAllByNote(long idn) {
		
		if (conn == null) return null;
						
		ArrayList<UsersNotes> usersNotesList = new ArrayList<UsersNotes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idn="+idn);

			while ( rs.next() ) {
				UsersNotes usersNotes = new UsersNotes();
				usersNotes.setIdu(rs.getInt("idu"));
				usersNotes.setIdn(rs.getInt("idn"));
				usersNotes.setOwner(rs.getInt("owner"));
				usersNotes.setArchived(rs.getInt("archived"));
				usersNotes.setPinned(rs.getInt("pinned"));
				usersNotes.setTrash(rs.getInt("trash"));
							
				usersNotesList.add(usersNotes);
				logger.info("fetching all usersNotes by idn: "+usersNotes.getIdn()+"-> "+usersNotes.getIdu()+" "+usersNotes.getOwner()
				+" "+usersNotes.getArchived()+" "+usersNotes.getPinned()+" "+usersNotes.getTrash());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersNotesList;
	}
	
	
	@Override
	public UsersNotes get(long idu,long idn) {
		if (conn == null) return null;
		
		UsersNotes usersNotes = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idu="+idu+" AND idn="+idn);			 
			if (!rs.next()) return null;
			usersNotes= new UsersNotes();
			usersNotes.setIdu(rs.getInt("idu"));
			usersNotes.setIdn(rs.getInt("idn"));
			usersNotes.setOwner(rs.getInt("owner"));
			usersNotes.setArchived(rs.getInt("archived"));
			usersNotes.setPinned(rs.getInt("pinned"));
			usersNotes.setTrash(rs.getInt("trash"));
			
			logger.info("fetching usersNotes by idu: "+usersNotes.getIdu()+"  and idn: "+usersNotes.getIdn()+" "+usersNotes.getOwner()
			+" "+usersNotes.getArchived()+" "+usersNotes.getPinned()+" "+usersNotes.getTrash());
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usersNotes;
	}
	
	

	@Override
	public boolean add(UsersNotes usersNotes) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO UsersNotes (idu,idn,owner,archived,pinned,trash) VALUES('"+
									usersNotes.getIdu()+"','"+
									usersNotes.getIdn()+"','"+
									usersNotes.getOwner()+"','"+
									usersNotes.getArchived()+"','"+
									usersNotes.getPinned()+"','"+
									usersNotes.getTrash()+"')");
						
				logger.info("creating UsersNotes:("+usersNotes.getIdu()+" "+usersNotes.getIdn()+" "+usersNotes.getOwner()+" "+usersNotes.getArchived()+" "+usersNotes.getPinned()+" "+usersNotes.getTrash());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean save(UsersNotes usersNotes) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("UPDATE UsersNotes SET owner='"+
									usersNotes.getOwner()+"' WHERE idu = "+usersNotes.getIdu()+" AND idn="+usersNotes.getIdn());
				
				
				stmt.executeUpdate("UPDATE UsersNotes SET owner='"+usersNotes.getOwner()
				+"', archived='"+usersNotes.getArchived()
				+"', pinned='"+usersNotes.getPinned()
				+"', trash='"+usersNotes.getTrash()
				+"' WHERE idu = "+usersNotes.getIdu()+" AND idn="+usersNotes.getIdn());
				
				logger.info("updating UsersNotes:("+usersNotes.getIdu()+" "+usersNotes.getIdn()+" "+usersNotes.getOwner()+" "+usersNotes.getArchived()+" "+usersNotes.getPinned()+" "+usersNotes.getTrash());
				
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long idu, long idn) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM UsersNotes WHERE idu ="+idu+" AND idn="+idn);
				logger.info("deleting UsersNotes: "+idu+" , idn="+idn);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean deleteIdn(long idn) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM UsersNotes WHERE idn="+idn);
				logger.info("deleting UsersNotes: idn="+idn);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean deleteAllTrash(long idu) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM UsersNotes WHERE idu="+idu +" AND trash=1");
				logger.info("deleting UsersNotes in trash");
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public long getOwner(long idn) {
		if (conn == null) return -1;

		long iduOwner = -1;		

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idn="+idn+" AND owner=1");			 
			if (!rs.next()) return -1;
			iduOwner = rs.getInt("idu");

			logger.info("fetching idu of user owner by idn: " +iduOwner);


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iduOwner;
	}

	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}

	
	
		

	
}
