package resources;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import model.*;
import resources.exceptions.CustomNotFoundException;
import dao.*;
import resources.exceptions.*;


@Path("/notes")
public class NotesResource {

	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	  
	private static final Logger logger = Logger.getLogger(NotesResource.class.getName());
	  
	  /**
	   * Return all the notes that the user can access 
	   * 
	   * @param request
	   *            request.
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<Note> getNotesJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List<UsersNotes> usersNotes = new ArrayList<UsersNotes>();
		List<Note> notes = new ArrayList<Note>();
		
		usersNotes = usersNotesDao.getAllByUser(user.getIdu());
	
		for (int i=0; i<usersNotes.size(); i++) {
			Note note = noteDao.get(usersNotes.get(i).getIdn());
			notes.add(note);
		}
		
		return notes;
	  }
	  
	  
	  /**
	   * Return the information of that note if the user have access to it
	   * 
	   * @param idn
	   *            idn of the note.
	   */
	  @GET
	  @Path("/{idn: [0-9]+}")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public Note getNoteJSON(@PathParam("idn") long idn,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Note note = noteDao.get(idn);
		
		UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), idn);
		
		//if it is null then the user doesn't have access this note
		if (usersNotes != null) return note;
		else throw new CustomNotFoundException("Note ("+ idn + ") is not found");		   
	  }
	  
	  /**
	   * Return all the notes that matches with the String in the context that the user selected
	   * 
	   * @param t
	   *            text that the user wants to search
	   * @param context
	   * 			context where the String has to be
	   * 			values: 
	   * 				- 0 : search in title and content
	   * 				- 1 : search in title
	   * 				- 2 : search in content
	   */
	  @GET
	  @Path("/search/text")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<Note> getNoteSearchColorJSON(@QueryParam("t") String t,
									@QueryParam("context") int context,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List <Note> notesSearched = new ArrayList<Note>();
		
		switch (context) {
		case 0:
			notesSearched = noteDao.getAllBySearchAll(t);
			break;
			
		case 1:
			notesSearched = noteDao.getAllBySearchTitle(t);
			break;
			
		case 2:
			notesSearched = noteDao.getAllBySearchContent(t);
			break;

		default:
			throw new BadRequestException("Error in context");
		}
		
		List <Note> userSearchNotes = new ArrayList<Note>();
		
		for (int i=0; i<notesSearched.size(); i++) {
			UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), notesSearched.get(i).getIdn());
			if(usersNotes != null)
				userSearchNotes.add(notesSearched.get(i));
		}
		
		return userSearchNotes;
		
	  }

	  
	  public List<Note> helperGetNotesByText(String t, Integer context, User user) {
		  
		  Connection conn = (Connection) sc.getAttribute("dbConn");
			NoteDAO noteDao = new JDBCNoteDAOImpl();
			noteDao.setConnection(conn);
			
			UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
			usersNotesDao.setConnection(conn);
			
			List <Note> notesSearched = new ArrayList<Note>();
			
			switch (context) {
			case 0:
				notesSearched = noteDao.getAllBySearchAll(t);
				break;
				
			case 1:
				notesSearched = noteDao.getAllBySearchTitle(t);
				break;
				
			case 2:
				notesSearched = noteDao.getAllBySearchContent(t);
				break;

			default:
				throw new BadRequestException("Error in context");
			}
			
			List <Note> userSearchNotes = new ArrayList<Note>();
			
			for (int i=0; i<notesSearched.size(); i++) {
				UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), notesSearched.get(i).getIdn());
				if(usersNotes != null)
					userSearchNotes.add(notesSearched.get(i));
			}
			
			return userSearchNotes;
	  }
	  
	  public List<Note> helperGetNotesByColors(List<Integer>c, User user) {
		  Connection conn = (Connection) sc.getAttribute("dbConn");
			NoteDAO noteDao = new JDBCNoteDAOImpl();
			noteDao.setConnection(conn);
			
			UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
			usersNotesDao.setConnection(conn);
			
			List <Note> notesSearched = new ArrayList<Note>();
			
			for (int i=0; i<c.size(); i++) {
				List<Note> notes = noteDao.getAllBySearchColor(c.get(i));
				for (int j=0; j<notes.size(); j++) 
					notesSearched.add(notes.get(j));
			}
			
			List <Note> userSearchNotes = new ArrayList<Note>();
			
			for (int i=0; i<notesSearched.size(); i++) {
				UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), notesSearched.get(i).getIdn());
				if(usersNotes != null)
					userSearchNotes.add(notesSearched.get(i));
			}
			
			if (userSearchNotes.size() != 0) return userSearchNotes;
			else throw new CustomNotFoundException("There are no matching notes");
	  }
	  
	  public List<Note> helperGetNotesByFriendsShare(List<String>friends, Integer sharedBy, User user) {
		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  NoteDAO noteDao = new JDBCNoteDAOImpl();
		  noteDao.setConnection(conn);

		  UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		  usersNotesDao.setConnection(conn);
		  
		  UserDAO userDao = new JDBCUserDAOImpl();
		  userDao.setConnection(conn);
		  
		  FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		  friendsDao.setConnection(conn);

		  List<Note> matchingNotes = new ArrayList<Note>();
		  List<Integer> friendsIdus = new ArrayList<Integer>();
		  int idu1, idu2;
		  
		  //Read all usernames and get only the ones that are friends
		  for(int i=0; i<friends.size(); i++) {
			  User friend = userDao.get(friends.get(i));
			  if(friend != null) {
				  if(user.getIdu() < friend.getIdu()) {idu1 = user.getIdu(); idu2 = friend.getIdu(); }
				  else { idu1 = friend.getIdu(); idu2 = user.getIdu(); }
				  
				  Friend friendship = friendsDao.getFriendship(idu1, idu2);
				  if(friendship != null)
					  friendsIdus.add(friend.getIdu());
			  }	  
		  }
		  
		  //Once we have all friends, we get the notes
		  if(sharedBy == 0) { //Get all notes owned by the friends shared with my user
			  List<UsersNotes> usersNotesByUser = usersNotesDao.getAllByUser(user.getIdu());
			  for (int i=0; i<usersNotesByUser.size(); i++) {
				  int owner = usersNotesByUser.get(i).getOwner();
				  if(owner != user.getIdu()) {
					  long iduLong = usersNotesDao.getOwner(usersNotesByUser.get(i).getIdn());
					  int idu = (int) iduLong;
					  for(int j=0; j<friendsIdus.size(); j++) {
						  if(friendsIdus.get(j)==idu) {
							  Note note = noteDao.get(usersNotesByUser.get(i).getIdn());
							  matchingNotes.add(note);
						  }
					  }
				  }
			  }
		  } else { //Get all notes owned by my user shared with that friends
			  List<UsersNotes> usersNotesByUser = usersNotesDao.getAllByUser(user.getIdu());
			  List<UsersNotes> notesOwner = new ArrayList<UsersNotes>();
			  for(int i=0; i<usersNotesByUser.size(); i++) { //Get all the notes owned by user
				  int owner = usersNotesByUser.get(i).getOwner();
				  if(owner == 1) {notesOwner.add(usersNotesByUser.get(i));}
			  }
			  
			  for(int i=0; i<notesOwner.size(); i++) {
				  for(int j=0; j<friendsIdus.size(); j++) {
					  UsersNotes usersNotes = usersNotesDao.get(friendsIdus.get(j), notesOwner.get(i).getIdn());
					  logger.info("lEGO AQUI");
					  if(usersNotes != null) { //Then this note is shared with that user
						  Note note = noteDao.get(notesOwner.get(i).getIdn());
						  boolean contains = false;
						  for(int it=0; it < matchingNotes.size(); it++) {
							  if(matchingNotes.get(it).getIdn() == note.getIdn()) contains = true;
						  }
						  if(!contains) matchingNotes.add(note);
					  }
				  }
			  }
		  }

		  return matchingNotes;
	  }

	  
	  /**
	   * Return all the notes that matches with the friends, String and colors in the context that the user selected
	   * 
	   * @param t
	   *            text that the user wants to search
	   * @param context
	   * 			context where the String has to be
	   * 			values: 
	   * 				- 0 : search in title and content
	   * 				- 1 : search in title
	   * 				- 2 : search in content
	   * 
	   *  * @param c
	   * 	List with all the colors that we want to search
	   * 
	   * @param friends
	   * 	List with all the friends that we want to search
	   * 
	   * @param sharedBy
	   * 	0 -> Search all the notes shared by that friends with the user
	   * 	1 -> Search all the notes that the user share with that friend
	   */
	  @GET
	  @Path("/search")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<Note> getNoteAdvancedSearchJSON(
			  @QueryParam("text") String text,
			  @QueryParam("context") Integer context,
			  @QueryParam("colors") List<Integer> colors,
			  @QueryParam("friends") List<String> friends,
			  @QueryParam("sharedBy") Integer sharedBy,
			  @Context HttpServletRequest request) {

		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  NoteDAO noteDao = new JDBCNoteDAOImpl();
		  noteDao.setConnection(conn);

		  UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		  usersNotesDao.setConnection(conn);
		  
		  UserDAO userDao = new JDBCUserDAOImpl();
		  userDao.setConnection(conn);
		  
		  FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		  friendsDao.setConnection(conn);

		  HttpSession session = request.getSession();
		  User user = (User) session.getAttribute("user");
		  
		  List<Note> matchingList = null;
		  
		  int situation=0;
		  if(text!=null && colors.size()>0 && friends.size()>0) situation=1;
		  else if(text!=null && colors.size()>0) 				situation=2;
		  else if(text!=null && friends.size()>0) 				situation=3;
		  else if(colors.size()>0 && friends.size()>0) 		 	situation=4;
		  else if(colors.size()>0)								situation=5;
		  else if(friends.size()>0)								situation=6;
		  else if(text!=null)									situation=7;
			  
		  String sit = String.valueOf(situation);
		  logger.info(sit);

		  List<Note> friendSearch, textSearch, colorSearch = null;
		  logger.info("Paso por aqui 2");
		  switch (situation) {
		  case 1:
			  textSearch = helperGetNotesByText(text, context, user);
			  colorSearch = helperGetNotesByColors(colors, user);
			  friendSearch = helperGetNotesByFriendsShare(friends, sharedBy, user);
			  colorSearch.retainAll(textSearch);
			  friendSearch.retainAll(colorSearch);
			  matchingList = new ArrayList<Note>(friendSearch);
			  break;
		  case 2:
			  textSearch = helperGetNotesByText(text, context, user);
			  colorSearch = helperGetNotesByColors(colors, user);
			  colorSearch.retainAll(textSearch);
			  matchingList = new ArrayList<Note>(colorSearch);
			  break;
		  case 3:
			  textSearch = helperGetNotesByText(text, context, user);
			  friendSearch = helperGetNotesByFriendsShare(friends, sharedBy, user);
			  friendSearch.retainAll(textSearch);
			  matchingList = new ArrayList<Note>(friendSearch);
			  break;
		  case 4:
			  colorSearch = helperGetNotesByColors(colors, user);
			  friendSearch = helperGetNotesByFriendsShare(friends, sharedBy, user);
			  friendSearch.retainAll(colorSearch);
			  matchingList = new ArrayList<Note>(friendSearch);
			  break;
		  case 5:
			  colorSearch = helperGetNotesByColors(colors, user);
			  matchingList = new ArrayList<Note>(colorSearch);
			  break;
		  case 6:
			  friendSearch = helperGetNotesByFriendsShare(friends, sharedBy, user);
			  matchingList = new ArrayList<Note>(friendSearch);
			  break;
		  case 7:
			  textSearch = helperGetNotesByText(text, context, user);
			  matchingList = new ArrayList<Note>(textSearch);
			  break;
		  default:
			  break;
		  }
		  return matchingList;
	  }
	  
	  /**
	   * Saves the new note in the table Note and creates a new entry in UsersNotes
	   * 
	   * @param newNote
	   *            Note that we want to add.
	   */
	  @POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response post(Note newNote, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);	
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		VersionsDAO versionsDao = new JDBCVersionsDAOImpl();
		versionsDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Response res;
		
		List<String>messages = new ArrayList<String>();

		if (!newNote.validate(messages))
			   throw new CustomBadRequestException("Errors in parameters");
		//save note in DB
		long idn = noteDao.add(newNote);
		
		UsersNotes userNote = new UsersNotes(user.getIdu(), (int)idn, 1);
		
		usersNotesDao.add(userNote);
		
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		NoteVersion noteVersion = new NoteVersion((int)idn, timestamp, newNote.getTitle(), newNote.getContent(), newNote.getColor());
		versionsDao.add(noteVersion);
		
    	res = Response //return 201 and Location: /notes/newidn
			   .created(
				uriInfo.getAbsolutePathBuilder()
					   .path(Long.toString(idn))
					   .build())
			   .contentLocation(
				uriInfo.getAbsolutePathBuilder()
				       .path(Long.toString(idn))
				       .build())
				.build();
	    return res; 
	  }
	  
	  /**
	   * Updates a note in the table Note after checking that the User have access to this note
	   * 
	   * @param noteUpdate
	   *            Note that we want to update.
	   * @param idn
	   * 			idn of the note we want to update
	   */
	  @PUT
	  @Path("/{idn: [0-9]+}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateNote(Note noteUpdate,
							@PathParam("idn") long idn,
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
		  
		  Note note = noteDao.get(noteUpdate.getIdn());
		  UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), idn);
		  
		 //We check that the note exists and the user have access to it
		  if(usersNotes == null)
			  throw new WebApplicationException(Response.Status.NOT_FOUND);
		  else
			  if(note.getIdn()!=idn) throw new CustomBadRequestException("Error in id");
			  else {
				  List <String> messages = new ArrayList<String>();
				  if (noteUpdate.validate(messages)) {
					  noteDao.save(noteUpdate);
					  String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
					  NoteVersion noteVersion = new NoteVersion(noteUpdate.getIdn(), timestamp, noteUpdate.getTitle(), noteUpdate.getContent(), noteUpdate.getColor());
					  versionsDao.add(noteVersion);
				  }
				  else throw new CustomBadRequestException("Errors in parameters");
			  }
		  
		  return res;
		}
	  
	  /**
	   * Sends the note to trash. This note will continue existing and other users will continue having access to the note
	   * 
	   * @param idn
	   * 			idn of the note we want to send to trash
	   */
	  @DELETE
	  @Path("/{idn: [0-9]+}")	  
	  public Response deleteNote(@PathParam("idn") long idn,
			  @Context HttpServletRequest request) {

		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  NoteDAO noteDao = new JDBCNoteDAOImpl();
		  noteDao.setConnection(conn);

		  UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		  usersNotesDao.setConnection(conn);

		  HttpSession session = request.getSession();
		  User user = (User) session.getAttribute("user");

		  Note note = noteDao.get(idn);
		  UsersNotes usersNotes = usersNotesDao.get(user.getIdu(), note.getIdn());

		  if (note != null && usersNotes != null && usersNotes.getTrash()==0) {
			  usersNotes.setArchived(0);
			  usersNotes.setPinned(0);
			  usersNotes.setTrash(1);
			  usersNotesDao.save(usersNotes);
			  return Response.noContent().build(); //204 no content
		  }
		  else
			  throw new CustomBadRequestException("Error in user or id");		

	  }
	  
	  /**
	   * Delete all the notes in trash. If User is the owner of this note it will be deleted for everyone
	   * (and also the versions of this note), else only for the User
	   * 
	   */
	  @DELETE
	  @Path("/trash")	  
	  public Response deleteNote(@Context HttpServletRequest request) {

		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  NoteDAO noteDao = new JDBCNoteDAOImpl();
		  noteDao.setConnection(conn);

		  UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		  usersNotesDao.setConnection(conn);
		  
		  VersionsDAO versionsDao = new JDBCVersionsDAOImpl();
		  versionsDao.setConnection(conn);

		  HttpSession session = request.getSession();
		  User user = (User) session.getAttribute("user");

		  List <UsersNotes> notesInTrash = usersNotesDao.getAllInTrash(user.getIdu());

		  //Check all the notes in trash and if ther user is the owner, then delete all the usersNotes with that
		  //idn and the note

		  if(notesInTrash.size()==0)
			  throw new CustomBadRequestException("Error deleting notes in trash");

		  for (int i=0; i<notesInTrash.size(); i++) {
			  if(notesInTrash.get(i).getOwner() == 1) {
				  usersNotesDao.deleteIdn(notesInTrash.get(i).getIdn());
				  noteDao.delete(notesInTrash.get(i).getIdn());
				  
				  versionsDao.delete(notesInTrash.get(i).getIdn());
				  
			  } else {
				  usersNotesDao.delete(user.getIdu(), notesInTrash.get(i).getIdn());
			  }
		  }

		  return Response.noContent().build(); //204 no content

	  }
	  
	  
	  /**
	   * Delete this note in trash. If User is the owner of this note it will be deleted for everyone
	   * (and also the versions of this note), else only for the User
	   * 
	   */
	  @DELETE
	  @Path("/trash/{idn: [0-9]+}")	  
	  public Response deleteNoteIdn(@PathParam("idn") long idn,
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

		  UsersNotes noteInTrash = usersNotesDao.get(user.getIdu(), idn);
		  
		  if(noteInTrash != null && noteInTrash.getTrash() == 1) {
			  if(noteInTrash.getOwner() == 1) {
				  usersNotesDao.deleteIdn(noteInTrash.getIdn());
				  noteDao.delete(noteInTrash.getIdn());
				  
				  versionsDao.delete(noteInTrash.getIdn());
				  
			  } else {
				  usersNotesDao.delete(user.getIdu(), noteInTrash.getIdn());
			  }
		  } else throw new CustomNotFoundException("There are no matching notes");

		  return Response.noContent().build(); //204 no content

	  }
	  
	
}
