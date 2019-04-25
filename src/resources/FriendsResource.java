package resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dao.*;
import model.*;
import resources.exceptions.*;


@Path("/friends")
public class FriendsResource {

	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;

	
	/**
	 * Obtain a list with all the friend of the user
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getFriendsJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		List<User> userFriends = new ArrayList<User>();

		List<Integer> iduFriendsList = friendsDao.getAllByUser(user.getIdu());

		for (int i=0; i<iduFriendsList.size(); i++) {
			User friend = userDao.get(iduFriendsList.get(i));
			friend.setIdu(-1);
			friend.setPassword("******");
			userFriends.add(friend);
		}
		
		return userFriends; 
	}
	
	
	/**
	 * Obtain the notes that the friend with the username in the Path shared with the user
	 * 
	 * @param username username of the friend
	 */
	@GET
	@Path("/sharedByFriend/{username: [a-zA-Z][a-zA-Z_0-9]*}")	  
	@Produces(MediaType.APPLICATION_JSON)
	public List<Note> getFriendSharedNotesJSON(@PathParam("username") String username,
			@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		FriendsDAO friendDao = new JDBCFriendsDAOImpl();
		friendDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		User friend = userDao.get(username);
		List <Note> friendSharedNotes = new ArrayList<Note>();
		
		int idu1, idu2;
		
		if (user != null) {
			
			if (friend.getIdu() < user.getIdu()) {
				idu1 = friend.getIdu();
				idu2 = user.getIdu();
			} else {
				idu1 = user.getIdu();
				idu2 = friend.getIdu();
			}
			
			if (friendDao.areFriends(idu1, idu2)) {
				
				List <UsersNotes> usersNotesList = usersNotesDao.getAllByUser(user.getIdu());
				
				for (int i=0; i<usersNotesList.size(); i++) {
					long iduOwner = usersNotesDao.getOwner(usersNotesList.get(i).getIdn());
					if (iduOwner == friend.getIdu()) {
						Note note = noteDao.get(usersNotesList.get(i).getIdn());
						friendSharedNotes.add(note);
					}
				}
				
				return friendSharedNotes;
			} else throw new BadRequestException("Friend ("+ username + ") is not found");
		}
		
		else throw new CustomNotFoundException("Friend ("+ username + ") is not found");		   
	}
	
	/**
	 * Obtain the notes that I own and I share with that user
	 * 
	 * @param username username of the friend
	 */
	@GET
	@Path("/sharedByMe/{username: [a-zA-Z][a-zA-Z_0-9]*}")	  
	@Produces(MediaType.APPLICATION_JSON)
	public List<Note> getFriendSharedByMeNotesJSON(@PathParam("username") String username,
			@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		FriendsDAO friendDao = new JDBCFriendsDAOImpl();
		friendDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		User friend = userDao.get(username);
		List <Note> friendSharedNotes = new ArrayList<Note>();
		
		int idu1, idu2;
		
		if (user != null) {
			
			if (friend.getIdu() < user.getIdu()) {
				idu1 = friend.getIdu();
				idu2 = user.getIdu();
			} else {
				idu1 = user.getIdu();
				idu2 = friend.getIdu();
			}
			
			if (friendDao.areFriends(idu1, idu2)) {
				
				List <UsersNotes> usersNotesList = usersNotesDao.getAllByUser(user.getIdu());
				
				for (int i=0; i<usersNotesList.size(); i++) {
					long iduOwner = usersNotesDao.getOwner(usersNotesList.get(i).getIdn());
					UsersNotes userNoteFriend = usersNotesDao.get(friend.getIdu(), usersNotesList.get(i).getIdn());
					if (iduOwner == user.getIdu() && userNoteFriend != null) {
						Note note = noteDao.get(usersNotesList.get(i).getIdn());
						friendSharedNotes.add(note);
					}
				}
				
				return friendSharedNotes;
			} else throw new BadRequestException("Friend ("+ username + ") is not found");
		}
		
		else throw new CustomNotFoundException("Friend ("+ username + ") is not found");		   
	}
	
	
	/**
	 * Creates a new friendship between the user and other user
	 * 
	 * @param newFriendship Friend object that we want to add to the database
	 */
	@POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response createFriendship(Friend newFriendship, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		FriendsDAO friendDao = new JDBCFriendsDAOImpl();
		friendDao.setConnection(conn);	  	 
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		User user1 = userDao.get(newFriendship.getIdu1());
		User user2 = userDao.get(newFriendship.getIdu2());

		if (user1==null || user2==null)
			throw new CustomBadRequestException("Errors in parameters");
		if(friendDao.areFriends(user1.getIdu(), user2.getIdu()))
			throw new CustomBadRequestException("Users are already friends");
		if(user1.getIdu() == user2.getIdu())
			throw new CustomBadRequestException("You can't add yourself as a friend");
		if (user1.getIdu() == user.getIdu() || user2.getIdu() == user.getIdu())
			friendDao.add(user1.getIdu(), user2.getIdu());
		else throw new CustomBadRequestException("Errors in parameters");
		
	    return Response.noContent().build();
	  }
	
	
	/**
	 * Deletes the friendship between the user and the user with that username
	 * 
	 * @param username of the friend 
	 */
	@DELETE
	  @Path("/{username: [a-zA-Z][a-zA-Z_0-9]*}")	  
	  public Response deleteFriendship(@PathParam("username") String username,
			  					  @Context HttpServletRequest request) {
		  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		FriendsDAO friendDao = new JDBCFriendsDAOImpl();
		friendDao.setConnection(conn);
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		long idu1, idu2;
		
		User friend = null;
		friend = userDao.get(username);
		
		if(friend == null)
			throw new CustomBadRequestException("Error in user or id");
		

		long iduFriend = friend.getIdu();
		
		if(user.getIdu() < iduFriend) {
			idu1 = user.getIdu();
			idu2 = iduFriend;
		} else {
			idu1 = iduFriend;
			idu2 = user.getIdu();
		}
		
		Friend friendship = friendDao.getFriendship(idu1, idu2);
		
		if ((friendship != null)
			&&((idu1 == user.getIdu() || idu2 == user.getIdu()))){
				List<UsersNotes> usersNotes = usersNotesDao.getAllByUser(iduFriend);
				for(int i=0; i<usersNotes.size(); i++) {
					if(user.getIdu() == usersNotesDao.getOwner(usersNotes.get(i).getIdn())) {
						usersNotesDao.delete(iduFriend, usersNotes.get(i).getIdn());
					}
				}
				List<UsersNotes> usersNotes2 = usersNotesDao.getAllByUser(user.getIdu());
				for(int i=0; i<usersNotes2.size(); i++) {
					if(iduFriend == usersNotesDao.getOwner(usersNotes2.get(i).getIdn())) {
						usersNotesDao.delete(user.getIdu(), usersNotes2.get(i).getIdn());
					}
				}
				friendDao.delete(idu1, idu2);
				return Response.noContent().build(); //204 no content 
		}
		else throw new CustomBadRequestException("Error in user or id");		
			
	  }

}
