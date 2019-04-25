package resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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


@Path("/Users")
public class UsersResource {

	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;

	
	/**
	 * Gets the information of the user. It does't return information of other users, only the one in session
	 * 
	 * @return User object.
	 */
	@GET  
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserJSON(@Context HttpServletRequest request) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		
		User returnUser = userDao.get(user.getIdu());
		returnUser.setPassword("******");

		return returnUser;
	}
	
	/**
	 * Gets the information of the user with that username
	 * 
	 * @return User object.
	 */
	@GET  
	@Path("/{username: [a-zA-Z][a-zA-Z_0-9]*}")	
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByUsernameJSON(@PathParam("username") String username,
			@Context HttpServletRequest request) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		User returnUser = new User();
		returnUser = userDao.get(username);
		

		if(returnUser != null) {
			returnUser.setPassword("******");
		}
		
		return returnUser;
	}
	
	
	/**
	 * Register a new user
	 * 
	 * @param newUser
	 *            User to add in the data base.
	 * 
	 */
	@POST	  	  
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(User newUser, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);	  	

		Response res;

		HttpSession session = request.getSession();
		
		List <String> messages = new ArrayList<String>();
		
		User userExists = null;
		userExists = userDao.get(newUser.getUsername());

		if (userExists == null)
			if(newUser.validate(messages)) {
				long idu = userDao.add(newUser);
				newUser.setIdu((int)idu);
				session.setAttribute("user", newUser);

				res = Response //return 201 and Location: /Users/newidu
						.created(
								uriInfo.getAbsolutePathBuilder()
								.path(Long.toString(newUser.getIdu()))
								.build())
						.contentLocation(
								uriInfo.getAbsolutePathBuilder()
								.path(Long.toString(newUser.getIdu()))
								.build())
						.build();
			}

			else throw new CustomBadRequestException("Errors in parameters");
		else throw new CustomBadRequestException("Username already exists");


		return res; 
	}
	
	
	/**
	 * Modify the profile of the user
	 * 
	 * @param userUpdate
	 *            User that we want to update
	 * 
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(User userUpdate,
			@Context HttpServletRequest request) throws Exception{
		Connection conn = (Connection)sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Response response = null;

		User oldUser = userDao.get(userUpdate.getIdu());
		
		if (oldUser != null) {

			if(user.getIdu() == oldUser.getIdu()) {
				if(userUpdate.getPassword().equals("******")) userUpdate.setPassword(oldUser.getPassword());
				List <String> messages = new ArrayList<String>();
				if (userUpdate.validate(messages)) {
					userDao.save(userUpdate);						
				} else throw new CustomBadRequestException("Errors in parameters");						
			} else throw new CustomBadRequestException("Error in idu");
		} else throw new WebApplicationException(Response.Status.NOT_FOUND);			

		return response;
	}
	
	
	/**
	 * Deletes the account of the user
	 * 
	 * @param idu
	 *            Identifier of the user that is going to be deleted
	 * 
	 */
	@DELETE
	@Path("/{idu: [0-9]+}")	  
	public Response deleteUser(@PathParam("idu") long idu,
			@Context HttpServletRequest request) {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);
		
		VersionsDAO versionsDao = new JDBCVersionsDAOImpl();
		versionsDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		User userDelete = userDao.get(idu);
		if (userDelete != null && user.getIdu() == idu) { //Delete all notes, usersNotes and friendship
			List<UsersNotes> usersNotes = usersNotesDao.getAllByUser(user.getIdu());
			for (int i=0; i<usersNotes.size(); i++) {
				if(user.getIdu() == usersNotesDao.getOwner(usersNotes.get(i).getIdn())) {
					usersNotesDao.deleteIdn(usersNotes.get(i).getIdn());
					noteDao.delete(usersNotes.get(i).getIdn());
					versionsDao.delete(usersNotes.get(i).getIdn());
				} else {
					usersNotesDao.delete(user.getIdu(), usersNotes.get(i).getIdn());
				}
			}
			friendsDao.deleteAllByUser(user.getIdu());
			session.removeAttribute("user");
			userDao.delete(idu);
			return Response.noContent().build(); //204 no content 
		}
		else throw new CustomBadRequestException("Error in idu");		

	}

}
