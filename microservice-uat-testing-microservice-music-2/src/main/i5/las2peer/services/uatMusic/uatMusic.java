package i5.las2peer.services.uatMusic;


import java.net.HttpURLConnection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import i5.las2peer.api.Context;
import i5.las2peer.api.ManualDeployment;
import i5.las2peer.api.ServiceException;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import i5.las2peer.services.uatMusic.database.DatabaseManager;
import java.sql.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import org.json.simple.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 *
 * uat-testing-microservice-music-2
 *
 * This microservice was generated by the CAE (Community Application Editor). If you edit it, please
 * make sure to keep the general structure of the file and only add the body of the methods provided
 * in this main file. Private methods are also allowed, but any "deeper" functionality should be
 * outsourced to (imported) classes.
 *
 */
@ServicePath("musicapp")
@ManualDeployment
public class uatMusic extends RESTService {


  /*
   * Database configuration
   */
  private String jdbcDriverClassName;
  private String jdbcLogin;
  private String jdbcPass;
  private String jdbcUrl;
  private static DatabaseManager dbm;



  public uatMusic() {
	super();
    // read and set properties values
    setFieldValues();
        // instantiate a database manager to handle database connection pooling and credentials
    dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl);
  }

  @Override
  public void initResources() {
	getResourceConfig().register(RootResource.class);
  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // REST methods
  // //////////////////////////////////////////////////////////////////////////////////////

  @Api
  @SwaggerDefinition(
      info = @Info(title = "uat-testing-microservice-music-2", version = "1",
          description = "UAT microservice music 2",
          termsOfService = "LICENSE.txt",
          contact = @Contact(name = "Melisa Cecilia", email = "CAEAddress@gmail.com") ,
          license = @License(name = "BSD",
              url = "https://github.com/testcae/microservice-uat-testing-microservice-music-2/blob/master/LICENSE.txt") ) )
  @Path("/")
  public static class RootResource {

    private final uatMusic service = (uatMusic) Context.getCurrent().getService();

      /**
   * 
   * getMusic
   *
   * 
   *
   * 
   * @return Response response get all
   * 
   */
  @GET
  @Path("/get")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "response get all")
  })
  @ApiOperation(value = "getMusic", notes = " ")
  public Response getMusic() {

    

    try {
      Object returnServiceGetImage = Context.getCurrent().invoke(
          "i5.las2peer.services.uatTestImage.uatTestImage@1.0", "getImage");
      HashMap<Integer, classes.image> imageMap = new HashMap<Integer, classes.image>();
      // put into array
      JSONParser parser = new JSONParser();
      JSONArray jsonArray = (JSONArray)parser.parse((String) returnServiceGetImage);
      Iterator i = jsonArray.iterator();

      // put into map of id and image object
      while (i.hasNext())
      {
          JSONObject jsonObj = (JSONObject) i.next();
          classes.image imageObj = new classes().new image();
          imageObj.fromJSON(jsonObj.toJSONString());
          imageMap.put(imageObj.getimageId(), imageObj);
          System.out.println(jsonObj);
      }

      // now process from music database
      Connection conn = service.dbm.getConnection();
      PreparedStatement query = conn.prepareStatement("SELECT * FROM uatTest.tblMusic");
      ResultSet result = query.executeQuery();

      JSONArray jsonResult = new JSONArray();
      while(result.next()) {
        
        // music object
        classes.music musicResult = new classes().new music();
        musicResult.setmusicName(result.getString("musicName"));
        musicResult.setmusicUrl(result.getString("musicUrl"));
        musicResult.setmusicId(result.getInt("musicId"));
        musicResult.setimageId(result.getInt("imageId"));

        // music + image
        classes.image imageResult = imageMap.get(musicResult.getimageId());
        classes.musicImage imageMusicResult = new classes().new musicImage();

        if(imageResult != null) {
          imageMusicResult.setimageName(imageResult.getimageName());
          imageMusicResult.setimageUrl(imageResult.getimageUrl());
        }
        imageMusicResult.setmusicName(musicResult.getmusicName());
        imageMusicResult.setmusicUrl(musicResult.getmusicUrl());

        jsonResult.add(imageMusicResult.toJSON());
      }
      // responseGetMusic
      return Response.status(HttpURLConnection.HTTP_OK).entity(jsonResult.toJSONString()).build();
    } catch (Exception e) {
      e.printStackTrace();
      JSONObject result = new JSONObject(); 
      return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(result.toJSONString()).build();
    }
  }

  /**
   * 
   * postMusic
   *
   * 
   * @param payloadPost payload post image music a JSONObject
   * 
   * @return Response response post music
   * 
   */
  @GET
  @Path("/post")
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "response post music")
  })
  @ApiOperation(value = "postMusic", notes = " ")
  public Response postMusic(String payloadPost) {
   classes.musicImage payloadpayloadPostObject = new classes().new musicImage();
   try { 
       payloadpayloadPostObject.fromJSON(payloadPost);
   } catch (Exception e) { 
       e.printStackTrace();
       JSONObject result = new JSONObject();
       return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity("Cannot convert json to object").build();
   }
       // get image object to pass to music service
   classes.image imageObject = new classes().new image();
   imageObject.setimageId(0);
   imageObject.setimageName(payloadpayloadPostObject.getimageName());
   imageObject.setimageUrl(payloadpayloadPostObject.getimageUrl());

   String postImageParameter = imageObject.toJSON().toJSONString();

    try {
      Object returnServicePostImage = Context.getCurrent().invoke(
          "i5.las2peer.services.uatTestImage.uatTestImage@1.0", "postImage", new Serializable[] {postImageParameter});
      int imageId = (int) returnServicePostImage;

      // now process music object
      Connection conn = service.dbm.getConnection();
      PreparedStatement query = conn.prepareStatement(
        "INSERT INTO uatTest.tblMusic(musicName, musicUrl, imageId) VALUES(?,?,?) ");
      query.setString(1, payloadpayloadPostObject.getmusicName());
      query.setString(2, payloadpayloadPostObject.getmusicUrl());
      query.setInt(3, imageId);
      query.executeUpdate();

      // get id of the new added image
      ResultSet generatedKeys = query.getGeneratedKeys();
      if (generatedKeys.next()) {
        return Response.status(HttpURLConnection.HTTP_OK).entity(generatedKeys.getLong(1)).build();
      } else {
        return Response.status(HttpURLConnection.HTTP_OK).entity(0).build();
      }

    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(0).build();
    }
  }



  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // Service methods (for inter service calls)
  // //////////////////////////////////////////////////////////////////////////////////////
  
  

}
