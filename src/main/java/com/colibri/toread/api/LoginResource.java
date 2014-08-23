package com.colibri.toread.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import com.colibri.toread.auth.Device;
import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.UserDAO;

import java.io.IOException;

public class LoginResource extends LoggableResource {

    @Get
    public String represent() {
        return "hello, world";
    }

    @Post
    public Representation acceptItem(Representation entity) throws JSONException, IOException {
        System.out.println("User attempting to login");

        //Get me the JSON
        JSONObject json = new JsonRepresentation(entity).getJsonObject();

        //Log it
        logRequest(json.toString());

        //Get the user specific JSON
        JSONObject userJSON;
        if(json.has("user"))
            userJSON = json.getJSONObject("user");
        else
            return getResponseRepresentation(false, "No data for a user was supplied");
        String username = userJSON.getString("username");
        String password = userJSON.getString("password");

        if(username == null)
            return new JsonRepresentation(
                    getErrorRepresentation("Invalid username or password")
            );

        UserDAO dao = new UserDAO();
        User thisUser = dao.findByUsername(username);

        //User doesnt exist
        if(thisUser == null)
            return new JsonRepresentation(
                    getErrorRepresentation("Invalid username or password")
            );

        //Validate the password for this user
        if(password != null){
            if(!thisUser.validatePassword(password)){
                return new JsonRepresentation(
                        getErrorRepresentation("Invalid username or password"));
            }
        }

        //They are logging in so this must be a new device, otherwise the app
        //would have a copy of the device id and auth token and would bypass
        //the login screen
        Device thisDevice = new Device();

        //Add the device to the user
        thisUser.addDevice(thisDevice);

        //Update the database
        dao.save(thisUser);

        JSONObject deviceJSON = thisDevice.toJson();
        try {
            deviceJSON.append("login_success", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        logger.info("New device created and details returned to user");

        //Return a JSONified version of the device containing the device id and auth code
        return new JsonRepresentation(deviceJSON);
    }

    private JsonRepresentation getResponseRepresentation(boolean success, String message){
        JSONObject object = new JSONObject();
        try {
            object.put("operation_success", success);
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        logger.info("Response to client was " + " sucess: " + success + " message: " + message);
        return new JsonRepresentation(object);
    }

    private JSONObject getErrorRepresentation(String message){
        JSONObject object = new JSONObject();
        try {
            object.put("login_success", false);
            object.put("auth_token", "");
            object.put("device_id", "");
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

}
