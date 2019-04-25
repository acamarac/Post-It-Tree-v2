package resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import model.*;
import dao.*;
import resources.exceptions.*;

@Path("/UsersNotes")
public class UsersNotesResource {

	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	
	 /**
	   * Return the information of all usersNotes that the user have access
	   * 
	   * @param idn
	   *            idn of the note.
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<UsersNotes> getAllUsersNotesJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List <UsersNotes> usersNotes = usersNotesDao.getAllByUser(user.getIdu());
		
		//if it is null then the user doesn't have access this note
		if (usersNotes != null) return usersNotes;
		else throw new CustomNotFoundException("Notes is not found");		   
	  }
	
	 /**
	   * Return the information of that userNote if the user have access to it
	   * 
	   * @param idn
	   *            idn of the note.
	   */
	  @GET
	  @Path("/{idn: [0-9]+}")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public UsersNotes getUsersNotesJSON(@PathParam("idn") long idn,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), idn);
		
		//if it is null then the user doesn't have access this note
		if (usersNotes != null) return usersNotes;
		else throw new CustomNotFoundException("Note ("+ idn + ") is not found");		   
	  }
	

	/**
	 * Share a note with other users only if the user is the owner of this note
	 * 
	 * @param noteShare 
	 * 			UsersNotes that we want to add
	 */
	@POST	  	  
	@Consumes(MediaType.APPLICATION_JSON)
	public Response shareNote(UsersNotes noteShare, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);	

		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);

		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		long idu1, idu2;


		UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), noteShare.getIdn());

		if (usersNotes != null && usersNotes.getOwner()==1) {
			User userFriend = userDao.get(noteShare.getIdu());
			if (userFriend.getIdu() < user.getIdu()) {
				idu1 = userFriend.getIdu();
				idu2 = user.getIdu();
			} else {
				idu1 = user.getIdu();
				idu2 = userFriend.getIdu();
			}
			if (friendsDao.areFriends(idu1, idu2)) {
				usersNotesDao.add(noteShare);
			} else {
				throw new CustomBadRequestException("Errors in parameters");
			}
			return Response.noContent().build();
		} 
		else
			throw new CustomBadRequestException("Errors in parameters");

	}
	
	
	/**
	 * Pin/unpin a note or/and archive/unarchive note
	 * 
	 * @param usersNotesUpdate
	 *            UsersNotes that we want to update.
	 * @param idn
	 * 			idn of the note we want to update
	 */
	@PUT
	@Path("/{idn: [0-9]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(UsersNotes usersNotesUpdate,
			@PathParam("idn") long idn,
			@Context HttpServletRequest request) throws Exception{
		Connection conn = (Connection)sc.getAttribute("dbConn");
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Response response = null;
		
		UsersNotes usersNotes = null;
		usersNotes = usersNotesDao.get(user.getIdu(), idn);
		
		List<String> messages = new ArrayList<String>();

		if ((usersNotes != null) && (user.getIdu() == usersNotes.getIdu())) {
			if (usersNotesUpdate.getIdn()!=idn) throw new CustomBadRequestException("Error in idn");
			else 
				if(usersNotes.getOwner() != usersNotesUpdate.getOwner()) throw new CustomBadRequestException("Error in owner");
				else {
					if(usersNotesUpdate.validate(messages))	usersNotesDao.save(usersNotesUpdate);						
					else throw new CustomBadRequestException("Errors in parameters");						
				}
		}
		else throw new WebApplicationException(Response.Status.NOT_FOUND);			

		return response;
	}
	  
	 
	
}
