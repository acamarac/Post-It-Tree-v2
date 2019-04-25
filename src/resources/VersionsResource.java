package resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dao.*;
import model.*;
import resources.exceptions.*;

@Path("/versions")
public class VersionsResource {
	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	
	/**
	 * Return all the versions of a note if the user have access to this note
	 * 
	 * @param idn
	 *            idn of the note.
	 */
	@GET
	@Path("/{idn: [0-9]+}")	  
	@Produces(MediaType.APPLICATION_JSON)
	public List<NoteVersion> getNoteJSON(@PathParam("idn") long idn,
			@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);

		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);

		VersionsDAO versionsDao = new JDBCVersionsDAOImpl();
		versionsDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), idn);

		List<NoteVersion> noteVersions = new ArrayList<NoteVersion>();

		//if it is null then the user doesn't have access this note
		if (usersNotes != null) {
			noteVersions = versionsDao.getAllByIdn(idn);
			return noteVersions;
		}
		else throw new CustomNotFoundException("Note ("+ idn + ") is not found");		   
	}
	
	/**
	 * Return all the versions of a note if the user have access to this note
	 * 
	 * @param idn
	 *            idn of the note.
	 */
	@GET
	@Path("/{idn: [0-9]+}/{timestamp: [0-9]+}")	  
	@Produces(MediaType.APPLICATION_JSON)
	public NoteVersion getNoteJSON(@PathParam("idn") long idn,
						@PathParam("timestamp") long timestamp,
			@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);

		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);

		VersionsDAO versionsDao = new JDBCVersionsDAOImpl();
		versionsDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), idn);

		NoteVersion noteVersions = new NoteVersion();

		//if it is null then the user doesn't have access this note
		if (usersNotes != null) {
			String ts = Long.toString(timestamp);
			noteVersions = versionsDao.get(idn, ts);
			return noteVersions;
		}
		else throw new CustomNotFoundException("Note ("+ idn + ") is not found");		   
	}
	
	
	/**
	   * Updates a note with the content of the version
	   * 
	   * @param idn
	   *            Note that we want to update.
	   * @param timestamp
	   * 			identifier of the version of this note
	   */
	  @PUT
	  @Path("/{idn: [0-9]+}/{timestamp: [0-9]+}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateNote(NoteVersion noteVersionUpdate,
							@PathParam("idn") long idn,
							@PathParam("timestamp") long timestamp,
							@Context HttpServletRequest request) throws Exception{
		  Connection conn = (Connection)sc.getAttribute("dbConn");
		  NoteDAO noteDao = new JDBCNoteDAOImpl();
		  noteDao.setConnection(conn);
		  
		  UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		  usersNotesDao.setConnection(conn);
		  
		  VersionsDAO versionsDao = new JDBCVersionsDAOImpl();
		  versionsDao.setConnection(conn);
		  
		  HttpSession session = request.getSession();
		  User user = (User) session.getAttribute("user");
			
		  Response res = null;
		  
		  Note note = noteDao.get(noteVersionUpdate.getIdn());
		  UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), idn);
		  NoteVersion noteVersion = versionsDao.get(idn, Long.toString(timestamp));
		  
		 //We check that the note exists and the user have access to it
		  if(usersNotes == null)
			  throw new WebApplicationException(Response.Status.NOT_FOUND);
		  else {
			  if(note.getIdn()!=idn || noteVersion == null) throw new CustomBadRequestException("Error in id or timestamp");
			  if(note == null || usersNotesDao.getOwner(idn) != user.getIdu()) 
				  throw new WebApplicationException(Response.Status.NOT_FOUND);
			  else {
				  List <String> messages = new ArrayList<String>();
				  Note noteUpdate = new Note();
				  noteUpdate.setIdn(noteVersionUpdate.getIdn());
				  noteUpdate.setTitle(noteVersionUpdate.getTitle());
				  noteUpdate.setContent(noteVersionUpdate.getContent());
				  noteUpdate.setColor(noteVersionUpdate.getColor());
				  if (noteUpdate.validate(messages)) {
					  noteDao.save(noteUpdate);
				  }
				  else throw new CustomBadRequestException("Errors in parameters");
			  }
		  }
		  
		  return res;
		}

}
