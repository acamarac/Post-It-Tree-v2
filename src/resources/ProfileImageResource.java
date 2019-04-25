package resources;

import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import dao.*;
import model.*;
import resources.exceptions.CustomNotFoundException;

@Path("/profileImage")
public class ProfileImageResource {
	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	/**
	   * Return all the profile images
	   * 
	   * @param request
	   *            request.
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<ProfileImage> getProfileImagesJSON(@Context HttpServletRequest request) {
		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  ProfileImageDAO profileImageDao = new JDBCProfileImageDAOImpl();
		  profileImageDao.setConnection(conn);
		  
		  List<ProfileImage> profileImages = profileImageDao.getAll();

		  return profileImages;
	  }
	  
	  
	  /**
	   * Return all the profile images
	   * 
	   * @param request
	   *            request.
	   */
	  @GET
	  @Path("/{idi: [0-9]+}")	
	  @Produces(MediaType.APPLICATION_JSON)
	  public ProfileImage getProfileImagesJSON(@PathParam("idi") long idi,
			  @Context HttpServletRequest request) {
		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  ProfileImageDAO profileImageDao = new JDBCProfileImageDAOImpl();
		  profileImageDao.setConnection(conn);
		  
		  ProfileImage profileImages = profileImageDao.get(idi);
		  
		  if(profileImages == null) throw new CustomNotFoundException("Profile image ("+ idi + ") is not found");
		  
		  return profileImages;
	  }

}
